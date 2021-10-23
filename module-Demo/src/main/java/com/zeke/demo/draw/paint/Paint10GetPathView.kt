package com.zeke.demo.draw.paint

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import com.zeke.demo.base.BasePracticeView
import com.zeke.demo.model.CardItemConst
import com.zeke.kangaroo.utils.UIUtils


/**
 * author: King.Z <br>
 * date:  2020/3/15 14:27 <br>
 * description:  <br>
 *     有效的绘制需要关闭硬件加速 ！！！
 *
 *     getFillPath(Path src, Path dst)  获取实际的path
 *     指的就是 drawPath() 的绘制内容的轮廓，要算上线条宽度和设置的 PathEffect。
 *     src 是原 Path ，而 dst 就是实际 Path 的保存位置。
 *     getFillPath(src, dst) 会计算出实际 Path，然后把结果保存在 dst 里。
 *
 *     getTextPath  获取文字的path.
 *
 */

class Paint10GetPathView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : BasePracticeView(context, attrs, defStyleAttr) {
    val content = "Android Studio 3.6.1"
    private var dstPath:Path = Path()
    private var pathPaint:Paint = Paint()

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        paint.textSize = UIUtils.dip2px(36f).toFloat()
        canvas.drawText(content, 0f, 130f, paint)

        canvas.translate(0f,150f)
        pathPaint.textSize = UIUtils.dip2px(36f).toFloat()
        pathPaint.color = Color.RED
        pathPaint.style = Paint.Style.STROKE
        pathPaint.getTextPath(content,0,content.length,40f,130f,dstPath)
        canvas.drawPath(dstPath,pathPaint)

        canvas.translate(0f,150f)
        pathPaint.color = Color.GREEN
        pathPaint.getTextPath(content,0,content.length,80f,130f,dstPath)
        canvas.drawPath(dstPath,pathPaint)
    }

    override fun getViewHeight(): Int {
        return CardItemConst.LARGE_HEIGHT
    }


}

