package com.view.scroll;

import android.content.Context;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.ViewConfiguration;
import android.view.animation.BounceInterpolator;
import android.widget.Scroller;
import android.widget.TextView;

/**
 * Copyright(C) 2016, 北京视达科科技有限公司
 * All rights reserved. <br>
 * author: King.Z <br>
 * date:  2016/8/19 17:37 <br>
 * description: FlingView <br>
 */
public class FlingView extends TextView {
    private Scroller mScroller;

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


    private float lastX;
    private float lastY;
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
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                lastX = event.getRawX();
                lastY = event.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
                float disX = event.getRawX() - lastX;
                float disY = event.getRawY() - lastY;
                offsetLeftAndRight((int) disX);
                offsetTopAndBottom((int) disY);
                lastX = event.getRawX();
                lastY = event.getRawY();
                break;
            case MotionEvent.ACTION_UP:
                mScroller.startScroll((int) getX(), (int) getY(), -(int) (getX() - startX),
                        -(int) (getY() - startY));
                invalidate();
                break;


        }
        return super.onTouchEvent(event);
        //return mGestureDetector.onTouchEvent(event);//由手势监听类处理
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