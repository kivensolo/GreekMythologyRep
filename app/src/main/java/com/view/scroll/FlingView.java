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
import com.utils.ZLog;

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
    private int velocityX;  //当前X方向上的速度
    private int velocityY;  //当前Y方向上的速度


    //手势监听类
    GestureDetector mGestureDetector; //可以代替很多onTouchEvent()中自己处理手势
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
        //解决长按屏幕后无法拖动的现象
        mGestureDetector.setIsLongpressEnabled(false);
        initViewConfiguaration(context);
        initPaints();
    }

    private void initPaints() {
        borderPaint = new Paint();
        textPaint = new Paint();
        imagePaint = new Paint();

        borderPaint.setStyle(Paint.Style.STROKE);
        borderPaint.setStrokeWidth(8);
        borderPaint.setColor(Color.CYAN);

        textPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        textPaint.setTextSize(26);
        textPaint.setColor(Color.YELLOW);

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

        //绘制矩形的宽高通过 getMeasuredXXXX()获取
        canvas.drawRect(0, 0, getMeasuredWidth(), getMeasuredHeight(), borderPaint);
        setBackground(getResources().getDrawable(R.drawable.floder_img));

        //直接setText会导致触摸失效
        //setText("我是文本");
        canvas.drawText("我是TextView",35,25,textPaint);
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
        if (velocityTracker == null) {
                velocityTracker = VelocityTracker.obtain();
        }
        velocityTracker.addMovement(event);
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                startX = event.getRawX();
                startY = event.getRawY();
                lastX = startX;
                lastY = startY;
                //scrollBy(200,100);
                int scrollX = getScrollX();
                int scrollY = getScrollY();
                ZLog.i(TAG, "ACTION_DOWN : scrollX = " + scrollX +";scrollY = " + scrollY);
                return true;
            case MotionEvent.ACTION_MOVE:
                float disX = event.getRawX() - lastX;
                float disY = event.getRawY() - lastY;
                offsetLeftAndRight((int) disX);
                offsetTopAndBottom((int) disY);
                //flingTest();
                lastX = event.getRawX();
                lastY = event.getRawY();
                ZLog.i(TAG,"ACTION_MOVE :" + lastX + "-" + lastY);
                return true;
            case MotionEvent.ACTION_UP:
                velocityX = getscrollerVelocity_X();
                velocityY = getscrollerVelocity_Y();
                ZLog.i(TAG,"ACTION_UP : velocityX=" + velocityX + "; --- velocityY" + velocityY);
                if((lastX != startX) && (lastY != startY)){
                    itemFling();
                    clearVelocityTracker();
                }
                break;
        }
        //return super.onTouchEvent(event);
        return mGestureDetector.onTouchEvent(event);//由手势监听类处理
    }

    private void itemFling() {
        //mScroller.startScroll((int)getX(), (int)getY(), -(int)(getX() - lastX),-(int)(getY() - lastY));
        /**
         * 手势滑动，滑动距离由初始速度决定
         * startX：开始滑动的X坐标
         * startY：开始滑动的Y坐标
         * velocityX: X方向上的初始化滑行速度  像素/秒
         * velocityY: Y方向上的初始化滑行速度  像素/秒
         * minX:最小的X值，Scroll不会划过这个点
         * maxX:最大的X值，Scroll不会划过这个点
         * minY:最小的Y值，Scroll不会划过这个点
         * maxY:最大的Y值，Scroll不会划过这个点
         *
         */
        mScroller.fling((int)lastX,(int)lastY,velocityX,velocityY, 0, 1080, 0, 720);
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
            velocityTracker.clear();
            velocityTracker.recycle();
            velocityTracker = null;
        }
    }

}