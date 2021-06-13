package com.kingz.library.player.internal

import android.content.Context
import android.graphics.SurfaceTexture
import android.media.MediaPlayer
import android.media.TimedText
import android.net.Uri
import android.util.Log
import android.view.Surface
import android.view.SurfaceHolder
import com.kingz.library.player.BasePlayer
import com.kingz.library.player.IPlayer
import com.kingz.library.player.IPlayer.Companion.MEDIA_INFO_VIDEO_RENDERING_START
import com.kingz.library.player.TrackInfo
import com.kingz.utils.runSafely

/**
 * author：KingZ
 * date：2019/7/30
 * description：Android原生播放器
 */
class AndroidPlayer(context: Context?) : BasePlayer(){
    private var mInternalMediaPlayer: MediaPlayer?
    private var bufferPercent = 0


    private val isSeeking = false
    private val mIsBuffering = false
    private var isAutoPlayWhenRedy = false

    init {
        mContext = context
        mInternalMediaPlayer = MediaPlayer()
        mInternalMediaPlayer?.setScreenOnWhilePlaying(true)
        attachListener()
    }

    companion object {
        private val TAG = AndroidPlayer::class.java.simpleName
        // Protect time of seek.
        private const val SEEK_PROTECT_TIME_MS = 10 * 1000
    }

    override fun selectAudioTrack(audioTrackIndex: Int) {}

    override fun setDisplayHolder(holder: SurfaceHolder?) {
        //TODO
    }

    override fun setSpeed(speed: Float) {} // Internal mediaPlayer is unsupport speed.

    override fun setPlayerOptions(
        playerOptionCategory: Int,
        optionName: String,
        optionValue: String
    ) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun setPlayerOptions(
        playerOptionCategory: Int,
        optionName: String,
        optionValue: Long
    ) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun setMirrorPlay() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun setBufferSize(bufferSize: Int) {}

    override fun onSurfaceTextureUpdated(surface: SurfaceTexture) {}

    override fun play() {
        super.play()
        runSafely({
            mInternalMediaPlayer?.start()
        }, {
            Log.e(TAG, "play error:" + it.message)
            playerListener?.onError(this,IPlayer.MEDIA_ERROR_PLAY_STATUS, -1)
        })
    }

    override fun getAudioTrack(): IntArray {
        return mInternalMediaPlayer?.run {
            val arrayList = ArrayList<Int>()
            for (index in trackInfo.indices) {
                if (trackInfo[index].trackType == MediaPlayer.TrackInfo.MEDIA_TRACK_TYPE_AUDIO) {
                    arrayList.add(index)
                }
            }
            arrayList.toIntArray()
        } ?: IntArray(0)
    }

    override fun pause() {
        super.pause()
        runSafely({
            mInternalMediaPlayer?.pause()
        }, {
            Log.e(TAG, "pause error:" + it.message)
            playerListener?.onError(this,IPlayer.MEDIA_ERROR_PLAY_STATUS, -1)
        })
    }

    override fun stop() {
        super.stop()
    }

    override fun destory() {
        super.destory()
        if (mInternalMediaPlayer == null) {
            return
        }
//        if (正在播放) {
//            mInternalMediaPlayer.stop();
//        }
        mInternalMediaPlayer!!.reset() // ———> [IDLE] State
        mInternalMediaPlayer!!.release() // ———> [END] State
        reset()
    }

    public override fun reset() {
        super.reset()
        bufferPercent = 0
        mInternalMediaPlayer = null
    }

