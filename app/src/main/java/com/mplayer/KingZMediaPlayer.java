package com.mplayer;

import android.app.Activity;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import com.kingz.customdemo.R;
import com.provider.ChannelData;
import com.utils.ToastTools;
import com.utils.ZLog;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Formatter;
import java.util.Locale;

/**
 * Created by KingZ on 2016/4/16.
 * Discription:播放器测试
 * mPlayer.reset();               //----->Idle
 * mPlayer.releaseMediaPlayer();  //Idle----->End
 * setDataSource();               //Initialized
 * prepare();                     //Prepared
 */
public class KingZMediaPlayer extends Activity {

    private static final String TAG = "KingZMediaPlayer";
    private SeekBarView seekBar;
    private MediaPlayerKernel mPlayer;

    private ListView leftListView;
    private TextView rightChangeBtn;    //画面比例切换，暂未使用
    private TextView rightTextView;     //片长总时间，暂未使用
    private TextView leftTimeView;      //当前播放时间，暂未使用
    private String playedTime;
    private String totalTime;
    private ChanellListAdapter chanellListAdapter;
    private ArrayList<ChannelData> channelLists;
    private ChannelData channelData = new ChannelData();

    private StringBuilder mFormatBuilder;
    private Formatter mFormatter;
    private String play_url = "";      //播放地址
    private MediaPlayerKernel.OnStateChangeListener mOnStateChangeListener;
     private int duration;
     private int minStepLen;
    private int mVideoWidth;
    private int mVideoHeight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mplayer_layout);
        initVideoData();
        initViews();
//      getPlayUrlFromNet();
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
     * 从网络获取播放串
     */
