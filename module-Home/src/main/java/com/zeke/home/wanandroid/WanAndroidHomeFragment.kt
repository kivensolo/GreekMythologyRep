package com.zeke.home.wanandroid

import android.text.Html
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import com.chad.library.adapter.base.animation.ScaleInAnimation
import com.kingz.module.home.R
import com.kingz.module.wanandroid.adapter.ArticleAdapter
import com.kingz.module.wanandroid.bean.Article
import com.kingz.module.wanandroid.bean.BannerItem
import com.kingz.module.wanandroid.bean.CollectActionBean
import com.kingz.module.wanandroid.fragemnts.AbsListFragment
import com.kingz.module.wanandroid.viewmodel.WanAndroidViewModelV2
import com.like.LikeButton
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener
import com.youth.banner.Banner
import com.youth.banner.indicator.CircleIndicator
import com.youth.banner.transformer.ScaleInTransformer
import com.zeke.home.wanandroid.adapter.HomeBannerAdapter
import com.zeke.home.wanandroid.viewmodel.HomeViewModel
import com.zeke.kangaroo.zlog.ZLog
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


/**
 * 首页热门推荐(玩android)的Fragemnt
 *
 * 顶部是Banner数据，下面是文章数据。
 */
class WanAndroidHomeFragment : AbsListFragment<WanAndroidViewModelV2>() {

    private var banner: Banner<BannerItem, HomeBannerAdapter<BannerItem>>? = null
    private var articleAdapter: ArticleAdapter? = null

    private var bannerUrls: MutableList<String> = ArrayList()
    private var bannerTitles: MutableList<String> = ArrayList()

    override fun getLayoutResID() = R.layout.fragment_refresh_layout

//    override val viewModel: HomeViewModel by viewModels {
//        ViewModelFactory.build { HomeViewModel() }
//    }
    override val viewModel: HomeViewModel by getViewModel(HomeViewModel::class.java)

    override fun initViewModel() {
        super.initViewModel()
        obServeArticalLiveData()
        obServeBannerLiveData()

        viewModel.articalCollectData.observe(this, Observer { result ->
            val message :String = if (result.isSuccess) {
                // 处理操作成功的情况
                if (result.actionType == CollectActionBean.TYPE.COLLECT) {
                    resources.getString(R.string.collect_success)
                } else {
                    resources.getString(R.string.uncollect_success)
                }
            }else{
                // 处理操作失败的情况，需要重置UI
                if (!TextUtils.isEmpty(result.errorMsg)) {
                    result.errorMsg
                } else {
                    val childView = mRecyclerView.getChildAt(result.articlePostion)
                    val likeBUtton = childView.findViewById<LikeButton>(R.id.ivLike)
                    if (result.actionType == CollectActionBean.TYPE.COLLECT) {
                        likeBUtton.isLiked = false
                        resources.getString(R.string.collect_failed)
                    } else {
                        likeBUtton.isLiked = true
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
    private fun obServeBannerLiveData() {
        viewModel.bannerLiveData.observe(this, { result ->
            ZLog.d("Banner data onChanged(). Data size:" + result?.size)
            bannerUrls.clear()
            bannerTitles.clear()
            result?.forEach { item ->
                bannerUrls.add(item.imagePath?:"")
                bannerTitles.add(Html.fromHtml(item.title).toString())
            }
            banner?.apply {
                setDatas(result)
            }
        })
    }

    /**
     * 文章数据观察回调
     */
    private fun obServeArticalLiveData() {
        viewModel.articalLiveData.observe(this, Observer {
            if (it == null) {
                ZLog.d("artical LiveData request error. result is null.")
                refreshLayout?.finishRefresh()
                showEmptyStatus()
                return@Observer
            }
            showContent()
            launchIO {
                ZLog.d("Artical LiveData onObserved. Data size = ${it.datas?.size}")

                val articleList = it.datas
                //当前数据为空时
                if (articleAdapter?.getDefItemCount() == 0) {
                    withContext(Dispatchers.Main) {
                        ZLog.w("Current articleAdapter data is empty! Load new data !")
                        articleAdapter?.addData(articleList!!)
                    }
                    return@launchIO
                }

                //下拉刷新UI的处理
                refreshLayout?.apply {
                    if (isLoadingMore) finishLoadMore() else finishRefresh()
                }

                // 数据非空时
                val currentFirstData = articleAdapter?.getItem(0)
                val currentHeadDataId = currentFirstData?.id
                val netHeadDataId = articleList!![0].id
                ZLog.d("localHeadDataId=${currentHeadDataId}, netHeadDataId=${netHeadDataId}")
                if (currentHeadDataId != netHeadDataId) {
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
                }else{
                    // 当前第一个数据等于接口第一个，表示没有新数据
                    ZLog.w("No need to refresh data. <------")
                }
                isLoadingMore = false
            }
        })
    }


    override fun initData() {
        super.initData()
        if (articleAdapter?.itemCount == 1) {
            // 无数据时(只有1个HeadView), 才请求数据
            viewModel.getBanner()
            requestArticalData(0)
        }
    }

    override fun initView() {
        super.initView()
        showLoading()
        setRecyclerAdapter()
        initLoadMore()
        // Banne须在RecyclerView之后初始化
        initBanner()
    }

    private fun setRecyclerAdapter() {
        mRecyclerView.apply {
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
                        viewModel.changeArticleLike(item, adapterPosition, true)
                    }

                    override fun unLiked(item: Article, adapterPosition: Int) {
                        viewModel.changeArticleLike(item, adapterPosition,false)
                    }

                }
            }
            adapter = articleAdapter
        }
    }

    private fun initLoadMore() {
        refreshLayout?.apply {
            setOnRefreshLoadMoreListener(object : OnRefreshLoadMoreListener {
                override fun onRefresh(refreshLayout: RefreshLayout) {
                    ZLog.d("onRefresh")
                    fireVibrate()
                    requestArticalData(0)
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
     * 通过index请求对应页数文章
     */
    private fun requestArticalData(pageId: Int){
        ZLog.d("request ArticalData page index = $pageId")
        mCurPage = pageId
        mPageCount = if(mCurPage > mPageCount) mCurPage else ++mPageCount
        viewModel.getArticalData(pageId)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewModel.cancle(this)
        articleAdapter = null
        banner?.onDestroy(this)
        banner = null
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
                openWeb(Article().apply {
                    val bannerItem = data as BannerItem
                    link = bannerItem.url
                    id = bannerItem.id
                })
            }
        }
        articleAdapter?.setHeaderView(view = banner!!)
    }

    override fun dismissLoading() {
        refreshLayout?.visibility = View.VISIBLE
    }
}