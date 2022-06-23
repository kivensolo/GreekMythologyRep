package com.kapplication.aitest.aidai.component

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.animation.DecelerateInterpolator
import com.kapplication.aitest.aidai.Direction

/**
 * author：ZekeWang
 * date：2022/5/9
 * description：Ai形象眼睛自定义View
 */
class AiLayer1EyesView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : AbsAICommonView(context, attrs, defStyleAttr) {
    // 设计尺寸
    private var eyesDesignRect: RectF = RectF(0f, 0f, dp2px(6f), dp2px(8f))
    private var eyesDrawingRect: RectF = RectF(eyesDesignRect)
    //眨眼动画
    private var blinkAnimator: ValueAnimator = ObjectAnimator.ofFloat(0f, eyesDesignRect.height())
    // 眨眼睛的尺寸变化
    private val eyesBinkDeltaValue = dp2px(4f)
    // 两眼间距
    private var eyesSpace = dp2px(9f)
    // 眼睛圆角
    private val radius = dp2px(5f)

    override var floatRadius = dp2px(1f)

    private var eyesNextFloatTargetPoint: PointF = PointF(0f, 0f)
    override var paint: Paint = Paint().apply {
        color = Color.WHITE
        isAntiAlias = true
    }
    private var eyesBlurPaint: Paint = Paint().apply {
        color = Color.parseColor("#3BD7E8")
        maskFilter = BlurMaskFilter( dp2px(8f), BlurMaskFilter.Blur.OUTER)
    }

    //面部裁剪区域，限制眼睛动画范围
    private var clipAreaPath: Path? = null
    private var aroundSeeAnimation: ValueAnimator = ValueAnimator.ofInt(0, 100).setDuration(2000)

    private var mBlinkHandler = Handler(Looper.getMainLooper()) { msg ->
        when (msg.what) {
            0x1000 -> {
                startBlinkEye(repeat = 1)
                true
            }
            else -> false
        }
    }

    val defaultDrawOffsetX = -dp2px(2f * scaleX)
    val defaultDrawOffsetY = -dp2px(40f * scaleX)
    override var drawOffsetX = defaultDrawOffsetX
    override var drawOffsetY = defaultDrawOffsetY


    init {
        blinkAnimator.apply {
            duration = 1000
            interpolator = DecelerateInterpolator()
            addUpdateListener { animation ->
                val delta = animation.animatedValue as Float
                if (delta <= eyesBinkDeltaValue) { //闭眼
                    eyesDrawingRect.top = eyesDesignRect.top + delta
                    eyesDrawingRect.bottom = eyesDesignRect.bottom -  delta
                } else { //睁眼
                    eyesDrawingRect.top = eyesDesignRect.top + 2 * eyesBinkDeltaValue - delta
                    eyesDrawingRect.bottom = eyesDesignRect.bottom - 2 * eyesBinkDeltaValue + delta
                }
                invalidate()
            }
        }
        moveAnimation = ValueAnimator.ofFloat(0f, dp2px(35f)).setDuration(moveDuration).apply {

            addUpdateListener { animation ->
                val delta = animation.animatedValue as Float
                when (currentDirection) {
                    Direction.LEFT -> {
                        drawOffsetX = delta
                        drawOffsetY = 0f
                    }
                    Direction.RIGHT -> { //OK
                        drawOffsetX = delta
                        drawOffsetY = -dp2px(40f)
                    }
                    Direction.BOTTOM -> {
                        drawOffsetX = 0f
                        drawOffsetY = -delta
                    }
                    Direction.TOP -> {
                        drawOffsetX = 0f
                        drawOffsetY = delta
                    }
                    Direction.FRONT -> { //OK
                        drawOffsetX = defaultDrawOffsetX
                        drawOffsetY = defaultDrawOffsetY
                    }
                    Direction.LEFT_TOP ->{
                        drawOffsetX = -dp2px(2f * scaleX)
                        drawOffsetY = delta
                    }
                }
                // don't scroll
//                scrollTo(drawOffsetX.toInt(), drawOffsetY.toInt())
            }
        }

        aroundSeeAnimation.apply {
            addUpdateListener { animation ->
                val percent = animation.animatedValue as Int
                // 眼睛动作 --- 眨眼
                if (percent <= 50) { //闭眼
                    eyesDrawingRect.bottom = 60f * (100 - percent).toFloat() / 100
                } else { //睁眼
                    eyesDrawingRect.bottom = 60f * percent.toFloat() / 100
                }
            }
        }
    }

