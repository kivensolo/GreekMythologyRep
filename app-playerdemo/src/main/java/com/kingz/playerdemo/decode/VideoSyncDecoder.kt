package com.kingz.playerdemo.decode

import android.media.MediaCodec
import android.util.Log
import android.view.Surface

/**
 * author：ZekeWang
 * date：2021/4/16
 * description：
 *  视频同步解码器
 */
class VideoSyncDecoder(
    private val surface: Surface,
    playUrl: String) : BaseDecoder(playUrl) {
    init {
        initAMExtractor()
    }

    override fun decodeType(): TrackType = TrackType.VIDEO

    override fun configure() {
        mMediaCodec.configure(mediaFormat, surface, null, 0)
    }

    /**
     * MediaCodec 输出数据至OutBuffer
     * @param outBuffer: 用于储存视频输出数据的buffer
     */
    override fun handleOutputData(outBuffer: MediaCodec.BufferInfo): Boolean {
        val outBufferIndex = mMediaCodec.dequeueOutputBuffer(outBuffer, 20 * 1000L)!!
        if (outBufferIndex >= 0) {
            Log.i(TAG, "------> OutData, time(Us):$timeStamp")
            //释放指定索引位置的buffer数据,并渲染到Surface上
            mMediaCodec.releaseOutputBuffer(outBufferIndex, true)
            outputErrorCount = 0
        }
        // 流是否解析完毕
        if((outBuffer.flags and MediaCodec.BUFFER_FLAG_END_OF_STREAM) != 0){
            Log.e(TAG, "Video OutputBuffer BUFFER_FLAG_END_OF_STREAM")
            return true
        }
        // 未解析完毕时, outBufferIndex < 0 则认为查找失败 进行重试机制
//        if (outputErrorCount > 10) {
//            outputErrorCount = 0
//            Log.e(TAG, "输出超过错误上限")
//            return true
//        }
//        outputErrorCount++
//        handleOutputData(outBuffer)
        return false
    }

    fun pauseMedia() {
        mMediaCodec.stop()
    }

    fun startMedia() {
        mMediaCodec.start()
    }

}