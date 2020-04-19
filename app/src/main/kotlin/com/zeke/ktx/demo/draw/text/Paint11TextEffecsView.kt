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
class Paint11TextEffecsView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : BasePracticeView(context, attrs, defStyleAttr) {

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        paint.shader = shaderCLAMP
        val x = 10f
        val y1 = 40f
        canvas.drawText("setTextSize(float)  设置文字大小", x, y1, paint)
        val y2 = y1 + 40
        canvas.drawText("setTypeface(typeface)  设置字体", x, y2, paint)

        val y3 = y2 + 40
        canvas.drawText("setStrikeThruText(boolean)  是否加删除线。", x, y3, paint)

        val y4 = y3 + 40
        canvas.drawText("setUnderlineText(boolean)  是否加下划线。", x, y4, paint)
        val y5 = y4 + 40
        canvas.drawText("setTextSkewX(float)  文字横向错切角度", x, y5, paint)
        val y6 = y5 + 40
        canvas.drawText("setTextScaleX(float)  文字横向放缩", x, y6, paint)
        val y7 = y6 + 40
        canvas.drawText("setLetterSpacing(float) 字符间距 默认值0", x, y7, paint)
        val y8 = y7 + 40
        canvas.drawText("setTextAlign(Paint.Align) 对齐方式,默认值LEFT", x, y8, paint)
    }

    override fun getViewHeight(): Int {
        return 400
    }
}

