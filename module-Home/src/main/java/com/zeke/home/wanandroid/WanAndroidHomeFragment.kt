package com.zeke.home.wanandroid

import android.app.Service
import android.os.VibrationEffect
import android.os.Vibrator
import android.text.Html
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.alibaba.android.arouter.launcher.ARouter
import com.chad.library.adapter.base.animation.ScaleInAnimation
import com.kingz.base.BaseVMFragment
import com.kingz.base.factory.ViewModelFactory
import com.kingz.module.common.LoadStatusView
import com.kingz.module.common.base.IRvScroller
import com.kingz.module.common.router.RPath
import com.kingz.module.common.utils.RvUtils
import com.kingz.module.common.utils.ktx.SDKVersion
import com.kingz.module.home.R
import com.kingz.module.wanandroid.WADConstants
import com.kingz.module.wanandroid.adapter.ArticleAdapter
import com.kingz.module.wanandroid.bean.Article
import com.kingz.module.wanandroid.bean.BannerItem
import com.kingz.module.wanandroid.bean.CollectActionBean
import com.kingz.module.wanandroid.viewmodel.WanAndroidViewModelV2
import com.scwang.smart.refresh.layout.SmartRefreshLayout
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener
import com.youth.banner.Banner
import com.youth.banner.indicator.CircleIndicator
import com.youth.banner.transformer.ScaleInTransformer
import com.zeke.home.wanandroid.adapter.HomeBannerAdapter
import com.zeke.home.wanandroid.viewmodel.HomeViewModel
import com.zeke.kangaroo.utils.ZLog
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.withContext
import java.util.*


/**
 * 首页热门推荐(玩android)的Fragemnt
 *  //TODO 显示网络异常情况下，数据加载失败的UI
 *  //TODO banner如果没数据  需要刷新
 */
