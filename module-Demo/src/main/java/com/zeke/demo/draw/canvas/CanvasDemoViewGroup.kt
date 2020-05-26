package com.zeke.demo.draw.canvas

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.ViewGroup
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import com.module.tools.ScreenTools
import com.zeke.demo.R
import com.zeke.kangaroo.utils.ScreenDisplayUtils
import com.zeke.kangaroo.utils.ZLog

/**
 * author: King.Z <br></br>
 * date:  2017/5/26 23:12 <br></br>
 */
class CanvasDemoViewGroup @JvmOverloads
constructor(context: Context, attrs: AttributeSet? = null) :
        ViewGroup(context, attrs) {
    private val viewWidth: Int
    private val viewHeight: Int
    private val charArray = arrayOf("T", "R", "S", "SK")
    private val bitMapPaint : Paint
    private lateinit var mSeekBar : Array<SeekBar?>//控制进度条
    private val mBitmap: Bitmap
    private val dstRect: RectF

    private var mTranslate = 0f
    private var mRotate = 0f
    private var mScale = 0f
    private var mSkew = 0f
    var mLayoutParams: LayoutParams

    companion object {
        private const val TAG = "CanvasDemoViewGroup"
        const val SEEKBAR_NUMS = 4
    }

    init {
        ZLog.d(TAG, "CanvasDemoViewGroup")
        // setBackgroundColor(context.resources.getColor(R.color.red))
        viewWidth = ScreenDisplayUtils.getScreenWidth(context)
        viewHeight = ScreenDisplayUtils.getScreenHeight(context)
        mLayoutParams = LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT)
        setNumberOfSeekBar(context)
        bitMapPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        bitMapPaint.color = Color.RED
        bitMapPaint.style = Paint.Style.FILL
        bitMapPaint.strokeWidth = 2f

        val bmpOption = BitmapFactory.Options()
        bmpOption.inPreferredConfig = Bitmap.Config.RGB_565 //8888是默认值
        mBitmap = BitmapFactory.decodeResource(resources, R.drawable.marvel_qiyi, bmpOption)
        dstRect = RectF(20f, 20f, (viewWidth - 20).toFloat(), 420f)
    }

    private fun setNumberOfSeekBar(context: Context) {
        mSeekBar = arrayOfNulls(SEEKBAR_NUMS)
        for (i in 0 until SEEKBAR_NUMS) {
            val seekBar = SeekBar(context)
            seekBar.progress = 0
            seekBar.max = 360
            seekBar.layoutParams = mLayoutParams
            addView(seekBar, i) // 添加到ViewGroup中
            seekBar.setOnSeekBarChangeListener(seekBarChange)
            seekBar.tag = charArray[i]
            mSeekBar[i] = seekBar
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
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        ZLog.d(TAG, "onMeasure")
        //计算出所有的childView的宽高
        measureChildren(widthMeasureSpec, heightMeasureSpec)
        val cCount = childCount
        var cWidth = 0
        var cHeight = 0
        //MarginLayoutParams cLayouP;
        var cLayouP: LayoutParams
        ZLog.d(TAG, "onMeasure  childsCount=$cCount")
        for (i in 0 until cCount) {
            val childView = getChildAt(i)
            cWidth = childView.measuredWidth
            cHeight = childView.measuredHeight
            cLayouP = childView.layoutParams
            //if(i==0){
            //    cLayouP.topMargin = ScreenTools.Operation(450);
            //}else{
            //    cLayouP.topMargin = ScreenTools.Operation(20);
            //}
            ZLog.d(TAG, "onMeasure  cWidth=$cWidth;cHeight=$cHeight")
        }
        setMeasuredDimension(viewWidth, ScreenTools.Operation(800))
    }

    /**
     * onLayout对其所有childView进行定位（设置childView的绘制区域）
     */
    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        ZLog.d(TAG, "onLayout")
        //MarginLayoutParams mLayoutPms = new MarginLayoutParams(LayoutParams.MATCH_PARENT,MarginLayoutParams.WRAP_CONTENT);
        //mLayoutPms.setMargins(0,ScreenTools.Operation(20),0,0);
        var y = 460
        for (i in 0 until childCount) {
            val childView = getChildAt(i)
            childView.x = 0f
            childView.y = y.toFloat()
            childView.layoutParams = mLayoutParams
            childView.layout(0, y, viewWidth, y + 70)
            y += 80
        }
    }

    override fun onDraw(canvas: Canvas) {
        ZLog.d(TAG,"onDrawonDrawonDraw")
        super.onDraw(canvas)
        //TODO 为啥不绘制Bitmap呢？
        canvas.drawBitmap(mBitmap, null, dstRect, bitMapPaint)
        //canvas.drawText(mMatrixValues,mTargetRect.left,mTargetRect.bottom,matrixValuePaint);
        canvas.save()
        // canvas.translate(mTranslate, 0f)
        // canvas.rotate(mRotate, 200f, 200f)
        // canvas.scale(mScale, mScale, 200f, 200f)
        // canvas.skew(0f, mSkew)
        canvas.restore()
    }

    private val seekBarChange: OnSeekBarChangeListener = object : OnSeekBarChangeListener {
        override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
            val filter = progress.toFloat() / 100
            when (seekBar.tag) {
                "T" -> {
                    mTranslate = filter
                }
                "R" -> {
                    mRotate = filter
                }
                "S" -> {
                    mScale = filter / 200
                }
                "SK" -> {
                    mSkew = filter / 360
                }
            }
            postInvalidate()
        }

        override fun onStartTrackingTouch(seekBar: SeekBar) {}
        override fun onStopTrackingTouch(seekBar: SeekBar) {}
    }

}