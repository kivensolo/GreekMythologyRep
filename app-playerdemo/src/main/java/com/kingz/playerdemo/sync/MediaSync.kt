package com.kingz.playerdemo.sync

import android.view.Surface
import com.kingz.playerdemo.decode.AudioSyncDecoder
import com.kingz.playerdemo.decode.VideoSyncDecoder
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

/**
 * author：ZekeWang
 * date：2021/4/27
 * description：音画同步控制器
 *
 * TODO
 * 抽帧和帧画面渲染用单独的线程处理
 * 硬解码所有的画面和render流程
 */
class MediaSync(
    private val surface: Surface?,
    playUrl: String   //文件源  后续抽离模块
) {
    @Volatile
    var audioRender: AudioSyncDecoder? = null
    @Volatile
    var videoRender: VideoSyncDecoder? = null
    private var mExecutorService: ExecutorService? = null
    // Current rate of playback
    private var mPlaybackRate = 1.0f

    @Volatile
    private var mPlayBaseClockOfns: Long = -1
    @Volatile
    private var mMainClock: Long = 0

    // Player stop state
    private var isStop = false


    companion object Conts{
        private const val DEFAULT_PLAYBACK_RATE = 1.0f
    }

    init {
        mExecutorService = Executors.newFixedThreadPool(2)

        // 需判断视频有没有音轨和声轨
        if(surface != null){
            videoRender =
                VideoSyncDecoder(surface, playUrl)
        }
        audioRender = AudioSyncDecoder(playUrl)
    }

    /**
     * 音画同步
      */
    private fun syncRender(aRender:VideoSyncDecoder?, vRender:AudioSyncDecoder?){
        val playbackRate = getPlaybackRate()
        if (mPlaybackRate != playbackRate) {
            mPlaybackRate = playbackRate
        }

        aRender?.let {
            it.setPlaybackRate(playbackRate)
            ////TODO 音频同步

        }

        vRender?.let {
            it.setPlaybackRate(playbackRate)
            ////TODO 画面同步
            val clock = System.nanoTime()
            if (mPlayBaseClockOfns <= 0) {
                mPlayBaseClockOfns = clock
            }
            val mainClock:Long = ((clock - mPlayBaseClockOfns) / 1000 * playbackRate).toLong()
            doRenderVideo(it,mainClock)

        }
    }

    /**
     * 视频画面渲染
     */
    private fun doRenderVideo(vRender: AudioSyncDecoder, mainClock: Long): Long {
//        while (!isStop) {
//            val ret: Long = vRender.doRender(mainClock)
//            if (ret == 0L) { // render again
//                continue
//            }
//            // ret <0 : no more frames
//            return if (ret < 0) {  -1 } else ret
//        }
        return -1
    }

    fun start(){
        // 并发启动音画处理器  但是如果出现卡顿掉帧，则会出现音画不同步的问题。
        mExecutorService?.execute(audioRender)
        mExecutorService?.execute(videoRender)
    }

    fun pause(){
        if(surface != null){
            videoRender?.pauseMedia()
        }
        audioRender?.pauseMedia()
    }

    /**
     * 供提供给外部, 设置播放速率的方法
     */
    fun getPlaybackRate():Float{
        return DEFAULT_PLAYBACK_RATE
    }


}
