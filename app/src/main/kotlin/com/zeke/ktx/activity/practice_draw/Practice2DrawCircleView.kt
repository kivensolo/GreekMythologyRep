package com.zeke.ktx.activity.practice_draw

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import com.zeke.kangaroo.utils.UIUtils

class Practice2DrawCircleView : View {
    var paint = Paint()
    var paintWidth = UIUtils.dip2px(context,20f)
    constructor(context: Context) : super(context) {}

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {}

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {}
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        //        练习内容：使用 canvas.drawCircle() 方法画圆
        // 1.实心圆
        paint.color = Color.BLACK
        paint.isAntiAlias = true
        paint.style = Paint.Style.FILL
        canvas.drawCircle(300f,300f,200f,paint)

        //2.空心圆
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = 1f
        canvas.drawCircle(800f,300f,200f,paint)

        //3.线宽为 20 的空心圆
        paint.strokeWidth = paintWidth.toFloat()
        canvas.drawCircle(800f,800f,200f,paint)

        //4.蓝色实心圆
        paint.style = Paint.Style.FILL
        paint.color = Color.BLUE
        canvas.drawCircle(300f,800f,200f,paint)
    }
}
