package com.zeke.demo.draw.base_api

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import com.zeke.demo.BasePracticeView
import com.zeke.kangaroo.utils.UIUtils

class Practice3RectView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : BasePracticeView(context, attrs, defStyleAttr) {

    var rect = Rect(50, 50, 500, 350)
    private var rectF_Inner = RectF(100f, 100f, 450f, 300f)
    private var rectF = RectF(550f, 50f, 1000f, 350f)

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas
        )
        //练习内容：使用 canvas.drawRect() 方法画矩形
        //练习内容：使用 canvas.drawRoundRect() 方法画圆角矩形
        paint.style = Paint.Style.STROKE
        paint.color = Color.BLACK
        paint.strokeWidth = UIUtils.px2dip(context, 30f).toFloat()
        canvas.drawRect(rect, paint)

        paint.style = Paint.Style.FILL
        paint.color = Color.MAGENTA
        canvas.drawRoundRect(rectF_Inner, 60f, 60f, paint)

        paint.style = Paint.Style.STROKE
        canvas.drawRoundRect(rectF, 40f, 40f, paint)
    }

    override fun getViewHeight(): Int {
        return 400
    }
}
