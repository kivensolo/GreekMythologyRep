package com.view.views;

import android.content.Context;
import android.graphics.*;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.TextView;
import com.App;
import com.kingz.customdemo.R;
import com.utils.ScreenTools;
import com.utils.ZLog;

/**
 * Copyright(C) 2016, 北京视达科科技有限公司
 * All rights reserved. <br>
 * author: King.Z <br>
 * date:  2017/5/26 23:12 <br>
 */
public class ColorMartixImageView extends ViewGroup {
    private static final String TAG = "ColorMartixImageView";
    private int viewWidth;
    private int viewHeight;
    private int seekBarHeightDec;
    public static final int SEEKBAR_NUMS = 4;
    private ColorMatrix mColorMatrix;
    private ColorFilter mColorFilter;
    //控制颜矩阵
    private int[] colorArray;
    //图片绘制画笔
    private Paint bitMapPaint;
    //控制进度条
    private SeekBar[] mSeekBar = new SeekBar[SEEKBAR_NUMS];
    //显示矩阵参数
    private TextView maluesView;
    private Bitmap mBitmap;
    private RectF mRect;
    private RectF mTargetRect;

    public float mRedFilter = 1f;
    public float mGreenFilter = 1f;
    public float mBlueFilter = 1f;
    public float mAlphaFilter = 1f;
    private final float[] mArray = new float[20];
    ViewGroup.LayoutParams lps;

    public ColorMartixImageView(Context context) {
        this(context, null);
    }

    public ColorMartixImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        ZLog.d(TAG, "SampleView()");
        setBackgroundColor(context.getResources().getColor(R.color.theme_100));
        viewWidth = App.SCREEN_WIDTH;
        viewHeight = App.SCREEN_HEIGHT;

        lps = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        for (int i=0;i<mSeekBar.length;i++){
            mSeekBar[i] = new SeekBar(context);
            mSeekBar[i].setProgress(100);
            mSeekBar[i].setMax(100);
            mSeekBar[i].setLayoutParams(lps);
        }
        bitMapPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        BitmapFactory.Options bmpOption = new BitmapFactory.Options();
        bmpOption.inPreferredConfig = Bitmap.Config.ARGB_8888;//也是默认值
        mBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.marvel_qiyi, bmpOption);

        mTargetRect = new RectF(ScreenTools.Operation(20), ScreenTools.Operation(20),
                viewWidth - ScreenTools.Operation(20), ScreenTools.Operation(420));
        int bw = mBitmap.getWidth();
        int bh = mBitmap.getHeight();
        mRect = new RectF(0, 0, bw, bh);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        ZLog.d(TAG, "onMeasure");
        setMeasuredDimension(viewWidth, ScreenTools.Operation(600));
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
    }

    @Override
    protected void onDraw(Canvas canvas) {
        ZLog.d(TAG, "onDraw");
        super.onDraw(canvas);
        canvas.drawBitmap(mBitmap, null, mTargetRect, bitMapPaint);
        mColorFilter = bitMapPaint.getColorFilter();
        if(mColorFilter instanceof ColorMatrixColorFilter){
            //((ColorMatrixColorFilter)mColorFilter).
        }
        for (SeekBar item:mSeekBar){
            if(item != null){
                attachViewToParent(item,0,lps);
            }
        }
    }

    /**
     * [ 1 0 0 0 0   - red vector
     *   0 1 0 0 0   - green vector
     *   0 0 1 0 0   - blue vector
     *   0 0 0 1 0 ] - alpha vector
     */
    public void setArgb(float alpha, float red, float green, float blue) {
        mRedFilter = red;
        mGreenFilter = green;
        mBlueFilter = blue;
        mAlphaFilter = alpha;
        mColorMatrix = new ColorMatrix(new float[]{
                                        mRedFilter, 0, 0, 0, 0,
                                        0, mGreenFilter, 0, 0, 0,
                                        0, 0, mBlueFilter, 0, 0,
                                        0, 0, 0, mAlphaFilter, 0,
                                      });
        bitMapPaint.setColorFilter(new ColorMatrixColorFilter(mColorMatrix));
        postInvalidate();
    }

    public void printColorMatrix(Canvas canvas){
    }
}
