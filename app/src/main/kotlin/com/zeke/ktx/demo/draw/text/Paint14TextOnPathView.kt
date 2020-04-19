package com.zeke.ktx.demo.draw.text
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import com.zeke.ktx.demo.BasePracticeView

/**
 * author: King.Z <br>
 * date:  2020/3/29 14:27 <br>
 * description: 沿着一条 Path 来绘制文字 <br>
 *     记住一条原则： drawTextOnPath() 使用的 Path ，拐弯处全用圆角，别用尖角。
 *
 *  drawTextOnPath(String text, Path path, float hOffset, float vOffset, Paint paint)
 *
 *  hOffset 和 vOffset。它们是文字相对于 Path 的水平偏移量和竖直偏移量，利用它们可以调整文字的位置。
 *  例如设置 hOffset 为 5， vOffset 为 10，文字就会右移 5 像素和下移 10 像素。
 */
class Paint14TextOnPathView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : BasePracticeView(context, attrs, defStyleAttr) {

    private val path:Path = makeFollowPath()
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.translate(0f,50f)
        paint.style = Paint.Style.STROKE
        paint.strokeJoin = Paint.Join.ROUND
        paint.strokeWidth = 2f
        canvas.drawPath(path, paint) // 把 Path 也绘制出来，理解起来更方便
        paint.style = Paint.Style.FILL
        paint.textSize = 40f
        canvas.drawTextOnPath("Hello ZekeWang, It's a good job!", path, 0f, -5f, paint)
    }

    /**
     * 制作随机Path路径
     */
    private fun makeFollowPath(): Path {
        val p = Path()
        p.moveTo(0f, 0f)
        //画15个阶段的路径
        for (i in 1..5) {
            p.lineTo(i * 200f, Math.random().toFloat() * 250)
        }
        return p
    }
}

