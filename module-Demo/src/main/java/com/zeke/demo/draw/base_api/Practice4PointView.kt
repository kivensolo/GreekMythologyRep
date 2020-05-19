package com.zeke.demo.draw.base_api

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import com.zeke.demo.BasePracticeView
import com.zeke.demo.model.CardItemConst

class Practice4PointView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : BasePracticeView(context, attrs, defStyleAttr) {

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        //        练习内容：使用 canvas.drawPoint() 方法画点
        //        一个圆点，一个方点
        //        圆点和方点的切换使用 paint.setStrokeCap(cap)：`ROUND` 是圆点，`BUTT` 或 `SQUARE` 是方点
        paint.color = Color.BLACK
        paint.strokeCap = Paint.Cap.ROUND
        paint.strokeWidth = 40f
        canvas.drawPoint(330f, 50f, paint)

        paint.strokeCap = Paint.Cap.BUTT
        canvas.drawPoint(550f, 50f, paint)

        paint.color = Color.RED
        paint.strokeCap = Paint.Cap.SQUARE
        canvas.drawPoint(750f, 50f, paint)
    }

    override fun getViewHeight(): Int {
        return CardItemConst.SMALL_HEIGHT
    }
}
