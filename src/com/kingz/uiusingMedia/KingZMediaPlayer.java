package com.kingz.uiusingMedia;

import android.app.Activity;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.*;
import android.widget.Toast;
import com.kingz.uiusingListViews.R;

import java.io.IOException;

/**
 * Created by KingZ on 2016/1/24.
 * Discription:
 */
public class KingZMediaPlayer extends Activity implements View.OnClickListener {

    private static final String TAG = "KingZMediaPlayer";

    /** 播放器状态参数 */
    private enum MPstate{Play,Stop,Pause,Idle,End}

	private MPstate mediaState = MPstate.Idle;	     //初始化播放器状态
	private MPstate currentPlayState = MPstate.Play; //默认为播放状态

	private SeekBarView seekBar;
    private MediaPlayer mPlayer;
    private SurfaceView mSFview;
    private SurfaceHolder holder;

    /** 影片参数 */
    private String currentPlayTime;		//当oonta前播放的时间
    private long duration;				// 影片持续时间
    private int mVideoWidth;
    private int mVideoHeight;
	private View rootView;

    private String play_url;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mplayer_layout);

        initViews();
        initMedia();
    }

	private void initViews() {
		mSFview = (SurfaceView) findViewById(R.id.surface);
		seekBar = (SeekBarView) LayoutInflater.from(this)
				.inflate(R.layout.mplayer_views,null)
				.findViewById(R.id.mplayer_progress);

	}

	/**
	 * 始化播放器
	 **/
    private void initMedia() {
        mPlayer = new MediaPlayer();
		mPlayer.setOnPreparedListener(mPreparedListener);
		mPlayer.setOnInfoListener(mOnInfoListener);
		mPlayer.setOnSeekCompleteListener(onSeekCompleteListener);
		mPlayer.setOnErrorListener(mOErrorListener);
		mPlayer.setOnCompletionListener(mOnCompletionListener);
		mPlayer.setOnVideoSizeChangedListener(mOnVideoSizeListener);
//		String play_url = Environment.getExternalStorageDirectory().getPath()+"/Test_Movie.m4v";
        /*************** SurfaceView 设置 *********************************/
		mSFview = (SurfaceView) this.findViewById(R.id.surface);
		holder = mSFview.getHolder();
		holder.setKeepScreenOn(true);//强制屏幕等待
		holder.addCallback(mSurfaceHolderCallback);
		//把输送给surfaceView的视频画面直接显示到屏幕上，不用维护自身的缓冲区,目前已经废弃  当需要的时候系统会自动设定
//		holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
//		holder.setFixedSize(480, 320);

    }

    /**
     * 开始播放
     */
    private void startoPlay() {
		play_url = "http://169.254.124.89:8080/video/test_1.mp4";
		Log.d(TAG, "the videoFilePath:"+play_url);
		mediaState = MPstate.Play;
		mPlayer.reset(); 										//转为Idel
		mPlayer.setDisplay(holder);  							//把视频画面输出到SurfaceView
		mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);  //设置多媒体流类型
			mPlayer.setVolume(1.0f, 1.0f);
            try {
                if(!TextUtils.isEmpty(play_url)){
                    mPlayer.setDataSource(play_url);  						//设置需要播放的视频 ，建立视频对象
                    mPlayer.prepare();  									//缓冲处理  监听缓冲完成就需要setOnPreparedListener
                }else{
                    Toast.makeText(this, "play_url is empty ！！", Toast.LENGTH_SHORT).show();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            mPlayer.start();
    }

    /**
     * Called when a view has been clicked.
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {

    }

    /*******************************  各类监听器  start   *********************************/
    /** 播放器准备Listener */
	private MediaPlayer.OnPreparedListener mPreparedListener = new MediaPlayer.OnPreparedListener() {

		@Override
		public void onPrepared(MediaPlayer mp) {
//			duration = mp.getDuration();
//			rightTime = timeFormat(duration);
//			totalTime.setText(rightTime);
			mVideoWidth = mPlayer.getVideoHeight();
			mVideoHeight = mPlayer.getVideoWidth();
			//如果Video的宽高超出了当前屏幕的大小 就进行缩放
			if(mVideoWidth > getWindowManager().getDefaultDisplay().getWidth()
					|| mVideoHeight > getWindowManager().getDefaultDisplay().getHeight()){


			}
		    mPlayer.start();
		}
	};

    private MediaPlayer.OnInfoListener mOnInfoListener = new MediaPlayer.OnInfoListener() {
		@Override
		public boolean onInfo(MediaPlayer mp, int what, int extra) {
			Log.i(TAG, "OnInfo：" + "waht = " + what + "---extra = " + extra);
			//当一些特定信息出现或者警告时触发
			switch(what){
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

    /**onSeekComplete Listener */
	private MediaPlayer.OnSeekCompleteListener onSeekCompleteListener = new MediaPlayer.OnSeekCompleteListener() {

		@Override
		public void onSeekComplete(MediaPlayer mp) {
            Toast.makeText(KingZMediaPlayer.this, "seekToEnd", Toast.LENGTH_SHORT).show();
            Log.i(TAG, "onSeekComplete");
		}
	};

    /** 播放错误Listener */
	private MediaPlayer.OnErrorListener mOErrorListener = new MediaPlayer.OnErrorListener() {
		@Override
		public boolean onError(MediaPlayer mp, int what, int extra) {
			Toast.makeText(KingZMediaPlayer.this, "播放出错啦~！", Toast.LENGTH_SHORT).show();
			switch (what) {
				case MediaPlayer.MEDIA_ERROR_SERVER_DIED:
					Log.v("Play Error:::", "MEDIA_ERROR_SERVER_DIED" +  ", extra:" + extra);
					break;
				case MediaPlayer.MEDIA_ERROR_UNKNOWN:
					Log.v("Play Error:::", "MEDIA_ERROR_UNKNOWN" +  ", extra:" + extra);
					break;
				default:
					break;
			}
			return false;
		}
	};
    /** 播放完成Listener */
	private MediaPlayer.OnCompletionListener mOnCompletionListener = new MediaPlayer.OnCompletionListener() {
		@Override
		public void onCompletion(MediaPlayer mp) {
			Log.i(TAG, "OnCompletionListener.onCompletion()   finish");
			Toast.makeText(KingZMediaPlayer.this,"播放完成", Toast.LENGTH_SHORT).show();
			mPlayer.stop();
		}
	};

	private MediaPlayer.OnVideoSizeChangedListener mOnVideoSizeListener = new MediaPlayer.OnVideoSizeChangedListener() {
		@Override
		public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {

		}
	};

	private SurfaceHolder.Callback mSurfaceHolderCallback = new SurfaceHolder.Callback() {
		@Override
		public void surfaceCreated(SurfaceHolder holder) {
			Toast.makeText(KingZMediaPlayer.this, "surfaceCreated", Toast.LENGTH_SHORT).show();
			startoPlay();
		}

		@Override
		public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

		}

		@Override
		public void surfaceDestroyed(SurfaceHolder holder) {

		}
	};
    /*******************************  各类监听器  End   *********************************/

    private void doPuase() {
		mediaState = MPstate.Pause;
		mPlayer.pause();
	}

    private void doPlay(){
		mediaState = MPstate.Play;
		mPlayer.start();
	}

    /************ 生命周期 ***************/

	@Override
	protected void onRestart() {
		mPlayer.start();
		super.onRestart();
	}

	protected void onDestroy() {
		//释放播放器
		Toast.makeText(this, "Release mplayer", Toast.LENGTH_SHORT).show();
	 	mPlayer.reset();
		mPlayer.release();
//		isStartToPlay = false;
		mediaState = MPstate.End;
		super.onDestroy();
	}


	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if(event.getAction() == MotionEvent.ACTION_DOWN){
			if( mediaState == MPstate.Play){
				doPuase();
				return true;
			}else if(mediaState == MPstate.Pause){
				doPlay();
				seekBar.setVisibility(View.VISIBLE);
				return true;
			}
		}
		if(event.getAction() == MotionEvent.ACTION_MOVE){
//			isSeekState = true;
			float y = event.getY();
			float x = event.getX();
			Log.i(TAG,"onTouchEvent   得到的X = "+x+";得到的y:"+y);
		}
		return super.onTouchEvent(event);
	}




}
