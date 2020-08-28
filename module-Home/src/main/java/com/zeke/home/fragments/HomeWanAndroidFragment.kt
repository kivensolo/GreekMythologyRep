package com.zeke.home.fragments

import android.os.Bundle
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kingz.base.BaseVMFragment
import com.kingz.base.CoroutineState
import com.kingz.base.factory.ViewModelFactory
import com.kingz.module.home.R
import com.zeke.home.adapter.ArticleDelegateAdapter
import com.zeke.home.adapter.HomeArticleAdapter
import com.zeke.home.repository.HomePageRepository
import com.zeke.home.viewmodel.MainPageViewModel
import com.zeke.kangaroo.utils.ZLog
import kotlinx.android.synthetic.main.activity_main.*

/**
 * 首页热门推荐(玩android)的Fragemnt
 */
class HomeWanAndroidFragment : BaseVMFragment<HomePageRepository, MainPageViewModel>() {
    lateinit var mRecyclerView: RecyclerView
    lateinit var articleAdapter:HomeArticleAdapter

    override val viewModel: MainPageViewModel by viewModels {
       ViewModelFactory.build { MainPageViewModel() }
    }

    override fun initViewModel() {
        super.initViewModel()
        // 初始化VIewModel的数据监听
        viewModel.articalLiveData.observe(this, Observer {
            if(it?.data != null && it.data?.datas != null){
                ZLog.d("存在文章数据,进行文章数据更新")
                articleAdapter.addAll(it.data?.datas)
                mRecyclerView.adapter = articleAdapter
                (mRecyclerView.adapter as HomeArticleAdapter).notifyDataSetChanged()

                if (viewModel.statusLiveData.value == CoroutineState.FINISH ||
                    viewModel.statusLiveData.value == CoroutineState.ERROR) {
                    swipeRefreshLayout?.isRefreshing = false
                }
            }
        })
    }

    override fun onViewCreated() {
        //doNothing
        ZLog.d("HomeHotPageFragment onViewCreated")
    }

    override fun getLayoutResID() = R.layout.fragment_common_page

    override fun initData(savedInstanceState: Bundle?) {
        articleAdapter = HomeArticleAdapter()
        // 添加委托Adapter
//        mAdapter.addDelegate(BannerDelegateAdapter())
//        mAdapter.addDelegate(ThreePicDelegateAdapter())
        articleAdapter.addDelegate(ArticleDelegateAdapter())

        // 获取网络数据
        viewModel.getArticalData("0")
    }

    override fun initView(savedInstanceState: Bundle?) {
        mRecyclerView = rootView.findViewById(R.id.recycler_view) as RecyclerView
        mRecyclerView.layoutManager = LinearLayoutManager(context)
    }

}