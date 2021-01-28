package com.zeke.home.wanandroid

import android.app.Service
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.alibaba.android.arouter.launcher.ARouter
import com.kingz.base.BaseVMFragment
import com.kingz.base.factory.ViewModelFactory
import com.kingz.module.common.router.RPath
import com.kingz.module.common.utils.ktx.SDKVersion
import com.kingz.module.home.R
import com.kingz.module.wanandroid.WADConstants
import com.kingz.module.wanandroid.bean.Article
import com.kingz.module.wanandroid.bean.BannerData
import com.scwang.smart.refresh.layout.SmartRefreshLayout
import com.youth.banner.Banner
import com.youth.banner.BannerConfig
import com.youth.banner.Transformer
import com.zeke.home.adapter.ArticleDelegateAdapter
import com.zeke.home.adapter.HomeArticleAdapter
import com.zeke.home.wanandroid.banner.BannerGlideImageLoader
import com.zeke.home.wanandroid.repository.HomeRepository
import com.zeke.home.wanandroid.viewmodel.HomeViewModel
import com.zeke.kangaroo.utils.ZLog
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.withContext
import java.util.*


/**
 * 首页热门推荐(玩android)的Fragemnt
 * 内部使用 SuperSwipeRefreshLayout
 */
class WanAndroidHomeFragment : BaseVMFragment<HomeRepository, HomeViewModel>() {
    private var banner: Banner? = null
    lateinit var mRecyclerView: RecyclerView
    private var articleAdapter: HomeArticleAdapter? = null
    private var swipeRefreshLayout: SmartRefreshLayout? = null

    // 当前页数
    private var mCurPage = 1

    //Banner数据
    private var mBannerData: BannerData? = null
    private var bannerUrls: MutableList<String> = ArrayList()
    private var bannerTitles: MutableList<String> = ArrayList()

    override val viewModel: HomeViewModel by viewModels {
        ViewModelFactory.build { HomeViewModel() }
    }

