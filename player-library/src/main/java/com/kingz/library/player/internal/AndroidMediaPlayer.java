package com.kingz.library.player.internal;

import android.content.Context;
import android.graphics.SurfaceTexture;
import android.media.MediaPlayer;
import android.media.TimedText;
import android.net.Uri;
import android.util.Log;
import android.view.Surface;

import com.kingz.library.player.AbstractMediaPlayer;

/**
 * author：KingZ
 * date：2019/7/30
 * description：Android原生播放器
 */
public class AndroidMediaPlayer extends AbstractMediaPlayer implements AndroidMediaPlayerListeners {
    // Seek 保护时间
    private static final int SEEK_PROTECT_TIME_MS = 10 * 1000;
    private MediaPlayer mInternalMediaPlayer;
    private int bufferPercent;   //当前缓冲的百分比

    public AndroidMediaPlayer(Context context) {
        this.mContext = context;
        mInternalMediaPlayer = new MediaPlayer();
        attachListener();
    }

    @Override
    public void play() {
        super.play();
        try {
            mInternalMediaPlayer.start();
        } catch (Exception e) {
            Log.e(TAG, "play error:" + e.getMessage());
            if (playCallBack != null) {
                playCallBack.onError(this, MEDIA_ERROR_PLAY_STATUS, -1);
            }
        }
    }

    @Override
    public void pause() {
        super.pause();
        try {
            mInternalMediaPlayer.pause();
        } catch (Exception e) {
            Log.e(TAG, "pause error:" + e.getMessage());
            if (playCallBack != null) {
                playCallBack.onError(this, MEDIA_ERROR_PAUSE_STATUS, -1);
            }
        }
    }

    @Override
    public void release() {
        super.release();
        if (mInternalMediaPlayer == null) {
            return;
        }
//        if (正在播放) {
//            mInternalMediaPlayer.stop();
//        }
        mInternalMediaPlayer.reset();  // ———> [IDLE] State
        mInternalMediaPlayer.release();// ———> [END] State
        reset();
    }

    public void reset() {
        super.reset();
        bufferPercent = 0;
        mInternalMediaPlayer = null;
    }

    @Override
    public void onBufferingUpdate(MediaPlayer mp, int percent) {
        this.bufferPercent = percent;
        if (playCallBack != null) {
            playCallBack.onBufferingUpdate(this, percent);
        }
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        if (playCallBack != null) {
            playCallBack.onCompletion(this);
        }
        mainHandler.removeCallbacksAndMessages(null);
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        isPrepared = false;
        isPaused = false;
        isBufferIng = false;
        switch (what) {
            case MediaPlayer.MEDIA_ERROR_SERVER_DIED:
                Log.e(TAG, "play error: MEDIA_ERROR_SERVER_DIED , extra:" + extra);
                break;
            case MediaPlayer.MEDIA_ERROR_UNKNOWN:
                Log.e(TAG, "play error: MEDIA_ERROR_UNKNOWN , extra:" + extra);
                break;
            default:
                Log.e(TAG, "play error: what is" + what + ",  extra is " + extra);
                break;
        }
        if (playCallBack != null) {
            playCallBack.onError(this, what, extra);
        }
        mainHandler.removeCallbacksAndMessages(null);
        //mInternalMediaPlayer.reset();//重用Error状态的MediaPlayer对象
        return false;
    }

    @Override
    public boolean onInfo(MediaPlayer mp, int what, int extra) {
        Log.d(TAG, "onInfo: what is" + what + ",  extra is " + extra);
        switch (what) {
            case MEDIA_INFO_BUFFERING_START:
                isBufferIng = true;
                if (playCallBack != null) {
                    playCallBack.onBufferStart(this);
                }
                break;
            case MEDIA_INFO_BUFFERING_END:
                isBufferIng = false;
                playCallBack.onBufferEnd(this);
                break;
            default:
                if (playCallBack != null) {
                    playCallBack.onInfo(this, what, extra);
                }
                break;
        }
        return false;
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        isPrepared = true;
        if (playCallBack != null) {
            playCallBack.onPrepared(this);
        }
    }

    @Override
    public void onSeekComplete(MediaPlayer mp) {
        if (playCallBack != null) {
            playCallBack.onSeekComplete(this);
        }
    }

    @Override
    public void onTimedText(MediaPlayer mp, TimedText text) {
        if (playCallBack != null) {
            playCallBack.onTimedText(this, text);
        }
    }

    @Override
    protected void setSurface(Surface surface) {
        if (playCallBack != null) {
            playCallBack.onSeekComplete(this);
        }
    }

    @Override
    protected void attachListener() {
        mInternalMediaPlayer.setOnPreparedListener(this);
        mInternalMediaPlayer.setOnErrorListener(this);
        mInternalMediaPlayer.setOnBufferingUpdateListener(this);
        mInternalMediaPlayer.setOnSeekCompleteListener(this);
        mInternalMediaPlayer.setOnCompletionListener(this);
        mInternalMediaPlayer.setOnInfoListener(this);
    }

    @Override
    protected void detachListener() {
        mInternalMediaPlayer.setOnPreparedListener(null);
        mInternalMediaPlayer.setOnErrorListener(null);
        mInternalMediaPlayer.setOnBufferingUpdateListener(null);
        mInternalMediaPlayer.setOnSeekCompleteListener(null);
        mInternalMediaPlayer.setOnCompletionListener(null);
        mInternalMediaPlayer.setOnInfoListener(null);
    }

    @Override
    public void seekTo(long msec) {
        try {
            // 拖动到结束的时候 最多拖动到结束前10秒
            if (msec >= getDuration()) {
                msec = getDuration() - SEEK_PROTECT_TIME_MS;
            }
            mInternalMediaPlayer.seekTo((int) msec);
        } catch (Exception e) {
            Log.e(TAG, "seek error:" + e.getMessage());
            if (playCallBack != null) {
                playCallBack.onError(this, MEDIA_ERROR_SEEK_STATUS, -1);
            }
        }
    }


    @Override
    public long getDuration() {
        return mInternalMediaPlayer.getDuration();
    }

    @Override
    public long getCurrentPosition() {
        return mInternalMediaPlayer.getCurrentPosition();
    }

    @Override
    public long getBufferedPosition() {
        return (long) (bufferPercent / 100.0 * getDuration());
    }

    @Override
    public void setPlayURI(Uri uri) {
        if (uri == null) {
            Log.e(TAG, "video uri can't null.");
            return;
        }
        if (mInternalMediaPlayer == null) {
            mInternalMediaPlayer = new MediaPlayer();
            mInternalMediaPlayer.setSurface(mSurface);
            attachListener();
        }
        try {
            mUri = uri;
            mInternalMediaPlayer.setDataSource(mContext, uri);
            //异步准备  onPrepared回调至Prepared状态 ————> start() 至Started状态
            mInternalMediaPlayer.prepareAsync();
        } catch (Exception e) {
            Log.e(TAG, "set play uri error:" + e.getMessage());
            if (playCallBack != null) {
                playCallBack.onError(this, MEDIA_ERROR_CUSTOM_ERROR, -1);
            }
        }
    }

    @Override
    public void setSpeed(float speed) {
    }

    @Override
    public void setBufferSize(int bufferSize) {
    }

    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture surface) {
    }
}
