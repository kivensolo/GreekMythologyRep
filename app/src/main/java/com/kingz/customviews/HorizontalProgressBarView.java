package com.kingz.customviews;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.View;

import com.kingz.utils.ScreenTools;

/**
 * Copyright(C) 2016, 北京视达科科技有限公司
 * All rights reserved. <br>
 * author: King.Z <br>
 * date:  2016/6/30 17:56 <br>
 * description: horizontal <br>
 */
public class HorizontalProgressBarView extends View {

    private static final String TAG = HorizontalProgressBarView.class.getSimpleName();
    private static final int MSG_NUM = 0x6029;
    private boolean isRunning = false;
    private boolean isComplete;
    private Context context;
    private Paint innerPaint;        //内部画笔
    private Paint outerPaint;        //外部画笔（显示进度)
    private Paint textPaint;
    private RectF innerRect;
    private RectF outerRect;

    /**
     * 进度条坐标初始化设置
     */
    private int progressLeft = ScreenTools.Operation(ScreenTools.SCREEN_WIDTH / 4);
    private int progressWidth = ScreenTools.Operation(ScreenTools.SCREEN_WIDTH / 2);
    private int progressTop = ScreenTools.Operation(450);
    private int progressHeight = ScreenTools.Operation(7);
    private int innerRectWidth = ScreenTools.Operation(0);
    private int totalWidth;

    private ProgressCompleteListener listener;

    public interface ProgressCompleteListener {
        void onComplete();
    }

    public void setProgressCompleteListener(ProgressCompleteListener listener) {
        this.listener = listener;
    }

    public HorizontalProgressBarView(Context context) {
        super(context);
        this.context = context;
        initViews();
    }

    public HorizontalProgressBarView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        initViews();
    }

    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_NUM:
                    refreshProgress();
                    break;
                default:
                    break;
            }
        }
    };

    private void initViews() {
        isRunning = true;
        innerPaint = new Paint();
        outerPaint = new Paint();
        textPaint = new Paint();

        innerPaint.setDither(true);
        innerPaint.setAntiAlias(true);
        innerPaint.setStyle(Paint.Style.FILL);
        outerPaint.setAntiAlias(true);
        outerPaint.setStyle(Paint.Style.FILL);
        //innerPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
        textPaint.setColor(0x99ffffff);
        textPaint.setTextSize(35);
        textPaint.setTypeface(Typeface.defaultFromStyle(Typeface.ITALIC));
        textPaint.setAntiAlias(true);

        outerRect = new RectF(progressLeft,
                progressTop,
                progressLeft + progressWidth,
                progressTop + progressHeight);
        innerRect = new RectF(progressLeft,
                progressTop,
                progressLeft + innerRectWidth,
                progressTop + progressHeight);
    }

    public void setInnerPaintColor(int paintColor) {
        if(innerPaint != null){
            innerPaint.setColor(paintColor);
        }else{
              throw new IllegalStateException(
            "innerPaint has not been initialized !!!");
        }
    }
    public void setOuterPaintColor(int paintColor) {
        if(outerPaint != null){
            outerPaint.setColor(paintColor);
        }else{
              throw new IllegalStateException(
            "innerPaint has not been initialized !!!");
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        innerRect = new RectF(progressLeft, progressTop, progressLeft + innerRectWidth, progressTop + progressHeight);    //内部矩形
        //Logger.i(TAG,"innerRect = "+innerRect.toString());
        canvas.save();
        canvas.drawRoundRect(outerRect, 5, 5, outerPaint);
        canvas.drawRoundRect(innerRect, 5, 5, innerPaint);
        canvas.drawText(String.valueOf(innerRectWidth * 100 / totalWidth) + "%",
                progressLeft + ScreenTools.Operation(274),
                progressTop + ScreenTools.Operation(50),
                textPaint);
    }

    private void refreshProgress() {
        if (innerRectWidth == progressWidth) {
            isComplete = true;
            if (listener != null) {
                listener.onComplete();
            }
            isRunning = false;
        }
        this.invalidate();
    }

    public boolean isComplete(){
        return isComplete;
    }

    /**
     * 设置进度条当前宽度
     * @param width 实际值：屏幕适配后的值
     */
    public void setWidth(int width) {
        innerRectWidth = width;
    }

    /**
     * 设置进度条总宽度
     * @param totalWidth  实际值：屏幕适配后的值
     */
    public void setTotalWidth(int totalWidth) {
        this.totalWidth = totalWidth;
    }

    public void beginDraw() {
        new Thread() {
            @Override
            public void run() {
                while (isRunning) {
                    mHandler.sendEmptyMessage(MSG_NUM);
                    try {
                        Thread.sleep(50);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();
    }
}
