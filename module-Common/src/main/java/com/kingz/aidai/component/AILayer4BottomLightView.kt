package com.kapplication.aitest.aidai.component

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import com.kapplication.aitest.aidai.Direction

/**
 * author：ZekeWang
 * date：2022/5/28
 * description：
 * 图层4  底色层  径向渐变、动态置换
 */
class AILayer4BottomLightView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : AbsAICommonView(context, attrs, defStyleAttr){
    private var faceClipRect: Path = Path()
    override var blurRadius = dp2px(10f)
    override var viewRadius = dp2px(140f)
    override var colors = intArrayOf(
        Color.parseColor("#FF0DA8FF"),
        Color.parseColor("#874DFFEA"),
        Color.parseColor("#C26CEDFF"),//Halo
        Color.parseColor("#AB39C4FF"),
        Color.parseColor("#9400F0FF")
    )
    override var positions = floatArrayOf(0.359f, 0.545f, 0.609f, 0.745f, 1.0f)
    override var radialShader: Shader = RadialGradient(0f, 0f, viewRadius,
        colors, positions, Shader.TileMode.CLAMP)

    val defaultDrawOffsetX = dp2px(3f * scaleX)
    val defaultDrawOffsetY = -dp2px(40f * scaleY)
    override var drawOffsetX = defaultDrawOffsetX
    override var drawOffsetY = defaultDrawOffsetY
    override var floatRadius =  dp2px(10f)

    init {
        //初始默认旋转15度
        canvasRotate = 15f
        paint = Paint().apply {
            isAntiAlias = true
            maskFilter = BlurMaskFilter(blurRadius, BlurMaskFilter.Blur.NORMAL)
        }

        moveAnimation = ValueAnimator.ofFloat(0f, dp2px(10f))
            .setDuration(moveDuration)
            .apply {
                addUpdateListener { animation ->
                    val delta = animation.animatedValue as Float
                    when (currentDirection) {
                        Direction.LEFT -> {
                            drawOffsetX = delta
                            drawOffsetY = 0f
                        }
                        Direction.RIGHT -> { //OK
                            drawOffsetX = delta / 2
                            drawOffsetY = delta / 3
                            canvasRotate = delta * 3.5f
                        }
                        Direction.BOTTOM -> {
                            drawOffsetX = 0f
                            drawOffsetY = -delta * 2f
                        }
                        Direction.TOP -> {
                            drawOffsetX = 0f
                            drawOffsetY = delta * 1.5f
                        }
                        Direction.FRONT -> { //OK
                            canvasRotate = 15f
                            drawOffsetX = -defaultDrawOffsetX
                            drawOffsetY = 0f
                        }
                        Direction.LEFT_TOP -> {
                            drawOffsetX = delta
                            drawOffsetY = delta
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
        faceClipRect.addCircle(viewRadius, viewRadius, viewRadius, Path.Direction.CW)
        // 画bitmap的径向渐变坐标是视图坐标
        blurRadialShader = RadialGradient(
            (mWidth/2).toFloat() + defaultDrawOffsetX,
            (mHeight/2).toFloat() + defaultDrawOffsetY,
            viewRadius + dp2px(5f), // 这个5f的偏移量很重要，否则图层4无法达到很好的效果
            colors, positions,
            Shader.TileMode.CLAMP)
        setMeasuredDimension(mWidth, mHeight)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        paint.apply {
            shader = if (BLUR_MODE) {
                blurRadialShader
            } else {
                radialShader
            }
        }
        with(canvas){
            save()
            if(BLUR_MODE){
                rotate(canvasRotate, viewRadius, viewRadius)
                mBlurBitmap = createMaskBitmap()
                if(!isInEditMode){
                    rsBlurBkg(context, mBlurBitmap!!, 15f)
                }
                canvas.drawBitmap(mBlurBitmap!!, 0f, 0f, paint)
            }else{
                translate((measuredWidth/2).toFloat() + dp2px(3f), (measuredHeight/2).toFloat() - dp2px(45f))
                rotate(canvasRotate)
                drawCircle(0f, 30f, viewRadius, paint)
            }
            if(DEBUG_MODE){
                setDebugBorder(this@AILayer4BottomLightView,this,
                    2,
                    Color.parseColor("#D68100"),
                    debugPaint)
            }
            restore()
        }
    }
}