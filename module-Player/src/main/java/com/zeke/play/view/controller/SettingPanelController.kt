package com.zeke.play.view.controller

import android.view.View
import android.view.ViewGroup
import com.zeke.module_player.R

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