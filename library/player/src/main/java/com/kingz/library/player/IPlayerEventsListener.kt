package com.kingz.library.player

import android.media.TimedText

/**
 * author：KingZ
 * date：2019/7/30
 * description：
 *  业务层级播放器事件回调接口
 */
interface IPlayerEventsListener {
    /**
     * 开始播放，调用play之后就会回调这方法
     * 可以在这个回调里面更新外部UI
     */
    fun onPlay()

    fun onPrepared(player:IPlayer)

    fun onPrepareTimeout(xmp: IPlayer)

    fun onError(player:IPlayer,what: Int, extra: Int):Boolean

    fun onBuffering(xmp: IPlayer, buffering: Boolean, percentage: Float)

    fun onBufferTimeout(xmp: IPlayer)

    fun onCompletion(player:IPlayer)

    fun onSeekComplete(player:IPlayer)

    fun onSeekComplete(xmp: IPlayer, pos: Long)

    fun onInfo(player:IPlayer, what: Int, extra: Int): Boolean

    fun onVideoSizeChanged(
        player: IPlayer,
        mVideoWidth: Int,
        mVideoHeight: Int
    )

    fun onVideoFirstFrameShow(player: IPlayer)

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