package com.mplayer;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.*;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import com.kingz.uiusingListViews.R;

/**
 * Created by KingZ on 2016/1/24.
 * Discription:
 * KingZ播放器的进度条View
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
    public static final int SEEK_PROGRESS_HEIGHT = 11; //进度条绘制高度
    private int totalLength = 0;                         //进度条宽度
    private int currentPlayPos = 0;                      //当前播放点
    private int playLength = 0;                          //当前播放长度--UI
    private String seekTime;                             //seek时间点
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

    private Canvas canvas;

    private Paint seekPaint;
    private Paint playProgressPaint;
    private Paint remainProgressPaint;
    private Paint thumbPaint;
    private Paint seekDotPaint;
    private Paint playStatusPaint;
    private Paint totalBarPaint;
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

        /** 总progress */
        totalBarRect = new Rect(0,6, 1088,SEEK_PROGRESS_HEIGHT);
        totalBarPaint = new Paint();
        totalBarPaint.setColor(getResources().getColor(R.color.gray));
        totalBarPaint.setStyle(Paint.Style.FILL);
        totalBarPaint.setAntiAlias(true);
//        totalBarPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_OVER));

        /** 播放进度参数 **/
        durationRect = new Rect(0,6, playLength,SEEK_PROGRESS_HEIGHT);
        seekPaint = new Paint();
        seekPaint.setColor(getResources().getColor(R.color.dodgerblue));
//      seekPaint.setStrokeWidth(4);
        seekPaint.setDither(true);
        seekPaint.setStyle(Paint.Style.FILL);
        seekPaint.setAntiAlias(true);
        seekPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_OVER));

        /** 图片型seek进度点 */
//        thumbPaint = new Paint();
//        thumbPaint.setAlpha(255);
//        thumb = decodeResource(getResources(), R.drawable.seekbar_thumb_img);
//        thumbRect = new Rect(0,0,0,0);

        /** 圆点Thumb */
        seekDotPaint = new Paint();
        seekDotPaint.setColor(getResources().getColor(R.color.dodgerblue));
        seekDotPaint.setStyle(Paint.Style.FILL);
        seekDotPaint.setAntiAlias(true);
        seekDotPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_OVER));

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

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawInVod(canvas);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        totalLength = MeasureSpec.getSize(widthMeasureSpec);
//        Log.d(TAG,"进度条总长度："+ totalLength);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    private void drawInVod(Canvas canvas) {
        if(playLength >= totalLength){
            playLength = totalLength;
            threadExitFlag = true;
        }
//        Log.d(TAG,"drawInVod --------- currentPlayPos = "+ currentPlayPos);

        //绘制总长度
        canvas.save();
        canvas.clipRect(totalBarRect);
        canvas.drawRect(totalBarRect, totalBarPaint);
        canvas.restore();

        //绘制播放长度及thumb
        canvas.save();
        canvas.clipRect(durationRect);
        canvas.drawRect(durationRect,seekPaint);
        canvas.restore();
        canvas.drawCircle(playLength,8,7,seekDotPaint);

        //<editor-fold desc="备用代码">
        /**
         * 绘制Thumb图片
         **/
//        thumbRect.left = currentPlayPos - 14;
//        thumbRect.top = 10;
//        thumbRect.right = thumbRect.left + thumb.getWidth();
//        thumbRect.bottom = 50;
//        canvas.drawBitmap(thumb, null, thumbRect, thumbPaint);

        /**
         * 绘制总时间长度
         */
//         Rect strRect = new Rect();
//        if(rightSideTime != null){
//            rightSideTextPaint.getTextBounds(defaultRightTime,0,defaultRightTime.length(),strRect);
//            canvas.drawText(rightSideTime,900,10,rightSideTextPaint);
//        }
        //</editor-fold>
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
                        setCurrentPlayPos2UI(currentPlayPos,mPlayer.getDuration());
					 break;
				 default:
					 break;
         	}
		}
	};

    public void setCurrentPlayPos2UI(long currentPlayPostion, long duration){
        mHandler.sendEmptyMessage(TIMER_START_FLAG);
        playLength = (int) (totalLength * currentPlayPostion/duration);
        durationRect = new Rect(0,6, playLength,SEEK_PROGRESS_HEIGHT);
        this.invalidate();
    }

    public void initMplayer(MediaPlayer mPlayer){
        this.mPlayer = mPlayer;
        playStatusFlag = PLAY_STATUS_NORMAL;
    }

    public void releaseMplayer(){
        this.mPlayer = null;
    }

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
