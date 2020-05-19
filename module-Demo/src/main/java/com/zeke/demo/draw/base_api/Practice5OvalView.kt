package com.zeke.demo.draw.base_api

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.os.Build
import android.util.AttributeSet
import androidx.annotation.RequiresApi
import com.zeke.demo.BasePracticeView

class Practice5OvalView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : BasePracticeView(context, attrs, defStyleAttr) {

    var ovalRect:RectF = RectF(350f, 20f, 650f, 280f)
    var ovalRectD:RectF = RectF(680f, 20f, 980f, 280f)
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        // 练习内容：使用 canvas.drawOval() 方法画椭圆
        paint.style = Paint.Style.FILL
        canvas.drawOval(20f, 20f, 320f, 280f, paint)

        paint.strokeWidth = 10f
        paint.style = Paint.Style.STROKE
        canvas.drawOval(ovalRect, paint)

        paint.isDither = true
        canvas.drawOval(ovalRectD, paint)
    }
}
