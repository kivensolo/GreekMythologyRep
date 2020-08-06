package com.mplayer;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.TimedText;
import android.net.Uri;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.FrameLayout;

import com.kingz.library.player.internal.AndroidMediaPlayerListeners;
import com.kingz.utils.ToastTools;
import com.zeke.kangaroo.utils.ZLog;

import java.io.IOException;

/**
 * author: King.Z
 * date:  2016/9/5 21:40
 * description: 带surface的原生播放器视图
 * */
public class KingzPlayerView extends SurfaceView {

    private static final String TAG = "KingzPlayerView";
    private Context mContext;
    private int mAudioSession;
    private Uri playUrl = null;
    private MediaPlayer mPlayer;
    //是否正常播放完毕
    boolean playFinished = false;
    private SurfaceHolder mSurfaceHolder;
    private AndroidMediaPlayerListeners mListenners;
    OnStateChangeListener onStateChangeListener;

    private MediaState mediaState;
    public enum MediaState {
        INIT, PREPARING, PLAYING, STOP, PAUSED, IDLE, END, ERROR, RELEASE
    }

    public KingzPlayerView(Context context) {
        super(context);
        mContext = context;
        initVideoView();
    }

    public KingzPlayerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        initVideoView();
    }

    public KingzPlayerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mContext = context;
        initVideoView();
    }

    private void initVideoView() {
        mPlayer = new MediaPlayer();
        mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mPlayer.setVolume(1.0f, 1.0f);
        mPlayer.setOnSeekCompleteListener(mListenners);
        mPlayer.setOnCompletionListener(mListenners);
        mPlayer.setOnPreparedListener(mListenners);
        mPlayer.setOnErrorListener(mListenners);
        mPlayer.setOnInfoListener(mListenners);
        mPlayer.setScreenOnWhilePlaying(true);
        mediaState = MediaState.PREPARING;
        initListeners();

        //getSurfaceHolder and setCallback
        mSurfaceHolder = this.getHolder();
        mSurfaceHolder.addCallback(mSurfaceHolderCallback);
        mSurfaceHolder.setKeepScreenOn(true); //强制屏幕等待
        mediaState = MediaState.IDLE;
    }

    private void initListeners() {
        mListenners = new AndroidMediaPlayerListeners() {
            @Override
            public void onTimedText(MediaPlayer mp, TimedText text) {

            }

            @Override
            public void onBufferingUpdate(MediaPlayer mp, int percent) {

            }

            @Override
            public void onCompletion(MediaPlayer mp) {
                playFinished = true;
                ZLog.d(TAG,"mp is onCompletion() ");
                if (null != onStateChangeListener) {
                    onStateChangeListener.onComplete();
                }
                stop();
            }

            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                ZLog.d(TAG,"mp is onError() ");
                ToastTools.getInstance().showToast(mContext, "播放出错！" + "what=" + what + ";extra=" + extra);
                if (null != onStateChangeListener) {
                    onStateChangeListener.onError();
                }
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
                mp.reset();//重用Error状态的MediaPlayer对象
                ZLog.d(TAG,"----- mp.reset()");
                mediaState = MediaState.IDLE;
                return true;
            }

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

            @Override
            public void onPrepared(MediaPlayer mp) {
                if (null != onStateChangeListener) {
                    onStateChangeListener.onPrepare();
                }
                mPlayer = mp;
            }

            @Override
            public void onSeekComplete(MediaPlayer mp) {
                if (null != onStateChangeListener) {
                    onStateChangeListener.onComplete();
                }
            }
        };
    }

    private SurfaceHolder.Callback mSurfaceHolderCallback = new SurfaceHolder.Callback() {
        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            ZLog.i(TAG, "surfaceCreated()  hodler:" + holder);
            mSurfaceHolder = holder;
             if (null != onStateChangeListener) {
                onStateChangeListener.onSurfaceViewCreated(mSurfaceHolder);
            }
        }

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            ZLog.i(TAG, "surfaceChanged() w:" + width + ", h:" + height);
            mSurfaceHolder = holder;
        }

        @Override
        public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
            ZLog.i(TAG, "surfaceDestroyed()");
            mSurfaceHolder = null;
            if (null != onStateChangeListener) {
                onStateChangeListener.onSurfaceViewDestroyed(surfaceHolder);
            }
            release();
        }

    };

    public interface OnStateChangeListener {
        public void onPrepare();

        public void onBuffering(MediaPlayer mp);

        public void onPlaying(MediaPlayer mp);

        public void onPause(MediaPlayer mp);

        public void onStop(MediaPlayer mp);

        public void onSeek(MediaPlayer mp, int max, int progress);

        public void onComplete();

        public void onError();

        public void onSurfaceViewDestroyed(SurfaceHolder surface);

        public void onSurfaceViewCreated(SurfaceHolder surface);
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

    public void setState(MediaState mediaState) {
        this.mediaState = mediaState;
    }

    public void setVideoScreenScale(int width, int height) {
        ZLog.i(TAG, "setVideoScreenScale()  width:" + width + "----height:" + height);
        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
        int videoWidth = getVideoWidth();
        lp.setMargins((Math.max(videoWidth, width) - Math.min(videoWidth, width)) / 2, 0, (Math.max(videoWidth, width) - Math.min(videoWidth, width)) / 2, 0);
        setLayoutParams(lp);
    }

    private final int START = 0x0001;
    private final int STOP = 0x0002;
    private final int PAUSE = 0x0003;
    private final int COMPLETE = 0x0004;

    public void pause() {
        if (mPlayer.isPlaying()) {
            mPlayer.pause();
            mediaState = MediaState.PAUSED;
        }
        if (onStateChangeListener != null) {
            onStateChangeListener.onPause(mPlayer);
        }
    }

    public void seekTo(int msec) {
        mPlayer.seekTo(msec);
        //onStateChangeListener.onSeek();
    }

    public void stop(){
        ZLog.d(TAG,"stop()");
        mPlayer.stop();
        invalidate();
        mediaState = MediaState.STOP;
        if (onStateChangeListener != null) {
            onStateChangeListener.onStop(mPlayer);
        }
    }


    public void releasePlayer() {
        ZLog.d(TAG,"releasePlayer()");
        mPlayer.stop();
        mPlayer.reset();
        mPlayer.release();
        mediaState = MediaState.END;
    }

    public void start() {
        ZLog.d(TAG,"start()");
        mPlayer.start();
        mediaState = MediaState.PLAYING;
        playFinished = false;
        if (onStateChangeListener != null) {
            onStateChangeListener.onPlaying(mPlayer);
        }
    }

    public int getDuration() {
        return mPlayer.getDuration();
    }

    public int getCurrentPosition() {
        return mPlayer.getCurrentPosition();
    }

    public int getVideoHeight() {
        return mPlayer.getVideoHeight();
    }

    public int getVideoWidth() {
        return mPlayer.getVideoWidth();
    }

     /**
     * reset the media player in any state
     */
    public void reset(){
        if (null == mPlayer) {
            return;
        }
        mPlayer.reset();
        mediaState = MediaState.IDLE    ;
    }

    /**
     * release the media player in any state
     */
    public void release() {
        if (null == mPlayer) {
            return;
        }
        if (mediaState == MediaState.PLAYING) {
            playFinished = false;
            mPlayer.stop();
        }
        mPlayer.reset();
        mPlayer.release();
        // ———> 【IDLE】通过 reset() 方法进入 idle 状态的话会触发 OnErrorListener.onError() ，
        // 并且 MediaPlayer 会进入 Error 状态；如果是新创建的 MediaPlayer 对象，
        // 则并不会触发 onError(), 也不会进入 Error 状态。
        ZLog.i(TAG, "release()...");
        //通过 release() 方法可以进入 End 状态，只要 MediaPlayer 对象不再被使用，
        // 就应当尽快将其通过 release() 方法释放掉，以释放相关的软硬件组件资源，
        // 这其中有些资源是只有一份的（相当于临界资源）。
        // 如果 MediaPlayer 对象进入了 End 状态，则不会在进入任何其他状态了。
        mPlayer = null;
        mediaState = MediaState.RELEASE;
    }


    public void setVideoURI(Uri uri) {
        playUrl = uri;
        ZLog.i(TAG, "setVideoURI() uri:" + uri);
        openVideo();
        requestLayout();
        invalidate();
    }

    public void openVideo(Uri uri){
        playUrl = uri;
          if (null == playUrl || null == mSurfaceHolder) {
            ZLog.e(TAG, "openVideo() playUrl/mSurfaceHolder is null");
            return;
        }
        try {
            mPlayer.setDataSource(mContext, playUrl); //进入Initialized状态
        } catch (IOException e) {
            e.printStackTrace();
        }
        mPlayer.prepareAsync(); //异步准备  onPrepared回调至Prepared状态 ————> start() 至Started状态
    }


    private void openVideo() {
        if (null == playUrl || null == mSurfaceHolder) {
            ZLog.e(TAG, "openVideo() playUrl/mSurfaceHolder is null");
            return;
        }
        AudioManager am = (AudioManager) mContext.getSystemService(Context.AUDIO_SERVICE);
        am.requestAudioFocus(null, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN);
        try {
            mPlayer = new MediaPlayer();
            if (mAudioSession != 0) {
                mPlayer.setAudioSessionId(mAudioSession);
            } else {
                mAudioSession = mPlayer.getAudioSessionId();
            }
            mPlayer.setOnPreparedListener(mListenners);
            mPlayer.setOnCompletionListener(mListenners);
            mPlayer.setOnErrorListener(mListenners);
            mPlayer.setOnSeekCompleteListener(mListenners);
            mPlayer.setOnInfoListener(mListenners);
            //mMediaPlayer.setOnBufferingUpdateListener(mBufferingUpdateListener);
            mPlayer.setDataSource(mContext, playUrl); //进入Initialized状态
            mediaState = MediaState.INIT;

            mPlayer.setDisplay(mSurfaceHolder);
            mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mPlayer.setScreenOnWhilePlaying(true);
            mPlayer.prepareAsync(); //异步准备  onPrepared回调至Prepared状态 ————> start() 至Started状态
            mediaState = MediaState.PREPARING;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setKeepScreenOn(boolean screenOn) {

    }

    public void suspend() {
        release();
    }

    public void resume() {
        openVideo();
    }
}
