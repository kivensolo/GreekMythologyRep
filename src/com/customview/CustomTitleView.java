package com.customview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.*;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import com.kingz.uiusingListViews.R;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

/**
 * Created by KingZ.
 * Data: 2016 2016/3/13
 * Discription:
 */
public class CustomTitleView extends View {

    public static final String TAG = CustomTitleView.class.getSimpleName();
    public static final int IMAGE_SCALE_MATCH = 0;
    public static final int IMAGE_SCALE_CENTER = 1;

    /**
     * 文本
     */
    private String mTitleText;
    /**
     * 文本的颜色
     */
    private int mTitleTextColor;
    /**
     * 文本的大小
     */
    private int mTitleTextSize;

    /**
     * 包裹文字的最小矩形
     */
    private Rect mBound;

    /**
     * Image图片
     */
    private Bitmap mImage;
     /**
     * 图片外部Rect
     */
    private Rect imageRect;
    /**
     * Pic Scale
     */
    private int mImageScale = -1;
    /**
     * 视图宽高
     */
    int width, height;

    private RectF mRectF;

    private Paint mPaint;


    /**
     * 3个构造方法，默认的布局文件调用的是两个参数的构造方法，
     * 让所有的构造调用三个参数的构造，
     * 在三个参数的构造中获得自定义属性
     *
     * @param context
     */
    public CustomTitleView(Context context) {
        this(context, null);
    }

    public CustomTitleView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    /**
     * 获得自定义的样式属性
     *
     * @param context
     * @param attrs
     * @param defStyle
     */
    public CustomTitleView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

//        this.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mTitleText = randomText();
//                postInvalidate();
//            }
//        });
        /**
         * 获得自定义的样式属性
         */
        TypedArray typedArray = context.getTheme().obtainStyledAttributes(attrs, R.styleable.testView, defStyle, 0);
       Log.d(TAG,"获取的 typeArray = " +  typedArray.toString());
        int typeCount = typedArray.getIndexCount();
        for (int i = 0; i < typeCount; i++) {
            int attr = typedArray.getIndex(i);
            switch (attr) {
                case R.styleable.testView_titleText:
                    Log.d(TAG,"GET titleText");
                    mTitleText = typedArray.getString(attr);
                    break;
                case R.styleable.testView_titleSize:
                    //Sise 默认设置为16sp，TypeValue也可以把sp转化为px
                    Log.d(TAG,"GET titleSize");
                    mTitleTextSize = typedArray.getDimensionPixelSize(attr,
                            (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 16, getResources().getDisplayMetrics()));
                    break;
                case R.styleable.testView_titleColor:
                    //color default is black
                     Log.d(TAG,"GET titleColor");
                    // 默认颜色设置为黑色
                    mTitleTextColor = typedArray.getColor(attr, Color.BLACK);
                    break;
                case R.styleable.testView_myImage:
                    Log.d(TAG,"GET image");
                    mImage = BitmapFactory.decodeResource(getResources(),typedArray.getResourceId(attr,0));
                    break;
                case R.styleable.testView_imageScaleType:
                    Log.d(TAG,"GET imageScaleType");
                    mImageScale = typedArray.getIndex(attr);
                    break;
            }
        }
        //注意回收
        typedArray.recycle();

