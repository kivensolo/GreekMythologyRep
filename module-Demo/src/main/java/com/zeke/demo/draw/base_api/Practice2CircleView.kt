package com.zeke.demo.draw.base_api

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import com.zeke.demo.BasePracticeView
import com.zeke.kangaroo.utils.UIUtils

class Practice2CircleView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : BasePracticeView(context, attrs, defStyleAttr) {

    var paintWidth = UIUtils.dip2px(context,20f)

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        //        练习内容：使用 canvas.drawCircle() 方法画圆
        // 1.实心圆
        paint.color = Color.BLACK
        paint.isAntiAlias = true
        paint.style = Paint.Style.FILL
        canvas.drawCircle(150f,150f,100f,paint)

        //2.空心圆
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = 2f
        canvas.drawCircle(400f,150f,100f,paint)

        //3.线宽为 20 的空心圆
        paint.strokeWidth = paintWidth.toFloat()
        canvas.drawCircle(650f,150f,100f,paint)

        //4.蓝色实心圆
        paint.style = Paint.Style.FILL
        paint.color = Color.BLUE
        canvas.drawCircle(900f,150f,100f,paint)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

    }
}
