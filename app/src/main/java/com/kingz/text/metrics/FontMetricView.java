package com.kingz.text.metrics;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.View;

import com.kingz.customdemo.R;
import com.module.tools.ScreenTools;
import com.zeke.kangaroo.utils.ZLog;

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
 * [Top]------最高字符到baseline的值
 * [Bottom]-----最低字符到baseline的值
 * ascent + descent 可以看成文字的height
 * 获取height ： mPaint.ascent() + mPaint.descent()
 * 获取width ： mPaint.measureText(text)
 */
public class FontMetricView extends View {
    public static final int text_draw_y = ScreenTools.Operation(150);
    private String text = "あリが中文Englishشكر";
    public int baseX = 0;
    private Paint mPaint;
    private Paint.FontMetrics fontMetrics;
    private Rect bounds = new Rect();
    private float baselineY = 0f;

    public FontMetricView(Context context) {
        this(context, null);
    }

    public FontMetricView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setFocusable(true);
        initPaint();
    }

    private void initPaint() {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStrokeWidth(2);
        mPaint.setTextSize(80);
        mPaint.setAntiAlias(true);
        mPaint.setColor(getResources().getColor(R.color.black));
        mPaint.setTypeface(Typeface.create(Typeface.SERIF,Typeface.NORMAL));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawColor(Color.WHITE);
        fontMetrics = mPaint.getFontMetrics();
        baselineY = text_draw_y - (fontMetrics.ascent + fontMetrics.descent) / 2;
        ZLog.d("FontMetricView","baselineY = " + baselineY);

        drawLines(canvas, baselineY);
        mPaint.setColor(Color.BLACK);
        if(mPaint.measureText(text) >= getWidth()){
            canvas.drawText(text,0,baselineY, mPaint);
        }else{
            canvas.drawText(text, getWidth() / 2 - mPaint.measureText(text) / 2, baselineY, mPaint);
        }
        drawTextPosDot(canvas, baselineY);
        drawCharDots(canvas);
        drawDescText(canvas);
    }

    private void drawCharDots(Canvas canvas) {
        float[] widths = new float[text.length()];
        int count = mPaint.getTextWidths(text, 0, text.length(), widths); //return the number of unichars / 每个字符的宽度放入widths中
        float[] pts = new float[2 + count*2];
        float x = getWidth() / 2 - mPaint.measureText(text) / 2;
        float y = baselineY;
        pts[0] = x; //基准点X坐标值
        pts[1] = y; //基准点Y坐标值
        for (int i = 0; i < count; i++) {
            x += widths[i];
            pts[2 + i*2] = x;
            pts[2 + i*2 + 1] = y;
        }
        mPaint.setColor(getResources().getColor(R.color.bright_green));
        mPaint.setStrokeWidth(5);
        canvas.drawPoints(pts, 0, (count + 1) << 1, mPaint);
    }

    private void drawLines(Canvas canvas, float baselineY) {
        drawLine(canvas,baseX,baselineY, getResources().getColor(R.color.blue), mPaint);
        drawLine(canvas,baseX,baselineY + fontMetrics.top,getResources().getColor(R.color.black), mPaint);
        drawLine(canvas,baseX,baselineY + fontMetrics.ascent,getResources().getColor(R.color.skygreen), mPaint);
        drawLine(canvas,baseX,baselineY + fontMetrics.descent,getResources().getColor(R.color.green), mPaint);
        drawLine(canvas,baseX,baselineY + fontMetrics.bottom,getResources().getColor(R.color.gold), mPaint);
        drawLine(canvas,baseX,text_draw_y,getResources().getColor(R.color.red), mPaint);
    }

    private void drawDescText(Canvas canvas) {
        canvas.translate(0,ScreenTools.Operation(250));
        mPaint.setTextSize(30);
        drawInfoText(canvas, getResources().getColor(R.color.black), "top|Max-ascent: " + fontMetrics.top, mPaint);
        drawInfoText(canvas, getResources().getColor(R.color.skygreen), "ascent: " + fontMetrics.ascent, mPaint);
        drawInfoText(canvas, getResources().getColor(R.color.blue), "baseline: 0",mPaint);
        drawInfoText(canvas, getResources().getColor(R.color.red), "middle: " + (fontMetrics.ascent + fontMetrics.descent) / 2,mPaint);
        drawInfoText(canvas, getResources().getColor(R.color.green), "descent: " + fontMetrics.descent,mPaint);
        drawInfoText(canvas, getResources().getColor(R.color.gold), "bottom|Max-Descent: " + fontMetrics.bottom,mPaint);
    }

    private void drawTextPosDot(Canvas canvas, float baselineY) {
        //Error text coordinate point
        mPaint.setColor(getResources().getColor(R.color.red));
        canvas.drawCircle(7,baselineY + fontMetrics.ascent, 7, mPaint);
        //Correct text coordinate point
        mPaint.setColor(getResources().getColor(R.color.green));
        canvas.drawCircle(7,baselineY, 7, mPaint);
    }

    private void drawInfoText(Canvas canvas, int color, String text, Paint mPaint) {
        canvas.translate(0,ScreenTools.Operation(35));
        mPaint.setColor(color);
        canvas.drawText(text, 20, 0, mPaint);
    }

    private void drawLine(Canvas canvas,int startX, float startY, int color, Paint mPaint) {
        mPaint.setColor(color);
        canvas.drawLine(startX, startY, getWidth(),startY, mPaint);
    }

    private void drawBounds(Canvas canvas) {
        mPaint.getTextBounds(text, 0, text.length(), bounds);
        mPaint.setColor(0xFF88FF88);
        canvas.drawRect(bounds, mPaint);
    }
}
