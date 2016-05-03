package com.mplayer;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.*;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import com.kingz.uiusingListViews.R;

/**
 * Created by KingZ on 2016/1/24.
 * Discription:KingZ播放器的进度条View
 */
public class SeekBarView extends View implements View.OnTouchListener{

    private static String TAG = "SeekBarView";

    public static final int TIMER_START_FLAG = 0x100;
    public static final int MAX_PROGRESS_RANG = 10000;  //最大范围，即10000个进度点

    private static final int PLAYER_SLOW_TIMER = 0x501;
     /**
      * 检测播放进度任务周期ms
      **/
    public static final int PLAY_TIMER_INTERVAL = 499;
     /**
      * 线程运行开关
      **/
    public boolean threadExitFlag = false;

    /*-----------seek方向-------------*/
	private static final int MOVING_DIRECTION_INVALID = 0; // 无效
	private static final int MOVING_DIRECTION_FRONT = 1; // 前进
	private static final int MOVING_DIRECTION_BACK = 2; // 后退

    /*-----------seek命令------------*/
	private static final int MOVING_CMD_INVALID = 0; // 无效
	private static final int MOVING_CMD_GO = 1; // 开始
	private static final int MOVING_CMD_STOP = 2; // 停止

     /*-----------播放状态------------*/
    private int playStatusFlag = 0;
    public static final int PLAY_STATUS_NORMAL = 1;   // 正常播放
    public static final int PLAY_STATUS_PAUSE = 2;    // 暂停
    public static final int PLAY_STATUS_FFORWARD = 3; // 快进
    public static final int PLAY_STATUS_REWIND= 4;    // 快退

    private MediaPlayer mPlayer = null;

    public boolean isPlaying = true;
    private int progressBarWidth = 0;   //进度条宽度
    private int progressBarHeight = 0;  //进度条高度
    private int currentPlayPos = 0;     //当前播放点
    private String seekTime;
    private String rightSideTime;   //右侧显示时间

    private boolean isPlayerComplete = false;  //是否播放完成
    private boolean isPlayerSucceed = false;   //是否已经成功播放，以进度条时间是否有变化为基准

    private static final String defaultRightTime = "00:00:00";

    /** 播放图标**/
    private Bitmap thumb;
    private Bitmap playStatus;
    private Bitmap pauseStatus;
    private Bitmap fforwordStatus;
    private Bitmap rewindStatus;
    /** 播放图标**/

    private Rect durationRect;
    private Rect totalBarRect;
    private Rect bgSrcRect;
    private Rect bgDstRect;
    private Rect playStatusRect;
    private Rect thumbRect;
//    private Rect durationRect;
//    private Rect durationRect;

    private Canvas canvas;

    private Paint seekPaint;
    private Paint playProgressPaint;
    private Paint remainProgressPaint;
    private Paint thumbPaint;
    private Paint playStatusPaint;
    private Paint borderPaint;
    private Paint rightSideTextPaint;
    private Context context;


    IMplayerSeekBarListener lsnr = null;
    public interface IMplayerSeekBarListener {
        public void onUserPauseOrStart();
        public void onUserSeekStart();
        public void onUserSeekEnd(long seekPos);

        public long uiProgress2PlayProgress(int uiProgress);
        public int playProgress2uiProgress(long playProgress);

        public String getPosDiscribByPlayPos(long pos);

        public void onPlayToPreNode();
    }

    public SeekBarView(Context context) {
        this(context,null);
    }

