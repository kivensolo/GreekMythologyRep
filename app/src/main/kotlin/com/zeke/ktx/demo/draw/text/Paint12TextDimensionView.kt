package com.zeke.ktx.demo.draw.text
import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import com.zeke.ktx.demo.BasePracticeView

/**
 * author: King.Z <br>
 * date:  2020/3/29 14:27 <br>
 * description:
 */
class Paint12TextDimensionView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : BasePracticeView(context, attrs, defStyleAttr) {

    val offSetY = 45f
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        paint.shader = shaderCLAMP
        val x = 10f
        val y1 = 40f
        canvas.drawText("getFontSpacing()  获取推荐的行距", x, y1, paint)
        val y2 = y1 + offSetY
        canvas.drawText("getFontMetrics() 获取 Paint 的 FontMetrics", x, y2, paint)

        val y3 = y2 + offSetY
        canvas.drawText("getTextBounds(String,int start,int end,Rect )", x, y3, paint)

        val y4 = y3 + offSetY
        canvas.drawText("measureText(String) 测量文字的宽度并返回", x, y4, paint)

        val y5 = y4 + offSetY
        canvas.drawText("getTextWidths(String, float[]) 获取字符串中每个字符的宽度", x, y5, paint)
    }

    override fun getViewHeight(): Int {
        return 350
    }
}

