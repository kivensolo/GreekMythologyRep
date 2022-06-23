package com.kapplication.aitest.aidai.component;

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.ViewGroup
import com.kapplication.aitest.aidai.Direction
import com.kingz.module.common.R


/**
 * author：ZekeWang
 * date：2022/5/9
 * description：Ai形象中心面部动态层
 */
class AiLayer2FaceView @JvmOverloads constructor(

    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : AbsAICommonView(context, attrs, defStyleAttr) {
    //脸部设计区域
    private var faceDesignRect: RectF = RectF()
    //脸部半径
    private var faceRadius:Float = 0f
    override var paint: Paint = Paint().apply {
        isAntiAlias = true
    }

    //脸部Clip区域(以view中心为区域)
    private var faceClipRect: RectF = RectF()
    private var faceClipPath: Path

    // 面部高光
    private var faceHightLightRadius = 0f
    private var faceHightLightPaint: Paint

    override var colors = intArrayOf(
        Color.parseColor("#FF1331DF"),
        Color.parseColor("#C90C4CEE"),
        Color.parseColor("#D40569FF"),
        Color.parseColor("#000569FF")
    )
    override var positions = floatArrayOf(0f, 0.786f, 0.95f, 1f)
    override var radialShader: Shader = RadialGradient(0f, 0f,
        dp2px(95f), colors, positions, Shader.TileMode.CLAMP)

    override var offsetY: Int = (-dp2px(15f)).toInt()
    override var drawOffsetY: Float = 0f

    init {
        faceHightLightRadius = dp2px(40f)
        faceClipPath = Path()

        faceHightLightPaint = Paint().apply {
            color = resources.getColor(R.color.ai_face_height_light)
            isAntiAlias = true
            style = Paint.Style.FILL
        }

        moveAnimation = ValueAnimator.ofFloat(0f, dp2px(20f)).apply {
            duration = moveDuration
            addUpdateListener { animation ->
                val delta = animation.animatedValue as Float
                when(currentDirection){
                    Direction.LEFT->{
                        drawOffsetX= delta
                        drawOffsetY = 0f
                    }
                    Direction.RIGHT->{  //OK
                        drawOffsetX = -delta
                        drawOffsetY = 0f
                    }
                    Direction.BOTTOM->{
                        drawOffsetX= delta/2
                        drawOffsetY = -delta/2
                    }
                    Direction.TOP->{
                        drawOffsetX= 0f
                        drawOffsetY = delta * 2f
                    }
                    Direction.FRONT -> { //OK
                        drawOffsetX = 0f
                        drawOffsetY = 0f
                    }
                    Direction.LEFT_TOP ->{
                        drawOffsetX = delta
                        drawOffsetY = delta * 1.5f
                    }
                }
                scrollTo(drawOffsetX.toInt(), drawOffsetY.toInt())
            }
        }
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        if(changed){
            y = (top + offsetY).toFloat()
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
            dp2px(240f).toInt() //decide by self
        }

        val mHeight = if (heightMode == MeasureSpec.EXACTLY || widthMode == MeasureSpec.AT_MOST) {
            heightSize
        }else{
            dp2px(240f).toInt() //decide by self
        }
        setMeasuredDimension(mWidth, mHeight)

        faceDesignRect.set(0f,0f, mWidth.toFloat(), mHeight.toFloat())
        faceRadius = faceDesignRect.width() / 2 + 5f
        faceClipRect.set(-faceRadius, -faceRadius, faceRadius + 1, faceRadius + 1)
        faceClipPath.addRoundRect(faceClipRect, faceRadius, faceRadius, Path.Direction.CW)
        (parent as ViewGroup).findViewById<AiLayer1EyesView>(R.id.aiEyesView).apply {
            setClipPath(faceClipPath)
        }
        viewRadius = (mWidth/2).toFloat()
        // 画bitmap的径向渐变坐标系是视图坐标
        blurRadialShader = RadialGradient(viewRadius, viewRadius,
            viewRadius - paddingLeft, colors, positions, Shader.TileMode.CLAMP)
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
        canvas.apply {
            save()
            if(BLUR_MODE){
                mBlurBitmap = createMaskBitmap()
                if(!isInEditMode){
                    rsBlurBkg(context, mBlurBitmap!!, 15f)
                }
                canvas.drawBitmap(mBlurBitmap!!, 0f, 0f, paint)
            }else{
                translate((measuredWidth/2).toFloat(), (measuredHeight/2).toFloat())
                drawCircle(0f, 0f, faceRadius, paint) //脸部
            }
            if(DEBUG_MODE){
                setDebugBorder(this@AiLayer2FaceView,this,
                    2,
                    Color.WHITE,
                    debugPaint)
            }
//            clipPath(faceClipPath)
//            //TODO 面部高光 动态移动
//            drawCircle(
//                faceHightOffSetX,
//                faceHightOffSetY,
//                faceHightLightRadius,
//                faceHightLightPaint
//            )
            restore()
        }
    }

}
