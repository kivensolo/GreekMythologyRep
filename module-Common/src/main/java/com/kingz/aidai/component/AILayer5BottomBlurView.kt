package com.kapplication.aitest.aidai.component

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import com.kapplication.aitest.aidai.Direction

/**
 * author：ZekeWang
 * date：2022/5/28
 * description：AI形象图层5  发光效果成层
 */
class AILayer5BottomBlurView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : AbsAICommonView(context, attrs, defStyleAttr){
    override var colors = intArrayOf(
        Color.parseColor("#CC77FFC6"),
        Color.parseColor("#CC8BEBDE")
    )
    override var positions = floatArrayOf(0f, 1f)
    override var blurRadius = dp2px(20f)
    override var paint = Paint().apply {
        isAntiAlias = true
        maskFilter = BlurMaskFilter(blurRadius, BlurMaskFilter.Blur.NORMAL)
    }

    init {
        setLayerType(LAYER_TYPE_SOFTWARE, null)

        moveAnimation = ValueAnimator.ofFloat(0f, dp2px(5f)).setDuration(moveDuration).apply {
            addUpdateListener { animation ->
                val delta = animation.animatedValue as Float
                when(currentDirection){
                    Direction.LEFT->{
                        drawOffsetX= delta
                        drawOffsetY = 0f
                    }
                    Direction.RIGHT->{ //OK
                        drawOffsetX= -delta
                        drawOffsetY = 0f
                    }
                    Direction.BOTTOM,
                    Direction.TOP ->{
                        return@addUpdateListener
                    }
                    Direction.FRONT ->{ //OK
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
            dp2px(300f).toInt() //decide by self
        }

        val mHeight = if (heightMode == MeasureSpec.EXACTLY || widthMode == MeasureSpec.AT_MOST) {
            heightSize
        }else{
            dp2px(300f).toInt() //decide by self
        }
        viewRadius = (mWidth/2).toFloat()
        radialShader = LinearGradient(
            paddingStart.toFloat(), viewRadius / 2, mWidth - paddingEnd.toFloat(), viewRadius / 2,
            colors, positions, Shader.TileMode.CLAMP
        )
        setMeasuredDimension(mWidth, mHeight)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        paint.shader = radialShader
        with(canvas){
            save()
            translate((width/2).toFloat(), (height/2).toFloat())
            drawCircle(0f, 0f, viewRadius - paddingLeft, paint)
            if(DEBUG_MODE){
                setDebugBorder(this@AILayer5BottomBlurView,this,
                    2,
                    Color.RED,
                    debugPaint)
            }
            restore()
        }
    }

    override fun setDebugBorder(v: View, canvas: Canvas, width: Int, color: Int, borderPaint: Paint) {
        borderPaint.style = Paint.Style.STROKE
        borderPaint.strokeWidth = width.toFloat()
        borderPaint.color = color
        val radius = v.measuredWidth.toFloat() / 2
        canvas.drawRect(
            - radius + 1,
            - radius + 1,
            (radius-3),
            (radius-3),
            borderPaint
        )
    }

    fun getBottomRadius():Float = viewRadius
}