package com.zeke.demo.draw

import android.content.Context
import android.graphics.*
import android.text.TextPaint
import android.text.TextUtils
import android.util.AttributeSet
import android.util.Log
import android.util.TypedValue
import android.view.KeyEvent
import android.view.MotionEvent
import android.view.View
import com.module.tools.ViewUtils
import com.zeke.demo.R
import java.util.*

/**
 * Created by KingZ.
 * Data: 2016 2016/3/13
 * Discription: 自定义View的onMeasure计算
 */
class CustomOnMeasureView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : View(context, attrs, defStyle) {

    /** 文本 */
    private var mTitleText: String? = null

    /** 文本的颜色 */
    private var mTitleTextColor = 0

    /** 文本的大小 */
    private var mTitleTextSize = 0
    private var mTextBoundRect: Rect? = null
    private var borderPaint: Paint? = null
    private var textPaint: Paint? = null

    private lateinit var mBitmapImg: Bitmap
    private var mBitmapImgW = 150f
    private var mBitmapImgH = 200f
    private var imagePaint: Paint? = null
    /** 图片外部Rect */
    private var imageRect: Rect? = null

    /** Pic Scale */
    private var mImageScale = -1

    /** 视图宽高 */
    var mWidth = 0
    var mHeight = 0
    private val mRectF: RectF? = null

    /**
     * 获得自定义的样式属性
     */
    init {
        getCustomArray(context, attrs, defStyle)
        mBitmapImg = Bitmap.createBitmap(10, 10, Bitmap.Config.ARGB_8888)
        initPaints()
    }

    private fun initPaints() {
        imageRect = Rect()
        borderPaint = Paint()
        textPaint = Paint()
        imagePaint = Paint()
        mTextBoundRect = Rect()
        textPaint!!.textSize = mTitleTextSize.toFloat()
        textPaint!!.color = mTitleTextColor
        //获取文字外围最小矩形
        textPaint!!.getTextBounds(mTitleText, 0, mTitleText!!.length, mTextBoundRect)
    }

