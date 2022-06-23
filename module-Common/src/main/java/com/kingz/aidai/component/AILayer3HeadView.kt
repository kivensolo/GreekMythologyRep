package com.kapplication.aitest.aidai.component

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import com.kapplication.aitest.aidai.Direction

/**
 * author：ZekeWang
 * date：2022/5/10
 * description：AI形象头部视图层
 * 径向渐变 + 动态置换 + 模糊
 */
class AILayer3HeadView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : AbsAICommonView(context, attrs, defStyleAttr){
    //头部相对于中心点的偏移量
    private val headDetalY = dp2px(20f)
    private val headDetalX = dp2px(0f)
    override var viewRadius = dp2px(120f)
    //头部边缘模糊半径
    private val headBlurRadius = dp2px(15f)
    override var colors = intArrayOf(
        Color.parseColor("#FF3687FF"),
        Color.parseColor("#CB50F4FF"),
        Color.parseColor("#BD58E1FF"),
        Color.parseColor("#0000A3FF")
    )
    override var positions = floatArrayOf(0.349f, 0.484f, 0.844f, 1.0f)
    override var radialShader: Shader = RadialGradient(0f, 0f,
        viewRadius, colors, positions, Shader.TileMode.CLAMP)


    //初始值，网上偏移，与layer4顶部
    override var offsetX = (dp2px(14f)).toInt()
    override var offsetY = (-dp2px(15f)).toInt()
    override var drawOffsetX = 0f
    override var drawOffsetY = 0f
    override var floatRadius = dp2px(7f)

    init {
        paint = Paint().apply {
            isAntiAlias = true
            maskFilter = BlurMaskFilter(headBlurRadius, BlurMaskFilter.Blur.NORMAL)
        }

        moveAnimation = ValueAnimator.ofFloat(0f, dp2px(30f)).apply {
            duration = moveDuration
            addUpdateListener { animation ->
                val delta = animation.animatedValue as Float
                when (currentDirection) {
                    Direction.LEFT -> {
                        drawOffsetX = delta
                        drawOffsetY = 0f
                    }
                    Direction.RIGHT -> { //OK
                        drawOffsetX = -delta / 5
                        drawOffsetY = delta / 5
                        canvasRotate = -delta * 1.3f
                    }
                    Direction.BOTTOM -> {
                        drawOffsetX = 0f
                        drawOffsetY = -delta
                    }
                    Direction.TOP -> {
                        drawOffsetX = 0f
                        drawOffsetY = delta * 2
                    }
                    Direction.FRONT -> { //OK
                        canvasRotate = 0f
                        drawOffsetX = 0f
                        drawOffsetY = 0f
                    }
                }
                scrollTo(drawOffsetX.toInt(), drawOffsetY.toInt())
            }
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val widthSize = MeasureSpec.getSize(widthMeasureSpec)
        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        val heightSize = MeasureSpec.getSize(heightMeasureSpec)
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)
        val mWidth = if (widthMode == MeasureSpec.EXACTLY || widthMode == MeasureSpec.AT_MOST) {
            widthSize
        }else{
            dp2px(320f).toInt() //decide by self
        }

        val mHeight = if (heightMode == MeasureSpec.EXACTLY || widthMode == MeasureSpec.AT_MOST) {
            heightSize
        }else{
            dp2px(320f).toInt() //decide by self
        }
        viewRadius = (mWidth/2).toFloat() - paddingStart
        setMeasuredDimension(mWidth, mHeight)

        updateLayout()
        blurRadialShader = RadialGradient(
            viewRadius + drawOffsetX,
            viewRadius + drawOffsetY,
            viewRadius + dp2px(10f),
            colors, positions,
            Shader.TileMode.CLAMP
        )
    }

    private fun updateLayout() {
        y = (top + offsetY).toFloat()
        x = (left + offsetX).toFloat()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        paint.shader = if (BLUR_MODE) {
            blurRadialShader
        } else {
            radialShader
        }
        with(canvas){
            save()
            if(BLUR_MODE){
                rotate(canvasRotate, viewRadius, viewRadius)
                mBlurBitmap = createMaskBitmap()
                if(!isInEditMode){
                    rsBlurBkg(context, mBlurBitmap!!, 10f)
                }
                canvas.drawBitmap(mBlurBitmap!!, 0f, 0f, paint)
            }else{
                // Y值偏上一点
                translate((measuredWidth/2).toFloat(), (measuredHeight/2).toFloat() - dp2px(5f))
                drawCircle(headDetalX, headDetalY, viewRadius, paint)
            }
            if(DEBUG_MODE){
                setDebugBorder(this@AILayer3HeadView,this,
                    2,
                    Color.parseColor("#8080FF"),
                    debugPaint)
            }
            //头部边缘高光
//            drawCircle(0f, -20f, headRadius, headBlurPaint)
            restore()
        }
    }

    fun getHeadRadius():Float = viewRadius
}