package com.zeke.ktx.demo.draw.base_api

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.util.AttributeSet
import com.zeke.ktx.demo.BasePracticeView

class Practice6LineView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : BasePracticeView(context, attrs, defStyleAttr) {

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        //        练习内容：使用 canvas.drawLine() 方法画直线
        paint.strokeWidth = 5f
        paint.color = Color.BLACK
        val points =
                floatArrayOf(20f, 20f, 120f, 20f,
                        70f, 20f, 70f, 120f,
                        20f, 120f, 120f, 120f,
                        150f, 20f, 250f, 20f,
                        150f, 20f, 150f, 120f,
                        250f, 20f, 250f, 120f,
                        150f, 120f, 250f, 120f)
        canvas.drawLines(points, paint)

        paint.color = Color.MAGENTA
        canvas.drawLine(10f, 10f, 800f, 10f, paint)
    }

    override fun getViewHeight(): Int {
        return 150
    }
}

