package com.zeke.ktx.activity.practice_draw

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.view.View

class Practice9DrawPathView : View {

    constructor(context: Context) : super(context) {}

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {}

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {}

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        //        练习内容：使用 canvas.drawPath() 方法画心形
    }
}
