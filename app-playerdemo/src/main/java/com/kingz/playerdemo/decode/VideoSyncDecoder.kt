package com.kingz.playerdemo.decode

import android.media.MediaCodec
import android.util.Log
import android.view.Surface



/**
 * author：ZekeWang
 * date：2021/4/16
 * description：
 *  视频解码器 ------ 同步方式
 */
class VideoSyncDecoder(
    private val surface: Surface,
    playUrl: String) : BaseDecoder(playUrl) {

    // 当前帧pts的时间戳(ms)
    private var ptsTimeStampMs:Long = -1
    // 当前帧解码后的真实时间戳
    private var decodeFrameTimeMs:Long = -1
    // pts时间戳与真实解码时间的差值
    private var timeDiff:Long = -1

    init {
        initAMExtractor()
    }

    override fun decodeType(): TrackType = TrackType.VIDEO

    override fun configure() {
        mMediaCodec.configure(mediaFormat, surface, null, 0)
    }

    /**
     * MediaCodec 输出数据至OutBuffer
     * @param outBufferInfo: 用于储存视频输出数据的buffer
     */
    override fun handleOutputData(outBufferInfo: MediaCodec.BufferInfo): Boolean {
        val outBufferIndex = mMediaCodec.dequeueOutputBuffer(outBufferInfo, 20 * 1000L)!!
        if (outBufferIndex >= 0) {
            // 屏蔽频繁的打印
            // Log.i(TAG, "------> Video OutData, time(Us):$mPresentationTimeUs")
            if(timeStamp == -1L){
                timeStamp = System.currentTimeMillis();
            }
            synRenderWithPTS(outBufferInfo,timeStamp)
            //释放指定索引位置的buffer，并渲染到 Surface 中
            mMediaCodec.releaseOutputBuffer(outBufferIndex, true)
            outputErrorCount = 0
        }
        return isEndOfStream(outBufferInfo)

//        if (isEndOfStream(outBufferInfo)) return true
        // 未解析完毕时, outBufferIndex < 0 则认为查找失败 进行重试机制
//        if (outputErrorCount > 10) {
//            outputErrorCount = 0
//            Log.e(TAG, "输出超过错误上限")
//            return true
//        }
//        outputErrorCount++
//        handleOutputData(outBuffer)
//        return false
    }

    /**
     * 流是否解析完毕
     */
    private fun isEndOfStream(outBufferInfo: MediaCodec.BufferInfo): Boolean {
        if ((outBufferInfo.flags and MediaCodec.BUFFER_FLAG_END_OF_STREAM) != 0) {
            Log.e(TAG, "Video OutputBuffer BUFFER_FLAG_END_OF_STREAM")
            //TODO 增加画面销毁的可控选择
            return true
        }
        return false
    }

    /**
     * 用时间戳来矫正pts,使得画面渲染的速度是符合pts的要求
     */
    private fun synRenderWithPTS(outBufferInfo: MediaCodec.BufferInfo, startTimeMs: Long) {
        ptsTimeStampMs = outBufferInfo.presentationTimeUs / 1000
        decodeFrameTimeMs = System.currentTimeMillis() - startTimeMs
        timeDiff = ptsTimeStampMs - decodeFrameTimeMs
        if (timeDiff > 0) { // 画面解码时间间隔小于显示时间间隔要求
            try {
                Thread.sleep(timeDiff)
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }
        }
    }

    fun pauseMedia() {
        mMediaCodec.stop()
    }

    fun startMedia() {
        mMediaCodec.start()
    }

}