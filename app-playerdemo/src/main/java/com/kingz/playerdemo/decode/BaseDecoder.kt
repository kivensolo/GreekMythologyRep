package com.kingz.playerdemo.decode

import android.media.MediaCodec
import android.media.MediaExtractor
import android.media.MediaFormat
import android.os.Build
import android.util.Log
import com.kingz.playerdemo.extractor.AMExtractor
import java.nio.ByteBuffer

enum class TrackType {
    VIDEO, AUDIO
}

/**
 * author：ZekeWang
 * date：2021/4/16
 * description：解码基类，用于解码音视频流
 * 默认同步解码
 * 若需使用异步解码,构造函数中设置isAsync为true即可。
 *
 * 在 5.0 之后，google 建议使用异步解码的方式去使用 MediaCodec
 */
abstract class BaseDecoder(private val playUrl: String,
                           protected val isAsync:Boolean = false) : Runnable {
    val TAG: String = javaClass.simpleName
    lateinit var baseExtractor: AMExtractor
    lateinit var mMediaCodec: MediaCodec
    lateinit var mediaFormat: MediaFormat
    var isStreamEnd = false
    // 当前帧的pts时间戳(从0开始)
    var mPresentationTimeUs = 0L
    // 输出数据系统时间戳
    var ouputTimeStamp = -1L

    var inputErrorCount = 0
    var outputErrorCount = 0
    @Suppress("PropertyName")
    val DEQUEUE_TIMEOUT_US = 10 * 1000L  // 10ms

    // Rate of play. Default is x1.0
    var mPlaybackRate = 1.0f

    /**
     * 初始化AMExtractor
     * 在子类run之前调用
     */
    protected fun initAMExtractor(){
        // STATE --- Executing (Can deal with data) ---> Flushed
        try {
            baseExtractor = AMExtractor(playUrl)
            val decodeType = decodeType()

            mediaFormat = if(
                decodeType == TrackType.VIDEO) {
                    baseExtractor.videoFormat!!
                } else {
                    baseExtractor.audioFormat!!
            }

            //选择要解析的轨道
            baseExtractor.selectTrack(
                if(decodeType == TrackType.VIDEO) {
                    baseExtractor.videoTrackId
                } else {
                    baseExtractor.audioTrackId
            })

            val mime = mediaFormat.getString(MediaFormat.KEY_MIME)

            //创建解码器  STATE --- Uninitialized
            mMediaCodec = MediaCodec.createDecoderByType(mime)
            if (isAsync) {
                setAsyncCallback()
            }
            // STATE --- Configured
            configure()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun run() {
        @Suppress("SENSELESS_COMPARISON")
        if(mMediaCodec == null){
            throw IllegalStateException("Please init AMExtractor first !")
        }
        // STATE --- Executing (Can deal with data) ---> Flushed
        mMediaCodec.start()
        if (!isAsync) { // 同步解码
            try {
                val bufferInfo = MediaCodec.BufferInfo()
                while (!isStreamEnd) {
                    dealInputData(getMediaExtractor())
                    if (handleOutputData(bufferInfo)) {
                        break
                    }
                }
                release()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    /**
     * 通过MediaExtractor从inputBuffer中读取数据
     * 并输入至MediaCodoc
     * @param extractor: MediaExtractor
     */
    private fun dealInputData(extractor: MediaExtractor) {
        // 获取有效输入缓冲区的索引，如果当前没有可用的缓冲区，则返回-1。
        val availableBufferIndex: Int = mMediaCodec.dequeueInputBuffer(DEQUEUE_TIMEOUT_US)
        if (availableBufferIndex >= 0) {
            val inputBuffer: ByteBuffer = (if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                mMediaCodec.getInputBuffer(availableBufferIndex)
            } else {
                mMediaCodec.inputBuffers[availableBufferIndex]
            }) ?: return

            /**
             * 拿到视频的当前帧的buffer，读取采样数据的buffer大小
             * 通过MediaExtractor检索当前已编码的样例，
             * 并将其存储在从给定偏移量开始的字节缓冲区中。
             * 返回样本大小(如果*没有更多的样本，则返回-1)
             */
            val size = extractor.readSampleData(inputBuffer, 0)

            mPresentationTimeUs = extractor.sampleTime
            val flag = extractor.sampleFlags

            if (size < 0) {
                Log.e(TAG, "Stream is end!")
                mMediaCodec.queueInputBuffer(availableBufferIndex,
                    0,0,0, MediaCodec.BUFFER_FLAG_END_OF_STREAM)
                isStreamEnd = true
                // seekTo到视频开头，并且重置解码器
                getMediaExtractor().seekTo(0,MediaExtractor.SEEK_TO_CLOSEST_SYNC)
                // reset decoder state
                mMediaCodec.flush()
                return
            }
            inputErrorCount = 0
            // 设置指定索引位置的buffer数据
            mMediaCodec.queueInputBuffer(availableBufferIndex, 0, size, mPresentationTimeUs, flag)
            // 拿到下一帧。若没有,则返回false(流结束)
            extractor.advance()
            return
        }else{
            inputErrorCount++
            if (inputErrorCount > 10) {
                inputErrorCount = 0
                Log.e(TAG, "Input buffer is busy during [DEQUEUE_TIMEOUT_US x 10]")
            }
            return
        }
    }

    /**
     * 处理OutBuffer
     * 解码输出交给子类
     * @return 流的解析是否finish
     */
    protected abstract fun handleOutputData(outBufferInfo: MediaCodec.BufferInfo):Boolean

    /**
     * 判断需要解码的类型
     * 视频、音频
     */
    protected abstract fun decodeType():TrackType

    /**
     * 获取Android提供的MediaExtractor
     */
    protected fun getMediaExtractor(): MediaExtractor {
        return baseExtractor.mMediaExtractor
    }

    /**
     * 由子类去自由实现mMediaCodec的配置
     */
    protected abstract fun configure()

    /**
     * 设置Codec的异步回调
     * 5.0之后推荐使用,需要在
     * @see configure()之前调用
     */
    protected abstract fun setAsyncCallback()

    /**
     * Set the play speed for playback.
     * @param rate 0.5~2.0f
     */
    fun setPlaybackRate(rate:Float){
        mPlaybackRate = rate
        syncPlaybackRate()
    }

    protected abstract fun syncPlaybackRate()

    protected abstract fun preDecode()

    protected fun release() {
        try {
            isStreamEnd = true
            //释放 mediacodec
            mMediaCodec.stop()
            mMediaCodec.release()
            //释放 MediaExtractor
            baseExtractor.release()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
