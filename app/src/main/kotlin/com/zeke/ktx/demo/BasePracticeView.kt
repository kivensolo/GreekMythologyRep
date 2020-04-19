package com.zeke.ktx.demo

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import com.zeke.kangaroo.utils.UIUtils
import com.zeke.ktx.demo.modle.CardItemConst

open class BasePracticeView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    var paint = Paint()

    // Demo统一的LinearGradient效果
    var shaderMIRROR: LinearGradient = LinearGradient(10f, 10f, 1000f, 350f,
            Color.parseColor("#E91E63"), Color.parseColor("#2196F3"),
            Shader.TileMode.REPEAT)
    var shaderCLAMP:LinearGradient = LinearGradient(0f, 150f, 250f, 550f,
            Color.parseColor("#E91E63"),Color.parseColor("#2196F3"),
            Shader.TileMode.CLAMP)
    var shaderREPEAT:LinearGradient = LinearGradient(500f, 150f, 800f, 550f,
            Color.parseColor("#E91E63"),Color.parseColor("#2196F3"),
            Shader.TileMode.REPEAT)
    companion object{
        const val defaultFontSize :Float = 16f
    }

    init{
        paint.textSize = UIUtils.dip2px(context, defaultFontSize).toFloat()
        paint.isAntiAlias = true
        paint.isDither = true
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val widthMode = MeasureSpec.getMode(widthMeasureSpec) //获取宽的模式
        val widthSize = MeasureSpec.getSize(widthMeasureSpec) //获取宽的尺寸
        val width = if (widthMode == MeasureSpec.AT_MOST ||
                widthMode == MeasureSpec.EXACTLY) {
            //如果match_parent或者具体的值，直接使用测量的值
            widthSize
        } else {
            //自己决定宽高
            getViewWidth()
        }

        val heightMode = MeasureSpec.getMode(heightMeasureSpec) //获取高的模式
        val heightSize = MeasureSpec.getSize(heightMeasureSpec) //获取高的尺寸
        val height = if (heightMode == MeasureSpec.AT_MOST) {
            heightSize
        } else {
            getViewHeight()
        }
        setMeasuredDimension(width, height)
    }

    open fun getViewWidth():Int{
        return CardItemConst.NORMAL_WIDTH
    }

    open fun getViewHeight():Int{
        return CardItemConst.NORMAL_HEIGHT
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
    }
}