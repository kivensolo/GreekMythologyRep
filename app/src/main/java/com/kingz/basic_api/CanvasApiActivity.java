package com.kingz.basic_api;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.zeke.kangaroo.utils.ZLog;

/**
 * author: King.Z <br>
 * date:  2018/3/10 19:16 <br>
 * description: 安卓画布基本API的效果展示 <br>
 *  主要展示scale的两种方式、以及scale后transalte的效果
 *
 *  scale(float sx, float sy): 前乘当前矩阵，放大宽高
 *  scale(float sx, float sy, float px, float py)：
 *  先translate到(px,py)点 再前乘当前矩阵，然后在此矩阵的前提下，
 *  再translate到(-px,-py)点,实际上已经是tarnslate(- sx * px, -sy * py)。
 *
 */
public class CanvasApiActivity extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initRootView();
    }

    private void initRootView() {
        ScrollView root = new ScrollView(this);
        LinearLayout linearLayout = new LinearLayout(this);
        ViewGroup.LayoutParams lps = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.setLayoutParams(lps);
        linearLayout.setBackgroundColor(0xff483d8b);
        setContentView(root);
         ApiView apiView = new ApiView(this);
        linearLayout.addView(apiView);
        addContentView(linearLayout,lps);
    }


    class ApiView extends View{
        int aColor = 0xff00ff7f;
        Paint drawPaint = new Paint();
        Paint draw2Paint = new Paint();
        Paint textPaint = new Paint();
        float xBase = 100f;
        Rect rect = new Rect(0,0,100,100);
        public ApiView(Context context) {
            super(context);
            textPaint.setDither(true);
            textPaint.setAntiAlias(true);
            textPaint.setTextSize(23);
            textPaint.setColor(0xffFF1744);
            drawPaint.setDither(true);
            drawPaint.setAntiAlias(true);
            drawPaint.setStrokeWidth(4);
            drawPaint.setColor(0xffff8c00);
            draw2Paint.setStrokeWidth(4);
            draw2Paint.setColor(0xffc71585);
        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            ZLog.d("KingAA","onDraw()");
            //原始图像
            canvas.drawText("Canvas normalRect：",0,20,textPaint);
            canvas.save();
            canvas.translate(xBase,22);
            canvas.drawRect(rect,drawPaint);
            canvas.restore();
            //Scale --- A
            canvas.save();
            canvas.translate(0,144);
            canvas.drawText("Canvas  scale(float sx, float sy)： [2,2]",0,20,textPaint);
            canvas.translate(xBase,22);
            canvas.scale(2,2);
            canvas.drawRect(rect,drawPaint);
            canvas.restore();

            //Scale --- B
            canvas.save();
            canvas.translate(0,400);
            canvas.drawText("Canvas  scale(float sx, float sy, float px, float py)： [2,2,50,50]",0,20,textPaint);
            canvas.translate(xBase,22);
            canvas.scale(2,2,50,50);
            //translate(px, py);
            //scale(sx, sy);
            //translate(-px, -py);
            canvas.drawRect(rect,draw2Paint);
            canvas.restore();

        }
    }
}
