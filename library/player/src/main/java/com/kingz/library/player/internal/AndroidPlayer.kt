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
import com.kingz.utils.safeExecute

/**
 * author：KingZ
 * date：2019/7/30
 * description：Android原生播放器
 */
class AndroidPlayer(context: Context?) : BasePlayer(), AndroidMediaPlayerListeners {
    private var mInternalMediaPlayer: MediaPlayer?
    private var bufferPercent = 0

    override fun selectAudioTrack(audioTrackIndex: Int) {}
    override fun setDisplayHolder(holder: SurfaceHolder?) {
        //TODO
    }
    override fun setSpeed(speed: Float) {} // Internal mediaPlayer is unsupport speed.
    override fun setBufferSize(bufferSize: Int) {}
    override fun onSurfaceTextureUpdated(surface: SurfaceTexture) {}
    override val mediaPlayer: IPlayer
        get() = this

    companion object {
        private val TAG = AndroidPlayer::class.java.simpleName
        // Protect time of seek.
        private const val SEEK_PROTECT_TIME_MS = 10 * 1000
    }

    init {
        mContext = context
        mInternalMediaPlayer = MediaPlayer()
        attachListener()
    }

    override fun play() {
        super.play()
        safeExecute({
            mInternalMediaPlayer?.start()
        }, {
            Log.e(TAG, "play error:" + it.message)
            playCallBack?.onError(IPlayer.MEDIA_ERROR_PLAY_STATUS, -1)
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
        safeExecute({
            mInternalMediaPlayer?.pause()
        }, {
            Log.e(TAG, "pause error:" + it.message)
            playCallBack?.onError(IPlayer.MEDIA_ERROR_PLAY_STATUS, -1)
        })
    }

    override fun release() {
        super.release()
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

    override fun onBufferingUpdate(mp: MediaPlayer, percent: Int) {
        bufferPercent = percent
        playCallBack?.onBufferingUpdate(percent)
    }

    override fun onCompletion(mp: MediaPlayer) {
        playCallBack?.onCompletion()
        mainHandler.removeCallbacksAndMessages(null)
    }

    override fun onError(mp: MediaPlayer, what: Int, extra: Int): Boolean {
        mIsPrepared = false
        mIsPaused = false
        mIsBufferIng = false
        when (what) {
            MediaPlayer.MEDIA_ERROR_SERVER_DIED ->
                Log.e(TAG, "play error: MEDIA_ERROR_SERVER_DIED , extra:$extra")
            MediaPlayer.MEDIA_ERROR_UNKNOWN ->
                Log.e(TAG, "play error: MEDIA_ERROR_UNKNOWN , extra:$extra")
            else ->
                Log.e(TAG, "play error: what is$what,  extra is $extra")
        }
        playCallBack?.onError(what, extra)
        mainHandler.removeCallbacksAndMessages(null)
        //mInternalMediaPlayer.reset();//重用Error状态的MediaPlayer对象
        return false
    }

    override fun onInfo(mp: MediaPlayer, what: Int, extra: Int): Boolean {
        Log.d(TAG, "onInfo: what is$what,  extra is $extra")
        when (what) {
            IPlayer.MEDIA_INFO_BUFFERING_START -> {
                mIsBufferIng = true
                playCallBack?.onBufferStart()
            }
            IPlayer.MEDIA_INFO_BUFFERING_END -> {
                mIsBufferIng = false
                playCallBack?.onBufferEnd()
            }
            else ->  playCallBack?.onInfo(what, extra)

        }
        return false
    }

    override fun onPrepared(mp: MediaPlayer) {
        mIsPrepared = true
        playCallBack?.onPrepared()
    }

    override fun onSeekComplete(mp: MediaPlayer) {
        playCallBack?.onSeekComplete()
    }

    override fun onTimedText(mp: MediaPlayer, text: TimedText) {
        playCallBack?.onTimedText(text)
    }

    override fun setSurface(surface: Surface?) {
        playCallBack?.onSeekComplete()
    }

    override fun attachListener() {
        mInternalMediaPlayer?.setOnPreparedListener(this)
        mInternalMediaPlayer?.setOnErrorListener(this)
        mInternalMediaPlayer?.setOnBufferingUpdateListener(this)
        mInternalMediaPlayer?.setOnSeekCompleteListener(this)
        mInternalMediaPlayer?.setOnCompletionListener(this)
        mInternalMediaPlayer?.setOnInfoListener(this)
    }

    override fun detachListener() {
        mInternalMediaPlayer?.setOnPreparedListener(null)
        mInternalMediaPlayer?.setOnErrorListener(null)
        mInternalMediaPlayer?.setOnBufferingUpdateListener(null)
        mInternalMediaPlayer?.setOnSeekCompleteListener(null)
        mInternalMediaPlayer?.setOnCompletionListener(null)
        mInternalMediaPlayer?.setOnInfoListener(null)
    }

    override fun seekTo(msec: Long) {
        safeExecute({
            var seekTo = msec
            if (msec >= duration) {
                seekTo = duration - SEEK_PROTECT_TIME_MS
            }
            mInternalMediaPlayer?.seekTo(seekTo.toInt())
        }, {
            Log.e(TAG, "seek error:" + it.message)
            playCallBack?.onError(IPlayer.MEDIA_ERROR_SEEK_STATUS, -1)
        })
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
            playCallBack?.onError(IPlayer.MEDIA_ERROR_CUSTOM_ERROR, -1)
        }
    }
}