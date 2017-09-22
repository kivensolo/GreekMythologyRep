package com.kingz.customviews;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;

import com.kingz.customdemo.R;

/**
 * Copyright(C) 2015, 北京视达科科技有限公司
 * All rights reserved.
 * author: King.Z
 * date: 2016 2016/3/27 21:57
 * description: 圆形进度条的绘制
 */
public class CircleProgressView extends View {
    public static final String TAG = "circleProgressView";

    /**
     * 是否停止绘图
     */
    public boolean isDrawing = false;

    /**
     * 第一圈的颜色
     */
    private int mFirstColor;
    /**
     * 第二圈的颜色
     */
    private int mSecondColor;
    /**
     * 圈的宽度
     */
    private int mCircleWidth;

    /**
     * 圈的半径
     */
    private int mCircleRadius;
    /**
     * Inner画笔
     */
    private Paint mFirstPaint;

    /**
     * Outer画笔
     */
    private Paint mSecondPaint;
    /**
     * 扫描角度
     */
    private int mSweepAngle;

    /**
     * 刷新速度
     */
    private int mSpeed;

    /**
     * 是否应该开始下一个
     */
    private boolean isNext = false;

    private ICircleDrawStart iCircleDrawStartListner;
    private ICircleDrawFinished iCircleDrawFinishedListner;

    public CircleProgressView(Context context) {
        this(context, null);
    }

    public CircleProgressView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircleProgressView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mSweepAngle = 0;
        getCustomArray(context, attrs, defStyle);
        initTools();
    }

    public void beginDrawCircle() {
        isDrawing = true;
        iCircleDrawStartListner.onStart();

        // 绘图线程
        new Thread(){
            public void run(){
                Log.i(TAG,"绘图线程在运行中......");
                while (isDrawing){
                    mSweepAngle++;
                    if (mSweepAngle == 360){
                        mSweepAngle = 0;
                        isNext = !isNext;
                        isDrawing = false;
                        iCircleDrawFinishedListner.onFinished();
                    }
                    postInvalidate();
                    try
                    {
                        Thread.sleep(mSpeed);
                    } catch (InterruptedException e)
                    {
                        e.printStackTrace();
                    }
                }
            }
        }.start();
    }

    /**
     * 获得自定义的样式属性
     */
    private void getCustomArray(Context context, AttributeSet attrs, int defStyle) {
        TypedArray typedArray = context.getTheme().obtainStyledAttributes(attrs, R.styleable.circleView, defStyle, 0);
        int typeCount = typedArray.getIndexCount();

        for (int i = 0; i < typeCount; i++) {
            int attr = typedArray.getIndex(i);
            switch (attr) {
                case R.styleable.circleView_innerColor:
                    mFirstColor = typedArray.getColor(attr, Color.RED);
                    break;
                case R.styleable.circleView_outerColor:
                    mSecondColor = typedArray.getColor(attr, Color.BLUE);
                    break;
                case R.styleable.circleView_circleWidth:
                    mCircleWidth = (int) typedArray.getDimension(attr,
                        TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,20,getResources().getDisplayMetrics())
                    );
                    break;
                case R.styleable.circleView_speed:
                    mSpeed = typedArray.getInteger(attr,2000);
                    break;
            }
        }
        //注意回收
        typedArray.recycle();
        mFirstPaint = new Paint();
        mSecondPaint = new Paint();
    }

    private void initTools() {

        mFirstPaint.setStyle(Paint.Style.STROKE);
        mFirstPaint.setColor(mFirstColor);
        mFirstPaint.setStrokeWidth(mCircleWidth);//设置圆环宽度
        mFirstPaint.setAntiAlias(true);

        mSecondPaint.setStyle(Paint.Style.STROKE);
        mSecondPaint.setColor(mSecondColor);
        mSecondPaint.setStrokeWidth(mCircleWidth);
        mSecondPaint.setAntiAlias(true);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int centre = Math.min(getWidth(),getHeight()) / 2;                // 获取圆心的x坐标
        int radius = centre - mCircleWidth / 2 - 10;     // 半径(防止切View的边缘)

        //设置一个弧的矩形区域(圆的直径长度)
        RectF oval = new RectF(centre - radius,centre - radius,centre + radius,centre + radius);
        canvas.drawCircle(centre,centre,radius,mFirstPaint);
        if(mSweepAngle != 0 &&  mSweepAngle % 360 == 0){
            canvas.drawCircle(centre,centre,radius,mSecondPaint);
        }else{
            canvas.drawArc(oval, -90, mSweepAngle, false, mSecondPaint); // 根据进度画圆弧
        }
    }

    public boolean getDrawState(){
        return isDrawing;
    }

    public void setCircleDrawFinishedListner(ICircleDrawFinished iCircleDrawFinishedListner){
        this.iCircleDrawFinishedListner = iCircleDrawFinishedListner;
    }

    public interface ICircleDrawFinished{
        void onFinished();
    }

    public void setCircleDrawStartListner(ICircleDrawStart iCircleDrawStartListner){
        this.iCircleDrawStartListner = iCircleDrawStartListner;
    }

    public interface ICircleDrawStart{
        void onStart();
    }
}
