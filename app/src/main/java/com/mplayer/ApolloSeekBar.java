package com.mplayer;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import com.kingz.customdemo.R;

/**
 * Created by KingZ on 2016/1/24.
 * Discription:
 * Apollo播放器的进度条View
 */
public class ApolloSeekBar extends View {

    private static String TAG = "ApolloSeekBar";

    public static final int TIMER_START_FLAG = 0x100;
    private Context context;
    private MediaPlayerKernel mPlayer = null;
    public boolean isPlaying = true;
    public static final int SEEK_PROGRESS_HEIGHT = 11; //进度条绘制高度
    private int totalLength = 0;                         //进度条宽度
    private int playLength = 0;                          //当前播放长度--UI
    private String seekTime;                             //seek时间点
    private String rightSideTime;   //右侧显示时间

    private boolean isPlayerComplete = false;  //是否播放完成
    private boolean isPlayerSucceed = false;   //是否已经成功播放，以进度条时间是否有变化为基准

    private static final String defaultRightTime = "00:00:00";

    private static final int PADDING_LEFT = 15;
    private static final int PROGRESS_HEIGHT = 15;

    private int mBarWidth;
    private int mBarHeight;

    private RectF areaRect = new RectF();
    private RectF totalRect = new RectF();
    private RectF playedRect = new RectF(totalRect);
    private Paint playedPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint totalPaint = new Paint();
    private Paint timeInfoPaint = new Paint();
    private Paint defaultPaint = new Paint();

    public int maxProgress = 100;
    private long currentPlayPos = 0;
    private boolean mIsDragging;

    private IOnApolloSeekBarChangeListener mApolloSeekBarChangeListener;

    public interface IOnApolloSeekBarChangeListener {
        void onProgressChanged(ApolloSeekBar seekBar, int progress, boolean fromUser);

        void onStartTrackingTouch(ApolloSeekBar seekBar);

        void onStopTrackingTouch(ApolloSeekBar seekBar);
    }

    public void setApolloSeekBarChangeListener(IOnApolloSeekBarChangeListener lsr) {
        mApolloSeekBarChangeListener = lsr;
    }

    void onProgressRefresh(float scale, boolean fromUser) {
        //super.onProgressRefresh(scale, fromUser);
        if (mApolloSeekBarChangeListener != null) {
            mApolloSeekBarChangeListener.onProgressChanged(this, (int) getProgress(), fromUser);
        }
    }

    void onStartTrackingTouch() {
        mIsDragging = true;
        if (mApolloSeekBarChangeListener != null) {
            mApolloSeekBarChangeListener.onStartTrackingTouch(this);
        }
    }

    void onStopTrackingTouch() {
        mIsDragging = false;
        if (mApolloSeekBarChangeListener != null) {
            mApolloSeekBarChangeListener.onStopTrackingTouch(this);
        }
    }

    public ApolloSeekBar(Context context) {
        this(context, null);
    }

    public ApolloSeekBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ApolloSeekBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.context = context;
        initView();
    }

    private void initView() {
        setFocusableInTouchMode(true);
        totalPaint.setDither(true);
        totalPaint.setAntiAlias(true);
        totalPaint.setStyle(Paint.Style.FILL);
        totalPaint.setColor(getResources().getColor(R.color.qianpurple));

        initStateImg();

        defaultPaint.setColor(getResources().getColor(R.color.transparent_ban));
        defaultPaint.setStyle(Paint.Style.FILL);
        defaultPaint.setAntiAlias(true);

        playedPaint.setColor(getResources().getColor(R.color.dodgerblue));
        playedPaint.setStyle(Paint.Style.FILL);
        playedPaint.setAntiAlias(true);
        playedPaint.setDither(true);
        //playedPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_OVER));
        //initThumb();

        Rect strRect = new Rect();
        timeInfoPaint = new Paint();
        timeInfoPaint.setColor(getResources().getColor(android.R.color.holo_purple));
        timeInfoPaint.setStyle(Paint.Style.FILL);
        timeInfoPaint.setAntiAlias(true);
        timeInfoPaint.getTextBounds(defaultRightTime, 0, defaultRightTime.length(), strRect);
        timeInfoPaint.setTextSize(18);
        timeInfoPaint.setStrokeWidth(0);

    }


    private void initStateImg() {
        //playStatus = decodeResource(getResources(), R.drawable.mplayerv_state_play);
        //pauseStatus = decodeResource(getResources(), R.drawable.mplayerv_state_pause);
        //fforwordStatus = decodeResource(getResources(), R.drawable.mplayerv_state_fforward);
        //rewindStatus = decodeResource(getResources(), R.drawable.mplayerv_state_rewind);
        //playStatusRect = new Rect(0,0,0,0);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        if (widthMode == MeasureSpec.EXACTLY) {
            mBarWidth = widthSize;
        } else {
            mBarWidth = widthSize;
        }

        if (heightMode == MeasureSpec.EXACTLY) {
            mBarHeight = heightSize;
        } else {
            mBarHeight = heightSize;
        }
        setMeasuredDimension(mBarWidth, mBarHeight);
        int left = PADDING_LEFT;
        int top = (mBarHeight - PROGRESS_HEIGHT) / 2;
        totalRect.set(left, top, mBarWidth - PADDING_LEFT, top + PROGRESS_HEIGHT);
        areaRect.set(0, 0, mBarWidth, mBarHeight);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawRoundRect(areaRect,25,25,defaultPaint);
        canvas.drawRect(totalRect,totalPaint);
        playedRect.right = totalRect.left + getCurrentUIProgress();
        canvas.drawRoundRect(playedRect, 5, 5, playedPaint);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        super.dispatchTouchEvent(event);
        float off_dx;
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (mApolloSeekBarChangeListener != null) {
                    mApolloSeekBarChangeListener.onStartTrackingTouch(this);
                }
                break;
            case MotionEvent.ACTION_MOVE:
                off_dx = event.getX() - totalRect.left;
                float pos = off_dx/getWidth() * maxProgress;
                setProgressInternal((int) pos, true);
                return true;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                if (mApolloSeekBarChangeListener != null) {
                    mApolloSeekBarChangeListener.onStopTrackingTouch(this);
                    mApolloSeekBarChangeListener.onProgressChanged(this, (int)currentPlayPos,true);
                }
                return true;
        }
        return false;
    }

    public int getMax() {
        return maxProgress;
    }

    public void setMax(int value) {
        maxProgress = value;
    }

    public long getProgress() {
        return currentPlayPos;
    }
    public void setProgress(int pos) {
        setProgressInternal(pos,false);
    }

     private void setProgressInternal(int pos, boolean fromUser) {
        if (pos > maxProgress) {
            pos = maxProgress;
        } else if (pos < 0) {
            pos = 0;
        }
        currentPlayPos = pos;
        invalidate();
    }

    public void setRightSideTime(String rightSideTime) {
        this.rightSideTime = rightSideTime;
    }

    public long getCurrentUIProgress() {
        return currentPlayPos * getWidth() / maxProgress;
    }

     private int getViewWidth() {
        return (int) totalRect.width();
    }
}
