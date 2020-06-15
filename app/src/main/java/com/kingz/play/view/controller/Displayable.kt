package com.kingz.play.view.controller

/**
 * author：KingZ
 * date：2019/7/31
 * description：组件显示控制接口
 */
interface Displayable {
    val isShown: Boolean
    fun show()
    fun close()
    fun bringToFront()
}