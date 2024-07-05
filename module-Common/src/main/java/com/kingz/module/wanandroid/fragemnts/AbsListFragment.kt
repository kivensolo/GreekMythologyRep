package com.kingz.module.wanandroid.fragemnts

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kingz.module.common.R
import com.kingz.module.common.base.IRvScroller
import com.kingz.module.common.databinding.FragmentRefreshLayoutBinding
import com.kingz.module.common.databinding.IncludeBaseRecyclerViewBinding
import com.kingz.module.common.utils.RvUtils
import com.scwang.smart.refresh.layout.SmartRefreshLayout
import com.zeke.reactivehttp.base.BaseReactiveViewModel
import kotlinx.coroutines.cancel

/**
 * author：ZekeWang
 * date：2022/1/29
 * description：MVVM模式的列表Fragment
 * 负责refreshLayout & recyclerView的公共初始化
 */
abstract class AbsListFragment<T : BaseReactiveViewModel> :
    CommonFragment<T>(),IRvScroller {
    private lateinit var layoutBinding: FragmentRefreshLayoutBinding
    private lateinit var rcViewBinding: IncludeBaseRecyclerViewBinding
    protected lateinit var mRecyclerView: RecyclerView
    protected var refreshLayout: SmartRefreshLayout? = null
    // 当前页数
    protected var mCurPage = 0
    protected var mPageCount = 0
    // 优化: 状态通过swipeRefreshLayout内部状态RefreshState判断
    protected var isLoadingMore = false

    override fun getLayoutResID(): Int  = R.layout.fragment_refresh_layout

    override fun inflateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        layoutBinding = FragmentRefreshLayoutBinding.inflate(inflater)
        rcViewBinding = IncludeBaseRecyclerViewBinding.inflate(inflater)
        //TODO include的布局，如何使用ViewBinding
        rootView = layoutBinding.root
        return rootView
    }

    override fun initView() {
        super.initView()
        multiStatusView = layoutBinding.statusView
        mRecyclerView = rcViewBinding.recyclerView
        mRecyclerView.run {
            isVerticalScrollBarEnabled = true
            layoutManager = LinearLayoutManager(context)
            itemAnimator = DefaultItemAnimator()
            addItemDecoration(DividerItemDecoration(activity, DividerItemDecoration.VERTICAL))
        }
        refreshLayout = layoutBinding.swipeRefreshLayout
    }

    override fun onDestroyView() {
        super.onDestroyView()
        lifecycleScope.cancel()
        refreshLayout?.setOnRefreshLoadMoreListener(null)
        refreshLayout = null
    }

    override fun scrollToTop() {
        RvUtils.smoothScrollTop(mRecyclerView)
    }

    override fun scrollToTopRefresh() {}
}