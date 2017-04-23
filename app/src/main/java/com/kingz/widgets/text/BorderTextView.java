package com.kingz.widgets.text;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.TextView;
import com.utils.ViewTools;

/**
 * Copyright(C) 2016, 北京视达科科技有限公司
 * All rights reserved. <br>
 * author: King.Z <br>
 * date:  2017/4/23 16:47 <br>
 * description: 带有border的TextView <br>
 */
public class BorderTextView extends TextView {
    private Paint mPaint;
    public BorderTextView(Context context) {
        super(context);
        mPaint = new Paint();
    }

    public BorderTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mPaint = new Paint();
    }

    public BorderTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mPaint = new Paint();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        ViewTools.setViewBorder(this,canvas,mPaint);
        super.onDraw(canvas);
    }
}
