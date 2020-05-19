package com.zeke.demo.draw.base_api

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.os.Build
import android.util.AttributeSet
import androidx.annotation.RequiresApi
import com.zeke.demo.BasePracticeView
import com.zeke.demo.model.CardItemConst

class Practice7ArcView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : BasePracticeView(context, attrs, defStyleAttr) {

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        //        练习内容：使用 canvas.drawArc() 方法画弧形和扇形
        paint.style = Paint.Style.FILL // 填充模式
        canvas.drawArc(200f, 100f, 800f, 500f,
                -110f, 100f, true, paint)  // 绘制扇形
        canvas.drawArc(200f, 100f, 800f, 500f,
                20f, 140f, false, paint) // 绘制弧形

        paint.style = Paint.Style.STROKE // 画线模式
        canvas.drawArc(200f, 100f, 800f, 500f,
                180f, 60f, false, paint) // 绘制不封口的弧形
    }

    override fun getViewHeight(): Int {
        return CardItemConst.LARGE_HEIGHT
    }
}
