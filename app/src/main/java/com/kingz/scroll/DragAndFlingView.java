package com.kingz.scroll;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.BounceInterpolator;
import android.widget.Scroller;

import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.AppCompatTextView;

import com.kingz.customdemo.R;
import com.zeke.kangaroo.utils.ScreenDisplayUtils;
import com.zeke.kangaroo.utils.ZLog;

/**
 * author: King.Z <br>
 * date:  2016/8/19 17:37 <br>
 *     update at : 2019/3/24
 * description: Drag & Fling View <br>
 *
 *     TODO 做边界回弹效果
 */
public class DragAndFlingView extends AppCompatTextView {

    public static final String TAG = DragAndFlingView.class.getSimpleName();
    public static final int VIEW_WIDTH = 250;
    public static final int VIEW_HEIGHT = 150;
    public static final int VELOCITYLIMIT = 15000;
    private Scroller mScroller;
    private Paint borderPaint;
    private Paint textPaint;
    private float preX;    //View坐标X
    private float preY;    //View坐标Y
    private float preMotionRawX;    //触摸点的x坐标
    private float preMotionRawY;    //触摸点的y坐标

    private int screenWidth;
    private int screenHeight;
    //SDK提供的手势监听类  封装了VelocityTracker和手势判断
    GestureDetector mGestureDetector;

    public DragAndFlingView(Context context) {
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

    public DragAndFlingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mScroller = new Scroller(context, new BounceInterpolator());
        mGestureDetector = new GestureDetector(context, new FlingViewGestureListener());
        //解决长按屏幕后无法拖动的现象
        //禁用后，用户可以按住然后按下移动他们的手指，会得到滚动事件
        mGestureDetector.setIsLongpressEnabled(false);
        initPaints();
        screenWidth = ScreenDisplayUtils.getScreenWidth(getContext());
        screenHeight = ScreenDisplayUtils.getScreenHeight(getContext());
        ZLog.d(TAG, "FlingView screenWidth=" + screenWidth + ";screenHeight=" + screenHeight);
    }

    private void initPaints() {
        borderPaint = new Paint();
        textPaint = new Paint();

        borderPaint.setStyle(Paint.Style.STROKE);
        borderPaint.setStrokeWidth(8);
        borderPaint.setColor(Color.CYAN);

        textPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        textPaint.setTextSize(26);
        textPaint.setColor(getResources().getColor(R.color.fruitpurple));
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        // 边界控制
        if(left < 0){
            left = 0;
            right = VIEW_WIDTH;
        }

        if(top < 0){
            top = 0;
            bottom = VIEW_HEIGHT;
        }

        if(right > screenWidth){
            right = screenWidth;
            left = right - VIEW_WIDTH;
        }
        if(bottom >= screenHeight){
            bottom = screenHeight - 20;
            top = bottom - VIEW_HEIGHT;
        }

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

        canvas.drawText("快乐的肥皂", 0, height / 2, textPaint);
    }


    public DragAndFlingView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }


    @Override
    public boolean onTouchEvent(MotionEvent event ) {
//        super.onTouchEvent(event);
//        ZLog.i(TAG, "Zeke  onTouchEvent....");
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                float x = event.getX();          //当前view为坐标系的x
                preMotionRawX = event.getRawX(); // 屏幕为坐标系，触点的x
                preMotionRawY = event.getRawY();
                preX = getX();
                preY = getY();
                ZLog.i(TAG, "ACTION_DOWN : preX = " + preX + ";preY = " + preY + "; x = " + x);
                break;
            case MotionEvent.ACTION_MOVE:
                float deltaX = event.getRawX() - preMotionRawX;
                float deltaY = event.getRawY() - preMotionRawY;

                offsetLeftAndRight((int) deltaX); // 调整左右Layout
                offsetTopAndBottom((int) deltaY); // 调整上下Layout
                preMotionRawX = event.getRawX();
                preMotionRawY = event.getRawY();
                preX = getX();
                preY = getY();
                ZLog.i(TAG, "ACTION_MOVE : deltaX=" + deltaX+";deltaY="+deltaY+"   " +
                        "prePostion=" + preX + "-" + preY);
                break;
            case MotionEvent.ACTION_UP:
                preX = getX();
                preY = getY();
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
        //mScroller.startScroll((int)getX(), (int)getY(), -(int)(getX() - preX),-(int)(getY() - preY));
        int scrollStartX = (int) preX;
        int scrollStartY = (int) preY;
        mScroller.fling(scrollStartX, scrollStartY, vX, vY,
                0, screenWidth - VIEW_WIDTH,
                0, screenHeight - VIEW_HEIGHT);
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
            ZLog.i(TAG, "FlingViewGestureListener : onFling   velocityX:"  + velocityX + "; velocityY:" + velocityY);
            velocityX  = velocityX >= VELOCITYLIMIT
                    ? VELOCITYLIMIT :
                    (velocityX <= -VELOCITYLIMIT ? -VELOCITYLIMIT : velocityX);
            velocityY  = velocityY >= VELOCITYLIMIT
                    ? VELOCITYLIMIT :
                    (velocityY <= -VELOCITYLIMIT ? -VELOCITYLIMIT : velocityY);
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