package com.kingz.library.player;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.SurfaceTexture;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.view.*;
import org.jetbrains.annotations.NotNull;

/**
 * author：KingZ
 * date：2019/7/30
 * desc: 描述播放器产品的公共接口
 */
public abstract class BasePlayer implements IPlayer,
        SurfaceHolder.Callback,
        TextureView.SurfaceTextureListener {

    protected static final int PLAYER_UPDATE_INTERVAL_MS = 500;
    protected Handler mainHandler = new Handler(Looper.getMainLooper());
    protected IPlayerEventsCallBack playCallBack;
    protected Surface mSurface;
    protected Context mContext;
    protected Uri mUri;

    //TODO 用位运算处理
    protected boolean mIsPrepared;
    protected boolean mIsPaused;
    protected boolean mIsBufferIng;

    protected View playView;  //承载播放的view

    private Runnable loopRunnable = new Runnable() {
        @Override
        public void run() {
            mainHandler.postDelayed(this, PLAYER_UPDATE_INTERVAL_MS);
            if (playCallBack != null) {
                playCallBack.onPlayerTimingUpdate();
            }
        }
    };

    public Runnable getLoopRunnable() {
        return loopRunnable;
    }

    protected abstract void attachListener();

    protected abstract void detachListener();

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        mSurface = holder.getSurface();
        setSurface(mSurface);
        if (playCallBack != null) {
            playCallBack.onViewCreated();
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        if (playCallBack != null) {
            playCallBack.onViewChanged(format, width, height);
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        mSurface = null;
        setSurface(null);
        if (playCallBack != null) {
            playCallBack.onViewDestroyed();
        }
    }
    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
        mSurface = new Surface(surface);
        setSurface(mSurface);
        if (playCallBack != null) {
            playCallBack.onViewCreated();
        }
    }

    @Override
    public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {
        if (playCallBack != null) {
            playCallBack.onViewChanged(-1, width, height);
        }
    }

    @Override
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
        mSurface = null;
        setSurface(null);
        if (playCallBack != null) {
            playCallBack.onViewDestroyed();
        }
        return false;
    }

    @Override
    public void play() {
        mIsPaused = false;
        removeHandler();
        mainHandler.post(getLoopRunnable());
        if (playCallBack != null) {
            playCallBack.onPlay();
        }
    }

    @Override
    public void pause() {
        mIsPaused = true;
        removeHandler();
    }

    @Override
    public void release() {
        removeHandler();
    }

    @Override
    public void setPlayerView(View playView) {
        this.playView = playView;
        this.playView.setKeepScreenOn(true);
        if (playView instanceof SurfaceView) {
            ((SurfaceView) playView).getHolder().addCallback(this);
        } else if (playView instanceof TextureView) {
            ((TextureView) playView).setSurfaceTextureListener(this);
        } else {
            throw new IllegalArgumentException("view must be SurfaceView or TextureView");
        }
    }

    @Override
    public void setPlayerEventCallBack(IPlayerEventsCallBack callBack) {
        playCallBack = callBack;
    }

    @Override
    public boolean isPrepared() {
        return mIsPrepared;
    }

    @Override
    public boolean isPlaying() {
        return false;
    }

    @Override
    public boolean isBuffering() {
        return mIsBufferIng;
    }

    @Override
    public boolean isPaused() {
        return mIsPaused;
    }

    @Override
    public Uri getCurrentURI() {
        return mUri;
    }

    @Override
    public long getDuration() {
        return 0;
    }

    @Override
    public long getCurrentPosition() {
        return 0;
    }

    @Override
    public long getBufferedPosition() {
        return 0;
    }

    @Override
    public long getTcpSpeed() {
        return 0;
    }

    @Override
    public Bitmap getCurrentThumb() {
        return playView instanceof TextureView ? ((TextureView) playView).getBitmap() : null;
    }

    @NotNull
    @Override
    public int[] getAudioTrack() {
        return new int[0];
    }

    @Override
    public abstract IPlayer getMediaPlayer();

    private void removeHandler() {
        mainHandler.removeCallbacksAndMessages(null);
    }

    protected void reset() {
        mUri = null;
        mIsPrepared = false;
        mIsPaused = false;
        mIsBufferIng = false;
        detachListener();
    }
}
