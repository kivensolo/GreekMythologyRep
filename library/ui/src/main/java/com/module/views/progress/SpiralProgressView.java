package com.module.views.progress;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;

import com.module.tools.ScreenTools;

import java.util.ArrayList;
import java.util.List;

/***
 * Created by IntelliJ IDEA.
 * Description: 螺旋条纹进度条.可控制螺纹线条的显示、宽度、长度、
 * 			间距、数量、进度圆角、进度条背景色、背景色边框等参数
 * Author: zhi.wang
 * Date:2015/11/28
 *
 * //TODO This is very old code. Need to refactor. 2019/4/2
 */
public class SpiralProgressView extends View{

	public static final String TAG = "SpiralProgressView";
	public static final int LINE_PADDING_RECT = ScreenTools.Operation(11);
	public static final int RECT_START_POINT = ScreenTools.Operation(10);
	public boolean isWaiting;

	private Paint mPaint1;	//背景
	private Paint mPaint2;	//背景边框
	private Paint mPaint3;	//斜线
	private Paint mPaint4;	//进度

	public interface ISpiralProgressListener {
		void onFinished(boolean isRecordSuccess);
	}

	private List<ISpiralProgressListener> spiralProgressListene = new ArrayList<SpiralProgressView.ISpiralProgressListener>();

	public void setOnSpiralBarFinishListener(ISpiralProgressListener spiralProgressListener){
        if(!this.spiralProgressListene.contains(spiralProgressListener)){
            this.spiralProgressListene.add(spiralProgressListener);
        }
    }

	/** 第一个斜线的起始位置 **/
	private float first_diagonal_line = ScreenTools.Operation(720)/27 ; // mWidth / 27

	/** 第一个斜线的底边最左端 **/
	private float first_diagonal_line_left = first_diagonal_line - ScreenTools.Operation(720)/80;	//first_diagonal_line - mWidth / 80

	/** 进度条当前值 */
	private float currentCount;

	/** 每条斜线之间的距离 **/
	private float dis_line = ScreenTools.Operation(720)/62;		//Width / 62

	/** 进度条最大值 */
	private float maxCount = 100;

	/** 绘画间隔时间 **/
	public int drawSleepTime = 21;

	/** 进度条百分率 **/
	private float progressPercent;

	/** 每条斜線的寬度 **/
	private float w_line = ScreenTools.Operation(720)/82; 	//mWidth / 82

	/** 矩形圆角**/
	private int round;

	/** 设置几条斜线 **/
	private int line_num;

	/** 进度条宽高 **/
	private static int mWidth = ScreenTools.Operation(540);
	private static int mHeight = ScreenTools.Operation(36);

	/** 进度条SeekComplete **/
	private boolean isRecordingFinished;

	/** 绘制动画开关 **/
	private boolean hasAnimation = true;

	/** 螺纹线条开关 **/
	private boolean hasSpiralLine = true;

	/** 加载中Flag **/
	private boolean isRecording;
	private Canvas canvas;
	private float b;
	boolean recordSuccess = false;//录制成功or失败

    public void setRecordSuccess(boolean recordSuccess) {
        this.recordSuccess = recordSuccess;
    }

	public SpiralProgressView(Context context) {
		super(context);
		initView(context);
	}
	public SpiralProgressView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView(context);
	}
	public SpiralProgressView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		initView(context);
	}

	private void initView(Context context) {
		init();
	}

	private void init() {
		mPaint1 = new Paint();
		mPaint2 = new Paint();
		mPaint3 = new Paint();
		mPaint4 = new Paint();
		mPaint1.setAntiAlias(true);
		mPaint2.setAntiAlias(true);
		mPaint3.setAntiAlias(true);
		mPaint4.setAntiAlias(true);

		mPaint1.setColor(0xff000000);
		mPaint2.setColor(0xff000000);
		mPaint4.setColor(0xff2448A8);

		mPaint3.setColor(0xff3A89FF);
		mPaint3.setStrokeWidth(1);		// 斜线画笔的粗度1像素

		round = mHeight / 4;
	}

	public void setmPreogressPainColor(int color){
		mPaint4.setColor(color);
	}

	private float RectF_X_R;
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		this.canvas = canvas;

		progressPercent = currentCount / maxCount;
		line_num = (int) ((progressPercent * (mWidth - 5) - first_diagonal_line) / (w_line + dis_line));
		RectF_X_R = (mWidth -3) * progressPercent;
		System.out.println("max=" + maxCount + "  current=" + currentCount + " percent = " + progressPercent * 100+"%");

		if(hasAnimation){
			//背景黑边
			//RectF rectBg = new RectF(-1, -1, mWidth + 3, mHeight + 3);
			//canvas.drawRoundRect(rectBg, round, round, mPaint2);
			//黑色背景
			//RectF rectBlackBg = new RectF(0, 0, mWidth +2, mHeight + 2);
			//canvas.drawRoundRect(rectBlackBg, round, round, mPaint1);
			//进度条

			RectF rectProgressBg_up = new RectF(RECT_START_POINT, RECT_START_POINT, RectF_X_R, mHeight - RECT_START_POINT);
			Log.i(TAG,"mWidth = " + mWidth+";progressPercent = " + progressPercent);
			canvas.drawRoundRect(rectProgressBg_up, round, round, mPaint4);
			if(hasSpiralLine){
				for (float k = first_diagonal_line; k <= progressPercent * (mWidth - 20) ; k = k + dis_line + w_line) {
					//此处K的最大值是减20是为了防止绘制最后一条斜线，否则斜线会超出进度条
					drawSpiralLine(k);
				}
			}
		}else{
			RectF rectProgressBg_up = new RectF(0, 0,RectF_X_R,mHeight);
			canvas.drawRoundRect(rectProgressBg_up, round, round, mPaint4);
		}

		/*
		 * 当进度条小于第一条斜线最右端，不显示斜线 当大于第一条进度后开始画线
		 */