        imageRect = new Rect();
        mPaint = new Paint();
        mPaint.setTextSize(mTitleTextSize);
        mPaint.setColor(mTitleTextColor);
        mBound = new Rect();
        //获取文字外围最小矩形
        mPaint.getTextBounds(mTitleText, 0, mTitleText.length(), mBound);
    }

    /*******************View的常用回调方法 ------ Start ****************/

    /**
     * 宽高是由父容器告之的，从外部ViewGroup传入，所以此处的两个参数,由
     * ViewGroup中的layout_width，layout_height和padding以及
     * View自身的layout_margin共同决定.权值weight也是尤其需要考虑的因素，
     * 有它的存在情况可能会稍微复杂点。
     * <p/>
     * <p/>
     * 所有的View的onMeasure()的最后一行都会调用setMeasureDimension()
     * 函数的作用,这个函数调用中传进去的值是View最终的视图大小。(可见TextView)
     * 也就是说onMeasure()中之前所作的所有工作都是为了最后这一句话服务的。
     *
     * @param widthMeasureSpec  是一个32位的int值，其中高两位为测量的模式，
     *                          低30位是测量的大小。采用位运算和运行效率有关。
     *                          <p/>
     *                          另一种说法：这个值由高32位和低16位组成，
     *                          高32位保存的值叫specMode，可以通过
     *                          MeasureSpec.getMode()获取；
     *                          低16位为specSize，
     *                          可以由MeasureSpec.getSize()获取。
     * @param heightMeasureSpec
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        Log.d(TAG,""+MeasureSpec.AT_MOST);
        //检测View组件及它所包含的所有子组件的大小
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
//        Log.d(TAG,"------", new Throwable());
         /**
         *  Get Width
         */
        int specSize = MeasureSpec.getSize(widthMeasureSpec);
        int specMode = MeasureSpec.getMode(widthMeasureSpec);
        if (specMode == MeasureSpec.EXACTLY) {
            Log.d(TAG, "---width EXACTLY---");
            width = specSize;
        } else {
            Log.d(TAG, "---width NOT EXACTLY---");
            mPaint.setTextSize(mTitleTextSize);
            mPaint.getTextBounds(mTitleText, 0, mTitleText.length(), mBound);
            //文本的宽度
            float textWidth = mBound.width();
            //实际宽度
            width = (int) (getPaddingLeft() + textWidth + getPaddingRight());
            Log.d(TAG, "width text = " + width);

            //int widthByImg = mImage.getWidth();
            //int widthByTitle = mBound.width();
            //if(specMode == MeasureSpec.AT_MOST){//wrao_content
            //    int maxWidth = Math.max(widthByImg,widthByTitle);
            //    width = Math.min(maxWidth,specSize);
            //    Log.d(TAG, "width final = " + width);
            //}
        }

        /**
         *  Get Height
         */
        specSize = MeasureSpec.getSize(heightMeasureSpec);
        specMode = MeasureSpec.getMode(heightMeasureSpec);
        if (specMode == MeasureSpec.EXACTLY) {
            Log.d(TAG, "---height EXACTLY---");
            height = specSize;
        } else {
            Log.d(TAG, "---height NOT EXACTLY---");
            mPaint.setTextSize(mTitleTextSize);
            mPaint.getTextBounds(mTitleText, 0, mTitleText.length(), mBound);
            //文本的宽度
            float textheight = mBound.height();
            //实际宽度
            height = (int) (getPaddingLeft() + textheight + getPaddingRight());
            Log.d(TAG, "height = " + height);
        }
        setMeasuredDimension(400, 300);
    }

    /**
     * 组件需要绘制内容的时候回调
     * 注意： onDraw的时候不能new对象哟
     * @param canvas
     */
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        drawBorder(canvas);
        drawImageMatchRect();


        mPaint.setColor(mTitleTextColor);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setShadowLayer(10, 15, 15, Color.RED);    //设置阴影
        /**
         *  文本宽度超过View宽度
         */
        if(mBound.width() > width){
            TextPaint paint = new TextPaint(mPaint);
            String msg = TextUtils.ellipsize(mTitleText,paint,
                    width-getPaddingLeft()-getPaddingRight(),
                    TextUtils.TruncateAt.END).toString();
            canvas.drawText(msg, getPaddingLeft(), height - getPaddingBottom(), mPaint);
        }else{
            canvas.drawText(mTitleText, getWidth() / 2 - mBound.width() / 2, getHeight() / 2 + mBound.height() / 2, mPaint);
        }

        if(mImageScale ==  IMAGE_SCALE_MATCH){
            Log.d(TAG,"mImageScale == IMAGE_SCALE_MATCH");
             canvas.drawBitmap(mImage,null,imageRect,mPaint);
        }else{
            Log.d(TAG,"mImageScale == IMAGE_SCALE_CENTER");
            //计算居中的矩形范围
            imageRect.left = width / 2 - mImage.getWidth() / 2;
            imageRect.right = width / 2 + mImage.getWidth() / 2;
            imageRect.top = (height - mBound.height()) / 2 - mImage.getHeight() / 2;
            imageRect.bottom = (height - mBound.height()) / 2 + mImage.getHeight() / 2;
            canvas.drawBitmap(mImage, null, imageRect, mPaint);
        }
