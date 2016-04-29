package com.mplayer;

import android.app.Activity;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.datainfo.ChannelData;
import com.kingz.uiusingListViews.R;
import com.utils.ToastTools;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.TimerTask;

/**
 * Created by KingZ on 2016/4/16.
 * Discription:播放器测试Demo
 *       mPlayer.reset();               //----->Idle
 *       mPlayer.releaseMediaPlayer();  //Idle----->End
 *       setDataSource();               //Initialized
 *       prepare();                     //Prepared
 */
public class KingZMediaPlayer extends Activity implements View.OnClickListener {

    private static final String TAG = "KingZMediaPlayer";
    public static final int AUTO_REFRESH_FLAG = 1;

    private enum MPstate {
        PREPARING,PREPARED,PLAYING, STOP, PAUSED, IDLE, End,ERROR,
    }

    private MPstate currentMediaState = MPstate.IDLE;      //初始化播放器状态

    private SeekBarView seekBar;
    private MediaPlayer mPlayer;
    private SurfaceView mSurfaceView;
    private SurfaceHolder holder;

    /**
     * 影片参数
     */
    private String currentPlayTime;     //当前播放时间
    private long duration;              //总持续时长
    private int mVideoWidth;
    private int mVideoHeight;
    private ListView leftListView;
    private TextView rightChangeBtn;    //画面比例切换，暂未使用
    private TextView totalTimeView;     //片长总时间，暂未使用
    private TextView currentTimeView;   //当前播放时间，暂未使用
    private ChanellListAdapter chanellListAdapter;
    private ArrayList<ChannelData> channelLists;
    private ChannelData channelData = new ChannelData();

    //测试播放串
    private String play_url = "http://182.138.101.49:5000/nn_vod/nn_x64/aWQ9MmI4NzQ3NDQ5Y2E3NmRkYTQxYmQzY2I5Y2UwNDU4NDkmdXJsX2MxPTU0NTY3MzY1NzI2OTY1NzMyZjY0NjE2Zjc4Njk2MTZjNjk3NTcyNjU2ZTJmNDIzMDM4NDUzNTM4NDU0NDM4NDM0NDQyMzE0NDM3MzEzODM2NDUzODM5MzkzMTQ1NDIzMzMyMzgzMDMyMzE0NDVmMzIzMDMxMzUzMTMyMzAzNzVmMzE1ZjMxNWYzMTMwMzMzMzJlNzQ3MzIwMDAmbm5fYWs9MDFlNzFkMTYzYzhjNmUxZTNmMmVhZjE1MjhkZmUwZDRhNiZudHRsPTMmbnBpcHM9NTIuOC4xODUuMTY4OjUxMDAmbmNtc2lkPTEwMDgwMDEmbmdzPTU3MjM0MjMxMDAwODMxZTRhMDU1NGIyOWIwMzhjM2MzJm5uZD1jbi56Z2R4LnNpY2h1YW4mbmZ0PXRzJm5uX3VzZXJfaWQ9Y250djAwMWFjYzE1NWM3YyZuZHQ9c3RiJm5kdj0xLjQuMC5DT00tQ0hJTkEtT1RULVNUQi4xLjBfRGVtbyZuYWw9MDEzMTQyMjM1NzA2MDc3N2VkNDM2M2RlZjQ5OGI1OGFkMWUxNTA5YWRjMWM5OA,,/2b8747449ca76dda41bd3cb9ce045849.ts";

