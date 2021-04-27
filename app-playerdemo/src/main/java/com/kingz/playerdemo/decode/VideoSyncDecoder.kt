package com.kingz.playerdemo.decode

import android.media.MediaCodec
import android.media.MediaExtractor
import android.media.MediaFormat
import android.os.Build
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
    private var renderDelta:Long = -1

    init {
        Log.d(TAG, "init() with async? --- $isAsync")
        initAMExtractor()
    }

  /*  private val synRenderMsg: Int = 0x0001
    var mPTSSyncHandler = Handler {
        when(it.what){
            synRenderMsg -> {
                getMediaExtractor().advance()
                true
            }
            else -> { false }
        }
    }*/

    override fun decodeType(): TrackType = TrackType.VIDEO

    override fun configure() {
        mMediaCodec.configure(mediaFormat, surface, null, 0)
    }

    override fun setAsyncCallback() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mMediaCodec.setCallback(object : MediaCodec.Callback() {

                override fun onOutputBufferAvailable(codec: MediaCodec,
                                                     index: Int,
                                                     info: MediaCodec.BufferInfo) {
                    if (info.flags and MediaCodec.BUFFER_FLAG_CODEC_CONFIG != 0) {
                        codec.releaseOutputBuffer(index, false)
                        return
                    }
                    synRenderWithPTS(info)
                    codec.releaseOutputBuffer(index, true)
                    checkStreamEnd(info)
                }

                override fun onInputBufferAvailable(codec: MediaCodec, index: Int) {
                    //从解码器中拿到输入buffer，让Client填充数据
                    val inputBuffer = codec.getInputBuffer(index)
                    val size = baseExtractor.mMediaExtractor.readSampleData(inputBuffer, 0)

                    mPresentationTimeUs = getMediaExtractor().sampleTime
                    val flag = getMediaExtractor().sampleFlags
                    if(size < 0){
                        codec.queueInputBuffer(index,
                    0,0,0, MediaCodec.BUFFER_FLAG_END_OF_STREAM)
                        isStreamEnd = true
                        return
                    }
                      // 设置指定索引位置的buffer数据
                    mMediaCodec.queueInputBuffer(index, 0, size, mPresentationTimeUs, flag)
                    //Post 下一帧处理
                }

                override fun onOutputFormatChanged(codec: MediaCodec, format: MediaFormat) {}

                override fun onError(codec: MediaCodec, e: MediaCodec.CodecException) {
                    codec.reset()
                }

            })
        }
    }

    /**
     * 画面的倍速是基于音频的PTS，所以无需单独做倍速同步
     */
    override fun syncPlaybackRate() {
        //do nothing
    }

    override fun preDecode() {
    }

    /**
     * 消费从Codec中输出的outBuffer数据
     * @param outBufferInfo: 用于储存视频输出数据的buffer
     */
    override fun handleOutputData(outBufferInfo: MediaCodec.BufferInfo): Boolean {
        // 获取可使用的缓冲区索引
        val outBufferIndex = mMediaCodec.dequeueOutputBuffer(outBufferInfo, DEQUEUE_TIMEOUT_US)
        if (outBufferIndex >= 0) {
            synRenderWithPTS(outBufferInfo)
            //释放指定索引位置的buffer，并渲染到 Surface 中
            mMediaCodec.releaseOutputBuffer(outBufferIndex, true)
            outputErrorCount = 0
        } else if (outBufferIndex == MediaCodec.INFO_OUTPUT_BUFFERS_CHANGED) {
            Log.w(TAG, "The output buffers have changed")
        } else if (outBufferIndex == MediaCodec.INFO_OUTPUT_FORMAT_CHANGED) {
            // 输出的format已经改变，后续数据会按照新的format输出,
            // 通过getOutputFormat()返回一个新的format
        } else {
            outputErrorCount++
            if (outputErrorCount > 10) {
                outputErrorCount = 0
                Log.e(TAG, "Out buffer is busy during [DEQUEUE_TIMEOUT_US x 10]")
            }
        }
        return checkStreamEnd(outBufferInfo)
    }

    /**
     * 用时间戳来矫正pts,使得画面渲染的速度是符合pts的要求
     */
    private fun synRenderWithPTS(outBufferInfo: MediaCodec.BufferInfo) {
        if (ouputTimeStamp == -1L) {
            ouputTimeStamp = System.currentTimeMillis()
        }
        ptsTimeStampMs = outBufferInfo.presentationTimeUs / 1000
        decodeFrameTimeMs = System.currentTimeMillis() - ouputTimeStamp
        renderDelta = ptsTimeStampMs - decodeFrameTimeMs
        if (renderDelta > 0) { // 画面解码时间间隔小于显示时间间隔要求
            try {
                Thread.sleep(renderDelta)
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }
        }
    }

    /**
     * 流是否解析完毕
     */
    private fun checkStreamEnd(info: MediaCodec.BufferInfo):Boolean{
        isStreamEnd = info.flags and MediaCodec.BUFFER_FLAG_END_OF_STREAM != 0
        if(isStreamEnd){
            Log.e(TAG, "Video OutputBuffer BUFFER_FLAG_END_OF_STREAM")
            getMediaExtractor().seekTo(0, MediaExtractor.SEEK_TO_CLOSEST_SYNC)
            mMediaCodec.flush() // reset decoder state
        }
        return isStreamEnd
    }

    /**
     * 获取当前播放时间(ms)
     */
    fun getCurrentPos():Long{
        return  ptsTimeStampMs
    }

    fun pauseMedia() {
        mMediaCodec.stop()
    }

    fun startMedia() {
        mMediaCodec.start()
    }

}