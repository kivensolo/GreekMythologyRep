package com.kingz.library.player

import android.media.TimedText

/**
 * author：KingZ
 * date：2019/7/30
 * description： 播放器事件回调接口
 */
interface IPlayerEventsCallBack {
    /**
     * 开始播放，调用play之后就会回调这方法
     * 可以在这个回调里面更新外部UI
     */
    fun onPlay()

    fun onPrepared()
    fun onError(what: Int, extra: Int): Boolean
    fun onBufferStart()
    fun onBufferEnd()
    fun onBufferingUpdate(percent: Int)
    fun onCompletion()
    fun onSeekComplete()
    /**
     * 播放器定时刷新回调  主要用于更新UI
     */
    fun onPlayerTimingUpdate()

    fun onInfo(what: Int, extra: Int): Boolean
    /**
     * @param text   字幕内容
     */
    fun onTimedText(text: TimedText?)

    /**
     * 承载播放的view创建
     */
    fun onViewCreated()

    /**
     * 承载播放的view变化
     */
    fun onViewChanged(format: Int, width: Int, height: Int)

    /**
     * 承载播放的view销毁
     */
    fun onViewDestroyed()
}