package com.zeke.demo.draw.paint

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.ComposePathEffect
import android.graphics.CornerPathEffect
import android.graphics.DashPathEffect
import android.graphics.LinearGradient
import android.graphics.Matrix
import android.graphics.Paint
import android.graphics.Path
import android.graphics.PathDashPathEffect
import android.graphics.PathEffect
import android.graphics.PathMeasure
import android.graphics.RectF
import android.graphics.Shader
import android.util.AttributeSet
import com.zeke.demo.base.BasePracticeView


/**
 * author: King.Z <br>
 * date:  2020/3/15 14:27 <br>
 * description: 验证画笔PathEffect特效的Demo <br>
 *     PathEffect是用来控制绘制轮廓(线条)的方式.
 *
 *     DashPathEffect是PathEffect类的一个子类,可以使paint画出类似虚线的样子,并且可以任意指定虚实的排列方式.
 *     Android包含了多个PathEffect，包括：
 *     ----------------- 单一效果
 *    【CornerPathEffect】：
 *          可以使用圆角来代替尖锐的角从而对基本图形的形状尖锐的边角进行平滑。
 *    【DashPathEffect】：
 *          可以使用DashPathEffect来创建一个虚线的轮廓(短横线/小圆点)，而不是使用实线。
 *          还可以指定任意的虚/实线段的重复模式。
 *          DashPathEffect(float[] intervals, float offset)
 *              其中intervals为虚线的ON(实)和OFF数组，该数组的length必须大于等于2，phase为绘制时的偏移量。
 *    【DiscretePathEffect】：
 *          把线条进行随机的偏离，让轮廓变得乱七八糟。乱七八糟的方式和程度由参数决定。
 *          与DashPathEffect相似，但是添加了随机性。
 *          当绘制它的时候，需要指定每一段的长度和与原始路径的偏离度。
 *          一般通过DiscretePathEffect(float segmentLength,float deviation)创建，
 *          其中，segmentLength指定最大的段长，deviation指定偏离量。
 *    【PathDashPathEffect】：
 *          这种效果可以定义一个新的形状(path)并将其用作原始路径的轮廓标记。
 *
 *     ----------------- 组合效果
 *    【ComposePathEffect】：
 *          将两种效果组合起来应用，先使用第一种效果，然后在这种效果的基础上应用第二种效果。
 *
 *    【SumPathEffect】:
 *          叠加效果 但与ComposePathEffect不同的是，
 *          在表现时，会分别对两个参数的效果各自独立进行表现，然后将两个效果简单的重叠在一起显示出来。
 *          对象形状的PathEffect的改变会影响到形状的区域。这就能够保证应用到相同形状的填充效果将会绘制到新的边界中。
 */
class Paint7PathEffectView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : BasePracticeView(context, attrs, defStyleAttr) {

    private data class EffectEntry(
        val effect: PathEffect?,
        val color: Int = 0,
        //useGradientShader为true时，Shader 的颜色会完全替代 paint.color，所以color默认为0
        val useGradientShader: Boolean = false
    )

    private companion object {
        const val INDEX_FLOW_COLOR_DASH = 4
    }

    private var effectEntries: MutableList<EffectEntry> = mutableListOf()
    private var mPaintRect: Paint
    private var mPath: Path
    private var pathLength = 0f
    private var dashShader: LinearGradient
    private val shaderMatrix = Matrix()
    private var bounds = RectF()
    private var dashAnimator: ValueAnimator? = null
    private val dashColors = intArrayOf(
        0x4C4285F4.toInt(), // Blue (30%透明 - 头部渐隐)
        0xFF4285F4.toInt(), // Blue
        0xFFEA4335.toInt(), // Red
        0xFFFBBC05.toInt(), // Yellow
        0xFF34A853.toInt(), // Green
        0xFF4285F4.toInt(), // Blue
        0x4C4285F4.toInt()  // Blue (30%透明 - 尾部渐隐)
    )

    init {
        effectEntries = makeEffects(0f)

        isFocusable = true
        isFocusableInTouchMode = true
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = 6f

        mPaintRect = Paint(paint)
        mPaintRect.strokeWidth = 2f

        mPath = makeFollowPath()
        pathLength = PathMeasure(mPath, false).length
        dashShader = LinearGradient(
            0f, 0f, pathLength * 0.5f, 0f,
            dashColors, null,
            Shader.TileMode.MIRROR
        )
        startDashAnimation()
    }

