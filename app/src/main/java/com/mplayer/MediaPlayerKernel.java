package com.mplayer;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Toast;

import com.utils.ToastTools;
import com.utils.ZLog;

import java.io.IOException;

/**
 * Copyright(C) 2015, 北京视达科科技有限公司
 * All rights reserved.
 * author: King.Z
 * date:  2016/9/5 21:40
 * description:播放器核心类
 * 提供播放、暂停、释放等方法
 */
public class MediaPlayerKernel extends SurfaceView{

    private static final String TAG = "MediaPlayerKernel";
    boolean playFinished = false;//是否播放完毕,在onCompletion中值修改为true，表示播放完毕，不在调用onSeek
    private MediaPlayer mPlayer;
    private SurfaceView surfaceView;
    private MediaState mediaState;
    private SurfaceView mSurfaceView;
    private SurfaceHolder mSurfaceHolder;
    private static final int PLAYER_SLOW_TIMER = 0x501;
    private static final int SET_TOTAL_TIME = 0x502;
    private static final int PLAY_TIMER_INTERVAL = 499;
    private boolean threadExitFlag = false;
    private Uri playUrl = null; // 播放地址
    private Context mContext;
    OnStateChangeListener onStateChangeListener;

    public MediaPlayerKernel(Context context,SurfaceView surfaceView) {
        super(context);
        mContext = context;
        this.surfaceView = surfaceView;
        initVideoView();
    }

    public MediaPlayerKernel(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        initVideoView();
    }

    public MediaPlayerKernel(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mContext = context;
        initVideoView();
    }

    //初始化播放器
    private void initVideoView() {
        mPlayer = new MediaPlayer();
        mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);     //设置多媒体流类型
        mPlayer.setVolume(1.0f, 1.0f);
        mPlayer.setOnVideoSizeChangedListener(mOnVideoSizeListener);
        mPlayer.setOnSeekCompleteListener(onSeekCompleteListener);
        mPlayer.setOnCompletionListener(mOnCompletionListener);
        mPlayer.setOnPreparedListener(mPreparedListener);
        mPlayer.setOnErrorListener(mOErrorListener);
        mPlayer.setOnInfoListener(mOnInfoListener);
        mPlayer.setScreenOnWhilePlaying(true);
        mediaState = MediaState.PREPARING;

        //getSurfaceHolder and setCallback
        mSurfaceHolder = surfaceView.getHolder();
        mSurfaceHolder.addCallback(mSurfaceHolderCallback);
        mSurfaceHolder.setKeepScreenOn(true); //强制屏幕等待
        Log.i(TAG, "initVideoView()  hodler:"+mSurfaceHolder);
        mediaState = MediaState.IDLE;
    }

    private SurfaceHolder.Callback mSurfaceHolderCallback = new SurfaceHolder.Callback() {
        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            Log.i(TAG, "surfaceCreated()  hodler:"+holder);
            mSurfaceHolder = holder;
            openVideo();
        }

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            Log.i(TAG, "surfaceChanged() w:" + width + ", h:" + height);
            mSurfaceHolder = holder;
            openVideo();
        }

        @Override
        public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
            Log.i(TAG, "surfaceDestroyed()");
            mSurfaceHolder = null;
            threadExitFlag = true;
