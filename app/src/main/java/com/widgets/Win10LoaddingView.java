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
    private static final int DURATION = 3 * 1000;
    private Paint mPaint;
    private Path mPath;
    private Path dstPath;
    private RectF rect;
    private PathMeasure mPathMeasure;   //截取Path中的一部分并显示
    private ValueAnimator valueAnimator;
    /**
     * 加载圈宽高
     */
    private int mWidth, mHeight;
    /**
     * Path绘制的总长度
     */
    private float mLength;
    /**
     * 每一圈的进度百分比
     */
    private float precent; //接受ValueAnimator的返回值，代表整个动画的进度
    /**
     * 圆点个数
     */
    private int dotNum;

    private float x;
    private float y;

    public Win10LoaddingView(Context context) {
        this(context, null);
    }

    public Win10LoaddingView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);

    }

    public Win10LoaddingView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mPaint = new Paint();
        mPaint.setStyle(Paint.Style.STROKE);
        //mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(15);
        mPaint.setColor(Color.WHITE);
        //设置画笔为圆笔
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setAntiAlias(true); //抗锯齿

        dstPath = new Path();                      //用来储存截取后的内容
        mPath = new Path();
        rect = new RectF(-150, -150, 150, 150);
        mPath.addArc(rect, -90, 359.9f);              //矩形内画弧线  如果是360f  那起点就会从0度开始
        mPathMeasure = new PathMeasure(mPath, false); //创建一个与path相关联的PathMeasure
        mLength = mPathMeasure.getLength();

        valueAnimator = ValueAnimator.ofFloat(0f, 1f).setDuration(DURATION);
        valueAnimator.setRepeatCount(-1);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                precent = (float) animation.getAnimatedValue();
                invalidate();
            }
        });
        valueAnimator.start();
        setLayerType(View.LAYER_TYPE_SOFTWARE, null);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        dotNum = (int) (precent / 0.05);

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
        ZLog.d(TAG, "mPathMeasure.getLength() * precent=" + mPathMeasure.getLength() * precent);

        drawHelpLine(canvas);

        dstPath.reset();
        dstPath.rLineTo(0, 0);        // 硬件加速的BUG
        //------ 进度百分比每变化5%就画一个点
        drawDotsOnPath();

        mPaint.setStrokeWidth(15);
        mPaint.setColor(Color.WHITE);
        //------ 画一个点
        //getDotOnPath();
        //------ 画实际的轨迹
        //getRellaryPath();
        canvas.drawPath(dstPath, mPaint);                //绘制dst
        //每次转动一圈聚成一个点后都会闪一下，这是因为重新开始动画刷新视图的原因，这里的补救方法就是我们在动画快结束的时候手动画一个点
        if (0.997 <= precent  && precent <=1) {
            canvas.drawPoint(0, -150, mPaint);
        }
    }

    /**
     * 画多个渐变的点
     */
    private void drawDotsOnPath() {
        dotNum = (int) (precent % 0.05);
        switch (dotNum) {
            case 0:
                //0%时 第一个点
                getSegmentWithPath(0f);
            case 1:
                //5%时 第二个点
                getSegmentWithPath(0.05f);

            case 2:
                //10%时 第三个点
                getSegmentWithPath(0.1f);
            case 3:
                //15%时 第四个点
                getSegmentWithPath(0.15f);
            //case 4:
            //    //20%时 第五个点
            //    getSegmentWithPath(0.2f);
            default:
                break;
        }
    }

    /**
     * 画辅助线
     * @param canvas
     */
    private void drawHelpLine(Canvas canvas) {
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(1);
        mPaint.setColor(Color.YELLOW);
        canvas.drawPath(mPath, mPaint);

        mPaint.setStrokeWidth(1);
        mPaint.setColor(Color.RED);
        canvas.drawRect(rect, mPaint);
    }

    /**
     * start=0：画path的实际路径
     * start != 0 :path长度变化
     */
    private void getRellaryPath() {
        //float start = (float) (mLength * Math.pow(precent,2)); //到二分之一处间距最大
        float stop = mLength * precent;
        float start = (float) (stop - ((0.5 - Math.abs(precent - 0.5)) * mLength));
        mPathMeasure.getSegment(start, stop, dstPath, true);
    }

    /**
     * 获取一个点
     */
    private void getDotOnPath() {
        float start = mLength * precent;
        float last = start + 1;
        mPathMeasure.getSegment(start, last, dstPath, true);        //截取一部分存入dst中
    }

    /**
     * 计算截取间隔位置
     * @param dfloat 百分比
     */
    private void getSegmentWithPath(float dfloat) {
        x = precent - dfloat * (1 - precent);   //间距由0.05线性平滑到0
        //y = mLength * x;                //点与点间距不变
        y = -mLength * (x * x - 2 * x);
        mPathMeasure.getSegment(y, y + 1, dstPath, true);
    }
}