//		if ((mWidth - 2) * progressPercent <= first_diagonal_line + w_line + mWidth/ 80) {
//			System.out.println("---------------");
//			canvas.drawRoundRect(rectProgressBg_up, round, round, mPaint4);
//		}else {
//			canvas.drawRoundRect(rectProgressBg_up, round, round, mPaint4);
//			double a = ((mWidth - 2) * progressPercent - first_diagonal_line)
//			for (float k = first_diagonal_line; k <= progressPercent * (mWidth - 20) ; k = k + dis_line + w_line) {
//				drawSpiralLine(k);
////				b = k;
//			}
//			if (a < w_line + 4) {
//			} else {
//				drawSpiralLine(b);
//			}
//		}
	}

	/**
	* 绘制螺纹线条(右上角至左下角)
	*/
	private void drawSpiralLine(float line_x) {
		for (float j = line_x; j <= line_x + w_line; j++) {
			canvas.drawLine(j, LINE_PADDING_RECT, j - mWidth / 80, mHeight - LINE_PADDING_RECT, mPaint3);
		}
	}

	/**
	 * 设置进度条最大值
	 * @param maxCount
     */
	public void setMaxCount(float maxCount) {
		this.maxCount = maxCount;
	}

	/**
	 * 自定义设置进度条宽高
	 * @param mWidth
	 * @param mHeight
     */
	public void setWidthAndHeight(int mWidth, int mHeight){
		this.mWidth  = mWidth;
		this.mHeight = mHeight;
	}

	/**
	 * 自定义设置绘图间隔时间
	 * @param drawSleepTime
     */
	public void setDrawSleepTime(int drawSleepTime) {
		this.drawSleepTime = drawSleepTime;
	}

	/**
	 * 设置绘图动画开关
	 * @param flag
     */
	public void setAnimation(boolean flag){
		hasAnimation = flag;
	}

	/**
	 * 设置螺纹线条开关
	 * @param flag
     */
	public void setPiralLine(boolean flag){
		hasSpiralLine = flag;
	}

	/**
	 * 自定义设置螺纹线条的属性
	 * @param firstLineX：第一条斜线的X坐标
	 * @param firstLineX_Offset：X偏移量
	 * @param line_Spacing：线间距
	 * @param line_Width：线宽度
     */
	public void setPiralLineAttrCustom(float firstLineX, float firstLineX_Offset, float line_Spacing, float line_Width){
		first_diagonal_line = firstLineX;
		first_diagonal_line_left = firstLineX_Offset;
		dis_line = line_Spacing;
		w_line = line_Width;
	}


	/**
	 * 自定义绘制时间: Time = maxCount * drawSleepTime
	 * @param maxCount
	 * @param drawSleepTime
     */
	public void setCustomDrawTime(float maxCount,int drawSleepTime) {
		this.maxCount = maxCount;
		this.drawSleepTime = drawSleepTime;
	}

	/**
	 * 设置进度条搜当前值
	 * @param currentCount
     */
	public void setCurrentCount(float currentCount) {
		this.currentCount = currentCount > maxCount ? maxCount : currentCount;
		if(currentCount >= maxCount * 0.95 && isWaiting){
			try {
				Thread.sleep(200);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			setCurrentCount(currentCount);
			return;
		}
		if(currentCount >= maxCount){
//			onSeekBarComplete();
			isRecordingFinished = true;
			isRecording = false;
			for (ISpiralProgressListener lsr : spiralProgressListene) {
                lsr.onFinished(recordSuccess);
            }
		}
	}

//	public void onSeekBarComplete(){};

	/**
	 * 是否加载完成
	 * @return
     */
	public boolean isRecordingFinished(){
		return isRecordingFinished;
	}

	/**
	 * 是否处于加载中
	 * @return
     */
	public boolean isLoading(){
		return isRecording;
	}

	public float getMaxCount() {
		return maxCount;
	}

	public float getCurrentCount() {
		return currentCount;
	}

	private int i = 0;

	Runnable runnableBar = new Runnable() {
		@Override
		public void run() {
			while (i <= maxCount) {
				i++;
				setCurrentCount(i);
				try {
					Thread.sleep(drawSleepTime);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				postInvalidate();
			}
		}
	};

	/**
	 * 绘制进度条
	 */
	public void beginDrawWithAnimation(boolean flag){
		hasAnimation = flag;
		if(hasAnimation){
			isRecording = true;
			new Thread(runnableBar).start();	//动态绘制
		}else{
			postInvalidate();					//直接绘制
		}
	}

	  /**
     * dp 2 px
     *
     * @param dpVal
     */
    protected int dp2px(int dpVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dpVal, getResources().getDisplayMetrics());
    }

    /**
     * sp 2 px
     *
     * @param spVal
     * @return
     */
    protected int sp2px(int spVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,
                spVal, getResources().getDisplayMetrics());

    }

}