class WanAndroidHomeFragment : BaseVMFragment<WanAndroidViewModelV2>(),
    IRvScroller {

    private var banner: Banner<BannerItem, HomeBannerAdapter<BannerItem>>? = null
    private lateinit var mRecyclerView: RecyclerView
    private var articleAdapter: ArticleAdapter? = null
    private var swipeRefreshLayout: SmartRefreshLayout? = null
    private var loadStatusView: LoadStatusView? = null

    // 当前页数
    private var mCurPage = 0
    private var mPageCount = 0
    // 优化: 状态通过swipeRefreshLayout内部状态RefreshState判断
    private var isLoadingMore = false

    private var bannerUrls: MutableList<String> = ArrayList()
    private var bannerTitles: MutableList<String> = ArrayList()

    override fun getLayoutResID() = R.layout.fragment_common_page

    override val viewModel: HomeViewModel by viewModels {
        ViewModelFactory.build { HomeViewModel() }
    }

    override fun initViewModel() {
        super.initViewModel()
        ZLog.d("initViewModel()")
        obServArticalLiveData()
        obServBannerLiveData()

        viewModel.articalCollectData.observe(this, Observer { result ->
            val message :String = if (result.isSuccess) {
                if (result.actionType == CollectActionBean.TYPE.COLLECT) {
                    resources.getString(R.string.collect_success)
                } else {
                    resources.getString(R.string.uncollect_success)
                }
            }else{
                //TODO 恢复失败情况下的UI
                if (!TextUtils.isEmpty(result.errorMsg)) {
                    result.errorMsg
                } else {
                    if (result.actionType == CollectActionBean.TYPE.COLLECT) {
                        resources.getString(R.string.collect_failed)
                    } else {
                        resources.getString(R.string.uncollect_failed)
                    }
                }
            }
            Toast.makeText(context,message,Toast.LENGTH_SHORT).show()
        })
    }

    /**
     * Banner数据观察回调
     */
    private fun obServBannerLiveData() {
        viewModel.bannerLiveData.observe(this, Observer { result ->
            ZLog.d("Banner data onChanged() data size = " + result.data?.size)
            bannerUrls.clear()
            bannerTitles.clear()
            result.data?.forEach { item ->
                bannerUrls.add(item.imagePath)
                bannerTitles.add(Html.fromHtml(item.title).toString())
            }
            banner?.apply {
                setDatas(result.data)
            }
        })
    }

    /**
     * 文章数据观察回调
     */
    private fun obServArticalLiveData() {
        viewModel.articalLiveData.observe(this, Observer {
            //FIXME 断网后再联网  手动刷新时，会回调两次
            launchIO {
                ZLog.d("articalLiveData onObserved.")
                if(it == null){
                    ZLog.d("articalLiveData request error. result is null.")
//                    showToast(R.string.exception_request_data)
                    swipeRefreshLayout?.finishRefresh()
                    showError()
                    return@launchIO
                }

                val articleList = it.datas
                ZLog.d("articalLiveData dataSize = ${articleList?.size};")
                //当前数据为空时
                if (articleAdapter?.getDefItemCount() == 0) {
                    withContext(Dispatchers.Main) {
                        articleAdapter?.addData(articleList!!)
                    }
                    return@launchIO
                }
                swipeRefreshLayout?.apply {
                    if (isLoadingMore) finishLoadMore() else finishRefresh()
                }
                // 数据非空时
                val currentFirstData = articleAdapter?.getItem(0)
                if (currentFirstData?.id != articleList!![0].id) {
                    //当前第一个数据不同于接口第一个，表示有新数据
                    ZLog.d("Has new article data. <------")
                    withContext(Dispatchers.Main) {
                        articleAdapter?.apply {
                            if (!isLoadingMore) {
                                addData(articleList)
                            } else {
                                val defItemCount = getDefItemCount()
                                addData(defItemCount, articleList)
                            }
                        }
                    }
                }
                isLoadingMore = false
            }
        })
    }

    private suspend fun showToast(@StringRes id:Int) {
        withContext(Dispatchers.Main) {
            Toast.makeText(
                context,
                resources.getString(id),
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    override fun onViewCreated() {
        //doNothing
        ZLog.d("onViewCreated")
    }


    /**
     * 以懒加载方式进行数据加载
     */
    override fun lazyInit() {
        if(articleAdapter?.itemCount == 1){
            // 无数据时(只有1个HeadView), 才请求数据
            viewModel.getBanner()
            requestArticalData(0)
        }
    }

    override fun initView() {
        ZLog.d("initView()")
        loadStatusView = rootView?.findViewById(R.id.load_status)
        loadStatusView?.showProgress()

        initRecyclerView()
        initSwipeRefreshLayout()
        initFABInflate()
        // Banne须在RecyclerView之后初始化
        initBanner()
    }

    private fun initRecyclerView() {
        mRecyclerView = rootView?.findViewById(R.id.recycler_view) as RecyclerView
        mRecyclerView.apply {
            isVerticalScrollBarEnabled = true
            layoutManager = LinearLayoutManager(context)
            articleAdapter = ArticleAdapter()
            articleAdapter?.apply {
                adapterAnimation = ScaleInAnimation()
                setOnItemClickListener { adapter, view, position ->
                    if (articleAdapter!!.getDefItemCount() > position) {
                        openWeb(articleAdapter?.getItem(position))
                    }
                }

                //设置文章收藏监听器
                likeListener = object : ArticleAdapter.LikeListener{
                    override fun liked(item: Article, adapterPosition: Int) {
                        //TODO 收藏后,进行绑定的数据刷新
                        viewModel.changeArticleLike(item)
                    }

                    override fun unLiked(item: Article, adapterPosition: Int) {
                        viewModel.changeArticleLike(item)
                    }

                }
            }
            adapter = articleAdapter
        }
    }

    private fun initFABInflate() {
        val fabView = rootView?.findViewById<View>(R.id.app_fab_btn)
        fabView?.setOnClickListener { scrollToTop() }
    }

    private fun initSwipeRefreshLayout() {
        swipeRefreshLayout = rootView?.findViewById(R.id.swipeRefreshLayout)!!
        swipeRefreshLayout?.apply {
            setOnRefreshLoadMoreListener(object : OnRefreshLoadMoreListener {
                override fun onRefresh(refreshLayout: RefreshLayout) {
                    ZLog.d("onRefresh")
                    fireVibrate()
                    requestArticalData(0)
                    //TODO banner如果没数据  需要刷新
                    if(banner?.childCount == 0){
                        viewModel.getBanner()
                    }
                }

                override fun onLoadMore(refreshLayout: RefreshLayout) {
                    ZLog.d("onLoadMore mPageCount = $mPageCount")
                    if(!isLoadingMore){
                        isLoadingMore = true
                        requestArticalData(mPageCount)
                    }
//                finishLoadMore(2000/*,false*/)//传入false表示加载失败
                }
            })
        }
    }

    /**
     * 触发震动效果
     */
    private fun fireVibrate() {
        val vibrator = context?.getSystemService(Service.VIBRATOR_SERVICE) as Vibrator
        if (SDKVersion.afterOreo()) {
            vibrator.vibrate(
                VibrationEffect.createOneShot(40, VibrationEffect.DEFAULT_AMPLITUDE)
            )
        } else {
            @Suppress("DEPRECATION")
            vibrator.vibrate(40)
        }
    }

    /**
     * 通过index请求对应页数文章
     */
    private fun requestArticalData(pageId: Int){
        ZLog.d("request ArticalData page index = $pageId")
        mCurPage = pageId
        mPageCount = if(mCurPage > mPageCount) mCurPage else ++mPageCount
        viewModel.getArticalData(pageId)
    }

    private fun openWeb(data: Article?) {
        ARouter.getInstance()
            .build(RPath.PAGE_WEB)
            .withString(WADConstants.KEY_URL, data?.link)
            .withString(WADConstants.KEY_TITLE, data?.title)
            .withString(WADConstants.KEY_AUTHOR, data?.author)
            .withBoolean(WADConstants.KEY_IS_COLLECT, data?.collect ?: false)
            .withInt(WADConstants.KEY_ID, data?.id ?: -1)
            .navigation(activity, 0x01)
    }

    private fun openWeb(url: String?) {
        ARouter.getInstance()
            .build(RPath.PAGE_WEB)
            .withString(WADConstants.KEY_URL, url)
            .navigation(activity, 0x01)
    }

    override fun onViewDestory() {
        ZLog.d("onViewDestory.")
        lifecycleScope.cancel()
        viewModel.cancle(this)
        super.onViewDestory()
    }

    override fun onDestroy() {
        super.onDestroy()
        ZLog.d("release.")
        swipeRefreshLayout?.setOnRefreshLoadMoreListener(null)
        swipeRefreshLayout = null
        articleAdapter = null
        banner?.onDestroy(this)
        banner = null
    }

    override fun onDetach() {
        ZLog.d("Detach fragment.")
        super.onDetach()
    }

    /**
     * 初始化Banner控件
     */
    private fun initBanner() {
        @Suppress("UNCHECKED_CAST")
        banner = LayoutInflater.from(context).inflate(
            R.layout.layout_banner, mRecyclerView, false
        ) as Banner<BannerItem, HomeBannerAdapter<BannerItem>>?
        banner?.apply {
            addBannerLifecycleObserver(activity)
            adapter = HomeBannerAdapter(null)
//            setLoopTime(3000L) //  设置轮播间隔时间 默认3000ms
//            scrollTime = 1000  //  设置轮播间隔时间 默认600ms
            setPageTransformer(ScaleInTransformer())
            indicator = CircleIndicator(context)
            setOnBannerListener { data, _ ->
                openWeb((data as BannerItem).url)
            }
        }
        articleAdapter?.setHeaderView(view = banner!!)
    }

    override fun scrollToTop() {
        RvUtils.smoothScrollTop(mRecyclerView)
    }

    override fun scrollToTopRefresh() {
    }

    fun hideLoading() {
        loadStatusView?.dismiss()
        swipeRefreshLayout?.visibility = View.VISIBLE
    }
    private fun showError() {
        swipeRefreshLayout?.visibility = View.GONE
        loadStatusView?.showError()
    }

    fun showEmpty() {
        swipeRefreshLayout?.visibility = View.GONE
        loadStatusView?.showEmpty()
    }


}