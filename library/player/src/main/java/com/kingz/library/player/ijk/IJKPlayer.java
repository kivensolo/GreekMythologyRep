package com.kingz.library.player.ijk;

import android.content.Context;
import android.graphics.SurfaceTexture;
import android.net.Uri;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;
import com.kingz.library.player.BasePlayer;
import com.kingz.library.player.IPlayer;
import tv.danmaku.ijk.media.player.IMediaPlayer;
import tv.danmaku.ijk.media.player.IjkMediaPlayer;
import tv.danmaku.ijk.media.player.IjkTimedText;

/**
 * author：KingZ
 * date：2019/7/30
 * description：IJK 媒体播放器
 */

public class IJKPlayer extends BasePlayer implements IJKMediaPlayerListeners {
    private static final String TAG = IJKPlayer.class.getSimpleName();
    private IjkMediaPlayer player;
    private int bufferSize;         //缓冲区大小，单位kb
    private long currentPosition;   //ijk 在暂停之后恢复播放的第一秒 时间不准，在缓冲的时候进度也一直在跑

    public IJKPlayer(Context context) {
        this.mContext = context;
        player = new IjkMediaPlayer();
        attachListener();
    }

    @Override
    public Runnable getLoopRunnable() {
        return loopRunnable;
    }

    //每秒一次的回调
    private Runnable loopRunnable = new Runnable() {
        @Override
        public void run() {
            //本次回调的进度是否有效，通过播放器取出来的进度和内部保存的进度进行对比确定
            boolean isCurrentPositionValidate = currentPosition > player.getCurrentPosition() - 2000
                    && currentPosition <= player.getCurrentPosition();
            if (player != null && (currentPosition == 0 || isCurrentPositionValidate)) {
                currentPosition = player.getCurrentPosition();
            } else if (player != null && player.isPlaying()) {
                //解决pause之后 play会跳一秒时间的问题
                currentPosition += PLAYER_UPDATE_INTERVAL_MS;
            }
            mainHandler.postDelayed(this, PLAYER_UPDATE_INTERVAL_MS);
            if (playCallBack != null) {
                playCallBack.onPlayerTimingUpdate();
            }
        }
    };


    @Override
    public void setSurface(Surface surface) {
        if (player != null) {
            player.setSurface(surface);
        }
    }

    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture surface) {

    }

    @Override
    public void seekTo(long msec) {
        if (msec >= getDuration()) {
            msec = getDuration() - 10 * 1000;
        }
        if (player != null) {
            player.seekTo(msec);
        }
    }

    @Override
    public void setDataSource(Uri uri) {
        if (player == null) {
            player = new IjkMediaPlayer();
            player.setSurface(mSurface);
            if (bufferSize > 0) {
                player.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "max-buffer-size", 1024);
            }
            attachListener();
        }
        mUri = uri;
        try {
            player.setDataSource(mContext, uri);
            player.prepareAsync();
        } catch (Exception e) {
            Log.e(TAG, "set play uri error:" + e.getMessage());
            if (playCallBack != null) {
                playCallBack.onError(MEDIA_ERROR_CUSTOM_ERROR, -1);
            }
        }
    }

    @Override
    public void selectAudioTrack(int audioTrackIndex) {

    }

    @Override
    public void setDisplayHolder(SurfaceHolder holder) {
        player.setDisplay(holder);
    }

    @Override
    public void setSpeed(float speed) {
        player.setSpeed(speed);
    }

    @Override
    public void setBufferSize(int bufferSize) {
        this.bufferSize = bufferSize;
        if (player != null) {
            player.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "max-buffer-size", bufferSize * 1024);
        }
    }

    @Override
    public void onBufferingUpdate(IMediaPlayer iMediaPlayer, int percent) {
        if (playCallBack != null) {
            playCallBack.onBufferingUpdate(percent);
        }
    }

    @Override
    public void onCompletion(IMediaPlayer iMediaPlayer) {
        if (currentPosition < getDuration() - 5000) {
            //解决播放过程中断网的情况下 回调的onCompletion  不是onError
            if (playCallBack != null) {
                playCallBack.onError(-10000, -1);
            }
        } else {
            if (playCallBack != null) {
                playCallBack.onCompletion();
            }
        }
        mainHandler.removeCallbacksAndMessages(null);
    }

    @Override
    public boolean onError(IMediaPlayer iMediaPlayer, int what, int extra) {
        Log.e(TAG, "play error: what is" + what + ",  extra is " + extra);
        mIsPrepared = false;
        mIsPaused = false;
        mIsBufferIng = false;
        if (playCallBack != null) {
            playCallBack.onError(what, extra);
        }
        mainHandler.removeCallbacksAndMessages(null);
        return false;
    }

    @Override
    public boolean onInfo(IMediaPlayer iMediaPlayer, int what, int extra) {
        Log.d(TAG, "onInfo: what is" + what + ",  extra is " + extra);
        switch (what) {
            case MEDIA_INFO_BUFFERING_START:
                mIsBufferIng = true;
                if (playCallBack != null) {
                    playCallBack.onBufferStart();
                }
                break;
            case MEDIA_INFO_BUFFERING_END:
                mIsBufferIng = false;
                playCallBack.onBufferEnd();
                break;
            default:
                if (playCallBack != null) {
                    playCallBack.onInfo(what, extra);
                }
                break;
        }
        return false;
    }

    @Override
    public void onPrepared(IMediaPlayer iMediaPlayer) {
        mIsPrepared = true;
        if (playCallBack != null) {
            playCallBack.onPrepared();
        }
    }

    @Override
    public void onSeekComplete(IMediaPlayer iMediaPlayer) {
        if (playCallBack != null) {
            playCallBack.onSeekComplete();
        }
    }

    @Override
    public void onTimedText(IMediaPlayer iMediaPlayer, IjkTimedText ijkTimedText) {
        if (playCallBack != null) {
            playCallBack.onTimedText(null);
        }
    }
    @Override
    protected void reset() {
        super.reset();
        currentPosition = 0;
        player = null;
    }

    @Override
    public void pause() {
        super.pause();
        if (player != null) {
            player.pause();
        }
    }

    @Override
    public void play() {
        super.play();
        if (player != null) {
            player.start();
        }
    }


    @Override
    public void release() {
        super.release();
        if (player != null) {
            player.reset();
            player.release();
            reset();
        }
    }


    @Override
    protected void attachListener() {
        player.setOnPreparedListener(this);
        player.setOnErrorListener(this);
        player.setOnBufferingUpdateListener(this);
        player.setOnSeekCompleteListener(this);
        player.setOnCompletionListener(this);
        player.setOnInfoListener(this);
    }

    @Override
    protected void detachListener() {
        player.setOnPreparedListener(null);
        player.setOnErrorListener(null);
        player.setOnBufferingUpdateListener(null);
        player.setOnSeekCompleteListener(null);
        player.setOnCompletionListener(null);
        player.setOnInfoListener(null);
    }

    @Override
    public IPlayer getMediaPlayer() {
        return this;
    }
}
