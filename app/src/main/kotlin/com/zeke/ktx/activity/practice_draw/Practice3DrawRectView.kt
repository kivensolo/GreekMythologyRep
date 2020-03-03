package com.zeke.ktx.activity.practice_draw

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import com.zeke.kangaroo.utils.UIUtils

class Practice3DrawRectView : View {

    var paint = Paint()
    var rect = Rect(50,50,950,650)
    var rectF = RectF(50f,750f,950f,1350f)
    constructor(context: Context) : super(context) {}

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {}

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {}

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas
        )
        //练习内容：使用 canvas.drawRect() 方法画矩形
        paint.style = Paint.Style.STROKE
        paint.color = Color.BLACK
        paint.strokeWidth = UIUtils.px2dip(context,30f).toFloat()
        canvas.drawRect(rect,paint)

        paint.color = Color.MAGENTA
        canvas.drawRoundRect(rectF,30f,30f,paint)
    }
}
