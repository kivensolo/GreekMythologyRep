package com.zeke.ktx.fragments

import com.kingz.customdemo.R
import com.zeke.ktx.base.BaseFragment

/**
 * author：KingZ
 * date：2020/2/22
 * description：热门推荐页
 */
class CommonPageFragment: BaseFragment(){
    override fun getLayoutId(): Int {
        return R.layout.fragment_common_page
    }

    override fun initView() {
        loadStatusView = rootView.findViewById(R.id.load_status_view)
    }

    override fun initData() {
        loadStatusView?.showEmpty()
    }

}