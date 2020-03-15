package com.zeke.ktx.fragments

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.kingz.customdemo.R
import com.zeke.ktx.activities.indicator.ScrollableTabExampleActivity
import com.zeke.ktx.base.BaseFragment


/**
 * author：KingZ
 * date：2020/2/16
 * description：MagicIndicator的使用demo
 */
class MagicIndicatorDemoFragment: BaseFragment(){
    override fun getLayoutId(): Int {
        return R.layout.fragment_indicator_demo_main_layout
    }

    override fun onViewCreated() {
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        activity!!.findViewById<View>(R.id.scrollable_tab)
                .setOnClickListener{
                    startActivity(Intent(activity, ScrollableTabExampleActivity::class.java))
                }
        //todo 对其他View进行监听
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

}