package com.zeke.demo.draw.base_api

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.os.Build
import android.util.AttributeSet
import androidx.annotation.RequiresApi
import com.zeke.demo.base.BasePracticeView
import com.zeke.demo.model.CardItemConst
import com.zeke.kangaroo.magicindicator.buildins.UIUtil

class Practice7ArcView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : BasePracticeView(context, attrs, defStyleAttr) {

    private val rectBound = RectF(200f,100f,800f,500f)

    /**
     * 使用 canvas.drawArc() 方法画弧形和扇形
     */
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        paint.style = Paint.Style.STROKE
        paint.color = Color.RED
        canvas.drawRoundRect(rectBound,0f,0f,paint)

        //限定绘制范围，不限定的话,弧形还是可以正常显示
        val saveLayer = canvas.saveLayer(rectBound, paint)

        paint.color = Color.BLACK
        // 绘制不封口的弧形
        paint.strokeWidth = UIUtil.dip2px(context, 10.0).toFloat()
        canvas.drawArc(rectBound,170f, 60f, false, paint)

        paint.strokeWidth = UIUtil.dip2px(context, 1.0).toFloat()
        paint.style = Paint.Style.FILL
        // 绘制扇形
        canvas.drawArc(rectBound,-110f, 100f, true, paint)
        // 绘制弧形
        canvas.drawArc(rectBound,20f, 140f, false, paint)

        canvas.restoreToCount(saveLayer)
    }

    override fun getViewHeight(): Int {
        return CardItemConst.LARGE_HEIGHT
    }
}