//        setBackgroundColor(Color.YELLOW);

    }


    @Override
    protected void onFinishInflate() {
        //当应用从XML布局文件在组建并利用他来构建页面之后，该方法将会被回调
        super.onFinishInflate();
        Log.d(TAG, "onFinishInflate()");
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        //当该组件需要分配其自组件的位置、大小时，该方法就会被回调
        super.onLayout(changed, left, top, right, bottom);
        Log.d(TAG, "onLayout()  isChanged:" + changed + ";left=" + left + ";top" + top + ";right:" + right + ";bottom:" + bottom);
    }
    //onDraw()

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        //当该组件的大小被改变时回调该方法
        super.onSizeChanged(w, h, oldw, oldh);
        Log.d(TAG, "onSizeChanged()  宽：" + w + "/ 高：" + h + "/ 旧的宽：" + oldw + "/旧的高：" + oldh);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        return super.onKeyUp(keyCode, event);
    }

    @Override
    public boolean onTrackballEvent(MotionEvent event) {
        //发生轨迹球事件的时候出发该方法
        return super.onTrackballEvent(event);
    }

    @Override
    public void onWindowFocusChanged(boolean hasWindowFocus) {
        //当组件得到、失去焦点的时候触发
        super.onWindowFocusChanged(hasWindowFocus);
    }

    /**
     * 当此view附加到窗体上时调用该方法，在这时，view有了一个用于显示的Surface，
     * 将开始绘制。【注意】此方法要保证在调用onDraw(Canvas) 之前调用（View还没画出来的时候）
     * 但可能在调用 onDraw(Canvas) 之前的任何时刻，
     * 包括调用 onMeasure(int, int) 之前或之后。
     * 如：google的AlarmClock动态时钟View就是在这个方法中进行广播的注册。
     * 详见：http://blog.csdn.net/eyu8874521/article/details/8493995
     */
    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        Log.i(TAG,"--------onAttachedToWindow()--------");
    }

    /**
     * 将视图从窗体上分离的时候调用该方法。这时视图已经不具有可绘制部分。
     * 在destroy view的时候调用.所以可以加入取消广播注册等的操作。
     * 见谷歌的闹钟代码。
     */
    @Override
    protected void onDetachedFromWindow() {
        //把该组件脱离某个窗口的时候触发
        super.onDetachedFromWindow();
    }

    @Override
    protected void onWindowVisibilityChanged(int visibility) {
        //当包含该组件的窗口的可见性发生改变时触发该方法
        super.onWindowVisibilityChanged(visibility);
    }
    /*******************View的常用回调方法 ------ End****************/

    /**
     * 生成随机数
     * @return
     */
    private String randomText() {
        Random random = new Random();
        Set<Integer> set = new HashSet<>();
        while (set.size() < 4) {
            int randomInt = random.nextInt(10);
            set.add(randomInt);
        }
        StringBuilder sb = new StringBuilder();
        for (Integer i : set) {
            sb.append("").append(i);
        }
        return sb.toString();
    }
     /**
     * 绘制图片外部矩形
     */
    private void drawImageMatchRect() {
        imageRect.left = getPaddingLeft();
        imageRect.right = width-getPaddingLeft();
        imageRect.top = getPaddingTop();
        imageRect.bottom = height - getPaddingBottom();
    }

    /**
     * 绘制View边框
     */
    private void drawBorder(Canvas canvas) {
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(5);
        mPaint.setColor(Color.CYAN);
        //绘制矩形的宽高通过 getMeasuredXXXX()获取
        canvas.drawRect(0, 0, getMeasuredWidth(), getMeasuredHeight(), mPaint);
    }


}
