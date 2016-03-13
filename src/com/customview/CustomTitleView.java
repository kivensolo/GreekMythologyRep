package com.customview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.*;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import com.kingz.uiusingListViews.R;

/**
 * Created by KingZ.
 * Data: 2016 2016/3/13
 * Discription:
 */
public class CustomTitleView extends View{

    public static final String TAG = CustomTitleView.class.getSimpleName();
    /**
     * 文本
     */
    private String mTitleText;
    /**
     * 文本的颜色
     */
    private int mTitleTextColor;
    /**
     * 文本的大小
     */
    private int mTitleTextSize;

    /**
     * 包裹文字的最小矩形
     */
    private Rect mBound;

    private RectF mRectF;

    private Paint mPaint;


    /**
     * 3个构造方法，默认的布局文件调用的是两个参数的构造方法，
     * 让所有的构造调用三个参数的构造，
     * 在三个参数的构造中获得自定义属性
     * @param context
     */
    public CustomTitleView(Context context) {
        super(context,null);
    }

    public CustomTitleView(Context context, AttributeSet attrs) {
        super(context, attrs,0);
    }
    /**
     * 获得自定义的样式属性
     * @param context
     * @param attrs
     * @param defStyle
     */
    public CustomTitleView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        /**
         * 获得自定义的样式属性
         */
        TypedArray typedArray = context.getTheme().obtainStyledAttributes(attrs, R.styleable.KingZView,defStyle,0);
        int typeCount = typedArray.getIndexCount();
        for (int i = 0; i < typeCount; i++) {
            int attr = typedArray.getIndex(i);
            switch (attr){
                case R.styleable.KingZView_titleText:
                    //Text
                     mTitleText = typedArray.getString(attr);
                    break;
                case R.styleable.KingZView_titleSize:
                    //Sise 默认设置为16sp，TypeValue也可以把sp转化为px
                    mTitleTextSize = typedArray.getDimensionPixelSize(attr,
                            (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 16, getResources().getDisplayMetrics()));
                    break;
                case R.styleable.KingZView_titleColor:
                    //color default is black
                    // 默认颜色设置为黑色
                    mTitleTextColor = typedArray.getColor(attr, Color.BLACK);
                    break;
            }
        }
        //注意回收
        typedArray.recycle();

        /**
        * 获得绘制文本的宽和高
        */
        mPaint = new Paint();
        mPaint.setTextSize(mTitleTextSize);
        mPaint.setColor(mTitleTextColor);
        mBound = new Rect();
        mPaint.getTextBounds(mTitleText, 0, mTitleText.length(), mBound);

        mRectF = new RectF(0,0,getMeasuredWidth(),getMeasuredHeight());
    }

    /*******************View的常用回调方法 ------ Start ****************/
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
         //检测View组件及它所包含的所有子组件的大小
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    /**
     * 注意： onDraw的时候不能new对象哟
     * @param canvas
     */
    @Override
    protected void onDraw(Canvas canvas) {
        //组件需要绘制内容的时候回调
        super.onDraw(canvas);
        mPaint.setColor(Color.GREEN);
        mPaint.setColor(mTitleTextColor);
        //回绘制矩形的宽高通过 getMeasuredXXXX()获取
        canvas.drawRoundRect(mRectF,25,25,mPaint);
        canvas.drawText(mTitleText,getWidth()/2,getHeight()/2,mPaint);

    }

    @Override
    protected void onFinishInflate() {
        //当应用从XML布局文件在组建并利用他来构建页面之后，该方法将会被回调
        super.onFinishInflate();
        Log.d(TAG,"onFinishInflate()");
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        //当该组件需要分配其自组件的位置、大小时，该方法就会被回调
        super.onLayout(changed, left, top, right, bottom);
        Log.d(TAG,"onLayout()  isChanged:"+changed +";left="+left+";top"+top+";right:"+right+";bottom:"+bottom);
    }
    //onDraw()

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        //当该组件的大小被改变时回调该方法
        super.onSizeChanged(w, h, oldw, oldh);
        Log.d(TAG,"onSizeChanged()" + w + "/"+h+"/"+oldw+"/"+oldh);
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
        //当组件得到、失去焦点的时候触发
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

}
