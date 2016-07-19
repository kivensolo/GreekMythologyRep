package com.view.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import com.kingz.customDemo.R;

/**
 * Created by KingZ on 2015/11/23.
 * Discription:自定义更随手指的小球
 */
public class DrawBallView extends View {

    public DrawBallView(Context context) {
        super(context);
    }
    public DrawBallView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    /*******************View的常用回调方法 ------ Start ****************/
    @Override
    protected void onFinishInflate() {
        //当应用从XML布局文佳佳在组建并利用他来构建页面之后，该方法将会被回调
        super.onFinishInflate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //检测View组件及它所包含的所有子组件的大小
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        //当该组件需要分配其自组件的位置、大小时，该方法就会被回调
        super.onLayout(changed, left, top, right, bottom);
    }
    //onDraw()

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        //当该组件的大小被改变时回调该方法
        super.onSizeChanged(w, h, oldw, oldh);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        return super.onKeyUp(keyCode, event);
    }

    @Override
    public boolean onTrackballEvent(MotionEvent event) {
        //发生轨迹球事件的时候出发该方法
        return super.onTrackballEvent(event);
    }

    @Override
    public void onWindowFocusChanged(boolean hasWindowFocus) {
        //当组件得到、食物焦点的时候触发
        super.onWindowFocusChanged(hasWindowFocus);
    }

    @Override
    protected void onAttachedToWindow() {
        //把该组件放入某个窗口的时候触发
        super.onAttachedToWindow();
    }

    @Override
    protected void onDetachedFromWindow() {
        //把该组件脱离某个窗口的时候触发
        super.onDetachedFromWindow();
    }

    @Override
    protected void onWindowVisibilityChanged(int visibility) {
        //当包含该组件的窗口的可见性发生改变时触发该方法
        super.onWindowVisibilityChanged(visibility);
    }
     /*******************View的常用回调方法 ------ End****************/




    private float currentX = 40;
    private float currentY = 50;
    private Paint mPaint = new Paint();

    @Override
    protected void onDraw(Canvas canvas) {
        // ******* 最重要的重写方法之一: 组件需要绘制内容的时候回调
        super.onDraw(canvas);
        //设置画笔
        mPaint.setColor(getResources().getColor(R.color.magenta));
        mPaint.setShadowLayer(30,30,30,Color.rgb(250,180,180));    //设置阴影
        mPaint.setAntiAlias(true);  //设置画笔抗锯齿
        //画一个圆
        canvas.drawCircle(currentX,currentY,20,mPaint);
    }

    /**
     * 触摸事件：
     *          要根据手指的位置来获取x.y从而画图
     * @param event
     * @return
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {

        currentX = event.getX();
        currentY = event.getY();
        invalidate();//画面重绘
        return true;//拦截触摸事件
    }
}
