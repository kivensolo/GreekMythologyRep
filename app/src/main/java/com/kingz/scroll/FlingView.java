package com.kingz.scroll;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.BounceInterpolator;
import android.widget.Scroller;
import android.widget.TextView;

import com.kingz.customdemo.R;
import com.kingz.utils.ZLog;

/**
 * author: King.Z <br>
 * date:  2016/8/19 17:37 <br>
 * description: 滑动Item的FlingView <br>
 * //TODO 对触摸点进行精准度校验
 */
public class FlingView extends TextView {

    public static final String TAG = FlingView.class.getSimpleName();
    public static final int VIEW_WIDTH = 250;
    public static final int VIEW_HEIGHT = 150;
    private Scroller mScroller;
    private Paint borderPaint;
    private Paint textPaint;
    private float prePostionX;    //初始坐标X
    private float prePostionY;    //初始坐标Y

    //SDK提供的手势监听类  封装了VelocityTracker和手势判断
    GestureDetector mGestureDetector;

    public FlingView(Context context) {
        this(context, null);
        setClickable(true);
        setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
//                ZLog.i(TAG, "Zeke  onTouch....");
                return false;
            }
        });
    }

    public FlingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mScroller = new Scroller(context, new BounceInterpolator());
        mGestureDetector = new GestureDetector(context, new FlingViewGestureListener());
        //解决长按屏幕后无法拖动的现象
        //禁用后，用户可以按住然后按下移动他们的手指，会得到滚动事件
        mGestureDetector.setIsLongpressEnabled(false);
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
        setMeasuredDimension(VIEW_WIDTH, VIEW_HEIGHT);
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


    public FlingView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }


    @Override
    public boolean onTouchEvent(MotionEvent event ) {
//        super.onTouchEvent(event);
//        ZLog.i(TAG, "Zeke  onTouchEvent....");
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                float x = event.getX();
                prePostionX = event.getRawX();
                prePostionY = event.getRawY();
                ZLog.i(TAG, "ACTION_DOWN : prePostionX = " + prePostionX + ";prePostionY = " + prePostionY + "; x = " + x);
                break;
            case MotionEvent.ACTION_MOVE:
                float disX = event.getRawX() - prePostionX;
                float disY = event.getRawY() - prePostionY;
                // ?????
                offsetLeftAndRight((int) disX);
                offsetTopAndBottom((int) disY);
                prePostionX = event.getRawX();
                prePostionY = event.getRawY();
                ZLog.i(TAG, "ACTION_MOVE :" + prePostionX + "-" + prePostionY);
                break;
        }
        mGestureDetector.onTouchEvent(event);
        return true;
    }

    @Override
    public void setOnGenericMotionListener(OnGenericMotionListener l) {
        super.setOnGenericMotionListener(l);
        if (Build.VERSION.SDK_INT >= 24) {
            setOnGenricMotionLsrOfGesture(l);
        }
    }

    @RequiresApi(24)
    private void setOnGenricMotionLsrOfGesture(OnGenericMotionListener l) {
//        mGestureDetector.onGenericMotionEvent(l);
    }

    private void itemFling(int vX, int vY) {
        ZLog.d(TAG,"itemFling   vX=" + vX + ";vY="+vY);
        //mScroller.startScroll((int)getX(), (int)getY(), -(int)(getX() - prePostionX),-(int)(getY() - prePostionY));
        /*
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
        mScroller.fling((int) prePostionX, (int) prePostionY, vX, vY, 0, 500, 0, 500);
    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        if (mScroller.computeScrollOffset()) {
            //Scroller滚动已完成
            setX(mScroller.getCurrX());
            setY(mScroller.getCurrY());
            invalidate();
        }
    }

    class FlingViewGestureListener implements GestureDetector.OnGestureListener {

        @Override
        public boolean onDown(MotionEvent e) {
            //MotionEvent.ACTION_DOWN
            return false;
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            ZLog.i(TAG, "FlingViewGestureListener : onFling   velocityX:"
                                    + velocityX + "; velocityY:" + velocityY);
            itemFling((int)velocityX,(int)velocityY);
            return true;
        }

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