package com.kingz.play.view.controller

import android.view.View
import android.view.ViewGroup
import com.kingz.customdemo.R
import kotlinx.android.synthetic.main.bitmap_demo_layout.*
import kotlinx.android.synthetic.main.progressbar_test.view.*

/**
 * author：KingZ
 * date：2020/6/14
 * description：播放器设置页面的Controller
 * TODO 待实现
 */
class SettingPanelController(val parentView:View):BaseController() {

    init {
        rootView = View.inflate(parentView.context, R.layout.player_setting_panel,null)
        if(parentView is ViewGroup){
            parentView.addView(rootView,
                    ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.MATCH_PARENT))
        }
    }

    override fun show() {
        // TODO 动画执行
        rootView.visibility = View.VISIBLE
    }
}