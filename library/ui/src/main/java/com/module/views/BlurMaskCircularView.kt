package com.module.views

import android.content.Context
import android.graphics.BlurMaskFilter
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import com.module.UIUtil


/**
 * author：ZekeWang
 * date：2021/12/17
 * description：周围有blur颜色效果的圆形View
 */
class BlurMaskCircularView(context: Context, attrs: AttributeSet?, defStyle: Int) :
    View(context, attrs, defStyle) {
    //光晕画笔
    private lateinit var blurPaint: Paint
    private var radius = 0f

    @JvmOverloads
    constructor(context: Context, attrs: AttributeSet? = null) : this(context, attrs, 0) {
        radius = UIUtil.dip2px(context, 80f).toFloat()
        init()
    }

    private fun init() {
        setLayerType(LAYER_TYPE_SOFTWARE, null)
        blurPaint = Paint().apply {
            color = Color.RED
            //BlurMaskFilter的radius是模糊半径
            maskFilter = BlurMaskFilter(radius + 60, BlurMaskFilter.Blur.SOLID)
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        with(canvas) {
            if(isInEditMode){
                blurPaint.color = Color.YELLOW
            }
            translate((width / 2).toFloat(), (height / 2.3).toFloat())
            drawCircle(0f, 0f, radius, blurPaint)
        }
    }

    /**
     * 刷新颜色zhi
     *
     * @param color
     */
    fun setColor(color: Int) {
        blurPaint.color = color
        invalidate()
    }

    /**
     * 取当前颜色
     *
     * @return 颜色值
     */
    fun getColor(): Int {
        return blurPaint.color
    }
}