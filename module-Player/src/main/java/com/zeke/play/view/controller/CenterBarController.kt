package com.zeke.play.view.controller

import android.view.View
import com.zeke.module_player.R

/**
 * author：KingZ
 * date：2019/7/31
 * description：中部UI组件Controller
 * 目前只有一个暂停图标
 */
class CenterBarController(view: View) : BaseController() {
    // 播放暂停时的View
//    private val playPauseView: ImageView

    init {
        rootView = view.findViewById(R.id.play_pause_center)
    }
    fun setOnClickListener(listener: View.OnClickListener?) {
        rootView?.setOnClickListener(listener)
    }

    override fun show() {
        rootView?.visibility = View.VISIBLE
    }

}