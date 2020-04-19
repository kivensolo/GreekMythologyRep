package com.zeke.ktx.demo.draw.paint

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.os.Build
import android.support.annotation.RequiresApi
import android.util.AttributeSet
import com.zeke.ktx.demo.BasePracticeView
import com.zeke.ktx.demo.modle.CardItemConst


/**
 * author: King.Z <br>
 * date:  2020/3/15 14:27 <br>
 * description:  <br>
 *     效果
 */

class Paint6NormalEffectView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : BasePracticeView(context, attrs, defStyleAttr) {

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        paint.shader = shaderMIRROR
        val x = 10f
        val y1 = 40f
        canvas.drawText("setAntiAlias(boolean) 设置抗锯齿", x, y1, paint)
        val y2 = y1 + 40
        canvas.drawText("setStyle(Paint.Style)  FILL/STROKE", x, y2, paint)

        val y3 = y2 + 40
        canvas.drawText("setStrokeCap(Paint.Cap)  BUTT、ROUND、方头。默认为 BUTT", x, y3, paint)

        val y4 = y3 + 40
        canvas.drawText("setStrokeJoin(Paint.Join join):", x, y4, paint)
        val y5 = y4 + 40
        canvas.drawText("     MITER 尖角、BEVEL 平角、ROUND 圆角", x, y5, paint)

    }

    override fun getViewHeight(): Int {
        return CardItemConst.NORMAL_HEIGHT
    }
}