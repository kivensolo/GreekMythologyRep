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

/**
 * Created by KingZ on 2016/1/24.
 * Discription:播放器测试Demo
 *      mPlayer.release(); //Idle----->End
 *       mPlayer.reset();  //----->Idle
 *       setDataSource();  //Initialized
 *       prepare();        //Prepared
 */
public class KingZMediaPlayer extends Activity implements View.OnClickListener {

    public static final boolean isPlaying = false;
    private static final String TAG = "KingZMediaPlayer";

    /**
     * 播放器状态参数
     */
    private enum MPstate {
        Play, Stop, Pause, Idle, End
    }

    private MPstate mediaState = MPstate.Idle;         //初始化播放器状态
    private MPstate currentPlayState = MPstate.Play; //默认为播放状态

    private SeekBarView seekBar;
    private MediaPlayer mPlayer;
    private SurfaceView mSFview;
    private SurfaceHolder holder;

    /**
     * 影片参数
     */
    private String currentPlayTime;        //当oonta前播放的时间
    private long duration;                // 影片持续时间
    private int mVideoWidth;
    private int mVideoHeight;
    private ListView leftListView;
    private TextView rightChangeBtn;
    private TextView totalTimeView;
    private TextView currentTimeView;
    private ChanellListAdapter chanellListAdapter;
    private ArrayList<ChannelData> channelLists;
    private ChannelData channelData = new ChannelData();
//	private ListView leftListView;


//    private String play_url = "http://182.138.101.48:57850/nn_live.ts?id=MGYY&url_c1=2000&nn_ak=01626a8d247dec670bc542614b6756e051&npips=192.168.95.78:5100&ncmsid=100001&ngs=56f1427800080441628cb21aa78d6b71&nn_user_id=wz&ndt=stb&nn_day=20160324&nn_begin=203239&ndv=4.2.24.0.0.SC-XJCBC-STB-QZ.0.0_Release&nn_timezone=8";

    private String play_url = "http://v6.pstatp.com/origin/9582/6002841301?Signature=orKrL7Nng7LFSqKYclJ58HhU5BM%3D&Expires=1458839167&KSSAccessKeyId=qh0h9TdcEMrm1VlR2ad/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mplayer_layout);

