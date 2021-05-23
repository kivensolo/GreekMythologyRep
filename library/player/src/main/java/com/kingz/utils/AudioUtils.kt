package com.kingz.utils

import android.content.Context
import android.media.AudioFormat
import android.media.AudioManager
import android.media.AudioRecord
import android.media.MediaRecorder
import android.os.Build
import com.kingz.library.audio.IAudioControl

/**
 * @author zeke.wang
 * @date 2020/6/19
 * @maintainer zeke.wang
 * @desc:  音频工具类，单例模式
 */
@Suppress("RedundantModalityModifier")
final class AudioUtils(context: Context): IAudioControl {

    private val mContext: Context = context

    /**
     * 缓冲区字节大小
     */
    private var bufferSizeInBytes = 0
    /**
     * 音频获取源
     */
    private val audioSource = MediaRecorder.AudioSource.MIC
    /**
     * 设置音频采样率，44100是目前的标准，但是某些设备仍然支持22050，16000，11025
     */
    private val sampleRateInHz = 44100
    /**
     * 设置音频的录制的声道CHANNEL_IN_STEREO为双声道，CHANNEL_CONFIGURATION_MONO为单声道
     */
    private val channelConfig = AudioFormat.CHANNEL_IN_STEREO
    /**
     * 音频数据格式:PCM 16位每个样本。保证设备支持。PCM 8位每个样本。不一定能得到设备支持。
     */
    private val audioFormat = AudioFormat.ENCODING_PCM_16BIT
    private val instance: AudioUtils? = null

    private var mAudioManager: AudioManager? = null
    private var audioRate = 16
    // Stream Music Max value.
    private var mMusicMaxAudio: Int

    private var audioFocusManager: AudioFocusManager? = null

    companion object{
        @Volatile
        private var instance: AudioUtils? = null

        fun getInstance(context:Context): AudioUtils{
            val i = instance
            if(i != null){ return i}
            return synchronized(this){
              val i2 = instance
              if (i2 != null){
                  i2
              }  else{
                  val audioUtils = AudioUtils(context)
                  instance = audioUtils
                  audioUtils
              }
            }
        }
    }


    init {
        mAudioManager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager?
        mMusicMaxAudio = mAudioManager!!.getStreamMaxVolume(AudioManager.STREAM_MUSIC)
        audioFocusManager = AudioFocusManager()
    }

    /**
     * 判断是否有录音权限
     * @return true | false
     */
    fun hasAudioRecordPermission(): Boolean {
        try {
            bufferSizeInBytes = 0
            bufferSizeInBytes = AudioRecord.getMinBufferSize(
                sampleRateInHz,
                channelConfig, audioFormat
            )
            val audioRecord = AudioRecord(
                audioSource,
                sampleRateInHz,
                channelConfig,
                audioFormat,
                bufferSizeInBytes
            )
            //开始录制音频
            try { // 防止某些手机崩溃，例如联想
                audioRecord.startRecording()
            } catch (e: IllegalStateException) {
                e.printStackTrace()
            }
            /*
             * 根据开始录音判断是否有录音权限
             */if (audioRecord.recordingState != AudioRecord.RECORDSTATE_RECORDING) {
                return false
            }
            audioRecord.stop()
            audioRecord.release()
        } catch (ignored: Exception) {
        }
        return true
    }

    override fun mute(isMute: Boolean) {
        setStreamMute(AudioManager.STREAM_MUSIC, isMute)
    }

