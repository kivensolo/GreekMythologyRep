package com.zeke.ktx.demo.draw.text

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.text.Layout
import android.text.StaticLayout
import android.text.TextPaint
import android.util.AttributeSet
import com.zeke.kangaroo.utils.UIUtils
import com.zeke.ktx.demo.draw.BasePracticeView
import com.zeke.ktx.demo.modle.CardItemConst


/**
 * author: King.Z <br>
 * date:  2020/3/29 14:27 <br>
 * description:
 *  使用 Canvas 来进行文字绘制时，无法做到自动换行！
 *  如果要做换行文字绘制，可以用StaticLayout。
 *
 *  StaticLayout 并不是一个 View 或者 ViewGroup ，而是 android.text.Layout 的子类，
 *  它是纯粹用来绘制文字的。
 *   StaticLayout 支持换行，它既可以为文字设置宽度上限来让文字自动换行，也会在 \n 处主动换行。
 *
 *  构造方法是:
 *  StaticLayout(CharSequence source, TextPaint paint, int width,
 *              Layout.Alignment align, float spacingmult, float spacingadd,
 *              boolean includepad)，其中参数里：
 *
 *  width 是文字区域的宽度，文字到达这个宽度后就会自动换行；
 *  align 是文字的对齐方向；
 *  spacingmult 是行间距的倍数，通常情况下填 1 就好；
 *  spacingadd 是行间距的额外增加值，通常情况下填 0 就好；
 *  includepad 是指是否在文字上下添加额外的空间，来避免某些过高的字符的绘制出现越界。
 */
class Paint15TextStaticLayoutView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : BasePracticeView(context, attrs, defStyleAttr) {
    var textPaint:TextPaint = TextPaint()
    private var staticLayout1:StaticLayout
    private var staticLayout2:StaticLayout
    init{
        textPaint.textSize = UIUtils.dip2px(context,18f).toFloat()
        val text1 = "Lorem Ipsum is simply dummy text of the printing and typesetting industry."
        staticLayout1 = StaticLayout(text1, textPaint, 600,
                Layout.Alignment.ALIGN_NORMAL, 1f, 0f, true)
        val text2 = "a\nbc\ndefghi\njklm\nnopqrst\nuvwx\nyz"
        staticLayout2 = StaticLayout(text2, textPaint, 600,
                Layout.Alignment.ALIGN_NORMAL, 1f, 0f, true)
    }
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.save()
        canvas.translate(50f, 50f)
        staticLayout1.draw(canvas)
        canvas.translate(0f, 200f)
        staticLayout2.draw(canvas)
        canvas.restore()
    }

    override fun getViewHeight(): Int {
        return CardItemConst.LARGE_HEIGHT
    }
}

