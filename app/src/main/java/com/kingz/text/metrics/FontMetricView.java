package com.kingz.text.metrics;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import com.kingz.customdemo.R;

/**
 * Copyright(C) 2016, 北京视达科科技有限公司
 * All rights reserved. <br>
 * author: King.Z <br>
 * date:  2017/4/24 15:44 <br>
 * description: FontMetric <br>
 * [baseline]----基准点
 * [Ascent]-----baseline之上至字符最高处的距离
 * [Descent]-----baseline之下至字符最低处的距离
 * [Leading]-----上一行字符的descent到下一行的ascent之间的距离
 * [Top]------最高字符到baseline的值，Max(Ascent)
 * [Bottom]-----最低字符到baseline的值，Max(Descent)
 * ascent + descent 可以看成文字的height
 * 获取height ： mPaint.ascent() + mPaint.descent()
 * 获取width ： mPaint.measureText(text)
 */
public class FontMetricView extends View {
    private static final String TEXT = "あリが中文汉字Englishشكر";
    public int baseX = 0;
    private Paint mPaint;
    private Paint.FontMetrics fontMetrics;

    public FontMetricView(Context context) {
        this(context, null);
    }

    public FontMetricView(Context context, AttributeSet attrs) {
        super(context, attrs);

        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStrokeWidth(2);
        mPaint.setTextSize(70);
        mPaint.setColor(Color.BLACK);

        fontMetrics = mPaint.getFontMetrics();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawColor(Color.WHITE);
        mPaint.setTextSize(80);
        float baselineY = getHeight() / 2 - (fontMetrics.ascent + fontMetrics.descent) / 2;
        //画BaseLine
        mPaint.setColor(Color.BLUE);
        canvas.drawLine(baseX, baselineY, getWidth(), baselineY, mPaint);

        //画Max-AscentLine
        mPaint.setColor(Color.BLACK);
        canvas.drawLine(0, baselineY + fontMetrics.top, getWidth(), baselineY + fontMetrics.top, mPaint);
        //画AscentLine
        mPaint.setColor(getResources().getColor(R.color.darkorange));
        canvas.drawLine(0, baselineY + fontMetrics.ascent, getWidth(), baselineY + fontMetrics.ascent, mPaint);
        //画DescentLine
        mPaint.setColor(Color.GREEN);
        canvas.drawLine(0, baselineY + fontMetrics.descent, getWidth(), baselineY + fontMetrics.descent, mPaint);
        //画Max-DescentLine
        mPaint.setColor(Color.LTGRAY);
        canvas.drawLine(0, baselineY + fontMetrics.bottom, getWidth(), baselineY + fontMetrics.bottom, mPaint);
        //画中线
        mPaint.setColor(Color.RED);
        canvas.drawLine(0, getHeight() / 2, getWidth(), getHeight() / 2, mPaint);

        //文本绘制  drawText的x,y是基准线的x和y
        mPaint.setColor(Color.BLACK);
        canvas.drawText(TEXT, getWidth() / 2 - mPaint.measureText(TEXT) / 2, baselineY, mPaint);

        //错误文本坐标描点
        mPaint.setColor(Color.RED);
        canvas.drawCircle(5,baselineY + fontMetrics.ascent, 5, mPaint);
        //正确文本坐标描点
        mPaint.setColor(Color.GREEN);
        canvas.drawCircle(5,baselineY, 5, mPaint);

        mPaint.setTextSize(30);
        Paint.FontMetrics tempFM = mPaint.getFontMetrics();
        float size = tempFM.bottom - tempFM.top;
        mPaint.setColor(Color.LTGRAY);
        canvas.drawText(
                "bottom|Max-Descent(LTGRAY): " + fontMetrics.bottom,
                20,
                getHeight() - tempFM.bottom - 20,
                mPaint);
        mPaint.setColor(Color.GREEN);
        canvas.drawText(
                "descent(GREEN): " + fontMetrics.descent,
                20,
                getHeight() - 20 - size - 5,
                mPaint);
        mPaint.setColor(Color.BLUE);
        canvas.drawText(
                "baseline(BLUE): 0",
                20,
                getHeight() - 20 - size * 3 - 15,
                mPaint);
        mPaint.setColor(Color.RED);
        canvas.drawText(
                "middle(RED): " + (fontMetrics.ascent + fontMetrics.descent) / 2,
                20,
                getHeight() - 20 - size * 2 - 10,
                mPaint);
        mPaint.setColor(getResources().getColor(R.color.darkorange));
        canvas.drawText(
                "ascent(ORANGE): " + fontMetrics.ascent,
                20,
                getHeight() - 20 - size * 4 - 20,
                mPaint);
        mPaint.setColor(Color.BLACK);
        canvas.drawText(
                "top|Max-ascent(BLACK): " + fontMetrics.top,
                20,
                getHeight() - 20 - size * 5 - 25,
                mPaint);
    }
}