    /**
     * 开始张望动作
     */
    fun startAroundSee(){

    }

    fun seeAround(){

    }

    /**
     * 创建随机眼睛停留点
     */
    private fun createRandomFloatStopPoint() {
//        val faceRadius = faceView?.getFaceRadius()?:0f
        val faceRadius = 20f

        val eyesRandomX = (Math.random() * faceRadius).toFloat()
        val eyesRandomY = (Math.random() * faceRadius).toFloat()
        eyesNextFloatTargetPoint.set(eyesRandomX, eyesRandomY)
    }

    fun invalidateByFloating(){
        if(!aroundSeeAnimation.isRunning){
            aroundSeeAnimation.start()
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val widthSize = View.MeasureSpec.getSize(widthMeasureSpec)
        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        val heightSize = MeasureSpec.getSize(heightMeasureSpec)
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)

        var mWidth = 0
        if (widthMode == MeasureSpec.EXACTLY) {
            mWidth = widthSize
        }

        var mHeight = 0
        if (heightMode == MeasureSpec.EXACTLY) {
            mHeight = heightSize
        }
        val halfW = mWidth/2
        val halfH = mHeight/2 - dp2px(40f)
        viewRadius = (mWidth/2).toFloat()
        if(mWidth != 0 && mHeight != 0){
            setMeasuredDimension(mWidth, mHeight)
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
//        ZLog.d(TAG,"onDraw")
        canvas.apply {
            save()
            // 移动至中心点 确定脸部裁剪区域
            translate(viewRadius + drawOffsetX, viewRadius + drawOffsetY )
//            clipPath(clipAreaPath)
            //Draw Left eye
            translate(-eyesSpace/2 - eyesDrawingRect.width(), 0f)
            drawRoundRect(eyesDrawingRect, radius, radius, paint)
            drawRoundRect(eyesDrawingRect, radius, radius, eyesBlurPaint)
            //Draw Right eye
            translate(eyesSpace + eyesDrawingRect.width(), 0f)
            drawRoundRect(eyesDrawingRect, radius, radius, paint)
            drawRoundRect(eyesDrawingRect, radius, radius, eyesBlurPaint)

            restore()
        }
    }

    fun startBlinkEye(duration: Long = 1000, repeat:Int) {
        if(blinkAnimator.isRunning){
            blinkAnimator.cancel()
        }
        blinkAnimator.duration = duration
        blinkAnimator.repeatCount = repeat
        blinkAnimator.start()
    }

    override fun onViewFloatting() {
        super.onViewFloatting()
        val random = Math.random()
        if(random > 0.8f){ // 20%的机率眨眼睛
            startBlinkEye(repeat = 0)
        }
    }

    fun stopBlinkEye() {
        blinkAnimator.apply {
            cancel()
            removeAllListeners()
        }
    }

    fun setClipPath(path:Path){
        clipAreaPath = path
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if(event.action == MotionEvent.ACTION_UP){
            mBlinkHandler.removeCallbacksAndMessages(null)
            mBlinkHandler.sendMessageDelayed(Message.obtain().apply {
                what = 0x1000
            }, 200)
        }
        return super.onTouchEvent(event)
    }

    override fun setAiSee(direction: Int) {
        super.setAiSee(direction)
        blinkAnimator.start()
    }
}