//            seekBar.threadExitFlag = true;
            release();
        }
    };


    /**
     * 播放器状态
     */
    public enum MediaState {
        INIT, PREPARING, PREPARED, PLAYING, STOP, PAUSED, IDLE, END, ERROR, RELEASE;
    }

    public interface OnStateChangeListener {
        public void onPrepare(MediaPlayer mp);

        public void onBuffering(MediaPlayer mp);

        public void onPlaying(MediaPlayer mp);

        public void onPause(MediaPlayer mp);

        public void onStop(MediaPlayer mp);

        public void onSeek(MediaPlayer mp,int max, int progress);

        public void playFinish(MediaPlayer mp);

        public void onSurfaceViewDestroyed(Surface surface);

    }

    public void setOnStateChangeListener(OnStateChangeListener onStateChangeListener) {
        this.onStateChangeListener = onStateChangeListener;
    }

    public MediaPlayer getMediaPlayer() {
        return mPlayer;
    }

    public MediaState getState() {
        return mediaState;
    }
    public MediaState setState() {
        return mediaState;
    }

    private final int START = 0x0001;
    private final int STOP = 0x0002;
    private final int PAUSE = 0x0003;
    private final int COMPLETE = 0x0004;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case PLAYER_SLOW_TIMER:
                    mHandler.sendEmptyMessageDelayed(PLAYER_SLOW_TIMER, PLAY_TIMER_INTERVAL);
                    break;
                case START:
                    if (onStateChangeListener != null) {
//                        onStateChangeListener.onPlaying();
                    }
                    break;
                case PAUSE:
                    if (onStateChangeListener != null) {
//                        onStateChangeListener.onPause();
                    }
                    break;
                case STOP:
                    if (onStateChangeListener != null) {
//                        onStateChangeListener.onStop();
                    }
                    break;
                case COMPLETE:
                    if (onStateChangeListener != null) {
//                        onStateChangeListener.onStop();
                    }
                    break;
                default:
                    break;
            }
        }
    };

    public void play(String url) {
//        mPlayer.stop();
//        mPlayer.reset();
        if (mPlayer == null) {
            mPlayer = new MediaPlayer();
        }
//       mPlayer.setLooping(true);
        try {
            mPlayer.setDataSource(url);
            mPlayer.prepare();
//            mPlayer.prepareAsync();
            mPlayer.start();
            mediaState = MediaState.PREPARING;
        } catch (IOException e) {
            e.printStackTrace();
            mPlayer.reset();
            mediaState = MediaState.INIT;
        }
    }

    public void pause() {
        if (mPlayer.isPlaying()) {
            mPlayer.pause();
            mediaState = MediaState.PAUSED;
        }
        if (onStateChangeListener != null) {
            onStateChangeListener.onPause(mPlayer);
        }
    }

    public void stop() {
        mPlayer.stop();
        mPlayer.reset();
        mediaState = MediaState.STOP;
        playFinished = true;
        if (onStateChangeListener != null) {
            onStateChangeListener.onStop(mPlayer);
        }
    }

    public void start() {
        mPlayer.start();
        mediaState = MediaState.PLAYING;
        playFinished = false;
        if (onStateChangeListener != null) {
            onStateChangeListener.onPlaying(mPlayer);
        }
    }

    /**
     * release the media player in any state
     */
    public void release() {
        if (null == mPlayer) {
            ZLog.i(TAG, "releaseAsync() mediaPlayer is null");
            return;
        }
        if (mPlayer.isPlaying()) {
            //兼容代码
            playFinished = false;
            mPlayer.stop();
        }
        mPlayer.reset();
        // ———> 【IDLE】通过 reset() 方法进入 idle 状态的话会触发 OnErrorListener.onError() ，
        // 并且 MediaPlayer 会进入 Error 状态；如果是新创建的 MediaPlayer 对象，
        // 则并不会触发 onError(), 也不会进入 Error 状态。
        mPlayer.release();
        //通过 release() 方法可以进入 End 状态，只要 MediaPlayer 对象不再被使用，
        // 就应当尽快将其通过 release() 方法释放掉，以释放相关的软硬件组件资源，
        // 这其中有些资源是只有一份的（相当于临界资源）。
        // 如果 MediaPlayer 对象进入了 End 状态，则不会在进入任何其他状态了。
        mPlayer = null;
        mediaState = MediaState.IDLE;
    }


    private void openVideo() {
        ZLog.i(TAG, "openVideo()  playUrl is：" + playUrl);
        if (null == playUrl) {
            return;
        }
        if (null == mSurfaceHolder) {
            ZLog.e(TAG, "openVideo() mSurfaceHolder is null");
            return;
        }
        release();
        mPlayer = new MediaPlayer();
        mPlayer.setOnVideoSizeChangedListener(mOnVideoSizeListener);
        mPlayer.setOnSeekCompleteListener(onSeekCompleteListener);
        mPlayer.setOnCompletionListener(mOnCompletionListener);
        mPlayer.setOnPreparedListener(mPreparedListener);
        mPlayer.setOnErrorListener(mOErrorListener);
        mPlayer.setOnInfoListener(mOnInfoListener);
        try {
            mPlayer.setDataSource(mContext, playUrl); //进入Initialized状态
            mPlayer.setDisplay(mSurfaceHolder);
            mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
			mPlayer.setScreenOnWhilePlaying(true);
            mPlayer.prepareAsync(); //异步准备  onPrepared回调至Prepared状态 ————> start() 至Started状态
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setVideoURI(Uri uri) {
        playUrl = uri;
        ZLog.i(TAG, "setVideoURI() uri:" + uri);
        openVideo();
        requestLayout();
        invalidate();
    }

    @Override
    public void setKeepScreenOn(boolean screenOn) {

    }

    /*******************************   各类监听器  start   *********************************/


    /**
     * 播放器准备Listener
     * mPlayer.prepareAsync()后回调到此
     */
    private MediaPlayer.OnPreparedListener mPreparedListener = new MediaPlayer.OnPreparedListener() {
        @Override
        public void onPrepared(MediaPlayer mp) {
            if(null != onStateChangeListener){
                onStateChangeListener.onPrepare(mp);
            }
            ToastTools.getInstance().showMgtvWaringToast(mContext, "开始播放");
            mediaState = MediaState.PREPARED;
        }
    };

    private MediaPlayer.OnInfoListener mOnInfoListener = new MediaPlayer.OnInfoListener() {
        @Override
        public boolean onInfo(MediaPlayer mp, int what, int extra) {
            Log.i(TAG, "OnInfo：" + "waht = " + what + "---extra = " + extra);
            switch (what) {
                case MediaPlayer.MEDIA_INFO_BAD_INTERLEAVING:
                    break;
                case MediaPlayer.MEDIA_INFO_METADATA_UPDATE:
                    break;
                case MediaPlayer.MEDIA_INFO_VIDEO_TRACK_LAGGING:
                    break;
                case MediaPlayer.MEDIA_INFO_NOT_SEEKABLE:
                    break;
            }
            return false;
        }
    };

    private MediaPlayer.OnSeekCompleteListener onSeekCompleteListener = new MediaPlayer.OnSeekCompleteListener() {

        @Override
        public void onSeekComplete(MediaPlayer mp) {
            Toast.makeText(mContext, "SeekComplete", Toast.LENGTH_SHORT).show();
            Log.i(TAG, "UserSeekComplete");
        }
    };

    private MediaPlayer.OnErrorListener mOErrorListener = new MediaPlayer.OnErrorListener() {
        @Override
        public boolean onError(MediaPlayer mp, int what, int extra) {
            ToastTools.getInstance().showMgtvWaringToast(mContext, "播放出错！" + "what=" + what + ";extra=" + extra);
            switch (what) {
                case MediaPlayer.MEDIA_ERROR_SERVER_DIED:
                    Log.e("Play Error:::", "MEDIA_ERROR_SERVER_DIED" + ", extra:" + extra);
                    break;
                case MediaPlayer.MEDIA_ERROR_UNKNOWN:
                    Log.e("Play Error:::", "MEDIA_ERROR_UNKNOWN" + ", extra:" + extra);
                    break;
                default:
                    break;
            }
            mPlayer.release();
            return false;
        }
    };

    private MediaPlayer.OnCompletionListener mOnCompletionListener = new MediaPlayer.OnCompletionListener() {
        @Override
        public void onCompletion(MediaPlayer mp) {
            Log.i(TAG, "OnCompletionListener.onCompletion()   finish");
//            Toast.makeText(KingZMediaPlayer.this, "播放完成", Toast.LENGTH_SHORT).show();
            threadExitFlag = true;
            mPlayer.stop();
            mPlayer.release();
        }
    };

    private MediaPlayer.OnVideoSizeChangedListener mOnVideoSizeListener = new MediaPlayer.OnVideoSizeChangedListener() {
        @Override
        public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {

        }
    };
    /******************************* 各类监听器  End  *********************************/

    public interface IMediaplerInterface extends MediaPlayer.OnVideoSizeChangedListener,
            MediaPlayer.OnCompletionListener, MediaPlayer.OnErrorListener,
            MediaPlayer.OnSeekCompleteListener, MediaPlayer.OnInfoListener,
            MediaPlayer.OnPreparedListener {

    }

}