    override fun initViewModel() {
        super.initViewModel()

        viewModel.articalLiveData.observe(this, Observer {
            ZLog.d("articalLiveData onobserve  Current thread= ${Thread.currentThread().name}")
            launchIO {
                val currentFirstData = articleAdapter?.getItem(0)
                if (it?.datas != null) {
                    swipeRefreshLayout?.finishRefresh()
                    val articleList = it.datas
                    if (currentFirstData?.id != articleList!![0].id) {
                        //当前第一个数据不同于接口第一个，表示有新数据
                        ZLog.d("Has new article data.")
                        //TODO 优化数据更新逻辑，只插入新数据
                        articleAdapter?.removeAll()
                        articleAdapter?.addAll(articleList)
                        withContext(Dispatchers.Main) {
                            (mRecyclerView.adapter as HomeArticleAdapter).notifyDataSetChanged()
                        }
                    } else {
                        withContext(Dispatchers.Main) {
                            Toast.makeText(context, "当前数据已是最新", Toast.LENGTH_SHORT).show()
                        }
                    }
                } else {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(context, "数据请求异常", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        })

        viewModel.bannerLiveData.observe(this, Observer { result ->
            ZLog.d("Banner data onChanged() data size = " + result.data?.itemList?.size)
            mBannerData = result.data
            bannerUrls.clear()
            bannerTitles.clear()
            mBannerData?.itemList?.forEach { item ->
                bannerUrls.add(item.imagePath)
                bannerTitles.add(Html.fromHtml(item.title).toString())
            }
//            banner?.apply {
//                setImages(bannerUrls)                    // 设置图片数据集合
//                setBannerTitles(bannerTitles)            // 设置标题集合（当banner样式有显示title时）
//                start()
//            }
//            // 解决IllegalStateException，不能通过addHeaderView重复添加子View
//            // 2.6.8版本新增setHeaderView方法
//            // 解决IllegalStateException，不能通过addHeaderView重复添加子View
//// 2.6.8版本新增setHeaderView方法
//            if (adapter.getHeaderLayoutCount() > 0) {
//                adapter.setHeaderView(banner)
//            } else {
//                adapter.addHeaderView(banner)
//            }
//            lifecycleScope.launch(Dispatchers.IO) {
//                if (result.data?.itemList?.size ?: 0 > 0) {
//                    result.data?.itemList?.forEach { item ->
//                        item.desc
//                        ZLog.d("+1  desc = " + item.desc)
//                    }
//                    //刷新UI
//                }
//            }

        })
    }

    override fun onViewCreated() {
        //doNothing
        ZLog.d("HomeWanAndroidFragment onViewCreated")
    }

    override fun getLayoutResID() = R.layout.fragment_common_page

    override fun initData(savedInstanceState: Bundle?) {
        if (articleAdapter == null) {
            ZLog.d("articleAdapter == null, getBanner.")
            viewModel.getBanner()

            articleAdapter = HomeArticleAdapter()
            articleAdapter?.apply {
                mOnItemClickListener = object : HomeArticleAdapter.OnItemClickListener {
                    override fun onItemClick(v: View?, position: Int) {
                        if (articleAdapter!!.count > position) {
                            openWeb(articleAdapter?.getItem(position))
                        }
                    }
                }
                // 添加委托Adapter
                //addDelegate(BannerDelegateAdapter())
                // addDelegate(ThreePicDelegateAdapter())
                addDelegate(ArticleDelegateAdapter())
            }
            // 执行VIewModel的数据请求
            // 获取网络数据
            viewModel.getArticalData(0)
        }
        mRecyclerView.adapter = articleAdapter
    }

    override fun initView(savedInstanceState: Bundle?) {
        mRecyclerView = rootView?.findViewById(R.id.recycler_view) as RecyclerView
        mRecyclerView.layoutManager = LinearLayoutManager(context)
        initSwipeRefreshLayout()
    }

    private fun initSwipeRefreshLayout() {
        swipeRefreshLayout = rootView?.findViewById(R.id.swipeRefreshLayout)!!
        swipeRefreshLayout?.apply {
            setOnRefreshListener {
                val vibrator = context?.getSystemService(Service.VIBRATOR_SERVICE) as Vibrator
                if (SDKVersion.afterOreo()) {
                    vibrator.vibrate(
                        VibrationEffect.createOneShot(40, VibrationEffect.DEFAULT_AMPLITUDE)
                    )
                } else {
                    @Suppress("DEPRECATION")
                    vibrator.vibrate(40)
                }
                // 获取最新数据
                viewModel.getArticalData(0)
            }

            setOnLoadMoreListener {
                finishLoadMore(2000/*,false*/)//传入false表示加载失败
                //TODO 上拉加载后续的数据
            }
        }
    }


    fun openWeb(data: Article?) {
        ARouter.getInstance()
            .build(RPath.PAGE_WEB)
            .withString(WADConstants.KEY_URL, data?.link)
            .withString(WADConstants.KEY_TITLE, data?.title)
            .withString(WADConstants.KEY_AUTHOR, data?.author)
            .withBoolean(WADConstants.KEY_IS_COLLECT, data?.isCollect ?: false)
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
        lifecycleScope.cancel()
        super.onViewDestory()
    }

    override fun onDestroy() {
        super.onDestroy()
        ZLog.d("release.")
        swipeRefreshLayout?.setOnRefreshListener(null)
        swipeRefreshLayout = null
        articleAdapter = null
    }

    override fun onDetach() {
        ZLog.d("Detach fragment.")
        super.onDetach()
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        if (savedInstanceState != null) {
            if (savedInstanceState.getBoolean("SAVE_INSTANCE_STATE")) {
                //                lazyLoadData();
            }
        }
        initBanner()
    }


    private fun initBanner() {
        // 用代码创建的banner无法显示指示器，换为使用布局创建
//        banner = new Banner(mContext);
        banner = LayoutInflater.from(context).inflate(
            R.layout.layout_banner, mRecyclerView, false
        ) as Banner
//        val params = ViewGroup.LayoutParams(
//            ViewGroup.LayoutParams.MATCH_PARENT, UIUtils.dp2px(mContext, 200L)
//        )
        banner?.apply {
            setBannerStyle(BannerConfig.CIRCLE_INDICATOR_TITLE_INSIDE) //显示圆形指示器和标题（水平显示)
            setImageLoader(BannerGlideImageLoader()) // 设置图片加载器
            setBannerAnimation(Transformer.Default)  // 设置banner动画效果
            setOnBannerListener { position: Int ->
                openWeb(bannerUrls[position])
            }
        }

    }
}