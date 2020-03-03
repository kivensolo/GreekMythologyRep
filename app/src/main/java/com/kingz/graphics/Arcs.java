package com.kingz.graphics;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.kingz.customdemo.R;
import com.kingz.widgets.text.LogTextBox;
import com.module.tools.ScreenTools;
import com.zeke.kangaroo.utils.ZLog;
import com.zeke.ktx.App;

/**
 * author: King.Z
 * date:  2016/10/25 21:33
 * description: 弧形的使用
 */
public class Arcs extends Activity {
    private LogTextBox logView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        logView = new LogTextBox(this);
        logView.setBackground(getResources().getDrawable(R.drawable.logbox));
        logView.setX(ScreenTools.OperationHeight(400));
        LinearLayout.LayoutParams lps = new LinearLayout.LayoutParams(ScreenTools.OperationHeight(600), ScreenTools.OperationHeight(600));
        logView.setLayoutParams(lps);
        logView.setOverScrollMode(View.OVER_SCROLL_ALWAYS);

        SampleView rootview = new SampleView(this);
        rootview.setLsr(new SampleView.IInvalidateLis() {
            @Override
            public void onInvalidate(float mSweep) {
                 logView.append("curretn sweep:"+mSweep+"\n");
                int totalH = logView.getLineCount() * logView.getLineHeight();
                if (totalH > (logView.getHeight() - logView.getLineHeight())) {
                    logView.scrollTo(0, totalH - logView.getHeight() + logView.getLineHeight());
                }
            }
        });
        setContentView(rootview);
        addContentView(logView,lps);


    }
    private static class SampleView extends View {
        private Paint[] mPaints;
        private Paint mFramePaint;
        private boolean[] mUseCenters;
        private RectF[] mOvals;
        private RectF mBigOval;
        private float mStart;
        private float mSweep;
        private int mBigIndex;


        private static final float SWEEP_INC = 2;
        private static final float START_INC = 15;
        private static final int NUM_PAINT = 4;

        IInvalidateLis lsr;
        public interface IInvalidateLis{
            public void onInvalidate(float sweep);
        }
        public void setLsr(IInvalidateLis lsr){
            this.lsr = lsr;
        }
        public SampleView(Context context) {
            super(context);
            setBackgroundColor(context.getResources().getColor(R.color.fruitpurple));

            mPaints = new Paint[NUM_PAINT];
            mUseCenters = new boolean[NUM_PAINT];
            mOvals = new RectF[NUM_PAINT];

            mPaints[0] = new Paint();
            mPaints[0].setAntiAlias(true);
            mPaints[0].setStyle(Paint.Style.FILL);
            mPaints[0].setColor(0x88FF0000);
            mUseCenters[0] = false;

            mPaints[1] = new Paint(mPaints[0]);
            mPaints[1].setColor(0x8800FF00);
            mUseCenters[1] = true;

            mPaints[2] = new Paint(mPaints[0]);
            mPaints[2].setStyle(Paint.Style.STROKE);
            mPaints[2].setStrokeWidth(4);
            mPaints[2].setColor(0x880000FF);
            mUseCenters[2] = false;

            mPaints[3] = new Paint(mPaints[2]);
            mPaints[3].setColor(0x88888888);
            mUseCenters[3] = true;

            mBigOval = new RectF(40, 10, 280, 250);

            mOvals[0] = new RectF( 10, 270,  70, 330);
            mOvals[1] = new RectF( 90, 270, 150, 330);
            mOvals[2] = new RectF(170, 270, 230, 330);
            mOvals[3] = new RectF(250, 270, 310, 330);

            mFramePaint = new Paint();
            mFramePaint.setAntiAlias(true);
            mFramePaint.setStyle(Paint.Style.STROKE);
            mFramePaint.setStrokeWidth(0);
        }

        private void drawArcs(Canvas canvas, RectF oval,
                              boolean useCenter,Paint paint) {
            canvas.drawRect(oval, mFramePaint);
            canvas.drawArc(oval, mStart, mSweep, useCenter, paint);
        }

        @Override
        protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
            int width = ScreenTools.Operation(ScreenTools.getScreenWidth(App.instance.getApplicationContext()));
            int height = ScreenTools.Operation(ScreenTools.getScreenHeight(App.instance.getApplicationContext()));
            setMeasuredDimension(width,height);
            ZLog.d("onMeasure kingz","width="+width+";height="+height);
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }

        @Override
        protected void onDraw(Canvas canvas) {
            canvas.drawColor(Color.WHITE);

            drawArcs(canvas, mBigOval, mUseCenters[mBigIndex],mPaints[mBigIndex]);

            for (int i = 0; i < NUM_PAINT; i++) {
                drawArcs(canvas, mOvals[i], mUseCenters[i], mPaints[i]);
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
            if(lsr != null){
                lsr.onInvalidate(mSweep);
            }
        }
    }
}
