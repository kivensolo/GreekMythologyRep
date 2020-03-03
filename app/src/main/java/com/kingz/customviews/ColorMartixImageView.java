package com.kingz.customviews;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;

import com.kingz.customdemo.R;
import com.module.tools.ScreenTools;
import com.zeke.kangaroo.utils.ZLog;
import com.zeke.ktx.App;

/**
 * Copyright(C) 2016, 北京视达科科技有限公司
 * All rights reserved. <br>
 * author: King.Z <br>
 * date:  2017/5/26 23:12 <br>
 *      * description: ColorMatrix的使用 <br>
 * setARGB (int a, int r, int g, int b):用于设置画笔颜色，A 代表 alpha（透明度），R 代表Red （红色），G 代表 Green（绿色），B 代表 Blue（蓝色）
 * 色值采用16进制，取值在 0 － 255 之间 ，0（0x00） 即 完全没有 ，255(0xff) 代表满值 ;
 * setAlpha(int a): 用于设置Paint 的透明度；
 * setColor(int color):同样设置颜色，如果是常用色，可以使用Color 类中定义好的一些色值 ，eg：Color.WHITE
 * setColorFilter(ColorFilter filter):设置颜色过滤器,可以通过颜色过滤器过滤掉对应的色值，比如去掉照片颜色，生成老照片效果；
 * ColorFilter有以下几个子类可用:
 *  ColorMatrixColorFilter
 *  LightingColorFilter
 *  PorterDuffColorFilter
 * <p>
 * 在Android中，图片是以一个个 RGBA 的像素点的形式加载到内存中的，所以如果需要改变图片的颜色，
 * 就需要针对这一个个像素点的RGBA的值进行修改，其实主要是RGB，A是透明度；
 * 修改图片 RGBA 的值需要ColorMatrix类的支持，它定义了一个 4*5 的float[]类型的矩阵，矩阵中每一行表示 RGBA 中的一个参数。
 * 颜色矩阵M是以一维数组的方式进行存储的
 * m=[a,b,c,d,e,  ----- 表示三原色中的红色
 *    f,g,h,i,j,  ----- 表示三原色中的绿色
 *    k,l,m,n,o,  ----- 表示三原色中的蓝色
 *    p,q,r,s,t]  ----- 表示颜色的透明度
 * 第五列用于表示颜色的偏移量
 * <p>
 * 而对于一张图像来说，展示的颜色效果取决于图像的RGBA（红色、绿色、蓝色、透明度）值。
 * 而图像的 RGBA 值则存储在一个5*1的颜色分量矩阵C中，由颜色分量矩阵C可以控制图像的颜色效果。
 * 颜色分量矩阵为：
 * |R|
 * |G|
 * c=|B|
 * |A|
 * |1|
 * 利用ColorFilter 和 ColorMatrixColorFilter类 和 Paint 的setColorFilter
 * 就可以改变图片的展示效果（颜色，饱和度，对比度等）
 *
 *
 * 参考文章:http://www.tuicool.com/articles/yYvEn2q
 */
//TODO 矩阵信息输出还需要优化，控件的布局还需要优化
public class ColorMartixImageView extends ViewGroup {
    private static final String TAG = "ColorMartixImageView";
    public static final int SEEKBAR_NUMS = 4;
    private int viewWidth;
    private int viewHeight;
    private ColorMatrix mColorMatrix;
    public String mMatrixValues = "default";
    private float[] mArray = new float[20];    //颜色矩阵
    private String[] charArry = new String[]{"R","G","B","A"};
    private Paint bitMapPaint;    //图片绘制画笔
    //Canvas绘制换行  不能使用paint的"/n"或者"/r/n"
    private TextPaint matrixValuePaint;
    private StaticLayout textLayout;
    private SeekBar[] mSeekBar;    //控制进度条
    private Bitmap mBitmap;
    private RectF mTargetRect;

    public float mRedFilter = 1f;
    public float mGreenFilter = 1f;
    public float mBlueFilter = 1f;
    public float mAlphaFilter = 1f;
    ViewGroup.LayoutParams lps;
    private Context mContext;

    public ColorMartixImageView(Context context) {
        this(context, null);
    }

    public ColorMartixImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        ZLog.d(TAG, "SampleView()");
        setBackgroundColor(context.getResources().getColor(R.color.theme_100));
        viewWidth = App.SCREEN_WIDTH;
        viewHeight = App.SCREEN_HEIGHT;

        lps = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        setNumberOfSeekBar(context,SEEKBAR_NUMS);

        matrixValuePaint = new TextPaint();
        matrixValuePaint.setStrokeWidth(2);
        matrixValuePaint.setColor(Color.RED);
        matrixValuePaint.setTextSize(32f);

        bitMapPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        BitmapFactory.Options bmpOption = new BitmapFactory.Options();
        bmpOption.inPreferredConfig = Bitmap.Config.ARGB_8888;//也是默认值
        mBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.marvel_qiyi, bmpOption);

        mTargetRect = new RectF(ScreenTools.Operation(20), ScreenTools.Operation(20),
                                viewWidth - ScreenTools.Operation(20), ScreenTools.Operation(420));
        int bw = mBitmap.getWidth();
        int bh = mBitmap.getHeight();

        setArgb(mAlphaFilter,mRedFilter,mGreenFilter,mBlueFilter);

    }

    private void setNumberOfSeekBar(Context context,int num) {
        mSeekBar = new SeekBar[num];
        for (int i=0; i < num;i++){
            SeekBar seekBar = new SeekBar(context);
            seekBar.setProgress(100);
            seekBar.setMax(100);
            seekBar.setLayoutParams(lps);
            addView(seekBar,i);
            seekBar.setOnSeekBarChangeListener(seekBarChange);
            seekBar.setTag(charArry[i]);
            mSeekBar[i] = seekBar;
        }
    }

    //@Override
    //public LayoutParams generateLayoutParams(AttributeSet attrs) {
    //    ZLog.d(TAG, "generateLayoutParams");
    //    return new MarginLayoutParams(mContext, attrs);
    //}

    /**
     * 计算childView的测量值以及模式，以及设置自己的宽和高
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        ZLog.d(TAG, "onMeasure");
        //计算出所有的childView的宽高
        measureChildren(widthMeasureSpec, heightMeasureSpec);
        int width = 0;
        int height = 0;
        int cCount = getChildCount();
        int cWidth = 0;
        int cHeight = 0;
        //MarginLayoutParams cLayouP;
        LayoutParams cLayouP;
        ZLog.d(TAG, "onMeasure  childsCount="+cCount);
        for(int i=0;i < cCount;i++){
            View childView = getChildAt(i);
            cWidth = childView.getMeasuredWidth();
            cHeight = childView.getMeasuredHeight();
            cLayouP = childView.getLayoutParams();
            //if(i==0){
            //    cLayouP.topMargin = ScreenTools.Operation(450);
            //}else{
            //    cLayouP.topMargin = ScreenTools.Operation(20);
            //}
            ZLog.d(TAG, "onMeasure  cWidth="+cWidth+";cHeight="+cHeight);
        }
        setMeasuredDimension(viewWidth, ScreenTools.Operation(600));
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    /**
     * onLayout对其所有childView进行定位（设置childView的绘制区域）
     */
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        ZLog.d(TAG, "onLayout");
        //MarginLayoutParams mLayoutPms = new MarginLayoutParams(LayoutParams.MATCH_PARENT,MarginLayoutParams.WRAP_CONTENT);
        //mLayoutPms.setMargins(0,ScreenTools.Operation(20),0,0);
        int y = ScreenTools.Operation(460);
        for (int i = 0; i < getChildCount(); i++) {
            View childView = getChildAt(i);
            childView.setX(0);
            childView.setY(y);
            childView.setLayoutParams(lps);
            childView.layout(0,y,viewWidth,y + ScreenTools.Operation(70));
            y += ScreenTools.Operation(80);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        ZLog.d(TAG, "onDraw");
        super.onDraw(canvas);
        canvas.drawBitmap(mBitmap, null, mTargetRect, bitMapPaint);
        //canvas.drawText(mMatrixValues,mTargetRect.left,mTargetRect.bottom,matrixValuePaint);
        canvas.save();
        canvas.translate(mTargetRect.left,mTargetRect.bottom);
        textLayout.draw(canvas);
        canvas.restore();
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
        mArray = mColorMatrix.getArray();
        mMatrixValues = colorMatrixToString(mArray);
        textLayout = new StaticLayout(mMatrixValues,matrixValuePaint,ScreenTools.OperationWidth(400), Layout.Alignment.ALIGN_NORMAL,1.0f, 0.0f, true);
    }

    private SeekBar.OnSeekBarChangeListener seekBarChange = new SeekBar.OnSeekBarChangeListener() {

        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            float filter = (float) progress / 100;
            if(seekBar.getTag().equals("R")){
                mRedFilter = filter;
            }else if(seekBar.getTag().equals("G")){
                mGreenFilter = filter;
            }else if(seekBar.getTag().equals("B")){
                mBlueFilter = filter;
            }else if(seekBar.getTag().equals("A")){
                mAlphaFilter = filter;
            }
            setArgb(mAlphaFilter, mRedFilter,mGreenFilter,mBlueFilter);
            postInvalidate();
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {

        }
    };

    public String colorMatrixToString(float[] array){
         StringBuilder sb = new StringBuilder(128);
        sb.append("Matrix{");
        toShortString(sb,array);
        sb.append('}');
        return sb.toString();
    }
     private void toShortString(StringBuilder sb,float[] values) {
        sb.append('[');
        sb.append(values[0]); sb.append(", "); sb.append(values[1]); sb.append(", ");
        sb.append(values[2]); sb.append(", ");sb.append(values[3]);sb.append(", ");
        sb.append(values[4]); sb.append(", ");sb.append("]");
        sb.append("[");
        sb.append(values[5]); sb.append(", "); sb.append(values[6]); sb.append(", ");
        sb.append(values[7]);sb.append(", ");sb.append(values[8]); sb.append(", ");
        sb.append(values[9]);sb.append("]");
        sb.append("[");
        sb.append(values[10]); sb.append(", ");sb.append(values[11]);sb.append(", ");
        sb.append(values[12]);sb.append(", ");sb.append(values[13]); sb.append(", ");
        sb.append(values[14]);sb.append("]");
        sb.append("[");
        sb.append(values[15]); sb.append(", "); sb.append(values[16]); sb.append(", ");
        sb.append(values[17]); sb.append(", ");sb.append(values[18]); sb.append(", ");
        sb.append(values[19]); sb.append(']');
    }

}
