package com.kingz.library.player;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.SurfaceTexture;
import android.net.TrafficStats;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.TextureView;
import android.view.View;

import androidx.annotation.CallSuper;

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
    protected Handler playerHandler = new Handler(Looper.getMainLooper());
    protected IPlayerEventsListener playerListener;
    protected Surface mSurface;
    protected Context mContext;
    protected Uri mUri;

    //播放器状态
    protected static final int STATE_UNINITIALIZED = 0x0000;
    protected static final int STATE_PLAYING = 0x0001;
    protected static final int STATE_BUFFERING = 0x0002;
    protected static final int STATE_EXO_SEEKING = 0x0004;
    protected static final int STATE_IDLE = 0x0010;
    protected static final int STATE_INITIALIZED = 0x0020;
    protected static final int STATE_PREPARING = 0x0040;
    protected static final int STATE_PREPARED = 0x0080;
    protected static final int STATE_STOPPED = 0x0100;
    protected static final int STATE_PAUSED = 0x0200;
    protected static final int STATE_COMPLETED = 0x0400;
    protected static final int STATE_RELEASE = 0x0800;
    protected static final int STATE_ERROR = 0x1000;
    protected static final int STATE_SURFACE_LOST = 0x2000;
    //player service died, must rebuild player
    protected static final int STATE_REBUILD = 0x4000;
    //Media state code
    protected static final int MEDIA_ERROR_SERVER_DIED = 0x8000;
    //播放器状态
    protected int playerState = STATE_UNINITIALIZED;

    //网速相关变量
    protected long mPreRxBytes;
    protected float mNetSpeed;

    protected View playView;  //承载播放的view

    // 同播放器生命周期
    private Runnable lifecyclerRunnable = new Runnable() {
        @Override
        public void run() {
            playerHandler.postDelayed(this, PLAYER_UPDATE_INTERVAL_MS);
            if (playerListener != null) {
                playerListener.onPlayerTimingUpdate(BasePlayer.this, getCurrentPosition());
            }
        }
    };

    public Runnable getLifecyclerRunnable() {
        return lifecyclerRunnable;
    }

    protected abstract void attachListener();

    protected abstract void detachListener();

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        mSurface = holder.getSurface();
        setSurface(mSurface);
        if (playerListener != null) {
            playerListener.onViewCreated();
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        if (isSurfaceValid(holder)) {
            onSurfaceInitialized(holder);
        }
        if (playerListener != null) {
            playerListener.onViewChanged(format, width, height);
        }
    }

    private void onSurfaceInitialized(SurfaceHolder holder) {
        //Surface重建 后续再完善
        setSurface(holder.getSurface());
        if (!hasState(STATE_UNINITIALIZED)) {
//                onSurfaceRestored();
            return;
        }
        changeState(STATE_UNINITIALIZED, 0);
    }
//     private void onSurfaceRestored() {
//        PlayerLog.i(TAG, ">>>>>>>>>>>>>>>onSurfaceRestored()");
//        if (!hasState(PS_SURFACE_LOST)) {
//            return;
//        }
//        changeState(PS_SURFACE_LOST, 0);
//        if (hasAnyState(PS_STOPPED | PS_ERROR)) {
//            return;
//        }
//        if (getRealPlayer() != null && hasState(PS_PLAYING)) {
//                    player.setPlayWhenReady(true);;
//        }
//    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        mSurface = null;
        setSurface(null);
        if (playerListener != null) {
            playerListener.onViewDestroyed();
        }
    }

    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
        mSurface = new Surface(surface);
        setSurface(mSurface);
        if (playerListener != null) {
            playerListener.onViewCreated();
        }
    }

    @Override
    public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {
        if (playerListener != null) {
            playerListener.onViewChanged(-1, width, height);
        }
    }

    @Override
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
        mSurface = null;
        setSurface(null);
        if (playerListener != null) {
            playerListener.onViewDestroyed();
        }
        return false;
    }

    private boolean isSurfaceValid(SurfaceHolder holder) {
        if (holder == null) {
            return false;
        }
        final Surface surface = holder.getSurface();
        return surface != null && surface.isValid();
    }

    @CallSuper
    @Override
    public void play() {
        changeState(STATE_PAUSED | STATE_STOPPED, STATE_PLAYING);
        removeHandler();
        playerHandler.post(getLifecyclerRunnable());
    }

    @CallSuper
    @Override
    public void pause() {
        changeState(STATE_PLAYING, STATE_PAUSED);
        removeHandler();
    }

    @CallSuper
    @Override
    public void stop() {
        changeState(playerState, STATE_STOPPED);
    }

    @CallSuper
    @Override
    public void destory() {
        changeState(playerState, STATE_UNINITIALIZED);
        removeHandler();
        lifecyclerRunnable = null;
    }

    @Override
    public void initRenderView(View renderView) {
        playView = renderView;
        playView.setKeepScreenOn(true);
        if (playView instanceof SurfaceView) {
            ((SurfaceView) playView).getHolder().addCallback(this);
        } else if (playView instanceof TextureView) {
            ((TextureView) playView).setSurfaceTextureListener(this);
        } else {
            throw new IllegalArgumentException("view must be SurfaceView or TextureView");
        }
    }

    @Override
    public void setPlayerEventListener(IPlayerEventsListener callBack) {
        playerListener = callBack;
    }

    @Override
    public boolean isPlaying() {
        return false;
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
    public IPlayer getIPlayer() {
        return this;
    }

    private void removeHandler() {
        playerHandler.removeCallbacksAndMessages(null);
    }

    @CallSuper
    protected void reset() {
        mUri = null;
        detachListener();
    }

    private int getUid() {
        if (mContext == null) {
            return -1;
        }
        try {
            PackageManager pm = mContext.getPackageManager();
            ApplicationInfo ai = pm.getApplicationInfo(mContext.getPackageName(),
                    PackageManager.GET_ACTIVITIES);
            return ai.uid;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

    protected long getNetworkRxBytes() {
        int currentUid = getUid();
        if (currentUid < 0) {
            return 0;
        }
        return TrafficStats.getTotalRxBytes();
    }


    protected void changeState(int addState) {
        changeState(0, addState);
    }

    protected void changeState(int removeState, int addState) {
        playerState = (playerState & ~removeState) | addState;
    }

    protected boolean hasState(int state) {
        return (playerState & state) == state;
    }

    protected boolean hasAnyState(int... states) {
        for (int state : states) {
            if ((playerState & state) != 0) {
                return true;
            }
        }
        return false;
    }
}