        initData();
        initViews();
        initMedia();
    }

    private void initData() {
        try {
            InputStream ins = getResources().getAssets().open("videopath.xml");
//			XmlPullParser xmlParser = Xml.newPullParser();
            XmlPullParser xmlParser = XmlPullParserFactory.
                    newInstance().newPullParser();
            xmlParser.setInput(ins, "utf-8");
            int evtType = xmlParser.getEventType();
            channelLists = new ArrayList<>();
            while (evtType != XmlPullParser.END_DOCUMENT) {
                switch (evtType) {
                    case XmlPullParser.START_TAG:
                        String attr = xmlParser.getName();
                        Log.d(TAG, "start Tag：" + attr);
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
                        Log.d(TAG, "end Tag：" + xmlParser.getName());
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
        mSFview = (SurfaceView) findViewById(R.id.surface);
        leftListView = (ListView) findViewById(R.id.leftchanellView);
        seekBar = (SeekBarView) findViewById(R.id.mplayer_progress);
        rightChangeBtn = (TextView) findViewById(R.id.changeSize_id);
        currentTimeView = (TextView) findViewById(R.id.leftTime);
        totalTimeView = (TextView) findViewById(R.id.rightTime);

        if (channelLists != null) {
            chanellListAdapter = new ChanellListAdapter(this, channelLists, R.layout.simple_listviewitem);
            leftListView.setAdapter(chanellListAdapter);
        } else {
            Log.e(TAG, "数据源错误！！");
        }
    }

    private Handler mHandler = new Handler(){
		public void handleMessage(Message msg){
			 switch (msg.what) {
				 case 1:
//					 autoRefreshProgress();
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
			}
		}.start();
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

        /*************** SurfaceView 设置 *********************************/
        mSFview = (SurfaceView) this.findViewById(R.id.surface);
        holder = mSFview.getHolder();
        holder.setKeepScreenOn(true);//强制屏幕等待
        holder.addCallback(mSurfaceHolderCallback);
        //把输送给surfaceView的视频画面直接显示到屏幕上，不用维护自身的缓冲区,目前已经废弃  当需要的时候系统会自动设定
//		holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
//		holder.setFixedSize(480, 320);

//		mSFview.getParent().

    }

    /**
     * 开始播放
     */
    private void startoPlay() {
        if (channelLists.size() != 0) {
            play_url = channelLists.get(0).playUrl;
        }
        Log.d(TAG, "the videoFilePath:" + play_url);
        mediaState = MPstate.Play;
        mPlayer.reset(); //转为Idel
        mPlayer.setDisplay(holder);                            //把视频画面输出到SurfaceView
        mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);  //设置多媒体流类型
        mPlayer.setVolume(1.0f, 1.0f);
        try {
            if (!TextUtils.isEmpty(play_url)) {
                mPlayer.setDataSource(play_url);                        //设置需要播放的视频 ，建立视频对象
                mPlayer.prepare();                                    //缓冲处理  监听缓冲完成就需要setOnPreparedListener
            } else {
                Toast.makeText(this, "play_url is empty ！！", Toast.LENGTH_SHORT).show();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        mPlayer.start();
    }

    /**
     * 点击频道名播放
     *
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

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {

    }

    /*******************************  各类监听器  start   *********************************/
    /**
     * 播放器准备Listener
     */
    private MediaPlayer.OnPreparedListener mPreparedListener = new MediaPlayer.OnPreparedListener() {

        @Override
        public void onPrepared(MediaPlayer mp) {
//			duration = mp.getDuration();
//			rightTime = timeFormat(duration);
//			totalTime.setText(rightTime);
            mVideoWidth = mPlayer.getVideoHeight();
            mVideoHeight = mPlayer.getVideoWidth();
            //如果Video的宽高超出了当前屏幕的大小 就进行缩放
            if (mVideoWidth > getWindowManager().getDefaultDisplay().getWidth()
                    || mVideoHeight > getWindowManager().getDefaultDisplay().getHeight()) {


            }
            mPlayer.start();
        }
    };

    private MediaPlayer.OnInfoListener mOnInfoListener = new MediaPlayer.OnInfoListener() {
        @Override
        public boolean onInfo(MediaPlayer mp, int what, int extra) {
            Log.i(TAG, "OnInfo：" + "waht = " + what + "---extra = " + extra);
            //当一些特定信息出现或者警告时触发
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
            Toast.makeText(KingZMediaPlayer.this, "seekToEnd", Toast.LENGTH_SHORT).show();
            Log.i(TAG, "onSeekComplete");
        }
    };

    /**
     * 播放错误Listener
     */
    private MediaPlayer.OnErrorListener mOErrorListener = new MediaPlayer.OnErrorListener() {
        @Override
        public boolean onError(MediaPlayer mp, int what, int extra) {
            ToastTools.showMgtvWaringToast(KingZMediaPlayer.this, "播放出错！");
            switch (what) {
                case MediaPlayer.MEDIA_ERROR_SERVER_DIED:
                    Log.v("Play Error:::", "MEDIA_ERROR_SERVER_DIED" + ", extra:" + extra);
                    break;
                case MediaPlayer.MEDIA_ERROR_UNKNOWN:
                    Log.v("Play Error:::", "MEDIA_ERROR_UNKNOWN" + ", extra:" + extra);
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
            startoPlay();
        }

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            Log.i(TAG, "surfaceChanged()");
        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {
            Log.i(TAG, "surfaceDestroyed()");
        }
    };

    /*******************************
     * 各类监听器  End
     *********************************/

    private void doPuase() {
        mediaState = MPstate.Pause;
        mPlayer.pause();
    }

    private void doPlay() {
        mediaState = MPstate.Play;
        mPlayer.start();
    }

    /************
     * 生命周期
     ***************/

    @Override
    protected void onRestart() {
        mPlayer.start();
        super.onRestart();
    }

    protected void onDestroy() {
        if (mPlayer != null) {
            mPlayer.release(); //Idle----->End
        }
        mediaState = MPstate.End;
        super.onDestroy();
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            showSeekBarView();
            showListView();
            if (mediaState == MPstate.Play) {
//				doPuase();
                return true;
            } else if (mediaState == MPstate.Pause) {
                doPlay();
                seekBar.setVisibility(View.VISIBLE);
                return true;
            }
        }
        if (event.getAction() == MotionEvent.ACTION_MOVE) {
//			isSeekState = true;
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

}
