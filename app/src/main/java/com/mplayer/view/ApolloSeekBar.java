package com.mplayer.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import com.kingz.customdemo.R;
import com.utils.ZLog;

/**
 * Created by KingZ on 2016/1/24.
 * Discription:
 * Apollo播放器的进度条View
 */
public class ApolloSeekBar extends View {

    private static final String TAG = "ApolloSeekBar";
    private Context context;
    private int mBarWidth;
    private int mBarHeight;

    private static final int PADDING_LEFT = 10;
    private static final int PROGRESS_HEIGHT = 10;
    private static final String defaultRightTime = "00:00:00";

    private RectF areaRect = new RectF();
    private RectF totalRect = new RectF();
    private RectF playedRect = new RectF();
    private Paint totalPaint = new Paint();
    private Paint timeInfoPaint = new Paint();
    private Paint areaBkgPaint = new Paint();
    private Paint thumbPaint = new Paint();
    private Paint playedPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    private int maxProgress = 100;
    private long currentPlayPos = 0;
    public boolean mIsDragging;

    private IOnApolloSeekBarChangeListener mApolloSeekBarChangeListener;

    public interface IOnApolloSeekBarChangeListener {
        void onProgressChanged(ApolloSeekBar seekBar, int progress, boolean fromUser);

        void onStartTrackingTouch(ApolloSeekBar seekBar);

        void onStopTrackingTouch(ApolloSeekBar seekBar);
    }

    public void setApolloSeekBarChangeListener(IOnApolloSeekBarChangeListener lsr) {
        mApolloSeekBarChangeListener = lsr;
    }

    void onProgressChanged(ApolloSeekBar seekBar, int progress, boolean fromUser) {
        //super.onProgressRefresh(scale, fromUser);
        if (mApolloSeekBarChangeListener != null) {
            mApolloSeekBarChangeListener.onProgressChanged(seekBar, progress, fromUser);
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
        totalPaint.setColor(getResources().getColor(R.color.black));

        initStateImg();

        areaBkgPaint.setColor(getResources().getColor(R.color.transparent_ban));
        areaBkgPaint.setStyle(Paint.Style.FILL);
        areaBkgPaint.setAntiAlias(true);

        playedPaint.setColor(getResources().getColor(R.color.darkorange));
        playedPaint.setStyle(Paint.Style.FILL);
        playedPaint.setAntiAlias(true);
        playedPaint.setDither(true);

        thumbPaint.setColor(getResources().getColor(R.color.darkorange));
        thumbPaint.setStyle(Paint.Style.FILL);
        thumbPaint.setAntiAlias(true);
        thumbPaint.setDither(true);

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
        //areaRect.set(0, 0, mBarWidth, mBarHeight); // 当前View的区域

    }


    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawRoundRect(areaRect,35,35, areaBkgPaint);
        canvas.drawRoundRect(totalRect,4,4,totalPaint);
        playedRect.set(totalRect);
        playedRect.right = totalRect.left + getCurrentUIProgress();
        canvas.drawRoundRect(playedRect,4,4, playedPaint);

        canvas.drawCircle(playedRect.right,mBarHeight/2,PADDING_LEFT,thumbPaint);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        super.dispatchTouchEvent(event);
        float off_dx;
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                off_dx = event.getX() - totalRect.left;
                float pos = off_dx/getWidth() * maxProgress;
                ZLog.i(TAG,"ACTION_DOWN  pos="+pos);
                setProgressInternal((int) pos, true);
                onStartTrackingTouch();
                return true;
            case MotionEvent.ACTION_MOVE:
                off_dx = event.getX() - totalRect.left;
                float pos2 = off_dx/getWidth() * maxProgress;
                ZLog.i(TAG,"ACTION_MOVE  pos2="+pos2);
                setProgressInternal((int) pos2, true);
                return true;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                onProgressChanged(this, (int)currentPlayPos,true);
                onStopTrackingTouch();
                invalidate();
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
    }

    public long getCurrentUIProgress() {
        return currentPlayPos * getWidth() / maxProgress;
    }

     private int getViewWidth() {
        return (int) totalRect.width();
    }
}
