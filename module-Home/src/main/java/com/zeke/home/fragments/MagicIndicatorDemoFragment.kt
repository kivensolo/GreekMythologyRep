package com.zeke.home.fragments

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.kingz.module.common.base.BaseFragment
import com.kingz.module.home.R
import com.zeke.home.ScrollableTabExampleActivity


/**
 * author：KingZ
 * date：2020/2/16
 * description：MagicIndicator的使用demo
 */
class MagicIndicatorDemoFragment: BaseFragment(){

    var requestFocusView:View ?= null

    override fun getLayoutId(): Int {
        return R.layout.fragment_indicator_demo_main_layout
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {}

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        requestFocusView = activity?.findViewById<View>(R.id.scrollable_tab)
            ?.apply {
                setOnClickListener {
                    startActivity(Intent(activity, ScrollableTabExampleActivity::class.java))
                }
        }
    }



    fun onClick(v: View?) {
        when (v!!.id){
//            R.id.fixed_tab
//            R.id.dynamic_tab
//            R.id.no_tab_only_indicator
//            R.id.tab_with_badge_view
//            R.id.work_with_fragment_container
//            R.id.load_custom_layout
//            R.id.custom_navigator
        }
    }

    override fun onDestroyView() {
        rootView = null
        requestFocusView?.setOnClickListener(null)
        super.onDestroyView()
    }

}