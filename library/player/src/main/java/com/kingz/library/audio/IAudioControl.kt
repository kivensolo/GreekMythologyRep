package com.kingz.library.audio

/**
 * author：ZekeWang
 * date：2021/1/25
 * description：多媒体音量控制
 */
interface IAudioControl {
    /**
     * 静音
     * @param isMute true:静音; false: 取消静音
     */
    fun mute(isMute: Boolean)

    /**
     * 增加媒体音量
     */
    fun increase()

    /**
     * 减少媒体音量
     */
    fun decrease()
}