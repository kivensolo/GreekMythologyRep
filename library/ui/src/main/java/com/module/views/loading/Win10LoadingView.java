package com.module.views.loading;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.ColorInt;

import com.module.UIUtil;

/**
 * author: King.Z <br>
 * date:  2016/10/11 18:57 <br>
 * description: 仿win10加载圈 <br>
 */
public class Win10LoadingView extends View {

    private static final String TAG = Win10LoadingView.class.getSimpleName();
    public static final int DEFAULT_STROKE_WIDTH = 15;
    //截取Path中的一部分并显示
    private PathMeasure mPathMeasure;
    private ValueAnimator valueAnimator;
    private Paint mHelpLineCirclePaint;
    private Paint mHelpLineRectPaint;
    private Paint mPaint;
    private Path mCirclePath;
    // 用来储存截取后的路径内容
    private Path dstPath;
    // 每一圈的绘制时间
    private static final int DURATION = 3 * 1000;
    // 弧形的BoundsRect
    private RectF rect;
    // 加载圈宽高
    private int mWidth, mHeight;
    /**
     * 圆形路径的总长度
     */
    private float mCirclePathLength;
    /**
     * 每一圈的进度百分比
     */
    private float percent; //接受ValueAnimator的返回值，代表整个动画的进度
    /**
     * 圆点个数
     */
    private int dotNum;
    /**
     * 是否是Debug模式
     */
    private boolean DEBUG = false;

    private float x;
    private float y;
    // 圆形半径dp值
    private float radius = UIUtil.INSTANCE.dip2px(getContext(), 25);
    private LoadingType mType = LoadingType.NORMAL;

    public enum LoadingType{
        NORMAL, DOTS,
    }

    public Win10LoadingView(Context context) {
        this(context, null);
    }

    public Win10LoadingView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);

    }

    public Win10LoadingView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mPaint = new Paint();
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(DEFAULT_STROKE_WIDTH);
        mPaint.setColor(Color.WHITE);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setAntiAlias(true);

        mHelpLineCirclePaint = new Paint();
        mHelpLineCirclePaint.setStyle(Paint.Style.STROKE);
        mHelpLineCirclePaint.setStrokeWidth(1);
        mHelpLineCirclePaint.setColor(Color.YELLOW);
        mHelpLineCirclePaint.setStrokeCap(Paint.Cap.ROUND);
        mHelpLineCirclePaint.setAntiAlias(true);

        mHelpLineRectPaint = new Paint(mHelpLineCirclePaint);
        mHelpLineRectPaint.setColor(Color.RED);

        dstPath = new Path();
        mCirclePath = new Path();
        rect = new RectF(-radius, -radius, radius, radius);
         //矩形内画弧线  如果是360f  那起点就会从0度开始
        mCirclePath.addArc(rect, -90, 359.9f);
        mPathMeasure = new PathMeasure(mCirclePath, false); //创建一个与path相关联的PathMeasure
        mCirclePathLength = mPathMeasure.getLength();

        valueAnimator = ValueAnimator.ofFloat(0f, 1f).setDuration(DURATION);
//        valueAnimator.setInterpolator(new DecelerateInterpolator());
        valueAnimator.setRepeatCount(-1);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                percent = (float) animation.getAnimatedValue();
                invalidate();
            }
        });
        setLayerType(View.LAYER_TYPE_SOFTWARE, null);
    }

    public Win10LoadingView setStrokeWidth(int width){
        mPaint.setStrokeWidth(width);
        return this;
    }
    public Win10LoadingView setColor(@ColorInt int color){
        mPaint.setColor(color);
        return this;
    }

    public Win10LoadingView setDuration(long duration){
        valueAnimator.setDuration(duration);
        return this;
    }

    /**
     * 设备半径大小
     * @param r 半径值 DP
     */
    public Win10LoadingView setRadius(int r){
        radius = r;
        return this;
    }

    public Win10LoadingView setType(LoadingType type){
        mType = type;
        return this;
    }

    public Win10LoadingView enableDebug(boolean enable){
        DEBUG = enable;
        return this;
    }

    public void start(){
        valueAnimator.start();
    }

    public void stop(){
        valueAnimator.cancel();
        valueAnimator.removeAllListeners();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        dotNum = (int) (percent / 0.05);

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
        //ZLog.d(TAG, "mPathMeasure.getLength() * precent=" + mPathMeasure.getLength() * precent);

        if (DEBUG) {
            drawHelpLine(canvas);
        }

        dstPath.reset();
        dstPath.rLineTo(0, 0);        // 硬件加速的BUG
        if (mType == LoadingType.DOTS) {
            drawDotsOnPath();
            getDotOnPath();     //画一个点
        } else if (mType == LoadingType.NORMAL) {
            //------ 画实际的轨迹
            getRellaryPath();
        }
        canvas.drawPath(dstPath, mPaint);                //绘制dst

        // 每次转动一圈聚成一个点后都会闪一下，这是因为重新开始动画刷新视图的原因，
        // 这里的补救方法就是在动画快结束的时候手动画一个点
        if (0.997 <= percent && percent <=1) {
            canvas.drawPoint(0, -radius, mPaint);
        }
    }

    /**
     * 画多个渐变的点, 进度每变化5%就画一个点
     */
    private void drawDotsOnPath() {
        dotNum = (int) (percent % 0.05);
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

    private void drawHelpLine(Canvas canvas) {
        canvas.drawPath(mCirclePath, mHelpLineCirclePaint);
        canvas.drawRect(rect, mHelpLineRectPaint);
    }

    /**
     * start = 0：画path的实际路径
     * start != 0 : path长度变化
     */
    private void getRellaryPath() {
        //float start = (float) (mLength * Math.pow(precent,2)); //到二分之一处间距最大
        float stop = mCirclePathLength * percent;
        float start = (float) (stop - ((0.5 - Math.abs(percent - 0.5)) * mCirclePathLength));
        mPathMeasure.getSegment(start, stop, dstPath, true);
    }

    /**
     * 获取一个点
     */
    private void getDotOnPath() {
        float start = mCirclePathLength * percent;
        float last = start + 1;
        //截取一部分存入dst中
        mPathMeasure.getSegment(start, last, dstPath, true);
    }

    /**
     * 计算截取间隔位置
     * @param dfloat 百分比
     */
    private void getSegmentWithPath(float dfloat) {
        x = percent - dfloat * (1 - percent);   //间距由0.05线性平滑到0
        //y = mLength * x;                      //点与点间距不变
        y = -mCirclePathLength * (x * x - 2 * x);
        mPathMeasure.getSegment(y, y + 1, dstPath, true);
    }
}
