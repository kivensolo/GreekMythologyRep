package com.zeke.demo.draw.paint

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.os.Build
import android.util.AttributeSet
import androidx.annotation.RequiresApi
import com.zeke.demo.base.BasePracticeView


/**
 * author: King.Z <br>
 * date:  2020/3/15 14:27 <br>
 * description:  <br>
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
    //储存Path效果容器
    private var mEffects: Array<PathEffect?>
    private var mPaintRect: Paint
    private var mPath: Path
    //储存颜色的数组
    private var mColors: IntArray
    private var mPhase = 0f
    private var bounds = RectF()

    init {
        isFocusable = true
        isFocusableInTouchMode = true
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = 6f

        mPaintRect = Paint(paint)
        mPaintRect.strokeWidth = 2f

        mPath = makeFollowPath()
        mEffects = arrayOfNulls(6)
        mColors = intArrayOf(Color.BLACK, Color.RED,
                Color.BLUE, Color.GREEN,
                Color.MAGENTA, Color.BLACK
        )
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



    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        mPath.computeBounds(bounds, false)
        canvas.translate(20 - bounds.left, 20 - bounds.top)

        //初始化每个效果
        makeEffects(mEffects, mPhase)
        mPhase += 1f
        invalidate()

        for (i in mEffects.indices) {
            paint.pathEffect = mEffects[i]
            paint.color = mColors[i]
            mPaintRect.pathEffect = mEffects[i]
            mPaintRect.color = mColors[i]

            canvas.drawPath(mPath, paint)
            canvas.drawRoundRect(bounds, 0f, 0f, mPaintRect)
            canvas.translate(0f, 145f)
        }
    }

    /**
     * 添加效果
     * @param e  path的效果
     * @param phase  偏移量
     */
    private fun makeEffects(e: Array<PathEffect?>, phase: Float) {
        // no effect
        e[0] = null
        //边角平滑效果, 所有拐角变成圆角
        e[1] = CornerPathEffect(30f)

        //画自定义虚线
        e[2] = DashPathEffect(floatArrayOf(24f, 6f, 15f, 30f, 3f), phase)

        //使用Path图形来填充当前的路径shape则是指填充图形，advance指每个图形间的间距，
        e[3] = PathDashPathEffect(makeStampPathDash(), 12f, phase,
                PathDashPathEffect.Style.ROTATE)

        e[4] = ComposePathEffect(e[2], e[1])
        e[5] = ComposePathEffect(e[3], e[1])
    }

    /**
     * 绘制填充图形的路径
     * @return 印花的形状
     */
    private fun makeStampPathDash(): Path? {
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


    override fun getViewHeight(): Int {
        return 880
    }
}