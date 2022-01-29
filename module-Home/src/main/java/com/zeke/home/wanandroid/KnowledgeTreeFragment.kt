package com.zeke.home.wanandroid

import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.kingz.base.factory.ViewModelFactory
import com.kingz.module.home.R
import com.kingz.module.wanandroid.bean.KnowledgeTreeBean
import com.kingz.module.wanandroid.fragemnts.AbsListFragment
import com.kingz.module.wanandroid.viewmodel.WanAndroidViewModelV2
import com.zeke.home.wanandroid.adapter.KnowledgeTreeAdapter
import com.zeke.home.wanandroid.viewmodel.HomeViewModel
import com.zeke.kangaroo.zlog.ZLog

/**
 * date：2022/01/28
 * description：首页 - 知识体系 Fragment
 */
class KnowledgeTreeFragment : AbsListFragment<WanAndroidViewModelV2>() {
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
        return R.layout.fragment_refresh_layout  //公共fragment改个名 改为refresh之内的
    }

    override fun initViewModel() {
        super.initViewModel()
        viewModel.systemLiveData.observe(this, Observer {
            if (it == null) {
                ZLog.d("artical LiveData request error. result is null.")
                showErrorStatus()
                return@Observer
            }
            showContent()
            //TODO 设置数据  并检查是否显示数据UI
            mAdapter.setList(it.data)
        })
    }

    override fun initView() {
        super.initView()
        showLoading()
        mAdapter.run {
            setOnItemClickListener { adapter, view, position ->
                val item = adapter.data[position] as KnowledgeTreeBean
            }
        }
        mRecyclerView.apply {
            adapter = mAdapter
        }
    }

    override fun initData() {
        viewModel.getSystemInfo()
    }
}
