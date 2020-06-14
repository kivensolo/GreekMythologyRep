package com.kingz.library.entity

/**
 * @author zeke.wang
 * @date 2020/4/28
 * @maintainer zeke.wang
 * @desc: 播放器显示模式： 全屏/小屏
 */
enum class ScreenMode(private val type:String) {
    FULL("Full"),
    SMALL("Small");

    override fun toString(): String {
        return type
    }
}