    private fun setStreamMute(type: Int, isMute:Boolean) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            mAudioManager?.adjustStreamVolume(type, AudioManager.ADJUST_UNMUTE, 0)
        } else {
            @Suppress("DEPRECATION")
            mAudioManager?.setStreamMute(type, isMute)
        }
    }

    override fun increase() {
        increaseMusic()
    }
    override fun decrease() {
        decreaseMusic()
    }

    @JvmOverloads
    fun decreaseMusic(isFollowSystem:Boolean = false){
        adjustStreamVolumeByType(AudioManager.STREAM_MUSIC,
            AudioManager.ADJUST_LOWER, isFollowSystem)
    }

    @JvmOverloads
    fun increaseMusic(isFollowSystem:Boolean = false){
        adjustStreamVolumeByType(AudioManager.STREAM_MUSIC,
            AudioManager.ADJUST_RAISE, isFollowSystem)
    }

    /**
     * 根据类型调整音量+-
     * @param streamType The stream type to adjust.
     * @param isFollowSystem Is follow system v.
     * @param direction The direction to adjust the volume.
     */
    private fun adjustStreamVolumeByType(streamType: Int, direction: Int, isFollowSystem: Boolean){
         setStreamMute(AudioManager.STREAM_MUSIC, false)
        if (isFollowSystem || mMusicMaxAudio < audioRate) {
            mAudioManager?.adjustStreamVolume(streamType, direction,
                AudioManager.FLAG_REMOVE_SOUND_AND_VIBRATE)
        } else {
            mAudioManager?.setStreamVolume(streamType,
                getTargetAudio(streamType, direction),
                AudioManager.FLAG_REMOVE_SOUND_AND_VIBRATE
            )
        }
    }

    /**
     * 获取目标类型指定方向的音量大小
     */
    private fun getTargetAudio(type: Int, direction: Int): Int {
        val currentAudioVolume = mAudioManager!!.getStreamVolume(type)
        var targetAudio: Int
        val rateValue = mMusicMaxAudio.div(audioRate)
        return if(AudioManager.ADJUST_RAISE == direction){
            targetAudio = currentAudioVolume + rateValue
            if(mMusicMaxAudio - targetAudio < rateValue){
                targetAudio = 100
            }
            if (targetAudio >= mMusicMaxAudio) mMusicMaxAudio else targetAudio
        }else{
            targetAudio = currentAudioVolume - rateValue
            if (targetAudio < rateValue) {
                targetAudio = 0
            }
            if (currentAudioVolume <= 0) 0 else targetAudio
        }
    }


    fun setAudioRate(audioRate: Int) {
        this.audioRate = audioRate
    }

    /**
     * 获取当前音量
     */
    fun getCurrAudio(audioType: Int): Int {
        return mAudioManager!!.getStreamVolume(audioType)
    }

    /**
     * 获取当前最大音量
     */
    fun getMaxAudio(audioType: Int): Int {
        return mAudioManager!!.getStreamMaxVolume(audioType)
    }

    /**
     * 音频焦点控制器, 避免音视频声音并发问题
     */
    inner class AudioFocusManager(
        private var mFocuseChangeLsr: AudioManager.OnAudioFocusChangeListener ?= null
    ){
        /**
         * 请求音频焦点 设置监听
         * @param onStart 音频焦点GAIN事件
         * @param onPause 音频焦点LOSS事件
         */
        fun getAudioFocusValue(context:Context, onStart:()->Unit, onPause:()->Unit):Int{
            //Android 2.2开始(API8)才有音频焦点机制
            if(mFocuseChangeLsr == null){
                mFocuseChangeLsr = AudioManager.OnAudioFocusChangeListener {
                    when(it){
                        AudioManager.AUDIOFOCUS_GAIN,
                        AudioManager.AUDIOFOCUS_GAIN_TRANSIENT,
                        AudioManager.AUDIOFOCUS_GAIN_TRANSIENT_MAY_DUCK -> {//播放操作
                            onStart()
                        }
                        AudioManager.AUDIOFOCUS_LOSS,
                        AudioManager.AUDIOFOCUS_LOSS_TRANSIENT,
                        AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK -> {//播放操作
                            onPause()
                        }
                    }
                }
            }
            //下面两个常量参数试过很多 都无效，最终反编译了其他app才搞定，汗~
            return mAudioManager?.requestAudioFocus(
                mFocuseChangeLsr,
                AudioManager.AUDIOFOCUS_GAIN_TRANSIENT_MAY_DUCK,
                AudioManager.AUDIOFOCUS_GAIN_TRANSIENT
            )?: AudioManager.AUDIOFOCUS_REQUEST_FAILED
        }

    }
}