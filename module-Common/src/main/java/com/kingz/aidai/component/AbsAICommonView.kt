package com.kapplication.aitest.aidai.component

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.renderscript.Allocation
import android.renderscript.Element
import android.renderscript.RenderScript
import android.renderscript.ScriptIntrinsicBlur
import android.util.AttributeSet
import android.view.View
import android.widget.Scroller
import com.kapplication.aitest.aidai.Direction
import com.zeke.kangaroo.utils.UIUtils
import kotlin.math.pow
import kotlin.math.sqrt

abstract class AbsAICommonView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) :View(context, attrs, defStyleAttr) {
    protected val TAG: String = javaClass.simpleName
    protected val DEBUG_MODE: Boolean = false
    /**
     * 模糊模式的运行效果开关,默认开启.
     * 因AS没法预览高斯模糊,关闭后,可预览个大概的效果
     */
    protected val BLUR_MODE: Boolean = true
    protected var mBlurBitmap: Bitmap?= null
    //高斯模糊的shader
    protected open lateinit var blurRadialShader: Shader

    protected val debugPaint: Paint = Paint().apply {
        style = Paint.Style.STROKE
    }
    protected open lateinit var paint:Paint

    //径向或者线性渐变的颜色数组
    open lateinit var colors:IntArray
    //颜色渐变位置信息数组
    open lateinit var positions:FloatArray
    //整体shader
    open lateinit var radialShader :Shader
    //绘制半径
    open var viewRadius:Float = 0f
    //整体模糊半径
    open var blurRadius:Float = 0f
    //AI当前看向的方向
    open var currentDirection = 0
    open lateinit var moveAnimation: ValueAnimator
    open var moveDuration:Long = 1000

    //Layout偏移量
    open var offsetX = 0
    open var offsetY = 0
    //画的内容偏移量
    open var drawOffsetX = 0f
    open var drawOffsetY = 0f
    open var canvasRotate = 0f

    open var mScroller = Scroller(context)
    var floatNextPoint: Point = Point(0, 0)
    open var floatLastPoint:Point = Point(0, 0)
    open var idlePoint:Point = Point(0, 0)
    open var floatRadius = dp2px(5f) //悬浮移动半径
    var preOffsetX = 0 //初始便宜量
    var preOffsetY = 0 //初始便宜量
    open val MSG_FLOAT_CODE = 0x2000
    open val durationOfFloat = 5000
    var enableFloat = true
    //悬浮动画

    open var mHandler = Handler(Looper.getMainLooper()){ msg ->
        when (msg.what) {
            MSG_FLOAT_CODE -> {
                createNextFloatPoint()
                mScroller.startScroll(
                    preOffsetX,
                    preOffsetY,
                    -(floatNextPoint.x - floatLastPoint.x),
                    -(floatNextPoint.y - floatLastPoint.y),
                    durationOfFloat
                )
                invalidate()
                //以下负值很重要
                preOffsetX = -floatNextPoint.x
                preOffsetY = -floatNextPoint.y
                floatLastPoint.set(floatNextPoint.x, floatNextPoint.y)
                loopSendMessage()
                onViewFloatting()
                true
            }
            else -> false
        }
    }

    private fun loopSendMessage(){
        val msg = Message.obtain()
        msg.what = MSG_FLOAT_CODE
        mHandler.sendMessageDelayed(msg,2000)
    }

    protected open fun onViewFloatting(){

    }

    //在基于原始点周围计算偏移量
    protected open fun createNextFloatPoint(){
        //园内找随机点x
        val randomX = (Math.random() * floatRadius * 2 - floatRadius).toInt()
        //根据点x获取点y的范围
        val yRadius = sqrt(
            floatRadius.toDouble().pow(2.0) - randomX.toDouble().pow(2.0)
        )
        //获取随机点y
        val randomY = (Math.random() * yRadius * 2 - yRadius).toInt()
        //基于原始中心点的偏移
        floatNextPoint.x = randomX
        floatNextPoint.y = randomY
    }

    override fun computeScroll() {
        super.computeScroll()
        if(mScroller.computeScrollOffset() && enableFloat){
            scrollTo(mScroller.currX, mScroller.currY)// 真正实现滚动操作的地方
            invalidate()// 刷新
        }
    }

    protected fun dp2px(dp: Float): Float {
        return UIUtils.dip2px(dp).toFloat()
    }


    open fun setDebugBorder(v: View, canvas: Canvas, borderPaint: Paint) {
        setDebugBorder(v, canvas, 1, Color.CYAN, borderPaint)
    }

    open fun setDebugBorder(v: View, canvas: Canvas, width: Int, borderPaint: Paint) {
        setDebugBorder(v, canvas, width, Color.CYAN, borderPaint)
    }

    open fun setDebugBorder(v: View, canvas: Canvas, width: Int, color: Int, borderPaint: Paint) {
        borderPaint.style = Paint.Style.STROKE
        borderPaint.strokeWidth = width.toFloat()
        borderPaint.color = color
        canvas.drawRect(
            1f,
            1f,
            v.measuredWidth.toFloat()-1,
            v.measuredHeight.toFloat()-1,
            borderPaint
        )
    }

    open fun setAiSee(@Direction.DIRECTION direction:Int){
        currentDirection = direction
        if(::moveAnimation.isInitialized){
            moveAnimation.start()
        }
    }

    open fun enableFloat(start: Boolean){
        enableFloat = start
        if (start) {
            val msg = Message.obtain()
            msg.what = MSG_FLOAT_CODE
            mHandler.sendMessage(msg)
        }else{
            mHandler.removeMessages(MSG_FLOAT_CODE)
        }
    }

    protected fun rsBlurBkg(
        context: Context,
        source: Bitmap,
        radius: Float
    ): Bitmap {
        //(1)Create renderScript instance
        val renderScript: RenderScript = RenderScript.create(context)
//        Log.i(TAG, "scale size:" + source.width.toString() + "*" + source.height)

        //(2) Allocate memory for Renderscript to work with
        val input: Allocation = Allocation.createFromBitmap(renderScript, source)
        val output: Allocation = Allocation.createTyped(renderScript, input.getType())
        //(3) Load up an instance of the specific script that we want to use.
        val scriptIntrinsicBlur: ScriptIntrinsicBlur =
            ScriptIntrinsicBlur.create(renderScript, Element.U8_4(renderScript))
        //(4) 设置blurScript对象的输入内存
        scriptIntrinsicBlur.setInput(input)
        //(5) Set the blur radius
        scriptIntrinsicBlur.setRadius(radius)
        //(6) Start the ScriptIntrinisicBlur
        scriptIntrinsicBlur.forEach(output)
        //(7) Copy the output to the blurred bitmap
        output.copyTo(source)
        //(8)
        renderScript.destroy()
        return source
    }

    protected fun createMaskBitmap():Bitmap{
//        mMaskBitmap = BitmapFactory.decodeResource(context.resources, R.drawable.halo)
        mBlurBitmap = Bitmap.createBitmap(measuredWidth, measuredHeight, Bitmap.Config.ARGB_8888)
        val mCanvas = Canvas(mBlurBitmap!!)
        mCanvas.drawCircle(
            (measuredWidth/2).toFloat(),
            (measuredWidth/2).toFloat(),
            viewRadius,
            paint)
        return mBlurBitmap!!
    }
}
