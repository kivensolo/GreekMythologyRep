package com.view.scroll;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.ViewConfiguration;
import android.view.animation.BounceInterpolator;
import android.widget.Scroller;
import android.widget.TextView;

import com.kingz.customdemo.R;

/**
 * Copyright(C) 2016, 北京视达科科技有限公司
 * All rights reserved. <br>
 * author: King.Z <br>
 * date:  2016/8/19 17:37 <br>
 * description: 滑动Item的FlingView <br>
 */
public class FlingView extends TextView {

    public static final String TAG = FlingView.class.getSimpleName();
    private Scroller mScroller;
    private Paint borderPaint;
    private Paint textPaint;
    private Paint imagePaint;
    private int velocityX;
    private int velocityY;


    //手势监听类
    GestureDetector mGestureDetector; //可以代替很多onTouchEvent()中自己处理手势的东西
    //速度追踪器
    VelocityTracker velocityTracker = VelocityTracker.obtain();

    ViewConfiguration viewConfiguration;

    /** 系统所能识别出的被认为是滑动的最小距离 */
    private int touchSlop;
    /** 获取Fling速度的最小值和最大值 */
    private int minimumVelocity;
    private int maximumVelocity;
    /** 是否有物理按键 */
    private boolean isHavePermanentMenuKey;


    private float lastX;    //初始坐标X
    private float lastY;    //初始坐标Y
    private float startX;
    private float startY;


    public FlingView(Context context) {
        this(context, null);
    }

    public FlingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mScroller = new Scroller(context, new BounceInterpolator());
        mGestureDetector = new GestureDetector(context, new GestureListenerImpl());
        initViewConfiguaration(context);
        initPaints();
    }

    private void initPaints() {
        borderPaint = new Paint();
        textPaint = new Paint();
        imagePaint = new Paint();
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(200, 120);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        borderPaint.setStyle(Paint.Style.STROKE);
        borderPaint.setStrokeWidth(8);
        borderPaint.setColor(Color.CYAN);
        //绘制矩形的宽高通过 getMeasuredXXXX()获取
        canvas.drawRect(0, 0, getMeasuredWidth(), getMeasuredHeight(), borderPaint);
        setBackground(getResources().getDrawable(R.drawable.bkg2));
    }

    /**
     * ViewConfiguaration
     * @param context
     */
    private void initViewConfiguaration(Context context) {
        viewConfiguration = ViewConfiguration.get(context);
        touchSlop = viewConfiguration.getScaledTouchSlop();
        minimumVelocity = viewConfiguration.getScaledMinimumFlingVelocity();
        maximumVelocity = viewConfiguration.getScaledMaximumFlingVelocity();
        isHavePermanentMenuKey = viewConfiguration.hasPermanentMenuKey();
        //双击间隔时间.在该时间内是双击，否则是单击
        //int doubleTapTimeout=ViewConfiguration.getDoubleTapTimeout();
        ////按住状态转变为长按状态需要的时间
        //int longPressTimeout=ViewConfiguration.getLongPressTimeout();
        ////重复按键的时间
        //int keyRepeatTimeout=ViewConfiguration.getKeyRepeatTimeout();
    }

    public FlingView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.i(TAG,"onTouchEvent ： event = "+event.toString());
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                startX = event.getRawX();
                startY = event.getRawY();
                lastX = startX;
                lastY = startY;
                Log.i(TAG,"ACTION_DOWN :" + lastX + "-" + lastY);
                return true;
            case MotionEvent.ACTION_MOVE:
                //获取X/Y位移值
                float disX = event.getRawX() - lastX;
                float disY = event.getRawY() - lastY;
                offsetLeftAndRight((int) disX);//水平方向偏移量
                offsetTopAndBottom((int) disY);//垂直方向偏移量
                flingTest();
                lastX = event.getRawX();
                lastY = event.getRawY();
                Log.i(TAG,"ACTION_MOVE :" + lastX + "-" + lastY);
                velocityX = getscrollerVelocity_X();
                velocityY = getscrollerVelocity_Y();
                return true;
            case MotionEvent.ACTION_UP:
//                mScroller.startScroll((int)getX(), (int)getY(), -(int)(getX() - lastX),-(int)(getY() - lastY));
                mScroller.fling((int)lastX,(int)lastY,velocityX,velocityY, 0, 0, 400, 400);
                invalidate();
                return true;
        }
        return super.onTouchEvent(event);
        //return mGestureDetector.onTouchEvent(event);//由手势监听类处理
    }

        private void flingTest() {
        /**
         * 手势滑动，滑动距离由初始速度决定
         * startX：开始滑动的X坐标
         * startY：开始滑动的Y坐标
         * velocityX: X方向上的初始化滑行速度  像素/秒
         * velocityY: Y方向上的初始化滑行速度  像素/秒
         * minX:最小的X值，Scroll不会划过这个点
         * minY:最小的Y值，Scroll不会划过这个点
         * maxX:最大的X值，Scroll不会划过这个点
         * maxY:最大的Y值，Scroll不会划过这个点
         *
         */
//        mScroller.fling(getScrollX(),getScrollY(), getscrollerVelocity_X(), getscrollerVelocity_Y(), 23, 23, 1920, 1920);
    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        // 先判断mScroller滚动是否完成
        if (mScroller.computeScrollOffset()) {
            setX(mScroller.getCurrX());
            setY(mScroller.getCurrY());
            invalidate();
        }
    }

    /**
     * 获取X方向上的滑动速度
     *
     * @return
     */
    private int getscrollerVelocity_X() {
        velocityTracker.computeCurrentVelocity(1000);//计算当前速度
        return (int) velocityTracker.getXVelocity();
    }

    /**
     * 获取Y方向上的滑动速度
     *
     * @return
     */
    private int getscrollerVelocity_Y() {
        velocityTracker.computeCurrentVelocity(1000);//计算当前速度
        return (int) velocityTracker.getYVelocity();
    }

    /**
     * 移除用户速度跟踪器 回收
     */
    private void clearVelocityTracker() {
        if (velocityTracker != null) {
            velocityTracker.recycle();
            velocityTracker = null;
        }
    }

}