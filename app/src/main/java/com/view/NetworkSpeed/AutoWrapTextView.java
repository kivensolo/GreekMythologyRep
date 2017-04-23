package com.view.NetworkSpeed;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import com.kingz.customdemo.R;
import com.utils.ZLog;

import java.util.ArrayList;

/**
 * Copyright(C) 2016, 北京视达科科技有限公司
 * All rights reserved. <br>
 * author: King.Z <br>
 * date:  2017/4/23 17:27 <br>
 * description: 自动换行的TextView
 * 解决中英文混合排版出现还行错误的情况<br>
 */
public class AutoWrapTextView extends View {

    private int mPaddingLeft;
    private int mPaddingRight;
    private int mPaddingTop;
    private int mPaddingBottom;
    private int mTextColor;
    private int mTextSize;
    private int mLineSpacingExtra;
    //单行文本的宽度
    private int mSingleTextWidth;
    /**
     * 文本总高度
     */
    private int mTextTotalHeight;

    private char[] mTextCharArray;
    private Paint mTextPaint;
    /**
     * 拆分后的文本集合
     */
    private ArrayList mSplitTextList;
    /**
     * 分割后的文本rect数组
     */
    private Rect[] mSplitTextRectArray;

    public AutoWrapTextView(Context context) {
        this(context, null);
    }

    public AutoWrapTextView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AutoWrapTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        intt(context, attrs, defStyleAttr);
        initPaint();
    }

    private void intt(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.AutoWrapTextView);
        mPaddingLeft = typedArray.getDimensionPixelSize(R.styleable.AutoWrapTextView_paddingLeft, 0);
        mPaddingRight = typedArray.getDimensionPixelSize(R.styleable.AutoWrapTextView_paddingRight, 0);
        mPaddingTop = typedArray.getDimensionPixelSize(R.styleable.AutoWrapTextView_paddingTop, 0);
        mPaddingBottom = typedArray.getDimensionPixelSize(R.styleable.AutoWrapTextView_paddingBottom, 0);
        mTextSize = typedArray.getDimensionPixelSize(R.styleable.AutoWrapTextView_textSize, 26);
        mLineSpacingExtra = typedArray.getDimensionPixelSize(R.styleable.AutoWrapTextView_lineSpacingExtra, 20);
        mTextColor = typedArray.getColor(R.styleable.AutoWrapTextView_textColor, Color.BLACK);
        typedArray.recycle();
    }

    private void initPaint() {
        mTextPaint = new Paint();
        mTextPaint.setAntiAlias(true);
        mTextPaint.setTextSize(mTextSize);
        mTextPaint.setColor(mTextColor);
        //mTextPaint.setTextAlign(Paint.Align.LEFT);
    }


    public void setText(CharSequence text) {
        setText(text.toString());
    }

    public void setText(String text) {
        if (TextUtils.isEmpty(text)) {
            ZLog.e("AutoWrapTextView", "input text is empty");
            return;
        }
        mTextCharArray = text.toCharArray();
        requestLayout();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        splitText(MeasureSpec.getMode(heightMeasureSpec));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawText(canvas);
    }

    private void drawText(Canvas canvas) {
        if (mSplitTextList == null || mSplitTextList.size() == 0) {
            return;
        }
        int marginTop = getTopTextMarginTop();
        for (int m = 0, length = mSplitTextList.size(); m < length; m++) {
            canvas.drawText( (String) mSplitTextList.get(m), mPaddingLeft, marginTop, mTextPaint);
            marginTop += (mSplitTextRectArray[m].height() + mLineSpacingExtra);
        }
    }

    private void splitText(int hightMode) {
        if (mTextCharArray == null) {
            return;
        }
        mSplitTextList = new ArrayList();
        dealContentOfTotalWidth();
        dealContentOfTotalHeight(hightMode);
        setMeasuredDimension(getMeasuredWidth(), mTextTotalHeight);
    }

    private void dealContentOfTotalHeight(int hightMode) {
        mSplitTextRectArray = new Rect[mSplitTextList.size()];
        for (int m = 0, lenght = mSplitTextList.size(); m < lenght; m++) {
            String linText = (String)mSplitTextList.get(m);//每一行的文字
            Rect lintTextREct = new Rect();
            mTextPaint.getTextBounds(linText, 0, linText.length(), lintTextREct);//获取到每一行文字的最边缘矩形
            if (hightMode == MeasureSpec.AT_MOST) {//match_parent
                mTextTotalHeight = mTextTotalHeight + mPaddingBottom + mPaddingTop;
            } else {
                if (mTextTotalHeight == 0) {
                    mTextTotalHeight = getMeasuredHeight();
                }
            }
            mSplitTextRectArray[m] = lintTextREct;
        }
    }

    private void dealContentOfTotalWidth() {
        mSingleTextWidth = getMeasuredWidth() - mPaddingLeft - mPaddingRight;
        int currentSingleTextWidth = 0;
        StringBuffer lineStringBuffer = new StringBuffer();
        for (int i = 0, length = mTextCharArray.length; i < length; i++) {
            char textChar = mTextCharArray[i];
            currentSingleTextWidth += getSingleCharWidth(textChar);
            if (currentSingleTextWidth == mSingleTextWidth) {
                mSplitTextList.add(lineStringBuffer.toString());
                //lineStringBuffer = new StringBuffer();
                lineStringBuffer.delete(0, mSingleTextWidth - 1);
                currentSingleTextWidth = 0;
            } else {
                lineStringBuffer.append(textChar);
                if (i == length - 1) {
                    //到达末尾
                    mSplitTextList.add(lineStringBuffer.toString());
                }
            }
        }
    }

    /**
     * 得到单个char的宽度
     * @param textChar
     * @return
     */
    public float getSingleCharWidth(char textChar) {
        float[] width = new float[1];
        mTextPaint.getTextWidths(new char[]{textChar}, 0, 1, width);
        return width[0];
    }
    private int getTopTextMarginTop() {
        return mSplitTextRectArray[0].height() / 2 + mPaddingTop + getFontSpace();
    }

    /**
     * TODO 这个是获取什么距离？
     * @return
     */
    private int getFontSpace() {
        Paint.FontMetricsInt fontMetrics = mTextPaint.getFontMetricsInt();
        return (fontMetrics.descent- fontMetrics.ascent) / 2 - fontMetrics.descent;
    }

}
