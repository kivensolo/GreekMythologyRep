package com.view.scroll;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.WindowManager;
import android.widget.Scroller;

import com.App;

/**
 * Copyright(C) 2016, 北京视达科科技有限公司
 * All rights reserved. <br>
 * author: King.Z <br>
 * date:  2016/6/26 23:53 <br>
 * description: 一个测试滑动类 <br>
 */
public class CustomScrollerItem extends View {

    private static String TAG = CustomScrollerItem.class.getSimpleName();

    /**
     * 速度追踪者
     */
    private VelocityTracker velocityTracker;
    private Scroller scroller;
    /**
     * 屏幕宽度
     */
    private int screenWidth;
    /**
     * 屏幕高度
     */
    private int screenHeight;
    /**
     * 滑动的最小距离
     */
    private int mMinDis;
    /**
     * 是否滑动
     */
    private boolean isSlider = false;
    private  int itemWidth = App.ScreenAdjuest(50);
    private  int itemHeight = App.ScreenAdjuest(100);
    private int startPoint_Y;       //手指按下Y的坐标
    private int startPoint_X;       //手指按下点的X坐标
    private int[] speedArr;

    private Rect mRect;
    private Paint paint;

    public CustomScrollerItem(Context context) {
        this(context, null);
    }

    public CustomScrollerItem(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomScrollerItem(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        screenWidth = size.x;
        screenHeight = size.y;
        Log.i(TAG, "屏幕宽度为：" + screenWidth + ";高度为= " + screenHeight);
        scroller = new Scroller(context);
        // getScaledTouchSlop返回一个距离值，表示滑动的时候，手的移动要大于这个距离才开始移动控件。
        // 如果小于这个距离就不触发移动控件，如viewpager就是用这个距离来判断用户是否翻页
        mMinDis = ViewConfiguration.get(getContext()).getScaledTouchSlop();
        mRect = new Rect(0,0,itemWidth,itemHeight);
        paint = new Paint();
        paint.setColor(Color.GREEN);
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int specWidthSize = MeasureSpec.getSize(widthMeasureSpec);
        int specHeightSize = MeasureSpec.getSize(heightMeasureSpec);
        int specWidthMode = MeasureSpec.getMode(widthMeasureSpec);
        int specHeightMode = MeasureSpec.getMode(widthMeasureSpec);
        if(specWidthMode == MeasureSpec.EXACTLY){
            //明确指定大小了的
            itemWidth = specWidthSize;
        }else{
            //并未明确指定大小

        }
        if(specHeightMode == MeasureSpec.EXACTLY){
            itemHeight = specHeightSize;
        }else{
        }

        setMeasuredDimension(itemWidth,itemHeight);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawRect(canvas);
    }

    private void drawRect(Canvas canvas){
        canvas.drawRect(mRect,paint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (isSlider) {
            //追踪运动轨迹
            addVelocityTracker(event);

            final int action = event.getAction();
            int current_X = (int) event.getX();
            int current_Y = (int) event.getY();
            switch (action) {
                case MotionEvent.ACTION_MOVE:
                    //左正右负
                    int decX = startPoint_X - current_X;
                    startPoint_X = current_X;
                    int decY = startPoint_Y - current_Y;
                    startPoint_Y = current_Y;
                    // 手指拖动itemView滚动, deltaX大于0向左滚动，小于0向右滚
                    //btnView.scrollBy(decX, decY);
                    break;
            }
            return true;
        }
        return super.onTouchEvent(event);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                addVelocityTracker(event);
                if (!scroller.isFinished()) {
                    return super.dispatchTouchEvent(event);
                }
                break;
            case MotionEvent.ACTION_MOVE:
                //滑动状态判定
                isSlider = true;
                break;
            case MotionEvent.ACTION_UP:
                clearVelocityTracker();
                break;
        }
        return super.dispatchTouchEvent(event);

    }

    @Override
    public void computeScroll() {
        //是否还处于滚动
        if (scroller.computeScrollOffset()) {
            //btnView.scrollTo(scroller.getCurrX(), scroller.getCurrY());

        }
        super.computeScroll();
    }

    /**
     * 开始速度追踪：速度追踪器添加运动事件
     *
     * @param event 轨迹事件
     */
    private void addVelocityTracker(MotionEvent event) {
        if (velocityTracker == null) {
            velocityTracker = VelocityTracker.obtain();
        }

        //为速度追踪者添加一个用户的移动轨迹
        //本应该在actionDown的时候就调用，但是可以随意的在任何我想调用的时候调用
        velocityTracker.addMovement(event);
    }

    /**
     * 获取追踪速度
     * @return
     */
    private int[] getScrollVelocity() {
        // 设置VelocityTracker单位.1000表示1秒时间内运动的像素
        velocityTracker.computeCurrentVelocity(1000);
        // 获取在1秒内X/Y方向所滑动像素值
        speedArr[0] = Math.abs((int) velocityTracker.getXVelocity());
        speedArr[1] = Math.abs((int) velocityTracker.getYVelocity());
        return speedArr;
    }

    /**
     * 停止速度追踪：移除用户速度跟踪器 回收
     */
    private void clearVelocityTracker() {
        if (velocityTracker != null) {
            velocityTracker.recycle();
            velocityTracker = null;
        }
    }
}