    //澳门风云“http://182.138.101.49:5000/nn_vod/nn_x64/aWQ9NTFjZDI3NTg4MjIwYjNlNDE4MWEwMWRhOTBkM2ZmZDgmdXJsX2MxPTZkNmY3NjY5NjUyZjYxNmY2ZDY1NmU2NjY1NmU2Nzc5NzU2ZTJmNjE2ZjZkNjU2ZTY2NjU2ZTY3Nzk3NTZlMmU3NDczMjAwMCZubl9haz0wMWNlODlkMGEyNjQ4MDRkZGQ0Mzg4ZGYyYTA3Y2IzYzJkJm50dGw9MyZucGlwcz01Mi44LjE4NS4xNjg6NTEwMCZuY21zaWQ9MTAwODAwMSZuZ3M9NTcyMzQ3MDQwMDBkNjhmNDIwZmM3YmVmNjRjNDJmNzYmbm5kPWNuLnpnZHguc2ljaHVhbiZuZnQ9dHMmbm5fdXNlcl9pZD1jbnR2MDAxYWNjMTU1YzdjJm5kdD1zdGImbmR2PTEuNC4wLkNPTS1DSElOQS1PVFQtU1RCLjEuMF9EZW1vJm5hbD0wMTA0NDcyMzU3MDYwNzRjMjNlMTFiZjcwMjljZDdkZTMxNWM1OTMzNTNmN2Vk/51cd27588220b3e4181a01da90d3ffd8.ts”

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mplayer_layout);
        //initVideoData();
        initViews();
        initMedia();
    }

    /**
     * 从本地xml文件获取一系列播放串
     */
    private void initVideoData() {
          try {
                InputStream ins = getResources().getAssets().open("videopath.xml");
                //XmlPullParser xmlParser = Xml.newPullParser();
                XmlPullParser xmlParser = XmlPullParserFactory.newInstance().newPullParser();
                xmlParser.setInput(ins, "utf-8");
                int evtType = xmlParser.getEventType();
                channelLists = new ArrayList<>();
                while (evtType != XmlPullParser.END_DOCUMENT) {
                    switch (evtType) {
                        case XmlPullParser.START_TAG:
                            String attr = xmlParser.getName();
                            //Log.d(TAG, "start Tag：" + attr);
                            if ("video".equalsIgnoreCase(attr)) {
                                channelData = new ChannelData();
                            } else if (channelData != null) {
                                if ("name".equalsIgnoreCase(attr)) {
                                    channelData.channelName = xmlParser.nextText();
                                } else if ("play_url".equalsIgnoreCase(attr)) {
                                    channelData.playUrl = xmlParser.nextText();
                                }
                            }
                            break;
                        case XmlPullParser.END_TAG:
                            //Log.d(TAG, "end Tag：" + xmlParser.getName());
                            if ("video".equals(xmlParser.getName()) && channelData != null) {
                                channelLists.add(channelData);
                                channelData = null;
                            }
                            break;
                        default:
                            break;
                    }
                    //获得下一个节点的信息
                    evtType = xmlParser.next();
                }
            } catch (IOException | XmlPullParserException e) {
                e.printStackTrace();
            }
            Log.i(TAG, "共有" + channelLists.size() + "条数据");
    }

    /**
     * 初始化视图
     */
    private void initViews() {
        mSurfaceView = (SurfaceView) findViewById(R.id.surface);
        //mSurfaceView = new SurfaceView(this);
        //root.addView(mSurfaceView, new RelativeLayout.LayoutParams(-1, -1));
        holder = mSurfaceView.getHolder();
        holder.setKeepScreenOn(true); //强制屏幕等待
        holder.addCallback(mSurfaceHolderCallback);
        leftListView = (ListView) findViewById(R.id.leftchanellView);
        //seekBar = (SeekBarView) findViewById(R.id.mplayer_progress);
        rightChangeBtn = (TextView) findViewById(R.id.changeSize_id);
        currentTimeView = (TextView) findViewById(R.id.leftTime);
        totalTimeView = (TextView) findViewById(R.id.rightTime);

        if (channelLists != null) {
            chanellListAdapter = new ChanellListAdapter(this, channelLists, R.layout.simple_listviewitem);
            leftListView.setAdapter(chanellListAdapter);
            //leftListView.setOnClickListener();
        } else {
            Log.e(TAG, "本地数据源为空！！");
        }
    }


    /*********** 通过定时器和Handler来更新进度条 ************/
    TimerTask mTimerTask = new TimerTask() {
        @Override public void run() {
            if(mPlayer==null){
                return;
            }
            if (mPlayer.isPlaying()) {
                mHandler.sendEmptyMessage(AUTO_REFRESH_FLAG);
            }
        }
    };

    private Handler mHandler = new Handler(){
		public void handleMessage(Message msg){
			 switch (msg.what) {
				 case AUTO_REFRESH_FLAG:
					 //autoRefreshProgress();
                     Log.i(TAG,"handleMessage RefreshProgress  +1");

					 break;
				 default:
					 break;
         	}
		}
	};

    /**
     * 刷新播放进度
     */
    public void startPlayerTimer() {
		new Thread() {
			@Override
			public void run() {
                //Message msg = Message.obtain();
                //msg.what = AUTO_REFRESH_FLAG;
                //mHandler.sendMessage(msg);
                mHandler.sendEmptyMessage(AUTO_REFRESH_FLAG);
			}
		}.start();
	}

    /**
     * 始化播放器
     **/
    private void initMedia() {
        //mPlayer.setLooping(true);
    }

    private void openVieo() {
        //if (channelLists.size() != 0) {
        //    play_url = channelLists.get(0).playUrl;
        //}
        Log.d(TAG, "openVieo() VideoFilePath:" + play_url);
        releaseMediaPlayer();
        mPlayer = new MediaPlayer();
        mPlayer.setOnVideoSizeChangedListener(mOnVideoSizeListener);
        mPlayer.setOnSeekCompleteListener(onSeekCompleteListener);
        mPlayer.setOnCompletionListener(mOnCompletionListener);
        mPlayer.setOnPreparedListener(mPreparedListener);
        mPlayer.setOnErrorListener(mOErrorListener);
        mPlayer.setOnInfoListener(mOnInfoListener);
        mPlayer.setDisplay(holder);
        try {
            if (!TextUtils.isEmpty(play_url)) {
                mPlayer.setDataSource(play_url);                            //Idle ———> Initialized
                mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);     //设置多媒体流类型
                mPlayer.setVolume(1.0f, 1.0f);

                //Initialized ———>Preparing  异步准备
                mPlayer.prepareAsync();
                currentMediaState = MPstate.PREPARING;
                // 监听缓冲完成后回调onPrepared()  Preparing ———> Prepared
                // prepare() 或 prepareAsync() 方法，这两个方法一个是同步的一个是异步的，
                // 只有进入 Prepared 状态，才表明 MediaPlayer 到目前为止都没有错误，可以进行文件播放.
                // 如果异步准备完成，会触发 OnPreparedListener.onPrepared() ，进而进入 Prepared 状态。
            } else {
                Toast.makeText(this, "play_url is empty ！！", Toast.LENGTH_SHORT).show();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 点击频道列表播放
     * @param url
     */
    private void clickToPlay(String url) {
        mPlayer.stop();
        mPlayer.reset();
        try {
            mPlayer.setDataSource(url);
            mPlayer.prepare();
            mPlayer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void onClick(View v) {

    }

    /*******************************   各类监听器  start   *********************************/


    /**
     * 播放器准备Listener
     *  mPlayer.prepareAsync()后回调到此
     */
    private MediaPlayer.OnPreparedListener mPreparedListener = new MediaPlayer.OnPreparedListener() {
        @Override
        public void onPrepared(MediaPlayer mp) {
            ToastTools.showMgtvWaringToast(KingZMediaPlayer.this, "onPrepared！");
            currentMediaState = MPstate.PREPARED;
			//duration = mp.getDuration();
			//rightTime = timeFormat(duration);
			//totalTime.setText(rightTime);
            mVideoWidth = mPlayer.getVideoHeight();
            mVideoHeight = mPlayer.getVideoWidth();
            Log.i(TAG,"mVideoWidth="+mVideoWidth+";mVideoHeight="+mVideoHeight);
            //如果Video的宽高超出了当前屏幕的大小 就进行缩放
            if (mVideoWidth > getWindowManager().getDefaultDisplay().getWidth()
                    || mVideoHeight > getWindowManager().getDefaultDisplay().getHeight()) {
            }
            if (mVideoWidth != 0 && mVideoHeight != 0) {
                startToPlay();
            }
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

    /**
     * onSeekComplete Listener
     */
    private MediaPlayer.OnSeekCompleteListener onSeekCompleteListener = new MediaPlayer.OnSeekCompleteListener() {

        @Override
        public void onSeekComplete(MediaPlayer mp) {
            Toast.makeText(KingZMediaPlayer.this, "UserSeekComplete", Toast.LENGTH_SHORT).show();
            Log.i(TAG, "UserSeekComplete");
        }
    };

    /**
     * 播放错误Listener
     */
    private MediaPlayer.OnErrorListener mOErrorListener = new MediaPlayer.OnErrorListener() {
        @Override
        public boolean onError(MediaPlayer mp, int what, int extra) {
            ToastTools.showMgtvWaringToast(KingZMediaPlayer.this, "播放出错！"+"what="+what+";extra="+extra);
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
            finish();
            return false;
        }
    };
    /**
     * 播放完成Listener
     */
    private MediaPlayer.OnCompletionListener mOnCompletionListener = new MediaPlayer.OnCompletionListener() {
        @Override
        public void onCompletion(MediaPlayer mp) {
            Log.i(TAG, "OnCompletionListener.onCompletion()   finish");
            Toast.makeText(KingZMediaPlayer.this, "播放完成", Toast.LENGTH_SHORT).show();
            mPlayer.stop();
            mPlayer.release();
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
            Log.i(TAG, "surfaceCreated()");
            openVieo();
        }

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            Log.i(TAG, "surfaceChanged()");
        }

        @Override
        public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
            Log.i(TAG, "surfaceDestroyed()");
            holder = null;
            //releaseMediaPlayer();
        }
    };

    /******************************* 各类监听器  End *********************************/

    private void doPuase() {
        if (mPlayer.isPlaying()) {
            mPlayer.pause();
            currentMediaState = MPstate.PAUSED;
        }
    }

    private void startToPlay() {
        Log.i(TAG,"startToPlay()");
        if(mPlayer != null){
            mPlayer.start();
            currentMediaState = MPstate.PLAYING;
        }
    }

    @Override
    protected void onRestart() {
        mPlayer.start();
        super.onRestart();
    }

    protected void onDestroy() {
        if (mPlayer != null) {
            mPlayer.release(); //Idle----->End
        }
        currentMediaState = MPstate.End;
        super.onDestroy();
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            showSeekBarView();
            showListView();
            if (currentMediaState == MPstate.PLAYING) {
				//doPuase();
                return true;
            } else if (currentMediaState == MPstate.PAUSED) {
                startToPlay();
                seekBar.setVisibility(View.VISIBLE);
                return true;
            }
        }
        if (event.getAction() == MotionEvent.ACTION_MOVE) {
			//isSeekState = true;
            float y = event.getY();
            float x = event.getX();
            Log.i(TAG, "onTouchEvent   得到的X = " + x + ";得到的y:" + y);
        }
        return super.onTouchEvent(event);
    }

    private void showSeekBarView() {
        if (!seekBar.isShown()) {
            seekBar.setVisibility(View.VISIBLE);
        }
    }

    private void showListView() {
        if (!leftListView.isShown()) {
            leftListView.setVisibility(View.VISIBLE);
        } else {
            leftListView.setVisibility(View.INVISIBLE);
        }
    }

    /**
     * 释放播放器
     */
     private void releaseMediaPlayer() {
        Log.i(TAG,"releaseMediaPlayer()");
        if (mPlayer != null) {
            mPlayer.reset();    // ———> 【IDLE】通过 reset() 方法进入 idle 状态的话会触发 OnErrorListener.onError() ，
                                // 并且 MediaPlayer 会进入 Error 状态；如果是新创建的 MediaPlayer 对象，
                                // 则并不会触发 onError(), 也不会进入 Error 状态。
            mPlayer.release();  //通过 releaseMediaPlayer() 方法可以进入 End 状态，只要 MediaPlayer 对象不再被使用，
                                // 就应当尽快将其通过 releaseMediaPlayer() 方法释放掉，以释放相关的软硬件组件资源，
                                // 这其中有些资源是只有一份的（相当于临界资源）。
                                // 如果 MediaPlayer 对象进入了 End 状态，则不会在进入任何其他状态了。
            mPlayer = null;
            currentMediaState = MPstate.IDLE;
        }
    }


    @Override
    protected void onPause() {
        //if (mPlayer.isPlaying()) {
        //    int position = mPlayer.getCurrentPosition();
        //    mPlayer.stop();
        //}
        super.onPause();
    }
}