//    private void getPlayUrlFromNet(){
//        Parameters para = new Parameters();
//        para.put("url", "http://tv.sohu.com/20150921/n421709205.shtml");
//
//        ApiStoreSDK.execute("http://apis.baidu.com/dmxy/truevideourl/truevideourl",
//                ApiStoreSDK.GET,
//                para,
//                new ApiCallBack(){
//                    @Override
//                    public void onSuccess(int i, String result) {
//                        Log.i(TAG, "getPlayUrlFromNet() onSuccess; result="+result);
//                        try {
//                            JSONObject jsonObject = new JSONObject(result);
//                            play_url =  jsonObject.getString("mp4");
//                             Log.i(TAG, "getPlayUrlFromNet() play_url="+play_url);
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                        openVieo();
//                    }
//                    @Override
//                    public void onError(int i, String s, Exception e) {
//                        Log.i(TAG, "onError, status: " + s);
//                        Log.i(TAG, "errMsg: " + (e == null ? "" : e.getMessage()));
//                    }
//                    @Override
//                    public void onComplete() {
//                        Log.i(TAG, "getPlayUrlFromNet() onComplete");
//
//                    }
//                });
//    }

    /**
     * 初始化视图
     */
    private void initViews() {
        mPlayer = (MediaPlayerKernel) findViewById(R.id.mplayercore);
        initListner();
        mFormatBuilder = new StringBuilder();
        mFormatter = new Formatter(mFormatBuilder, Locale.getDefault());
        leftListView = (ListView) findViewById(R.id.leftchanellView);
        seekBar = (SeekBarView) findViewById(R.id.mplayer_progress);
        rightChangeBtn = (TextView) findViewById(R.id.changeSize_id);
        leftTimeView = (TextView) findViewById(R.id.leftTime);
        leftTimeView.setTextColor(getResources().getColor(R.color.white));
        rightTextView = (TextView) findViewById(R.id.rightTime);
        rightTextView.setTextColor(getResources().getColor(R.color.white));

        if (channelLists != null) {
            chanellListAdapter = new ChanellListAdapter(this, channelLists, R.layout.simple_listviewitem);
            leftListView.setAdapter(chanellListAdapter);
            //leftListView.setOnClickListener();
        } else {
            Log.e(TAG, "本地数据源为空！！");
        }
    }

    private void initListner() {
        mPlayer.setOnStateChangeListener(new MediaPlayerKernel.OnStateChangeListener() {
            @Override
            public void onSurfaceViewDestroyed(Surface surface) {

            }

            @Override
            public void onBuffering(MediaPlayer mp) {

            }

            @Override
            public void onPlaying(MediaPlayer mp) {

            }

            @Override
            public void onSeek(MediaPlayer mp, int max, int progress) {

            }

            @Override
            public void onStop(MediaPlayer mp) {

            }

            @Override
            public void onPause(MediaPlayer mp) {

            }

            @Override
            public void playFinish(MediaPlayer mp) {

            }

            @Override
            public void onPrepare() {
                duration =  mPlayer.getMediaPlayer().getDuration();
                if (duration > 0) {
                    minStepLen = duration / seekBar.getMax();
                    seekBar.setRightSideTime(formatTimeToHHMMSS(duration));
                    setRightSideTime(formatTimeToHHMMSS(duration));
                }
                mVideoWidth = mPlayer.getMediaPlayer().getVideoHeight();
                mVideoHeight = mPlayer.getMediaPlayer().getVideoWidth();
                Log.i(TAG, "mVideoWidth=" + mVideoWidth + ";mVideoHeight=" + mVideoHeight + ";   video duration = " + duration);
                if (mVideoWidth > getWindowManager().getDefaultDisplay().getWidth() || mVideoHeight > getWindowManager().getDefaultDisplay().getHeight()) {
                }
                if (mVideoWidth != 0 && mVideoHeight != 0) {
                    if (mPlayer != null) {
                        mPlayer.start();
                        mPlayer.setState(MediaPlayerKernel.MediaState.PLAYING);
                        ZLog.i(TAG,"onPrepare() setPlayeState is Playing");
                    }
                }
            }
        });
    }

    private boolean playerTimerIsRunning = false;

    private void startPlayerTimer() {
        if (playerTimerIsRunning) {
            return;
        }
        playerTimerIsRunning = true;
//        mHandler.sendEmptyMessage(PLAYER_SLOW_TIMER);
    }

    @Override
    protected void onRestart() {
        mPlayer.start();
        super.onRestart();
    }

    protected void onDestroy() {
        if (mPlayer != null) {
            mPlayer.release();
        }
        seekBar.threadExitFlag = true;
        super.onDestroy();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                ZLog.i(TAG, "getCurrentPlayerState：" + mPlayer.getState());
                if (mPlayer.getState() == MediaPlayerKernel.MediaState.IDLE) {
                    if (channelLists.isEmpty() || TextUtils.isEmpty(channelLists.get(0).playUrl)) {
                        ToastTools.getInstance().showMgtvWaringToast(this, "视频地址不能为空");
                    }
                    ZLog.i(TAG, "播放的视频地址：" + channelLists.get(0).playUrl);
                    mPlayer.setVideoURI(Uri.parse(channelLists.get(0).playUrl));
                    return true;
                } else if (mPlayer.getState() == MediaPlayerKernel.MediaState.PLAYING) {
                    mPlayer.pause();
                    return true;
                } else if (mPlayer.getState() == MediaPlayerKernel.MediaState.PAUSED) {
                    mPlayer.start();
                    return true;
                }

            case MotionEvent.ACTION_MOVE:
                //isSeekState = true;
                float y = event.getY();
                float x = event.getX();
                Log.i(TAG, "onTouchEvent   得到的X = " + x + ";得到的y:" + y);
                break;
            case MotionEvent.ACTION_UP:
                changeSeekBarView();
                changeShowListView();
                break;
        }
        return super.onTouchEvent(event);
    }

    private void changeSeekBarView() {
        if (!seekBar.isShown()) {
            seekBar.setVisibility(View.VISIBLE);
        } else {
            seekBar.setVisibility(View.INVISIBLE);
        }
    }

    private void changeShowListView() {
        if (!leftListView.isShown()) {
            leftListView.setVisibility(View.VISIBLE);
        } else {
            leftListView.setVisibility(View.INVISIBLE);
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

    /**
     * 设置进度条显示隐藏
     *
     * @param display
     */
    public void setSeekBarDisplay(int display) {
        seekBar.setVisibility(display);
    }


    class MplayerSeekBarListener implements SeekBarView.IMplayerSeekBarListener {

        @Override
        public void onUserPauseOrStart() {
//            doPauseOrStartVideo();
        }

        @Override
        public void onUserSeekStart() {

        }

        @Override
        public void onUserSeekEnd(long seekPos) {

        }

        @Override
        public long uiProgress2PlayProgress(int uiProgress) {

            return 0;
        }

        @Override
        public int playProgress2uiProgress(long playProgress) {
            return 0;
        }

        @Override
        public String getPosDiscribByPlayPos(long pos) {
            return null;
        }

        @Override
        public void onPlayToPreNode() {

        }
    }

    public String formatTimeToHHMMSS(int s) {
        s = s / 1000;
        int seconds = s % 60;
        int minutes = (s / 60) % 60;
        int hours = s / 3600;

        mFormatBuilder.setLength(0);
        if (hours > 0) {
            return mFormatter.format("%02d:%02d:%02d", hours, minutes, seconds).toString();
        } else {
            return mFormatter.format("00:%02d:%02d", minutes, seconds).toString();
        }
    }

    public void setRightSideTime(String rightSideTime) {
        totalTime = rightSideTime;
//        mHandler.sendEmptyMessage(SET_TOTAL_TIME);
    }
}
