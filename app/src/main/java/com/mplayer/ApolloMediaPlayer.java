package com.mplayer;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import com.base.BaseActivity;
import com.kingz.customdemo.R;
import com.kingz.utils.ToastTools;
import com.kingz.utils.ZLog;
import com.mplayer.adapter.ChanellListAdapter;
import com.mplayer.view.ApolloSeekBar;
import com.provider.ChannelData;
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
 * Discription:播放器页面
 * mPlayer.reset();               //----->Idle
 * mPlayer.releaseMediaPlayer();  //Idle----->End
 * setDataSource();               //Initialized
 * prepare();                     //Prepared
 * <p/>
 * <p/>
 * 还需完成的代码：
 * 1：进度条
 * 2：加载圈
 * 3：滑动快进
 * 4：浮层的出入动画
 * 5:频道列表
 */
public class ApolloMediaPlayer extends BaseActivity {

    private static final String TAG = "ApolloMediaPlayer";
    private ApolloSeekBar seekBar;
    private MediaPlayerKernel mPlayer;

    private ListView leftListView;
    private TextView rightChangeBtn;
    private String playedTime;
    private String totalTime;
    private ChanellListAdapter chanellListAdapter;
    private ArrayList<ChannelData> channelLists;
    private ChannelData channelData = new ChannelData();
    public static final int TIMER_FLAG = 0x0001;

    private StringBuilder mFormatBuilder;
    private Formatter mFormatter;
    private String play_url = "";      //播放地址
    private int duration;
    private int minStepLen;
    private int mVideoWidth;
    private int mVideoHeight;
    private int currentPosition;

    /**
     * 画面比例
     */
    private ScreenScaletype videoScreenMode = ScreenScaletype.SCREENTYPE_TOW;

    private enum ScreenScaletype {
        SCREENTYPE_ONE("4:3"), SCREENTYPE_TOW("16:9");

        private String mode;

        ScreenScaletype(String mode) {
            this.mode = mode;
        }

        @Override
        public String toString() {
            return mode;
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mplayer_layout);
        showLoadingDialog();
        mFormatBuilder = new StringBuilder();
        mFormatter = new Formatter(mFormatBuilder, Locale.getDefault());
        initVideoData();
        init();
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
        ZLog.i(TAG, "共有" + channelLists.size() + "条数据");
    }

    /**
     * 从网络获取播放串
     */
//    private void getPlayUrlFromNet(){
//    }

    private void init() {
        initChannelListView();
        initMPlayer();
        initSeekBar();
    }