    /**
     * 制作随机Path路径
     */
    private fun makeFollowPath(): Path {
        val p = Path()
        p.moveTo(0f, 0f)
        //画15个阶段的路径
        for (i in 1..15) {
            p.lineTo(i * 65f, Math.random().toFloat() * 110)
        }
        return p
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        mPath.computeBounds(bounds, false)
        canvas.translate(20 - bounds.left, 20 - bounds.top)
        mPaintRect.color = Color.BLACK
        for (entry in effectEntries) {
            paint.pathEffect = entry.effect
            paint.color = entry.color
            paint.shader = if (entry.useGradientShader) dashShader else null
            canvas.drawPath(mPath, paint)
            canvas.drawRoundRect(bounds, 0f, 0f, mPaintRect)
            canvas.translate(0f, 145f)
        }
    }

    /**
     * 创建多个Path效果
     * 1. 无效果
     * 2. 圆角平滑的path
     * 3. Dash风格的path (非平滑)
     * 4. 彩色渐变Dash(由ValueAnimator驱动phase)
     * 5. 组合Path(平滑 + Dash)
     * 6. 使用path图形来填充当前路径的dash(导流线)
     * 7. 组合Path(平滑 + 图形path)
     *
     * @param phase  偏移量
     */
    private fun makeEffects(phase: Float): MutableList<EffectEntry> {
        val cornerPathEffect = CornerPathEffect(30f)
        val dashPathEffect = DashPathEffect(floatArrayOf(24f, 6f, 15f, 30f, 3f), phase)
        val colorDashPathEffect = DashPathEffect(floatArrayOf(24f, 6f, 15f, 30f, 3f), phase)
        val pathDashPathEffect = PathDashPathEffect(
            makeStampPathDash(), 12f, phase,
            PathDashPathEffect.Style.ROTATE
        )

        return mutableListOf(
            EffectEntry(null, Color.BLACK),
            EffectEntry(cornerPathEffect, Color.RED),
            EffectEntry(cornerPathEffect, useGradientShader = true),
            EffectEntry(dashPathEffect, Color.BLUE),
            EffectEntry(colorDashPathEffect, useGradientShader = true),
            EffectEntry(ComposePathEffect(dashPathEffect, cornerPathEffect), Color.GREEN),
            EffectEntry(pathDashPathEffect, Color.MAGENTA)
        )
    }

    /**
     * 绘制填充图形的路径
     * @return 印花的形状(导流线一样)
     */
    private fun makeStampPathDash(): Path {
        val p = Path()
        //            p.moveTo(4, 0);
        //            p.lineTo(0, -4);
        //            p.lineTo(8, -4);
        //            p.lineTo(12, 0);
        //            p.lineTo(8, 4);
        //            p.lineTo(0, 4);
        p.lineTo(12f, -6f)
        p.lineTo(5f, 0f)
        p.lineTo(12f, 6f)
        return p
    }

    /**
     * 启动Dash动画，仅重建索引3的colorDashPathEffect
     */
    private fun startDashAnimation() {
        val intervals = floatArrayOf(24f, 6f, 15f, 30f, 3f)
        val patternCycleLength = intervals.sum()
        dashAnimator = ValueAnimator.ofFloat(patternCycleLength * 4, 0f).apply {
            duration = 2000L
            repeatCount = ValueAnimator.INFINITE
            addUpdateListener { animation ->
                val phase = animation.animatedValue as Float
                // 同步移动shader，让渐变颜色跟随dash流动
                shaderMatrix.reset()
                shaderMatrix.setTranslate(animation.animatedFraction * pathLength, 0f)
                dashShader.setLocalMatrix(shaderMatrix)

                effectEntries[INDEX_FLOW_COLOR_DASH] = EffectEntry(
                    DashPathEffect(intervals, phase),
                    useGradientShader = true
                )
                invalidate()
            }
            start()
        }
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        dashAnimator?.cancel()
        dashAnimator = null
    }


    override fun getViewHeight(): Int {
        return 146 * effectEntries.size
    }
}