    public SeekBarView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public SeekBarView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.context = context;
        initView();
    }

    private void initView() {

//        playStatus = decodeResource(getResources(), R.drawable.mplayerv_state_play);
//        pauseStatus = decodeResource(getResources(), R.drawable.mplayerv_state_pause);
//        fforwordStatus = decodeResource(getResources(), R.drawable.mplayerv_state_fforward);
//        rewindStatus = decodeResource(getResources(), R.drawable.mplayerv_state_rewind);
//        playStatusRect = new Rect(0,0,0,0);

        progressBarWidth = 720;
        currentPlayPos = 0;
        totalBarRect = new Rect(0,0,1190,50);

        seekPaint = new Paint();
        seekPaint.setColor(getResources().getColor(R.color.orange));
//        seekPaint.setStrokeWidth(4);
        seekPaint.setDither(true);
        seekPaint.setStyle(Paint.Style.FILL);
        seekPaint.setAntiAlias(true);
        seekPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_OVER));

        thumbPaint = new Paint();
        thumbPaint.setAlpha(255);
        thumb = decodeResource(getResources(), R.drawable.seekbar_thumb_img);
        thumbRect = new Rect(0,0,0,0);

        /** 总progress */
        borderPaint = new Paint();
        borderPaint.setColor(getResources().getColor(R.color.lightskyblue));
        borderPaint.setStrokeWidth(4);
        borderPaint.setStyle(Paint.Style.FILL);
        borderPaint.setAntiAlias(true);


        /** 文字画笔 **/
        Rect strRect = new Rect();
        rightSideTextPaint = new Paint();
        rightSideTextPaint.setColor(getResources().getColor(android.R.color.holo_purple));
        rightSideTextPaint.setStyle(Paint.Style.FILL);
        rightSideTextPaint.setAntiAlias(true);
        rightSideTextPaint.getTextBounds(defaultRightTime, 0, defaultRightTime.length(), strRect);
        rightSideTextPaint.setTextSize(18);
        rightSideTextPaint.setStrokeWidth(0);


    }

    public void show(){

    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawInVod(canvas);
    }

    private void drawInVod(Canvas canvas) {
        if(currentPlayPos >= 1100){
            currentPlayPos = 1100;
        }
        Log.d(TAG,"drawInVod --------- currentPlayPos = "+ currentPlayPos);
        durationRect = new Rect(0,0, currentPlayPos,50);

        canvas.save();
        canvas.clipRect(totalBarRect);
        canvas.drawRect(totalBarRect,borderPaint);
        canvas.restore();

        canvas.save();
        canvas.clipRect(durationRect);
        canvas.drawRect(durationRect,seekPaint);
        canvas.restore();

        /**
         * 绘制Thumb图片
         **/
        thumbRect.left = currentPlayPos - 14;
        thumbRect.top = 10;
        thumbRect.right = thumbRect.left + thumb.getWidth();
        thumbRect.bottom = 50;
        canvas.drawBitmap(thumb, null, thumbRect, thumbPaint);

        /**
         * 绘制总时间长度
         */
//         Rect strRect = new Rect();
//        if(rightSideTime != null){
//            rightSideTextPaint.getTextBounds(defaultRightTime,0,defaultRightTime.length(),strRect);
//            canvas.drawText(rightSideTime,900,10,rightSideTextPaint);
//        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()){
//            case :
//            break;
        }
        return false;
    }

    private Handler mHandler = new Handler(){
		public void handleMessage(Message msg){
			 switch (msg.what) {
				 case TIMER_START_FLAG:
                       //反复执行线程检测
                        if(threadExitFlag){
                            return;
                        }
                        currentPlayPos = mPlayer.getCurrentPosition();
                        setCurrentPlayPos(currentPlayPos,mPlayer.getDuration());
					 break;
				 default:
					 break;
         	}
		}
	};
     private boolean playerTimerIsRunning = false;
     public void startPlayerTimer() {
    	if(playerTimerIsRunning){
    		return;
    	}
    	playerTimerIsRunning = true;
        mHandler.sendEmptyMessage(TIMER_START_FLAG);
    }

    public void setCurrentPlayPos(long postion,long duration){
        mHandler.sendEmptyMessage(TIMER_START_FLAG);
        currentPlayPos = (int) (500 * postion/duration);
        this.invalidate();
    }

    public void initMplayer(MediaPlayer mPlayer){
        this.mPlayer = mPlayer;
//        startPlayerTimer();
        playStatusFlag = PLAY_STATUS_NORMAL;
    }
//    public void initMplayer(MediaPlayer mPlayer,IMplayerSeekBarListener lsnr){
////        playerTimerSlowTask();
//        if (null == lsnr) {
//			return;
//		}
//        this.mPlayer = mPlayer;
//        this.lsnr = lsnr;
//        startPlayerTimer();
//        playStatusFlag = PLAY_STATUS_NORMAL;
//    }
    public void unInit() {
		isPlaying = false;
	}


    public int getMax() {
        return MAX_PROGRESS_RANG;
    }
    public void setRightSideTime(String rightSideTime) {
        this.rightSideTime = rightSideTime;
    }

      /**
     * 获取Resource 图片资源
     * @param resources
     * @param id
     * @return
     */
     private Bitmap decodeResource(Resources resources, int id) {
    	TypedValue value = new TypedValue();
    	resources.openRawResource(id, value);
    	BitmapFactory.Options opts = new BitmapFactory.Options();
    	opts.inTargetDensity = value.density;
    	return BitmapFactory.decodeResource(resources, id, opts);
    }

}