    private void initChannelListView() {
        leftListView = (ListView) findViewById(R.id.leftchanellView);
        if (channelLists != null) {
            chanellListAdapter = new ChanellListAdapter(this, channelLists);
            leftListView.setAdapter(chanellListAdapter);
            leftListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    ZLog.i(TAG, "onItemClick() getCurrentPlayerState：" + mPlayer.getState());
                    stopSeekBarTimer();
                    if (mPlayer.getState() == MediaPlayerKernel.MediaState.PLAYING) {
                        mPlayer.stop();
                    }
                    if(mPlayer.getState() != MediaPlayerKernel.MediaState.IDLE){
                        //IDLE状态去reset会抛出非法的状态异常
                        mPlayer.reset();
                    }
                    if (channelLists.isEmpty() || TextUtils.isEmpty(channelLists.get(position).playUrl)) {
                        ToastTools.getInstance().showMgtvWaringToast(ApolloMediaPlayer.this, "视频地址不能为空");
                        return;
                    }
                    String url = channelLists.get(position).playUrl;
                    Uri uri = Uri.parse(url);
                    mPlayer.openVideo(uri);
                    ZLog.i(TAG, "播放的视频地址：" + url);
                    anthorFlag = true;
                }
            });
        } else {
            Log.e(TAG, "本地数据源为空！！");
        }
    }

    private void initSeekBar() {
        seekBar = (ApolloSeekBar) findViewById(R.id.mediaplayer_seekbar);
        seekBar.setApolloSeekBarChangeListener(new ApolloSeekBar.IOnApolloSeekBarChangeListener() {
            @Override
            public void onProgressChanged(ApolloSeekBar seekBar, int progress, boolean fromUser) {
            }

            @Override
            public void onStartTrackingTouch(ApolloSeekBar seekBar) {
                anthorFlag = false;
            }

            @Override
            public void onStopTrackingTouch(ApolloSeekBar seekBar) {
                anthorFlag = true;
                long onStopP0stion = seekBar.getProgress();
                mPlayer.seekTo((int) onStopP0stion);
            }
        });
        rightChangeBtn = (TextView) findViewById(R.id.changeSize_id);
        rightChangeBtn.setOnClickListener(ItemClickedListenner);
    }

    private void initMPlayer() {
        mPlayer = (MediaPlayerKernel) findViewById(R.id.mplayercore);
        mPlayer.setOnStateChangeListener(new MediaPlayerKernel.OnStateChangeListener() {
            @Override
            public void onSurfaceViewDestroyed(SurfaceHolder surface) {
                anthorFlag = false;
            }

            @Override
            public void onSurfaceViewCreated(SurfaceHolder surface) {
                ZLog.d(TAG,"ApolloMpalayer onSurfaceViewCreated. Auto play first Video.");
                mPlayer.setVideoURI(Uri.parse(channelLists.get(0).playUrl));
            }

            @Override
            public void onBuffering(MediaPlayer mp) {
            }

            @Override
            public void onPlaying(MediaPlayer mp) {
                ZLog.d(TAG,"ApolloMpalayer onPlaying.");

            }

            @Override
            public void onSeek(MediaPlayer mp, int max, int progress) {
                ZLog.d(TAG,"ApolloMpalayer onSeek.  progress = " + progress + "; max = " + max);
            }

            @Override
            public void onStop(MediaPlayer mp) {
                ZLog.d(TAG,"ApolloMpalayer onStop.");
            }

            @Override
            public void onPause(MediaPlayer mp) {
            }

            @Override
            public void onError() {
                stopSeekBarTimer();
                ZLog.i(TAG, "ApolloMpalayer onError");
            }

            @Override
            public void onComplete() {
                stopSeekBarTimer();
                ZLog.i(TAG, "UserSeekComplete");
            }

            @Override
            public void onPrepare() {
                dismissLoadingDialog();
                startSeekBarTimer();
                ToastTools.getInstance().showMgtvWaringToast(ApolloMediaPlayer.this, "开始播放");
                duration = mPlayer.getMediaPlayer().getDuration();
                if (duration > 0) {
                    setRightSideTime(formatTimeToHHMMSS(duration));
                    seekBar.setMax(duration);
                }
                mVideoHeight = mPlayer.getMediaPlayer().getVideoHeight();
                mVideoWidth = mPlayer.getMediaPlayer().getVideoWidth();
                Log.i(TAG, "mVideoWidth=" + mVideoWidth + ";mVideoHeight=" + mVideoHeight + ";   video duration = " + duration);
                if (mVideoWidth > getWindowManager().getDefaultDisplay().getWidth() || mVideoHeight > getWindowManager().getDefaultDisplay().getHeight()) {
                }
                if (mVideoWidth != 0 && mVideoHeight != 0) {
                    if (mPlayer != null) {
                        mPlayer.start();
                        mPlayer.setState(MediaPlayerKernel.MediaState.PLAYING);
                        ZLog.i(TAG, "onPrepare() setPlayeState is Playing");
                    }
                }
            }
        });
    }

    private boolean anthorFlag = false;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case TIMER_FLAG:
                    if (seekBar.getVisibility() == View.VISIBLE) {
                        seekBar.setProgress(mPlayer.getCurrentPosition());
                    }
                    break;
            }
        }
    };

    private boolean isSeekTimerStart = false;
    private void startSeekBarTimer() {
        isSeekTimerStart = true;
        new Thread() {
            @Override
            public void run() {
                while (isSeekTimerStart) {
                    if (!seekBar.mIsDragging) {
                        try {
                            mHandler.sendEmptyMessage(TIMER_FLAG);
                            Thread.sleep(400);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }.start();
    }

    private void stopSeekBarTimer(){
        isSeekTimerStart = false;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        super.dispatchTouchEvent(event);
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:

                return false;
            case MotionEvent.ACTION_MOVE:
                //isSeekState = true;
                //通过滑动改变进度条，有个滑动因子
                //float y = event.getY();
                //float x = event.getX();
                return false;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                Log.i(TAG, "ACTION_UP");
                //changeAllViews();
                return false;
        }
        //super.dispatchTouchEvent(ev)，事件向下分发
        //onInterceptTouchEvent是ViewGroup提供的方法，默认返回false，返回true表示拦截。
        //onTouchEvent是View中提供的方法，ViewGroup也有这个方法，view中不提供onInterceptTouchEvent。view中默认返回true，表示消费了这个事件。
        return false;
    }

    private void changeAllViews() {
        changeSeekBarView();
        changeShowListView();
        changeScaleBtnView();
    }

    private void changeSeekBarView() {
        if (seekBar.getVisibility() != View.VISIBLE) {
            ZLog.i(TAG, "changeSeekBarView() show seekBar");
            seekBar.setVisibility(View.VISIBLE);
        } else {
            seekBar.setVisibility(View.INVISIBLE);
        }
    }

    private void changeShowListView() {
        if (leftListView.getVisibility() != View.VISIBLE) {
            leftListView.setVisibility(View.VISIBLE);
        } else {
            leftListView.setVisibility(View.INVISIBLE);
        }
    }

    private void changeScaleBtnView() {
        if (rightChangeBtn.getVisibility() != View.VISIBLE) {
            rightChangeBtn.setVisibility(View.VISIBLE);
        } else {
            rightChangeBtn.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    protected void onPause() {
        //if (mPlayer) {
        //    int position = mPlayer.getCurrentPosition();
        //    mPlayer.releasePlayer();
        //}
        super.onPause();
    }

    @Override
    protected void onRestart() {
        mPlayer.start();
        super.onRestart();
    }

    protected void onDestroy() {
        stopSeekBarTimer();
        if (mPlayer != null) {
            mPlayer.release();
        }
        super.onDestroy();
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
    }

    View.OnClickListener ItemClickedListenner = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.changeSize_id:
                    changeVideoScale();
                    break;
            }
        }
    };

    public void changeVideoScale() {
        if (videoScreenMode == ScreenScaletype.SCREENTYPE_ONE) {
            mVideoWidth = mVideoWidth * 3 / 4;
            videoScreenMode = ScreenScaletype.SCREENTYPE_TOW;
            changeInUIThread(ScreenScaletype.SCREENTYPE_ONE.toString());
        } else if (videoScreenMode == ScreenScaletype.SCREENTYPE_TOW) {
            mVideoWidth = mVideoWidth * 4 / 3;
            videoScreenMode = ScreenScaletype.SCREENTYPE_ONE;
            changeInUIThread(ScreenScaletype.SCREENTYPE_TOW.toString());

        }
        ZLog.i(TAG, "onClick() changePlayerScale mVideoWidth=" + mVideoWidth);
        mPlayer.setVideoScreenScale(mVideoWidth, mVideoHeight);
    }

    public void changeInUIThread(final String str) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                rightChangeBtn.setText(str);
            }
        });
    }

    private void releasePlayer(){
        mPlayer.releasePlayer();
    }

    private MediaPlayerKernel.MediaState getMplayerState(){
        return mPlayer.getState();
    }
}
