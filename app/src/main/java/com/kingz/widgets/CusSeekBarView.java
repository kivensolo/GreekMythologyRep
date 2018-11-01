package com.kingz.widgets;

import android.content.Context;
import android.graphics.*;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

/**
 * @author: KingZ
 * @Description:自定义画笔
 *
 * //需要重写此页面
 */
public class CusSeekBarView extends View implements View.OnTouchListener {

	private static final int MSG_NUM = 1280;
	public boolean FLAG = false;
	private Paint innerPaint;		//内部画笔
	private Paint outerPaint;		//外部画笔
	private RectF rectRemain;
	private Context context;

    private int InnerBarWidth;		//内部总长度
    private int InnerBarheight;		//总高度
    private int InnerBarLeft;
    private int InnerBarTop;
    private int InnerBarBottom;

    private int OuterBarWidth = 110;		//外seekBar长度
    private int OuterBarHeight = 32;		//外seekBar高度

    private int InnerBarMaxWidth = 295;
	private String TAG = "CusSeekBarView";

	private RectF innerRect;
	private RectF outerRect;

	public CusSeekBarView(Context context) {
		super(context);
		this.context = context;
		initViews();
	}

	public CusSeekBarView(Context context, AttributeSet attrs) {
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

	/**
	 * 初始化画笔等工具
	 */
	private void initViews() {
		Log.i(TAG,"initViews");
		InnerBarWidth = 0;
		innerPaint = new Paint();
		outerPaint = new Paint();

		innerPaint.setColor(0xFF224FDD);
		outerPaint.setColor(0xFF8A8C8F);
		innerPaint.setDither(true);
		innerPaint.setAntiAlias(true);
		outerPaint.setAntiAlias(true);
		innerPaint.setStyle(Paint.Style.FILL);
		outerPaint.setStyle(Paint.Style.FILL);
		innerPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_OVER));

		outerRect = new RectF(100,50,100+305,50+32);			//外部矩形
//		innerRect = new RectF(100+5,50+5,125 + InnerBarWidth,50+32-5);	//内部矩形


		InnerBarWidth = 0;
		InnerBarLeft = 105;
		InnerBarTop = 50;
		InnerBarBottom = 82;

		OuterBarWidth = 110;
		OuterBarHeight = 32;
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		drawInTest(canvas);
	}

	private void drawInTest(Canvas canvas) {
		innerRect = new RectF(100+5,50+5,105 + InnerBarWidth,50+32-5);	//内部矩形
		canvas.save();
		canvas.drawRoundRect(outerRect, 15, 15, outerPaint);
		canvas.drawRoundRect(innerRect, 15, 15, innerPaint);
	}

	public void autoRefreshProgress() {

		if(InnerBarWidth == InnerBarMaxWidth){
			FLAG = false;
			return;
		}else{
			InnerBarWidth += 1;
		}
		this.invalidate();
	}

	public void startPlayerTimer() {
		new Thread() {
			@Override
			public void run() {
				while (FLAG) {
					try {
						Log.i(TAG,"刷新进度条+1");
						mHandler.sendEmptyMessage(MSG_NUM);
						Thread.sleep(5);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}.start();
	}

	/**
	 * 初始化进度条
     */
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		return false;
	}

}
