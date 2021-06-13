package com.kingz.library.player

import android.graphics.Bitmap
import android.net.Uri
import android.view.Surface
import android.view.SurfaceHolder
import android.view.View

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

    fun stop()

    fun destory()

    fun seekTo(msec: Long)

    fun setDataSource(uri: Uri?)

    //TODO
    fun setDataSource(url: String)

    /**
     * 初始化画面渲染的View
     * SurfaceView或者TextureView
     */
    fun initRenderView(renderView: View?)


    // <editor-fold defaultstate="collapsed" desc="Getter & Setter">
    fun getIPlayer(): IPlayer

    fun setPlayerEventListener(callBack: IPlayerEventsListener?)

    fun selectAudioTrack(audioTrackIndex: Int)

    fun setDisplayHolder(holder: SurfaceHolder?)

    fun setSurface(surface: Surface?)

    /**
     * 设置音轨
     *
     * @param stcTrackInfo
     */
    fun setAudioTrack(stcTrackInfo: TrackInfo?)

    fun getAudioTrack(): IntArray

    /**
     * 设置声道
     *
     * @param soundTrack
     */
    fun setSoundTrack(soundTrack: String?)

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
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="功能扩展">
    /**
     * 设置缓冲超时的阈值
     *
     * @param threshold
     */
    fun setBufferTimeOutThreshold(threshold: Long)

    /**
     * 设置镜像播放 TODO 未实现
     */
    fun setMirrorPlay()

    /**
     * 获取当前缓冲速度
     *
     * @return 单位KB/s
     */
    fun getCurrentLoadSpeed(): Float

    /**
     * 外部设置ffmpeg参数方法
     *
     * @param playerOptionCategory 设置类型
     * @param optionName           参数名称
     * @param optionValue          参数值
     */
    fun setPlayerOptions( playerOptionCategory: Int,
                          optionName: String,
        optionValue: String
    )

    fun setPlayerOptions(
        playerOptionCategory: Int,
        optionName: String,
        optionValue: Long
    )

    // </editor-fold>

    fun isPlaying():Boolean

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

}