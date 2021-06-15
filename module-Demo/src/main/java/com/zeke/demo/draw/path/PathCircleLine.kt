package com.zeke.demo.draw.path

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import com.zeke.demo.base.BasePracticeView

/**
 * author：ZekeWang
 * date：2021/6/15
 * description：
 *      带内容渐变色填充的折线图
 */
class ShadowLineChartView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : BasePracticeView(context, attrs, defStyleAttr) {
    //
    private var mPath: Path = Path()
    private var mShaderPath: Path = Path()
    private var mShaderPaint: Paint = Paint()
    private var mPaintRect: Paint
    private var mBaseLinePaint: Paint
    private var bounds = RectF()
    private var mLine = RectF()

    private var dots = arrayOf(
        arrayOf( 0, 10, 20, 30, 35, 37, 38, 40, 50, 51, 52, 55, 60,70,80, 90,100), //Time
        arrayOf(60,180,120,150,200,220,240,250,245,240,260,280,100,60,80,120,100) //Value
)

    init {
        val h = dots[0]
        val v = dots[1]
        for (i in h.indices) {
            val dx = h[i].toFloat() * 10
            val dy = v[i].toFloat() * 1.2f
            if (i == 0) {
                mPath.moveTo(dx, dy)
                mShaderPath.moveTo(dx,dy)
                mLine.left = dx
            } else {
                mPath.lineTo(dx, dy)
                mShaderPath.lineTo(dx,dy)
                if (i == h.lastIndex) {
                    //封闭shader
                    mShaderPath.lineTo(dx,500f)
                    mShaderPath.lineTo(0f,500f)
                    mShaderPath.close()
                    mLine.right = dx
                }
            }
        }

        //平均值线
        mLine.top = 200f
        mLine.bottom = 200f


        with(paint) {
            style = Paint.Style.STROKE
            strokeWidth = 10f
            isAntiAlias = true
            pathEffect = CornerPathEffect(6f)
            color = Color.parseColor("#FAE847") // 动态设置 橙黄色
        }

        mPaintRect = Paint(paint)
        mPaintRect.strokeWidth = 2f

        mBaseLinePaint = Paint(mPaintRect)

        with(mShaderPaint) {
            isAntiAlias = true
            strokeWidth = 2f
            val shadeColors = arrayOf(
                Color.parseColor("#D6C105"),
                Color.TRANSPARENT
            )
            val positions = arrayOf(0.2f, 1.0f)
            shader = LinearGradient(
                0f, 0f, 0f, 350f,
                shadeColors.toIntArray(), positions.toFloatArray(),
                Shader.TileMode.CLAMP
            )
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.translate(100f,0f)

        setBackgroundColor(Color.parseColor("#303030"))
        mPath.computeBounds(bounds, false)

        canvas.drawPath(mPath, paint)
        canvas.drawPath(mShaderPath, mShaderPaint)
//        canvas.drawRoundRect(bounds, 0f, 0f, mPaintRect)
//
//        mPaintRect.color = Color.parseColor("#9C7A48") // 动态设置
//        canvas.drawLine(mLine.left, mLine.top, mLine.right, mLine.bottom, mBaseLinePaint)
    }

    override fun getViewHeight(): Int {
        return 600
    }
}