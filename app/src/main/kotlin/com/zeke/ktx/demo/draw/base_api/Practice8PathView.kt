package com.zeke.ktx.demo.draw.base_api

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Path
import android.os.Build
import android.util.AttributeSet
import com.zeke.ktx.demo.BasePracticeView
import com.zeke.ktx.demo.modle.CardItemConst

class Practice8PathView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : BasePracticeView(context, attrs, defStyleAttr) {

    var path: Path = Path()
    init {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            path.addArc(200f, 200f, 400f, 400f, -225f, 225f)
            path.arcTo(400f, 200f, 600f, 400f, -180f, 225f, false)
        }
        path.lineTo(400f, 542f)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        //练习内容：使用 canvas.drawPath() 方法画心形
        paint.color = Color.RED
        canvas.drawPath(path, paint)
    }

    override fun getViewHeight(): Int {
        return CardItemConst.LARGE_HEIGHT
    }
}
