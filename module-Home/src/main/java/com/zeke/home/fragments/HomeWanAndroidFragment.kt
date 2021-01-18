package com.zeke.home.fragments

import android.app.Service
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.alibaba.android.arouter.launcher.ARouter
import com.kingz.base.BaseVMFragment
import com.kingz.base.CoroutineState
import com.kingz.base.factory.ViewModelFactory
import com.kingz.module.common.base.WADConstants
import com.kingz.module.common.bean.ArticleData
import com.kingz.module.common.repository.WanAndroidRepository
import com.kingz.module.common.router.RPath
import com.kingz.module.common.utils.ktx.SDKVersion
import com.kingz.module.common.viewmodel.WanAndroidViewModel
import com.kingz.module.home.R
import com.module.slide.SuperSwipeRefreshLayout
import com.zeke.home.adapter.ArticleDelegateAdapter
import com.zeke.home.adapter.HomeArticleAdapter
import com.zeke.kangaroo.utils.ZLog
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


/**
 * 首页热门推荐(玩android)的Fragemnt
 * 内部使用 SuperSwipeRefreshLayout
 */
class HomeWanAndroidFragment : BaseVMFragment<WanAndroidRepository, WanAndroidViewModel>() {
    lateinit var mRecyclerView: RecyclerView
    lateinit var articleAdapter: HomeArticleAdapter
    private var swipeRefreshLayout: SuperSwipeRefreshLayout? = null

    // 当前页数
    private var mCurPage = 1

    override val viewModel: WanAndroidViewModel by viewModels {
        ViewModelFactory.build { WanAndroidViewModel() }
    }

    override fun initViewModel() {
        super.initViewModel()
        viewModel.articalLiveData.observe(this, Observer {
            if (it?.data != null && it.data?.datas != null) {
                ZLog.d("存在文章数据,进行文章数据更新")
                articleAdapter.addAll(it.data?.datas)
                (mRecyclerView.adapter as HomeArticleAdapter).notifyDataSetChanged()

                if (viewModel.statusLiveData.value == CoroutineState.FINISH ||
                    viewModel.statusLiveData.value == CoroutineState.ERROR
                ) {
                    swipeRefreshLayout?.isRefreshing = false
                }
            }
        })

        viewModel.bannerLiveData.observe(this,  Observer {
                ZLog.d("Banner data onChanged(): " + it.data?.data?.size)
            if(it.data?.data?.size?:0 > 0){
                it.data?.data?.forEach { item ->
                    item.desc
                    ZLog.d("+1  desc = " + item.desc)
                }
                //TODO 刷新UI
            }
        })
    }

    override fun onViewCreated() {
        //doNothing
        ZLog.d("HomeHotPageFragment onViewCreated")
    }

    override fun getLayoutResID() = R.layout.fragment_common_page

    override fun initData(savedInstanceState: Bundle?) {
        viewModel.getBanner()

        articleAdapter = HomeArticleAdapter()
        articleAdapter.mOnItemClickListener = object : HomeArticleAdapter.OnItemClickListener {
            override fun onItemClick(v: View?, position: Int) {
                if (articleAdapter.count > position) {
                    openWeb(articleAdapter.getItem(position))
                }
            }
        }
        // 添加委托Adapter
//        mAdapter.addDelegate(BannerDelegateAdapter())
//        mAdapter.addDelegate(ThreePicDelegateAdapter())
        articleAdapter.addDelegate(ArticleDelegateAdapter())
        mRecyclerView.adapter = articleAdapter

        // 执行VIewModel的数据请求
        // 获取网络数据
        viewModel.getArticalData(0)
    }

    override fun initView(savedInstanceState: Bundle?) {
        mRecyclerView = rootView?.findViewById(R.id.recycler_view) as RecyclerView
        mRecyclerView.layoutManager = LinearLayoutManager(context)
        initSwipeRefreshLayout()
    }

    private fun initSwipeRefreshLayout() {
        swipeRefreshLayout = rootView?.findViewById(R.id.swipeRefreshLayout)!!
        swipeRefreshLayout?.setOnRefreshListener { direction ->
            if (direction == SuperSwipeRefreshLayout.Direction.TOP) {
                mCurPage = 1
                // 进行banner数据获取

                // 模拟刷新完毕
                lifecycleScope.launch {
                    delay(1000)
                    withContext(Dispatchers.Main) {
                        swipeRefreshLayout?.isRefreshing = false
                    }
                }
            }
            val vibrator = context?.getSystemService(Service.VIBRATOR_SERVICE) as Vibrator
            if (SDKVersion.afterOreo()) {
                vibrator.vibrate(
                    VibrationEffect.createOneShot(
                        70,
                        VibrationEffect.DEFAULT_AMPLITUDE
                    )
                )
            } else {
                vibrator.vibrate(70)
            }
        }
        swipeRefreshLayout?.isRefreshing = true
    }


    fun openWeb(data: ArticleData.DataBean.ArticleItem?) {
        ARouter.getInstance()
            .build(RPath.PAGE_WEB)
            .withString(WADConstants.KEY_URL, data?.link)
            .withString(WADConstants.KEY_TITLE, data?.title)
            .withString(WADConstants.KEY_AUTHOR, data?.author)
            .withBoolean(WADConstants.KEY_IS_COLLECT, data?.isCollect ?: false)
            .withInt(WADConstants.KEY_ID, data?.id ?: -1)
            .navigation(activity, 0x01)
    }

    override fun onViewDestory() {
        swipeRefreshLayout?.setOnRefreshListener(null)
        swipeRefreshLayout = null
        super.onViewDestory()
    }
}