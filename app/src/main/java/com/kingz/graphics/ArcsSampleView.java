package com.kingz.graphics;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import com.kingz.customdemo.R;
import com.zeke.kangaroo.utils.UIUtils;

public class ArcsSampleView extends View {
    private Paint[] mPaints;
    private Paint mFramePaint;
    private boolean[] useCenterArray;
    private RectF[] mOvals;
    private RectF mBigOval;
    private float mStart;
    private float mSweep;
    private int mBigIndex;


    private static final float SWEEP_INC = 2;
    private static final float START_INC = 15;
    private static final int NUM_PAINT = 4;

    IInvalidateLis lsr;

    public interface IInvalidateLis {
        void onInvalidate(float sweep);
    }

    public void setLsr(IInvalidateLis lsr) {
        this.lsr = lsr;
    }

    public ArcsSampleView(Context context) {
        this(context,null);
    }

    public ArcsSampleView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setBackgroundColor(context.getResources().getColor(R.color.fruitpurple));

        mPaints = new Paint[NUM_PAINT];
        useCenterArray = new boolean[NUM_PAINT];
        mOvals = new RectF[NUM_PAINT];

        mPaints[0] = new Paint();
        mPaints[0].setAntiAlias(true);
        mPaints[0].setStyle(Paint.Style.FILL);
        mPaints[0].setColor(0x88FF0000);
        useCenterArray[0] = false;

        mPaints[1] = new Paint(mPaints[0]);
        mPaints[1].setColor(0x8800FF00);
        useCenterArray[1] = true;

        mPaints[2] = new Paint(mPaints[0]);
        mPaints[2].setStyle(Paint.Style.STROKE);
        mPaints[2].setStrokeWidth(4);
        mPaints[2].setColor(0x880000FF);
        useCenterArray[2] = false;

        mPaints[3] = new Paint(mPaints[2]);
        mPaints[3].setColor(0x88888888);
        useCenterArray[3] = true;

        mBigOval = new RectF(UIUtils.dip2px(context, 40f),
                UIUtils.dip2px(context, 10f),
                UIUtils.dip2px(context, 280f),
                UIUtils.dip2px(context, 250f));

        mOvals[0] = new RectF(UIUtils.dip2px(context, 10f),
                UIUtils.dip2px(context, 270f),
                UIUtils.dip2px(context, 70f),
                UIUtils.dip2px(context, 330f));
        mOvals[1] = new RectF(UIUtils.dip2px(context, 90f),
                UIUtils.dip2px(context, 270f),
                UIUtils.dip2px(context, 150f),
                UIUtils.dip2px(context, 330f));
        mOvals[2] = new RectF(UIUtils.dip2px(context, 170f),
                UIUtils.dip2px(context, 270f),
                UIUtils.dip2px(context, 230f),
                UIUtils.dip2px(context, 330f));
        mOvals[3] = new RectF(UIUtils.dip2px(context, 250f),
                UIUtils.dip2px(context, 270f),
                UIUtils.dip2px(context, 310f),
                UIUtils.dip2px(context, 330f));

        mFramePaint = new Paint();
        mFramePaint.setAntiAlias(true);
        mFramePaint.setStyle(Paint.Style.STROKE);
        mFramePaint.setStrokeWidth(0);
    }

    private void drawArcs(Canvas canvas, RectF oval,
                          boolean useCenter, Paint paint) {
        canvas.drawRect(oval, mFramePaint);
        canvas.drawArc(oval, mStart, mSweep, useCenter, paint);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        int width = ScreenTools.Operation(ScreenTools.getScreenWidth(App.instance.getApplicationContext()));
////        int height = ScreenTools.Operation(ScreenTools.getScreenHeight(App.instance.getApplicationContext()));
//        setMeasuredDimension(width, height);
//        ZLog.d("onMeasure kingz", "width=" + width + ";height=" + height);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawColor(Color.WHITE);

        drawArcs(canvas, mBigOval, useCenterArray[mBigIndex], mPaints[mBigIndex]);

        for (int i = 0; i < NUM_PAINT; i++) {
            drawArcs(canvas, mOvals[i], useCenterArray[i], mPaints[i]);
        }

        mSweep += SWEEP_INC;
        if (mSweep > 360) {
            mSweep -= 360;
            mStart += START_INC;
            if (mStart >= 360) {
                mStart -= 360;
            }
            mBigIndex = (mBigIndex + 1) % mOvals.length;
        }
        invalidate();
        if (lsr != null) {
            lsr.onInvalidate(mSweep);
        }
    }
}
