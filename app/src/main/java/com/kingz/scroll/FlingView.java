package com.kingz.scroll;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.animation.BounceInterpolator;
import android.widget.Scroller;
import android.widget.TextView;
import com.kingz.customdemo.R;
import com.kingz.utils.ZLog;

/**
 * author: King.Z <br>
 * date:  2016/8/19 17:37 <br>
 * description: 滑动Item的FlingView <br>
 */
public class FlingView extends TextView {

    public static final String TAG = FlingView.class.getSimpleName();
    public static final int MEASURED_WIDTH = 250;
    public static final int MEASURED_HEIGHT = 150;
    private Scroller mScroller;
    private Paint borderPaint;
    private Paint textPaint;
    private float lastX;    //初始坐标X
    private float lastY;    //初始坐标Y
    private float startX;   //手势起点X坐标
    private float startY;   //手势起点Y坐标

    @Deprecated
    private int velocityX;  //当前X方向上的速度
    @Deprecated
    private int velocityY;  //当前Y方向上的速度

    //手势监听类
    GestureDetector mGestureDetector; //可以代替很多onTouchEvent()中自己处理手势
    //速度追踪器
    //VelocityTracker velocityTracker = VelocityTracker.obtain();

    /**
     * 系统所能识别出的被认为是滑动的最小像素距离
     */
    private int touchSlop;

    /**
     * 获取Fling速度的最小值和最大值
     */
    private int minimumVelocity;
    private int maximumVelocity;

    /**
     * 是否有物理按键
     */
    private boolean isHavePermanentMenuKey;

    /**
     * 双击间隔时间.在该时间内是双击，否则是单击 系统默认值：300ms
     */
    private int doubleTapTimeout;

    /**
     * 按住状态转变为长按状态需要的时间 系统默认值：500ms
     */
    private int longPressTimeout;

    /**
     * 重复按键的时间
     */
    private int keyRepeatTimeout;


    public FlingView(Context context) {
        this(context, null);
        setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                ZLog.i(TAG, "Zeke  onTouch....");
                return false;
            }
        });
    }

    public FlingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mScroller = new Scroller(context, new BounceInterpolator());
        mGestureDetector = new GestureDetector(context, new ScrollGestureListener());
        //解决长按屏幕后无法拖动的现象
        //禁用后，用户可以按住然后按下移动他们的手指，会得到滚动事件
        mGestureDetector.setIsLongpressEnabled(false);
        initViewConfiguaration(context);
        initPaints();
    }

    private void initPaints() {
        borderPaint = new Paint();
        textPaint = new Paint();

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
        setMeasuredDimension(MEASURED_WIDTH, MEASURED_HEIGHT);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int width = getMeasuredWidth();
        int height = getMeasuredHeight();
        //绘制矩形的宽高通过 getMeasuredXXXX()获取
        canvas.drawRect(0, 0,width , height, borderPaint);
        setBackground(getResources().getDrawable(R.drawable.soap));

        //FIXME 直接setText会导致触摸失效
        //setText("我是文本");
        canvas.drawText("快乐的肥皂", width / 2, height / 2, textPaint);
    }

    private void initViewConfiguaration(Context context) {
        final ViewConfiguration vc = ViewConfiguration.get(context);
        touchSlop = vc.getScaledTouchSlop();
        minimumVelocity = vc.getScaledMinimumFlingVelocity();
        maximumVelocity = vc.getScaledMaximumFlingVelocity();
        isHavePermanentMenuKey = vc.hasPermanentMenuKey();

        doubleTapTimeout = ViewConfiguration.getDoubleTapTimeout();
        longPressTimeout = ViewConfiguration.getLongPressTimeout();
        keyRepeatTimeout = ViewConfiguration.getKeyRepeatTimeout();
    }

    public FlingView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event ) {
        ZLog.i(TAG, "Zeke  onTouchEvent....");
        //if (velocityTracker == null) {
        //    velocityTracker = VelocityTracker.obtain();
        //}
        //velocityTracker.addMovement(event);

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                startX = event.getRawX();
                float x = event.getX();
                startY = event.getRawY();
                lastX = startX;
                lastY = startY;
                //scrollBy(200,100);
                ZLog.i(TAG, "ACTION_DOWN : startX = " + startX + ";startY = " + startY + "; x = " + x);
                return true;
            case MotionEvent.ACTION_MOVE:
                float disX = event.getRawX() - lastX;
                float disY = event.getRawY() - lastY;
                // ?????
                offsetLeftAndRight((int) disX);
                offsetTopAndBottom((int) disY);
                lastX = event.getRawX();
                lastY = event.getRawY();
                ZLog.i(TAG, "ACTION_MOVE :" + lastX + "-" + lastY);
                return true;
            case MotionEvent.ACTION_UP:
                //velocityX = getscrollerVelocity_X();
                //velocityY = getscrollerVelocity_Y();
                ZLog.i(TAG, "ACTION_UP : velocityX=" + velocityX + "; --- velocityY" + velocityY);
                if ((lastX != startX) && (lastY != startY)) {
                    //itemFling();
                    //clearVelocityTracker();
                }
                break;
        }
        //继续透传给 GestureDetector
        return mGestureDetector.onTouchEvent(event);
    }

    private void itemFling(int vX, int vY) {
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
        mScroller.fling((int) lastX, (int) lastY, vX, vY, 0, 500, 0, 500);
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
     */
    //@Deprecated
    //private int getscrollerVelocity_X() {
    //    velocityTracker.computeCurrentVelocity(1000);//计算当前速度
    //    return (int) velocityTracker.getXVelocity();
    //}

    /**
     * 获取Y方向上的滑动速度
     */
    //@Deprecated
    //private int getscrollerVelocity_Y() {
    //    //velocityTracker.computeCurrentVelocity(1000);//计算当前速度
    //    //return (int) velocityTracker.getYVelocity();
    //}

    /**
     * 移除用户速度跟踪器 回收
     */
    //private void clearVelocityTracker() {
    //    if (velocityTracker != null) {
    //        velocityTracker.clear();
    //        velocityTracker.recycle();
    //        velocityTracker = null;
    //    }
    //}


    class ScrollGestureListener implements GestureDetector.OnGestureListener {

        @Override
        public boolean onDown(MotionEvent e) {
            //MotionEvent.ACTION_DOWN
            return false;
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            ZLog.i(TAG, "ScrollGestureListener : onFling   velocityX:" + velocityX + "; velocityY:" + velocityY);
            //TODO 是否可以用这个fling替换掉FlingView中的Fling
            itemFling((int)velocityX,(int)velocityY);
            return true;
        }

        //长按时回调
        @Override
        public void onLongPress(MotionEvent e) {
        }

        //MotionEvent.ACTION_MOVE   期间满足条件回调
        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            return false;
        }

        /**
         * 会在MotionEvent.ACTION_DOWN 之后 TAP_TIMEOUT (100ms) 后回调此方法 (通过handler发延时消息)
         * 若短时间内MotionEvent.ACTION_UP 则会remove掉此操作。
         */
        @Override
        public void onShowPress(MotionEvent e) {
        }

        //轻击屏幕时调用该方法  MotionEvent.ACTION_UP之时
        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            return false;
        }
    }
}