    override fun setAudioTrack(stcTrackInfo: TrackInfo?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    private fun onBufferingUpdate(mp: MediaPlayer, percent: Int) {
        bufferPercent = percent
        // 后续可以做预加载更新 onPreLoadPosition
    }

    private fun onCompletion(mp: MediaPlayer) {
           if (isMediaStopped()) {
            return
        }
        changeState(STATE_COMPLETED)
        playerListener?.onCompletion(this)
        playerHandler.removeCallbacksAndMessages(null)
    }

    private fun onError(mp: MediaPlayer?, what: Int, extra: Int):Boolean {
        changeState(STATE_BUFFERING, 0)

        changeState(STATE_ERROR)
        when (what) {
            MediaPlayer.MEDIA_ERROR_SERVER_DIED ->
                Log.e(TAG, "play error: MEDIA_ERROR_SERVER_DIED , extra:$extra")
            MediaPlayer.MEDIA_ERROR_UNKNOWN ->
                Log.e(TAG, "play error: MEDIA_ERROR_UNKNOWN , extra:$extra")
            else ->
                Log.e(TAG, "play error: what is$what,  extra is $extra")
        }
        playerHandler.removeCallbacksAndMessages(null)

        //mInternalMediaPlayer.reset();//重用Error状态的MediaPlayer对象
        return playerListener?.onError(this, what, extra)?:true
    }

    private fun onInfo(mp: MediaPlayer, what: Int, extra: Int): Boolean {
        if (isMediaStopped()) {
            return false
        }
        when (what) {
            IPlayer.MEDIA_INFO_BUFFERING_START -> {
                 if (!hasState(STATE_BUFFERING)) {
                    changeState(STATE_BUFFERING)
                }
                playerListener?.onBuffering(this,true,100f)
            }
            IPlayer.MEDIA_INFO_BUFFERING_END -> {
                if (hasState(STATE_BUFFERING)) {
                    changeState(STATE_BUFFERING, 0)
                }
                playerListener?.onBuffering(this,false,0f)
            }
            MEDIA_INFO_VIDEO_RENDERING_START ->{
                playerListener?.onVideoFirstFrameShow(this)
            }
        }
        return false
    }

    private fun onPrepared(mp: MediaPlayer) {
//        handleTrackInfo()
        changeState(STATE_PREPARED)
        if (isAutoPlayWhenRedy) {
            isAutoPlayWhenRedy = false
            mInternalMediaPlayer?.start()
            changeState(STATE_PLAYING)
        }
//      mSurface?.requestLayout()
        playerListener?.onPrepared(this)
    }

    private fun onSeekComplete(mp: MediaPlayer?) {
        playerListener?.onSeekComplete(this)
    }

    private fun onTimedText(mp: MediaPlayer?, text: TimedText) {
        playerListener?.onTimedText(text)
    }

    override fun setSurface(surface: Surface?) {
        playerListener?.onSeekComplete(this)
    }

    override fun attachListener() {
        val playerListners = PlayerCallBack()
        mInternalMediaPlayer?.apply {
            setOnPreparedListener(playerListners)
            setOnErrorListener(playerListners)
            setOnBufferingUpdateListener(playerListners)
            setOnSeekCompleteListener(playerListners)
            setOnCompletionListener(playerListners)
            setOnInfoListener(playerListners)
        }
    }

    override fun detachListener() {
        mInternalMediaPlayer?.apply {
            setOnPreparedListener(null)
            setOnErrorListener(null)
            setOnBufferingUpdateListener(null)
            setOnSeekCompleteListener(null)
            setOnCompletionListener(null)
            setOnInfoListener(null)
        }
    }

    override fun seekTo(msec: Long) {
        runSafely({
            var seekTo = msec
            if (msec >= duration) {
                seekTo = duration - SEEK_PROTECT_TIME_MS
            }
            mInternalMediaPlayer?.seekTo(seekTo.toInt())
        }, {
            Log.e(TAG, "seek error:" + it.message)
            onError(null,IPlayer.MEDIA_ERROR_SEEK_STATUS, -1)
        })
    }

    override fun setSoundTrack(soundTrack: String?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override val duration: Long
        get() = mInternalMediaPlayer!!.duration.toLong()

    override val currentPosition: Long
        get() = mInternalMediaPlayer!!.currentPosition.toLong()

    override val bufferedPosition: Long
        get() = (bufferPercent / 100.0 * duration).toLong()

    override fun setDataSource(uri: Uri?) {
        if (uri == null) {
            Log.e(TAG, "video uri can't null.")
            return
        }
        if (mInternalMediaPlayer == null) {
            mInternalMediaPlayer = MediaPlayer()
            mInternalMediaPlayer?.setSurface(mSurface)
            attachListener()
        }
        try {
            mUri = uri
            mInternalMediaPlayer?.setDataSource(mContext, uri)
            //异步准备  onPrepared回调至Prepared状态 ————> start() 至Started状态
            mInternalMediaPlayer?.prepareAsync()
        } catch (e: Exception) {
            Log.e(TAG, "set play uri error:" + e.message)
            playerListener?.onError(this, IPlayer.MEDIA_ERROR_CUSTOM_ERROR, -1)
        }
    }

    override fun setDataSource(url: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getCurrentLoadSpeed(): Float {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun setBufferTimeOutThreshold(threshold: Long) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

// <editor-fold defaultstate="collapsed" desc="播放器状态判断">

    private fun isMediaStopped(): Boolean {
        return hasAnyState(
            STATE_IDLE,
            STATE_INITIALIZED,
            STATE_STOPPED,
            STATE_RELEASE,
            STATE_UNINITIALIZED,
            STATE_COMPLETED,
            STATE_ERROR
        )
    }
// </editor-fold>

    inner class PlayerCallBack : AndroidMediaPlayerListeners {
        override fun onSeekComplete(mp: MediaPlayer) {
            this@AndroidPlayer.onSeekComplete(mp)
        }

        override fun onInfo(mp: MediaPlayer, what: Int, extra: Int): Boolean {
            Log.d(TAG, "onInfo: what is$what,  extra is $extra")
            return this@AndroidPlayer.onInfo(mp, what, extra)
        }

        override fun onTimedText(mp: MediaPlayer, text: TimedText) {
            this@AndroidPlayer.onTimedText(mp, text)
        }

        override fun onPrepared(mp: MediaPlayer) {
            this@AndroidPlayer.onPrepared(mp)
        }

        override fun onBufferingUpdate(mp: MediaPlayer, percent: Int) {
            this@AndroidPlayer.onBufferingUpdate(mp, percent)
        }

        override fun onCompletion(mp: MediaPlayer) {
            this@AndroidPlayer.onCompletion(mp)
        }

        override fun onError(mp: MediaPlayer, what: Int, extra: Int): Boolean {
            return this@AndroidPlayer.onError(mp, what, extra)
        }
    }
}