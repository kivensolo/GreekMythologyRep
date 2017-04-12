package com.kingz.widgets;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class SeekBarView extends View implements View.OnTouchListener {

	private static final int MSG_NUM = 1280;
	private Paint mPaint;
	private RectF rectRemain;
	private Context context;


    private int progressBarWidth = 100;
    private int progressPaddingLeft = 0;
    private int progressPaddingTop = 0;
    private int progressBottom = 0;

    private int playProgressWidth = 100;

	public SeekBarView(Context context) {
		super(context);
		this.context = context;
		initViews();
	}

	public SeekBarView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		initViews();
	}


	private Handler mHandler = new Handler(){
		public void handleMessage(Message msg){
			 switch (msg.what) {
			 case MSG_NUM:
				 autoRefreshProgress();
				 break;
             default:
                 break;
         }
		}
	};

	private void initViews() {
		mPaint = new Paint();
		mPaint.setColor(0xFF4C6EC4);
		mPaint.setAntiAlias(true);
		mPaint.setStyle(Paint.Style.FILL);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);

		drawInTest(canvas);
	}

	private void drawInTest(Canvas canvas) {
		RectF rectPlay = new RectF(progressPaddingLeft,
									progressPaddingTop,
									playProgressWidth,
									progressBottom);
		canvas.save();
		canvas.drawRoundRect(rectPlay, 15, 15, mPaint);
		canvas.restore();
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		// TODO Auto-generated method stub

		return false;
	}

	public void autoRefreshProgress() {

		if(playProgressWidth == progressBarWidth){
			return;
		}
		playProgressWidth += 1;
		this.invalidate();
	}

	public void startPlayerTimer() {
		new Thread() {
			@Override
			public void run() {
				while (true) {
					try {
						mHandler.sendEmptyMessage(MSG_NUM);
						Thread.sleep(5);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}.start();
	}

	public void initUIPara(int progressPaddingLeft, int ProgressWidth,
			int progressPaddingTop, int progressBottom) {
		this.progressBarWidth = ProgressWidth;
		this.progressPaddingLeft = progressPaddingLeft;
		this.progressPaddingTop = progressPaddingTop;
		this.progressBottom = progressBottom;
	}

}
