package com.view.porterduffmode;

import android.content.Context;
import android.graphics.*;
import android.util.AttributeSet;
import android.view.View;
import com.kingz.uiusingListViews.R;

/**
 * Copyright(C) 2016, 北京视达科科技有限公司
 * All rights reserved.
 * author: King.Z
 * date:  2016/5/29 17:05
 * description:
 */
public class DuffTestView extends View{

    private Paint mPaint;

    public DuffTestView(Context context) {
        this(context,null);
    }

    public DuffTestView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public DuffTestView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initPaint();
    }

    public void initPaint(){
        mPaint = new Paint();
//        mPaint.setColor(Color.parseColor("##ff0099ff"));
        mPaint.setColor(getResources().getColor(R.color.darkturquoise));
        mPaint.setStrokeWidth(5);
        mPaint.setAntiAlias(true);
    }

    @Override protected void onDraw(Canvas canvas) {
        canvas.drawRect(getWidth()/2 -200,getHeight()/2-200,getWidth()/2+200,getHeight()/2+200,mPaint);
        mPaint.setColor(getResources().getColor(R.color.accent_A100));
        mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_OVER));
        canvas.drawCircle(getWidth()/2 -200,getHeight()/2 + 20,100,mPaint);
    }
}
