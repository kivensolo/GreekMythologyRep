package com.view.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.kingz.customdemo.R;

/**
 * Copyright(C) 2015, 北京视达科科技有限公司
 * All rights reserved.
 * author: King.Z
 * date:  2016/7/12 10:50
 * description:
 * 实现染色效果的思路：用clipRect将一行文字剪切成两部分，
 * 前半部分用一个颜色，后边部分用另一个颜色，根据进度的变化，改变这个剪切点的位置。
 * <p>
 * 主要是对Text绘制的边界做处理
 */
public class ColorTrackView extends View {

    private static final String TAG = "ColorTrackView";

    private int mTextStartX;        //文字起始x位置
    private int mOldStyleTextLeft;  //原始文字左边缘
    private int mOldStyleTextRight; //原始文字右边缘
    private int mNewStyleTextLeft;  //新文字左边缘
    private int mNewStyleTextRight; //新文字右边缘

    private int mDirection = LEFT2RIGHT;
    public static final int LEFT2RIGHT = 0;
    public static final int RIGHT2LEFT = 1;
    public static final int TOP2BUTTPM = 2;
    public static final int BUTTOM2TOP = 3;

    //default value
    private String mText = "Default TextDesc";
    private int mTextSize = 30;
    private int mTextWidth;
    private int mRealWidth;
    private float mProgress;

    private Paint mPaint;

    /**
     * 文字原始颜色
     */
    private int mTextOriginColor = 0xff00ff99;
    /**
     * 改变后的颜色
     */
    private int mTextChangeColor = 0xffffff00;

    private Rect mTextBound = new Rect();

    public void setDirection(int direction) {
        mDirection = direction;
    }

    public ColorTrackView(Context context) {
        this(context, null);
    }

    public ColorTrackView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ColorTrackView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs);

        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        TypedArray ta = context.getTheme().obtainStyledAttributes(attrs, R.styleable.ColorTrackView, defStyle, 0);
        mText = ta.getString(R.styleable.ColorTrackView_text);
        mTextSize = ta.getDimensionPixelSize(R.styleable.ColorTrackView_text_size, mTextSize);
        mTextOriginColor = ta.getColor(R.styleable.ColorTrackView_text_origin_color, mTextOriginColor);
        mTextChangeColor = ta.getColor(R.styleable.ColorTrackView_text_change_color, mTextChangeColor);
        mProgress = ta.getFloat(R.styleable.ColorTrackView_progress, 0);
        mDirection = ta.getInt(R.styleable.ColorTrackView_direction, mDirection);
        ta.recycle();

        mPaint.setTextSize(mTextSize);
        measureText();
    }

    /**
     * 测量Text  获取文本宽度
     */
    private void measureText() {
        mTextWidth = (int) mPaint.measureText(mText);
        mPaint.getTextBounds(mText, 0, mText.length(), mTextBound);
    }

    //如果集成TextView  测量就不需要写了
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = measureWidth(widthMeasureSpec);
        int height = measureHeight(heightMeasureSpec);
        Log.i(TAG, "onMeasure()  width:" + width + "  ------ height:" + height);
        setMeasuredDimension(width, height);

        mRealWidth = getMeasuredWidth() - getPaddingLeft() - getPaddingRight();
        mTextStartX = mRealWidth / 2 - mTextWidth / 2;
        Log.i(TAG, "onMeasure()  mTextStartX:" + mTextStartX);
    }

    private int measureHeight(int measureSpec) {
        int mode = MeasureSpec.getMode(measureSpec);
        int val = MeasureSpec.getSize(measureSpec);
        int result = 0;
        switch (mode) {
            case MeasureSpec.EXACTLY:
                result = val;
                break;
            case MeasureSpec.AT_MOST:
            case MeasureSpec.UNSPECIFIED:
                result = mTextBound.height();
                break;
        }
        result = mode == MeasureSpec.AT_MOST ? Math.min(result, val) : result;
        return result + getPaddingTop() + getPaddingBottom();
    }

    private int measureWidth(int measureSpec) {
        int mode = MeasureSpec.getMode(measureSpec);
        int val = MeasureSpec.getSize(measureSpec);
        int result = 0;
        switch (mode) {
            case MeasureSpec.EXACTLY:
                result = val;
                break;
            case MeasureSpec.AT_MOST:
            case MeasureSpec.UNSPECIFIED:
                // result = mTextBound.width();
                result = mTextWidth;
                break;
        }
        result = mode == MeasureSpec.AT_MOST ? Math.min(result, val) : result;
        return result + getPaddingLeft() + getPaddingRight();
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int r = (int) (mProgress * mTextWidth + mTextStartX); //文本右侧位置
        Log.i(TAG, "onDraw() r = " + r);
        if (mDirection == LEFT2RIGHT) {
            mNewStyleTextLeft = mTextStartX;
            mNewStyleTextRight = (int) (mTextStartX + mProgress * mTextWidth);
            mOldStyleTextLeft = mNewStyleTextRight;
            mOldStyleTextRight = mTextStartX + mTextWidth;
            drawText(canvas, mTextChangeColor, mNewStyleTextLeft, mNewStyleTextRight);
            drawText(canvas, mTextOriginColor, mOldStyleTextLeft, mOldStyleTextRight);
        } else {
            mNewStyleTextRight = mTextStartX + mTextWidth;
            mNewStyleTextLeft = (int) (mTextStartX + (1 - mProgress) * mTextWidth);
            mOldStyleTextLeft = mTextStartX;
            mOldStyleTextRight = mNewStyleTextLeft;
            drawText(canvas, mTextChangeColor, mNewStyleTextLeft, mNewStyleTextRight);
            drawText(canvas, mTextOriginColor, mOldStyleTextLeft, mOldStyleTextRight);
        }
    }

    /**
     * 利用mProgress和方向去计算应该clip的范围
     *
     * @param canvas 当前画布
     * @param color  颜色
     * @param startX 起始位置
     * @param endX   结束为止
     */
    private void drawText(Canvas canvas, int color, int startX, int endX) {
        mPaint.setColor(color);
        canvas.save(Canvas.CLIP_SAVE_FLAG);
        canvas.clipRect(startX, 0, endX, getMeasuredHeight());
        canvas.drawText(mText, mTextStartX, getMeasuredHeight() / 2 + mTextBound.height() / 2, mPaint);
        canvas.restore();
    }

    public void setProgress(float progress) {
        mProgress = progress;
        invalidate();
    }
}
