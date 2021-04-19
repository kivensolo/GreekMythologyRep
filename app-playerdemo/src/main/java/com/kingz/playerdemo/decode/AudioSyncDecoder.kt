package com.kingz.playerdemo.decode

import android.media.*
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import java.nio.ByteBuffer

/**
 * author：ZekeWang
 * date：2021/4/16
 * description：
 *  音频解码器 ------ 同步方式
 *  使用AudioTrack
 *  https://blog.csdn.net/u011418943/article/details/107561111
 *
 *  兼容了5.0以下的版本
 */
@RequiresApi(Build.VERSION_CODES.LOLLIPOP)
class AudioSyncDecoder constructor(playUrl: String) : BaseDecoder(playUrl) {
    // 音频采样位数
    private var mPcmEncodeBit: Int = 0
    private var mAudioTrack: AudioTrack? = null

    init {
        initAMExtractor()
        initAudoTrack()
    }

    private fun initAudoTrack() {
        initPcmEncode()
        //音频采样率
        val sampleRate = mediaFormat.getInteger(MediaFormat.KEY_SAMPLE_RATE)
        // 获取视频通道数判断声道配置
        val channelCount = mediaFormat.getInteger(MediaFormat.KEY_CHANNEL_COUNT)
        val channelConfig = if (channelCount == 1) {
            AudioFormat.CHANNEL_OUT_MONO   //单声道
        } else {
            AudioFormat.CHANNEL_OUT_STEREO //立体声
        }

        // 一帧的最小buffer大小
        val minBufferSize = AudioTrack.getMinBufferSize(sampleRate, channelConfig, mPcmEncodeBit)

        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.KITKAT) {
            Log.d(TAG, "Init Audio Track <= KITKAT")
            // < 5.0
            val audioFormat = AudioFormat.ENCODING_PCM_16BIT
            mAudioTrack = AudioTrack(
                AudioManager.STREAM_MUSIC,
                sampleRate, channelConfig,
                audioFormat, minBufferSize,
                AudioTrack.MODE_STREAM
            )
        } else {
            Log.d(TAG, "Init Audio Track > KITKAT")
            // 设置音频信息属性 1.设置支持多媒体属性，比如audio，video; 2.设置音频格式，比如 music
            val audioAttributes = AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_MEDIA)
                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                .build()

            //设置音频数据(量化精度)
            val audioFormat = AudioFormat.Builder()
                .setSampleRate(sampleRate)      //设置采样率
                .setEncoding(mPcmEncodeBit)     //设置采样位数
                .setChannelMask(channelConfig)  //设置声道
                .build()

            //采用流模式配置 audioTrack
            mAudioTrack = AudioTrack(
                audioAttributes,
                audioFormat,
                minBufferSize, // 只需要设置一帧的最小buffer即可
                AudioTrack.MODE_STREAM, //
                AudioManager.AUDIO_SESSION_ID_GENERATE
            )
        }
        //监听播放,等待音频数据
        mAudioTrack?.play()
    }

    override fun decodeType(): TrackType = TrackType.AUDIO

    override fun configure() {
        mMediaCodec.configure(mediaFormat, null, null, 0)
    }

    override fun handleOutputData(outBufferInfo: MediaCodec.BufferInfo): Boolean {
        // 获取可使用的缓冲区索引
        val outBufferIndex = mMediaCodec.dequeueOutputBuffer(outBufferInfo, DEQUEUE_TIMEOUT_US)
        if (outBufferIndex >= 0) {
            val outputBuffer: ByteBuffer? = if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                mMediaCodec.outputBuffers[outBufferIndex]
            } else {
                mMediaCodec.getOutputBuffer(outBufferIndex)
            }

            // 检测format是否改变
            if ((outBufferInfo.flags and MediaCodec.BUFFER_FLAG_CODEC_CONFIG) != 0) {
                val format = mMediaCodec.getOutputFormat(outBufferIndex)
                format.setByteBuffer("csd-0", outputBuffer)
                outBufferInfo.size = 0
                return false
            }

            if(Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP){
                val pcmByteData = ByteArray(outBufferInfo.size)
                if(outputBuffer != null){
                    outputBuffer.get(pcmByteData)
                    outputBuffer.clear()//用完后清空，复用
                }
                // 播放pcm数据
                mAudioTrack?.write(pcmByteData, 0, pcmByteData.size)
            }else{
                // 流式模式写数据到 AudioTrack中，排队阻塞等待播放,实现音频播放
                mAudioTrack?.write(outputBuffer!!, outBufferInfo.size, AudioTrack.WRITE_BLOCKING)
            }

            //释放指定索引位置的buffer数据,并渲染到Surface上
            mMediaCodec.releaseOutputBuffer(outBufferIndex, false)
            outputErrorCount = 0
            return false
        }else{
            outputErrorCount++
            if (outputErrorCount > 10) {
                Log.e(TAG, "Audio outbuffer is busy.")
            }
        }
        // 在所有解码后的帧都被渲染后，就可以停止播放了
        if ((outBufferInfo.flags and MediaCodec.BUFFER_FLAG_END_OF_STREAM) != 0) {
            Log.e(TAG, "Audio OutputBuffer BUFFER_FLAG_END_OF_STREAM")
            return true
        }
        return false
    }

    /**
     * 初始化音频数据格式
     */
    private fun initPcmEncode() {
        mPcmEncodeBit = if (mediaFormat.containsKey(MediaFormat.KEY_PCM_ENCODING)) {
            mediaFormat.getInteger(MediaFormat.KEY_PCM_ENCODING)
        } else {
            // 兼容<5.0
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