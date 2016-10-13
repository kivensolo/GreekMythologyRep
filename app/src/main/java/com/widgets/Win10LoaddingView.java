package com.widgets;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.*;
import android.util.AttributeSet;
import android.view.View;
import com.utils.ZLog;

/**
 * Copyright(C) 2016, 北京视达科科技有限公司
 * All rights reserved. <br>
 * author: King.Z <br>
 * date:  2016/10/11 18:57 <br>
 * description: 仿win10加载圈 <br>
 */

public class Win10LoaddingView extends View {
    private static final String TAG = Win10LoaddingView.class.getSimpleName();
    private Paint mPaint;
    private Path mPath;
    private Path dstPath;
    private PathMeasure mPathMeasure;   //截取Path中的一部分并显示
    private ValueAnimator valueAnimator;
    private int mWidth, mHeight;
    //用这个来接受ValueAnimator的返回值，代表整个动画的进度
    private float precent;

    public Win10LoaddingView(Context context) {
        this(context, null);
    }

    public Win10LoaddingView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);

    }

    public Win10LoaddingView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mPaint = new Paint();
        mPaint.setStyle(Paint.Style.FILL);
        //mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(15);
        mPaint.setColor(Color.WHITE);
        //设置画笔为圆笔
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setAntiAlias(true); //抗锯齿

        dstPath = new Path();                      //用来储存截取后的内容
        mPath = new Path();
        RectF rect = new RectF(-150, -150, 150, 150); //矩形
        mPath.addArc(rect, -90, 360f);              //矩形内画圆弧形
        mPathMeasure = new PathMeasure(mPath, false); //创建一个与path相关联的PathMeasure

        valueAnimator = ValueAnimator.ofFloat(0f, 1f).setDuration(3 * 1000);
        valueAnimator.setRepeatCount(-1);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                precent = (float) animation.getAnimatedValue();
                invalidate();
            }
        });
        valueAnimator.start();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        if (widthMode == MeasureSpec.EXACTLY) {
            mWidth = widthSize;
        } else {
            mWidth = widthSize;
        }

        if (heightMode == MeasureSpec.EXACTLY) {
            mHeight = heightSize;
        } else {
            mHeight = heightSize;
        }
        setMeasuredDimension(mWidth, mHeight);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
       canvas.translate(mWidth / 2, mHeight / 2);  //画布原点移动至中心
        ZLog.d(TAG,"mPathMeasure.getLength() * precent="+mPathMeasure.getLength() * precent);
        dstPath.reset();
        dstPath.lineTo(0,0); // android.os.Build.VERSION_CODES#KITKAT 更早  硬件加速的bug
        //截取一部分存入dst中
        mPathMeasure.getSegment(mPathMeasure.getLength() * precent,
                                mPathMeasure.getLength() * precent + 7,
                                dstPath,
                                true);
        dstPath.lineTo(50,0);
        canvas.drawPath(dstPath, mPaint);                //绘制dst
    }
}
