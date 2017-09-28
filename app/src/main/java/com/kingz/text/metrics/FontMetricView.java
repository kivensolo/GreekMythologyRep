package com.kingz.text.metrics;

import android.content.Context;
import android.graphics.*;
import android.util.AttributeSet;
import android.view.View;
import com.kingz.customdemo.R;
import com.kingz.utils.ScreenTools;
import com.kingz.utils.ZLog;

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
 *
 *
 * //TODO 横竖屏比例
 */
public class FontMetricView extends View {
    public static final int text_draw_y = ScreenTools.Operation(300);
    private String text = "あリが中文Englishشكر";
    public int baseX = 0;
    private Paint mPaint;
    private Paint.FontMetrics fontMetrics;
    private Rect bounds = new Rect();

    public FontMetricView(Context context) {
        this(context, null);
    }

    public FontMetricView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setFocusable(true);
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStrokeWidth(2);
        mPaint.setAntiAlias(true);
        mPaint.setColor(getResources().getColor(R.color.black));
        mPaint.setTypeface(Typeface.create(Typeface.SERIF,Typeface.ITALIC));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawColor(Color.WHITE);
        //float[] widths = new float[text.length()];
        //int unicharsCount = mPaint.getTextWidths(text, 0, text.length(), widths);
        //float textWidth = mPaint.measureText(text, 0, text.length());
         //float[] pts = new float[2 + count*2];
         //   float x = 0;
         //   float y = 0;
         //   pts[0] = x;
         //   pts[1] = y;
         //   for (int i = 0; i < count; i++) {
         //       x += widths[i];
         //       pts[2 + i*2] = x;
         //       pts[2 + i*2 + 1] = y;
         //   }
         //   mPaint.setColor(Color.RED);
         //   mPaint.setStrokeWidth(0);
         //   canvas.drawLine(0, 0, w, 0, mPaint);
         //   mPaint.setStrokeWidth(5);
         //   canvas.drawPoints(pts, 0, (count + 1) << 1, mPaint);
        //drawBounds(canvas);
        mPaint.setTextSize(80);
        fontMetrics = mPaint.getFontMetrics();
        //使文本绘制起来对于指定位置是对应的
        float baselineY = text_draw_y - (fontMetrics.ascent + fontMetrics.descent) / 2;
        ZLog.d("FontMetricView","baselineY = " + baselineY);
        drawLines(canvas, baselineY);
        mPaint.setColor(Color.BLACK);
        if(mPaint.measureText(text) >= getWidth()){
            //文本长度超过屏幕显示宽度  从最左侧开始绘制
            canvas.drawText(text,0,baselineY, mPaint);
        }else{
            //居中绘制
            canvas.drawText(text, getWidth() / 2 - mPaint.measureText(text) / 2, baselineY, mPaint);
        }
        drawTextPosDot(canvas, baselineY);
        drawDescText(canvas);
    }

    private void drawLines(Canvas canvas, float baselineY) {
        drawLine(canvas,baseX,baselineY, getResources().getColor(R.color.blue), mPaint);
        drawLine(canvas,baseX,baselineY + fontMetrics.top,getResources().getColor(R.color.black), mPaint);
        drawLine(canvas,baseX,baselineY + fontMetrics.ascent,getResources().getColor(R.color.skygreen), mPaint);
        drawLine(canvas,baseX,baselineY + fontMetrics.descent,getResources().getColor(R.color.green), mPaint);
        drawLine(canvas,baseX,baselineY + fontMetrics.bottom,getResources().getColor(R.color.gold), mPaint);
        drawLine(canvas,baseX,text_draw_y,getResources().getColor(R.color.red), mPaint);
    }

    /**
     * 绘制描述文字
     * @param canvas 当前画布对象
     */
    private void drawDescText(Canvas canvas) {
        canvas.translate(0,ScreenTools.Operation(600));
        mPaint.setTextSize(30);
        drawInfoText(canvas, getResources().getColor(R.color.black), "top|Max-ascent: " + fontMetrics.top, mPaint);
        drawInfoText(canvas, getResources().getColor(R.color.skygreen), "ascent: " + fontMetrics.ascent, mPaint);
        drawInfoText(canvas, getResources().getColor(R.color.blue), "baseline: 0",mPaint);
        drawInfoText(canvas, getResources().getColor(R.color.red), "middle: " + (fontMetrics.ascent + fontMetrics.descent) / 2,mPaint);
        drawInfoText(canvas, getResources().getColor(R.color.green), "descent: " + fontMetrics.descent,mPaint);
        drawInfoText(canvas, getResources().getColor(R.color.gold), "bottom|Max-Descent: " + fontMetrics.bottom,mPaint);
    }

    private void drawTextPosDot(Canvas canvas, float baselineY) {
        //错误文本坐标描点
        mPaint.setColor(getResources().getColor(R.color.red));
        canvas.drawCircle(7,baselineY + fontMetrics.ascent, 7, mPaint);
        //正确文本坐标描点
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
