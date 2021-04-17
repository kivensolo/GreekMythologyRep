package com.kingz.playerdemo.decode

import android.media.*
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi

/**
 * author：ZekeWang
 * date：2021/4/16
 * description：
 *  音频解码器 ------ 同步方式
 *  使用AudioTrack
 *  https://blog.csdn.net/u011418943/article/details/107561111
 */
@RequiresApi(Build.VERSION_CODES.LOLLIPOP)
class AudioSyncDecoder constructor(playUrl: String) : BaseDecoder(playUrl) {
    // 音频采样率
    private var mPcmEncodeBit: Int = 0
    private var mAudioTrack: AudioTrack? = null

    init {
        initAMExtractor()
        initAudoTrack()
    }

    private fun initAudoTrack() {
        //
        initPcmEncode()
        //音频采样率
        val sampleRate = mediaFormat.getInteger(MediaFormat.KEY_SAMPLE_RATE)
        // 获取视频通道数
        val channelCount = mediaFormat.getInteger(MediaFormat.KEY_CHANNEL_COUNT)
        // 拿到声道
        val channelConfig = if (channelCount == 1) {
            AudioFormat.CHANNEL_IN_MONO
        } else {
            AudioFormat.CHANNEL_IN_STEREO //立体声
        }
        // 一帧的最小buffer大小
        val minBufferSize = AudioTrack.getMinBufferSize(sampleRate, channelConfig, mPcmEncodeBit)

        /**
         * 设置音频信息属性
         * 1.设置支持多媒体属性，比如audio，video
         * 2.设置音频格式，比如 music
         */
        val audioAttributes = AudioAttributes.Builder()
            .setUsage(AudioAttributes.USAGE_MEDIA)
            .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
            .build()

        /**
         * 设置音频数据
         * 1. 设置采样率
         * 2. 设置采样位数
         * 3. 设置声道
         */
        val audioFormat = AudioFormat.Builder()
            .setSampleRate(sampleRate)
            .setEncoding(AudioFormat.ENCODING_PCM_16BIT)
            .setChannelMask(channelConfig)
            .build()

        /**
         * 采用流模式配置 audioTrack
         * 所以只需要设置一帧的最小buffer即可，
         * 然后调用 play() 方法去等待数据
         */
        mAudioTrack = AudioTrack(
            audioAttributes,
            audioFormat,
            minBufferSize, //
            AudioTrack.MODE_STREAM, //
            AudioManager.AUDIO_SESSION_ID_GENERATE
        )
        //监听播放,等待音频数据
        mAudioTrack?.play()
    }

    override fun decodeType(): TrackType = TrackType.AUDIO

    override fun configure() {
        mMediaCodec.configure(mediaFormat, null, null, 0)
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun handleOutputData(outBuffer: MediaCodec.BufferInfo): Boolean {
        //FIXME java.lang.IllegalStateException Stop时
        val outBufferIndex = mMediaCodec.dequeueOutputBuffer(outBuffer, DEQUEUE_TIMEOUT_US)
        if (outBufferIndex >= 0) {
//            Log.i(TAG, "------>>>Audio OutData, time(Us):$mPresentationTimeUs")
            val outputBuffer = mMediaCodec.getOutputBuffer(outBufferIndex)
            // 流式模式写数据到 AudioTrack中，排队阻塞等待播放,实现音频播放
            mAudioTrack?.write(outputBuffer, outBuffer.size, AudioTrack.WRITE_BLOCKING)
            //释放指定索引位置的buffer数据,并渲染到Surface上
            mMediaCodec.releaseOutputBuffer(outBufferIndex, false)
            outputErrorCount = 0
            return false
        }
        // 在所有解码后的帧都被渲染后，就可以停止播放了
        if ((outBuffer.flags and MediaCodec.BUFFER_FLAG_END_OF_STREAM) != 0) {
            Log.e(TAG, "Audio OutputBuffer BUFFER_FLAG_END_OF_STREAM")
            return true
        }
        Log.e(TAG, "Audio 输出错误  outBufferIndex = $outBufferIndex")
//      outBufferIndex < 0 则认为查找失败
//      if (outputErrorCount > 10) {
//          outputErrorCount = 0
//          Log.e(TAG, "Audio 输出超过错误上限")
//          return true
//      }
//      outputErrorCount++
//      handleOutputData(outBuffer)
        return false
    }

    /**
     * 初始化音频采样率
     */
    private fun initPcmEncode() {
        mPcmEncodeBit = if (mediaFormat.containsKey(MediaFormat.KEY_PCM_ENCODING)) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                mediaFormat.getInteger(MediaFormat.KEY_PCM_ENCODING)
            } else {
                mediaFormat.getInteger("pcm-encoding")
            }
        } else {
            //默认采样率为 16bit
            AudioFormat.ENCODING_PCM_16BIT
        }
    }

    fun pauseMedia() {
        mMediaCodec.stop()
    }

    fun startMedia() {
        mMediaCodec.start()
    }
}