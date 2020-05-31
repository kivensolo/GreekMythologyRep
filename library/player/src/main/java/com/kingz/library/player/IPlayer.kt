package com.kingz.library.player

import android.graphics.Bitmap
import android.net.Uri
import android.view.Surface
import android.view.SurfaceHolder
import android.view.View
import java.lang.reflect.Array

/**
 * author：KingZ
 * date：2019/7/30
 * description：播放器对象顶层接口
 */
interface IPlayer {
    companion object {
        val TAG = IPlayer::class.java.simpleName
        const val MEDIA_ERROR_SEEK_STATUS = 0x1001
        const val MEDIA_ERROR_PLAY_STATUS = 0x1002
        const val MEDIA_ERROR_PAUSE_STATUS = 0x1003
        const val MEDIA_ERROR_CUSTOM_ERROR = 0x1011
        const val MEDIA_INFO_BUFFERING_START = 701
        const val MEDIA_INFO_BUFFERING_END = 702
        //视频第一帧显示出来
        const val MEDIA_INFO_VIDEO_RENDERING_START = 3
    }

    // -------------------- 播放操作api ----------
    fun play()

    fun pause()
    fun release()
    fun seekTo(msec: Long)
    // -------------------- 设置api ---------------
    fun setPlayerView(playView: View?)

    fun setPlayURI(uri: Uri?)
    fun setPlayerEventCallBack(callBack: IPlayerEventsCallBack?)
    fun selectAudioTrack(audioTrackIndex: Int)
    fun setDisplayHolder(holder: SurfaceHolder?)
    fun setSurface(surface: Surface?)
    val audioTrack: Array?
    /**
     * 设置播放速率
     * @param speed 速率 支持0.5-2.0
     */
    fun setSpeed(speed: Float)

    /**
     * 设置播放器的缓冲区大小
     *
     * @param bufferSize 缓冲区大小  单位kb
     */
    fun setBufferSize(bufferSize: Int)

    // -------------------- 读取api ----------
    val isPrepared: Boolean

    val isPlaying: Boolean
    val isBuffering: Boolean
    val isPaused: Boolean
    val currentURI: Uri?
    /**
     * Gets the duration of the file.
     * @return 流总长度 ms
     */
    val duration: Long

    /**
     * Gets the current playback position.
     * @return the current position in milliseconds
     */
    val currentPosition: Long

    /**
     * @return 缓冲的进度  ms
     */
    val bufferedPosition: Long

    /**
     * 获取当前耗费的流量
     * @return 单位byte
     */
    val tcpSpeed: Long

    /**
     * 获取当前帧的图
     * @return 位图数据
     */
    val currentThumb: Bitmap?

    val mediaPlayer: IPlayer?

}