    /**
     * 获得自定义的样式属性
     */
    private fun getCustomArray(context: Context, attrs: AttributeSet?, defStyle: Int) {
        val typedArray = context.theme.obtainStyledAttributes(attrs, R.styleable.testView, defStyle, 0)
        val typeCount = typedArray.indexCount
        for (i in 0 until typeCount) {
            when (val attr = typedArray.getIndex(i)) {
                R.styleable.testView_titleText -> {
                    Log.d(TAG, "Get titleText")
                    mTitleText = typedArray.getString(attr)
                }
                R.styleable.testView_titleSize -> {
                    //Sise 默认设置为16sp，TypeValue可以把sp转化为px
                    Log.d(TAG, "Get titleSize")
                    mTitleTextSize = typedArray.getDimensionPixelSize(attr,
                            TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 16f, resources.displayMetrics).toInt())
                }
                R.styleable.testView_titleColor -> {
                    //color default is black
                    Log.d(TAG, "Get titleColor")
                    // 默认颜色设置为黑色
                    mTitleTextColor = typedArray.getColor(attr, Color.RED)
                }
                R.styleable.testView_myImage -> {
                    Log.d(TAG, "Get image")
                    mBitmapImg = BitmapFactory.decodeResource(resources, typedArray.getResourceId(attr, 0))
                }
                R.styleable.testView_imageScaleType -> {
                    Log.d(TAG, "Get imageScaleType")
                    mImageScale = typedArray.getIndex(attr)
                }
                R.styleable.testView_imageHeight -> mBitmapImgH = typedArray.getDimensionPixelSize(attr, 150).toFloat()
                R.styleable.testView_imageWidth -> mBitmapImgW = typedArray.getDimensionPixelSize(attr, 200).toFloat()
            }
        }
        //注意回收
        typedArray.recycle()
    }
    /*******************View的常用回调方法 ------ Start  */
    /**
     * 宽高是由父容器告之的，从外部ViewGroup传入，所以此处的两个参数,由
     * ViewGroup中的layout_width，layout_height和padding
     * 以及View自身的layout_margin共同决定.
     * 权值weight也是尤其需要考虑的因素，
     * 有它的存在情况可能会稍微复杂点。
     *
     *
     *
     *
     * 所有的View的onMeasure()的最后一行都会调用setMeasureDimension()
     * 函数的作用,这个函数调用中传进去的值是View最终的视图大小。(可见TextView)
     * 也就是说onMeasure()中之前所作的所有工作都是为了最后这一句话服务的。
     *
     * MeasureSpec :
     * 是一个32位的int值，其中高两位为测量的模式，
     * 低30位是测量的大小。采用位运算和运行效率有关。
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     */
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        //检测View组件及它所包含的所有子组件的大小
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        // Get Width
        var specMode = MeasureSpec.getMode(widthMeasureSpec)
        var specSize = MeasureSpec.getSize(widthMeasureSpec)
        if (specMode == MeasureSpec.EXACTLY) {
            // 父节点已经为子节点确定了一个精确的尺寸。不管孩子想要多大，孩子都会得到这些界限。
            mWidth = specSize
        } else {
            Log.d(TAG, "---mWidth NOT EXACTLY---")
            textPaint!!.textSize = mTitleTextSize.toFloat()
            textPaint!!.getTextBounds(mTitleText, 0, mTitleText!!.length, mTextBoundRect)
            val textWidth = mTextBoundRect!!.width().toFloat()
            mWidth = (paddingLeft + textWidth + paddingRight).toInt()
        }

        // Get height
        specSize = MeasureSpec.getSize(heightMeasureSpec)
        specMode = MeasureSpec.getMode(heightMeasureSpec)
        if (specMode == MeasureSpec.EXACTLY) {
            Log.d(TAG, "---mHeight EXACTLY---")
            mHeight = specSize
        } else {
            Log.d(TAG, "---mHeight NOT EXACTLY---")
            textPaint!!.textSize = mTitleTextSize.toFloat()
            textPaint!!.getTextBounds(mTitleText, 0, mTitleText!!.length, mTextBoundRect)
            val textheight = mTextBoundRect!!.height().toFloat()
            mHeight = (paddingBottom + textheight + paddingTop).toInt()
        }
        setMeasuredDimension(mWidth, mHeight)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        ViewUtils.setViewBorder(this, canvas, borderPaint)
        drawImageMatchRect()
        setShadowLayer()
        drawImage(canvas)
        drawText(canvas)
        background = resources.getDrawable(R.drawable.bkg2)
    }

    private fun drawText(canvas: Canvas) {
        if (mTextBoundRect!!.width() > mWidth) {
            val paint = TextPaint(textPaint)
            val msg = TextUtils.ellipsize(mTitleText, paint,
                    mWidth - paddingLeft - paddingRight.toFloat(),
                    TextUtils.TruncateAt.END).toString()
            canvas.drawText(msg, paddingLeft.toFloat(), mHeight + paddingBottom.toFloat(), textPaint)
        } else {
            canvas.drawText(mTitleText, width / 2 - mTextBoundRect!!.width() / 2.toFloat(), height - 20.toFloat(), textPaint)
        }
    }

    private fun drawImage(canvas: Canvas) {
        if (mImageScale == IMAGE_SCALE_MATCH) {
            Log.d(TAG, "ImageType is Match")
            canvas.drawBitmap(mBitmapImg, null, imageRect, imagePaint)
        } else if (mImageScale == IMAGE_SCALE_CENTER) {
            Log.d(TAG, "ImageType is Center")
            //计算居中的矩形范围
            //            imageRect.left = mWidth / 2 - mBitmapImg.getWidth() / 2 - 3;
            //            imageRect.right = mWidth / 2 + mBitmapImg.getWidth() / 2 + 3;
            imageRect!!.left = (mWidth / 2 - mBitmapImgW / 2).toInt()
            imageRect!!.right = (mWidth / 2 + mBitmapImgW / 2).toInt()

            //            imageRect.top = (mHeight - mTextBoundRect.height()) / 2 - mBitmapImg.getHeight() / 2;
            //            imageRect.bottom = (mHeight - mTextBoundRect.height()) / 2 + mBitmapImg.getHeight() / 2;
            imageRect!!.top = (mHeight / 2 - mBitmapImgH / 2).toInt()
            imageRect!!.bottom = (mHeight / 2 + mBitmapImgH / 2).toInt()
            /**
             * @param bitmap The bitmap to be drawn
             * @param src    May be null. The subset of the bitmap to be drawn
             * @param dst    The rectangle that the bitmap will be scaled/translated
             * to fit into
             * @param paint  May be null. The paint used to draw the bitmap
             */
            canvas.drawBitmap(mBitmapImg, null, imageRect, imagePaint)
        }
    }

    /**
     * 显示投影
     */
    private fun setShadowLayer() {
        textPaint!!.color = mTitleTextColor
        textPaint!!.style = Paint.Style.FILL
        //float radius（blur radius，为0则无投影）, float dx, float dy, int color
        textPaint!!.setShadowLayer(2f, 35f, 10f, resources.getColor(R.color.darkslategray)) //设置阴影
    }

    override fun onFinishInflate() {
        //当应用从XML布局文件在组建并利用他来构建页面之后，该方法将会被回调
        super.onFinishInflate()
        Log.d(TAG, "onFinishInflate()")
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        //当该组件需要分配其自组件的位置、大小时，该方法就会被回调
        super.onLayout(changed, left, top, right, bottom)
        Log.d(TAG,
                "onLayout()  isChanged:$changed;left=$left;top$top;right:$right;bottom:$bottom")
    }

    //onDraw()
    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        //当该组件的大小被改变时回调该方法
        super.onSizeChanged(w, h, oldw, oldh)
        Log.d(TAG, "onSizeChanged()  宽：$w/ 高：$h/ 旧的宽：$oldw/旧的高：$oldh")
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        return super.onKeyDown(keyCode, event)
    }

    override fun onKeyUp(keyCode: Int, event: KeyEvent): Boolean {
        return super.onKeyUp(keyCode, event)
    }

    override fun onTrackballEvent(event: MotionEvent): Boolean {
        //发生轨迹球事件的时候出发该方法
        return super.onTrackballEvent(event)
    }

    override fun onWindowFocusChanged(hasWindowFocus: Boolean) {
        //当组件得到、失去焦点的时候触发
        super.onWindowFocusChanged(hasWindowFocus)
    }

    /**
     * 当此view附加到窗体上时调用该方法，在这时，view有了一个用于显示的Surface，
     * 将开始绘制。【注意】此方法要保证在调用onDraw(Canvas) 之前调用（View还没画出来的时候）
     * 但可能在调用 onDraw(Canvas) 之前的任何时刻，
     * 包括调用 onMeasure(int, int) 之前或之后。
     * 如：google的AlarmClock动态时钟View就是在这个方法中进行广播的注册。
     * 详见：http://blog.csdn.net/eyu8874521/article/details/8493995
     */
    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        Log.i(TAG, "--------onAttachedToWindow()--------")
    }

    /**
     * 将视图从窗体上分离的时候调用该方法。这时视图已经不具有可绘制部分。
     * 在destroy view的时候调用.所以可以加入取消广播注册等的操作。
     * 见谷歌的闹钟代码。
     */
    override fun onDetachedFromWindow() {
        //把该组件脱离某个窗口的时候触发
        super.onDetachedFromWindow()
    }

    override fun onWindowVisibilityChanged(visibility: Int) {
        //当包含该组件的窗口的可见性发生改变时触发该方法
        super.onWindowVisibilityChanged(visibility)
    }
    /*******************View的常用回调方法 ------ End */
    /**
     * 生成随机数
     * @return
     */
    private fun randomText(): String {
        val random = Random()
        val set: MutableSet<Int> = HashSet()
        while (set.size < 4) {
            val randomInt = random.nextInt(10)
            set.add(randomInt)
        }
        val sb = StringBuilder()
        for (i in set) {
            sb.append("").append(i)
        }
        return sb.toString()
    }

    /**
     * 绘制图片外部矩形
     */
    private fun drawImageMatchRect() {
        imageRect!!.left = paddingLeft
        imageRect!!.right = mWidth - paddingLeft
        imageRect!!.top = paddingTop
        imageRect!!.bottom = mHeight - paddingBottom
        Log.i(TAG, "drawImageMatchRect() Left:" + imageRect!!.left + "; Right:" + imageRect!!.right
                + "Top：" + imageRect!!.top + "Buttom:" + imageRect!!.bottom)
    }

    companion object {
        val TAG = CustomOnMeasureView::class.java.simpleName
        const val IMAGE_SCALE_MATCH = 0
        const val IMAGE_SCALE_CENTER = 1
    }

}