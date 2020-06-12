package com.kingz.library.player

import android.media.TimedText

/**
 * author：KingZ
 * date：2019/7/30
 * description：
 *  业务层级播放器事件回调接口
 */
interface IPlayerEventsCallBack {
    /**
     * 开始播放，调用play之后就会回调这方法
     * 可以在这个回调里面更新外部UI
     */
    fun onPlay()

    fun onPrepared()

    fun onError(what: Int, extra: Int)

    fun onBufferStart()

    fun onBufferEnd()

    fun onBufferingUpdate(percent: Int)

    fun onCompletion()

    fun onSeekComplete()

    fun onInfo(what: Int, extra: Int): Boolean

    /**
     * @param text   字幕内容
     */
    fun onTimedText(text: TimedText?)

    fun onViewCreated()

    fun onViewChanged(format: Int, width: Int, height: Int)

    fun onViewDestroyed()

     /**
     * 播放器定时刷新回调  主要用于更新UI
     */
    fun onPlayerTimingUpdate()
}