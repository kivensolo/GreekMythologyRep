package com.kingz.playerdemo.decode

import android.media.MediaCodec
import android.media.MediaExtractor
import android.media.MediaFormat
import android.util.Log
import com.kingz.playerdemo.extractor.AMExtractor

/**
 * author：ZekeWang
 * date：2021/4/16
 * description：解码基类，用于解码音视频
 */
abstract class BaseDecoder(private val playUrl: String) : Runnable {
    val TAG: String = BaseDecoder::class.java.simpleName
    lateinit var baseExtractor: AMExtractor
    lateinit var mMediaCodec: MediaCodec
    lateinit var mediaFormat: MediaFormat
    var isStreamEnd = false
    //数据显示的时间戳
    var timeStamp = 0L

    var inputErrorCount = 0
    var outputErrorCount = 0
    val DEQUEUE_TIMEOUT_US = 20 * 1000L  // 10ms

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
            // STATE --- Configured
            configure()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun run() {
        // STATE --- Executing (Can deal with data) ---> Flushed
        mMediaCodec?.start()

        try {
            val bufferInfo = MediaCodec.BufferInfo()
            while (!isStreamEnd) {
                dealInputBuffer(getMediaExtractor())
                //解码输出交给子类
                val isFinish: Boolean = handleOutputData(bufferInfo)
                if(isFinish){
                    break
                }
            }
            done()
        }catch (e: Exception) {
            e.printStackTrace()
        }

    }

    /**
     * 通过MediaExtractor从inputBuffer中读取数据
     * 并输入至MediaCodoc
     */
    private fun dealInputBuffer(extractor: MediaExtractor) {
        //返回可以使用的输入buffer索引
        val bufferIndex: Int = mMediaCodec.dequeueInputBuffer(DEQUEUE_TIMEOUT_US)

        // buffer有效
        if (bufferIndex >= 0) {
            val inputBuffer = mMediaCodec.inputBuffers
//          var input: ByteBuffer? = null
//          if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//              input = mMediaCodec?.getInputBuffer(bufferIndex)
//          } else {
//            input = inputBuffer?.get(bufferIndex)
//          }
            // 获取的有效ByteBuffer
            val tmpByteBuffer = inputBuffer[bufferIndex]

            /**
             * 拿到视频的当前帧的buffer，读取采样数据的buffer大小
             * 通过MediaExtractor检索当前已编码的样例，
             * 并将其存储在从给定偏移量开始的字节缓冲区中。
             * 返回样本大小(如果*没有更多的样本，则返回-1)
             */
            val size = extractor.readSampleData(tmpByteBuffer, 0)

            timeStamp = extractor.sampleTime
            val flag = extractor.sampleFlags

//            Log.i(TAG, "InputData time(us):$timeStamp    size(byte):$size   ------>")
            if (size < 0) {
                // 结束,传递 end-of-stream 标志
                Log.e(TAG, "视频流结束")
                mMediaCodec.queueInputBuffer(bufferIndex,0,0,0,
                                        MediaCodec.BUFFER_FLAG_END_OF_STREAM)
                isStreamEnd = true
                return
            }
            inputErrorCount = 0
            // 设置指定索引位置的buffer数据
            mMediaCodec.queueInputBuffer(bufferIndex, 0, size, timeStamp, flag)
            // 拿到下一帧。若没有,则返回false(流结束)
            extractor.advance()
            return
        }

        if (inputErrorCount > 10) {
            inputErrorCount = 0
            Log.e(TAG, "数据注入超过错误上限")
            return
        }
        outputErrorCount++
        dealInputBuffer(extractor)
    }

    /**
     * 处理OutBuffer
     * @return 流的解析是否finish
     */
    protected abstract fun handleOutputData(outBuffer: MediaCodec.BufferInfo):Boolean

    /**
     * 判断需要解码的类型
     * 视频、音频
     */
    protected abstract fun decodeType():TrackType

    private fun getMediaExtractor(): MediaExtractor {
        return baseExtractor.mMediaExtractor
    }

    /**
     * 由子类去自由实现mMediaCodec的配置
     */
    protected abstract fun configure()


    protected fun done() {
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

enum class TrackType {
    VIDEO, AUDIO
}