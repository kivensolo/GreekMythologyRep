package com.module.views.text;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.TextView;

import com.module.tools.ViewUtils;

/**
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

    public BorderTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mPaint = new Paint();
    }

    public BorderTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mPaint = new Paint();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        ViewUtils.setViewBorder(this,canvas,mPaint);
        super.onDraw(canvas);
    }
}
