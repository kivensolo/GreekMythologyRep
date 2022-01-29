package com.zeke.home.wanandroid

import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.kingz.base.factory.ViewModelFactory
import com.kingz.module.home.R
import com.kingz.module.wanandroid.adapter.KnowledgeTreeAdapter
import com.kingz.module.wanandroid.bean.KnowledgeTreeBean
import com.kingz.module.wanandroid.fragemnts.CommonFragment
import com.kingz.module.wanandroid.viewmodel.WanAndroidViewModelV2
import com.zeke.home.wanandroid.viewmodel.HomeViewModel
import com.zeke.kangaroo.zlog.ZLog

/**
 * date：2022/01/28
 * description：首页 - 知识体系 Fragment
 */
class KnowledgeTreeFragment : CommonFragment<WanAndroidViewModelV2>() {
    companion object {
        fun getInstance(): KnowledgeTreeFragment = KnowledgeTreeFragment()
    }

    override val viewModel: HomeViewModel by viewModels {
        ViewModelFactory.build { HomeViewModel() }
    }
    private val mAdapter: KnowledgeTreeAdapter by lazy {
        KnowledgeTreeAdapter()
    }

    override fun getLayoutResID(): Int {
        return R.layout.fragment_common_page  //公共fragment改个名 改为refresh之内的
    }

    override fun initViewModel() {
        super.initViewModel()
        viewModel.systemLiveData.observe(this, Observer {
            if (it == null) {
                ZLog.d("artical LiveData request error. result is null.")
                showErrorStatus()
                return@Observer
            }
            //TODO 设置数据  并检查是否显示数据UI
            mAdapter.setList(it.data)
            dismissLoading()
        })
    }

    override fun onViewCreated() {
        //doNothing
        ZLog.d("onViewCreated")
        //TODO id设置为 驼峰式
        rootView?.findViewById<RecyclerView>(R.id.recycler_view)?.apply {
            layoutManager = linearLayoutManager
            itemAnimator = DefaultItemAnimator()
            addItemDecoration(DividerItemDecoration(activity,DividerItemDecoration.VERTICAL))
            adapter = mAdapter
        }
    }

    override fun initView() {
        super.initView()
        mAdapter.run {
            setOnItemClickListener { adapter, view, position ->
                val item = adapter.data[position] as KnowledgeTreeBean
            }
        }
    }

    override fun initData() {
        viewModel.getSystemInfo()
    }
}
