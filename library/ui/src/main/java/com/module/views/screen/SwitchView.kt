package com.module.views.screen

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.content.Context
import android.graphics.BitmapShader
import android.graphics.Canvas
import android.graphics.Matrix
import android.graphics.Paint
import android.graphics.Shader
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.util.TypedValue
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.animation.AccelerateInterpolator
import android.view.animation.DecelerateInterpolator
import android.widget.FrameLayout
import com.module.tools.BezierCurveHelper
import com.module.tools.ScreenTools
import com.module.views.R
import com.module.views.databinding.ViewTextBinding

/**
 * Author: liliangyi
 * Maintainer: liliangyi
 * Date: 2020/7/21
 * binding.desc: 切换效果的View
 */
class SwitchView : FrameLayout {
    private var binding:ViewTextBinding = ViewTextBinding.inflate((LayoutInflater.from(context)))

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)


    companion object {
        private const val TAG = "SwitchView"
        private const val DURATION_TIME = 1500L
    }

    private val mMatrix by lazy { Matrix() }
    private val paint by lazy { Paint() }
    private var textLayout: View? = null

    private val screenWidth: Float = ScreenTools.getScreenWidth(context).toFloat()
    private val screenHeight: Float = ScreenTools.getScreenWidth(context).toFloat()

    init {
        // 设置viewGroup的onDraw可重写(可处理onDraw()方法不被执行的解决方法)
        setWillNotDraw(false)
    }

    fun setDrawable(drawable: Drawable) {
        if (drawable is BitmapDrawable) {
            paint.shader = BitmapShader(drawable.bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)
            postInvalidate()
        }
    }

    /**
     *  矩阵偏移位置
     */
    private fun move(dx: Float, dy: Float = 0f) {
        mMatrix.postTranslate(dx, dy)
        postInvalidate()
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        paint.shader?.setLocalMatrix(mMatrix)
        canvas?.drawRect(0f, 0f, screenWidth, screenHeight, paint)
    }

    /**
     *  添加文本布局
     */
    fun addTextLayout(titleTxt: String? = null, descTxt: String? = null) {

        if (titleTxt.isNullOrEmpty() && descTxt.isNullOrEmpty()) return

        if (textLayout == null) {
            textLayout = View.inflate(context, R.layout.view_text, null)
            textLayout?.layoutParams = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
                    .also {
                        it.gravity = Gravity.BOTTOM or Gravity.START
                        it.bottomMargin = 54
                        it.marginStart = 64
                    }

            addView(textLayout)
            binding.desc.setLineSpacing(10f, 1.0f)
        }
        // 处理数据
        if (titleTxt.isNullOrEmpty()) {
            binding.title.visibility = View.GONE
            binding.desc.maxLines = 3
            binding.desc.maxEms = 16
            binding.desc.layoutParams.width = 660
            binding.desc.setTextSize(TypedValue.COMPLEX_UNIT_PX, 40f)
        } else {
            binding.title.text = titleTxt
        }
        if (descTxt.isNullOrEmpty()) {
            binding.desc.visibility = View.GONE
        } else {
            binding.desc.text = descTxt
        }
    }

    private var lastValue = 0f

    /**
     *  移除动画
     */
    fun animOut(isPre: Boolean, func: () -> Unit) {
        val animator =
                if (isPre)
                    ObjectAnimator.ofFloat(this, View.TRANSLATION_X, -screenWidth, 0f)
                            .setDuration(DURATION_TIME)
                else
                    ObjectAnimator.ofFloat(this, View.TRANSLATION_X, 0f, -screenWidth)
                            .setDuration(DURATION_TIME)
        animator.addUpdateListener {
            val animatedValue = it.animatedValue
            // 计算matrix矩阵需要同步反方向平移距离
            move(lastValue - animatedValue as Float)
            lastValue = animatedValue
        }
        animator.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator?) {
                super.onAnimationEnd(animation)
                func()
                // 动画结束还原状态
                reset()
            }
        })
        animator.interpolator = BezierCurveHelper.getInterpolator(BezierCurveHelper.Config.MODE_025F_010F_025F_1F)
        animator.start()

        textLayoutAnimOut(isPre)
    }

    /**
     *  移入动画
     */
    fun animIn(isPre: Boolean) {
        textLayoutAnimIn(isPre)
    }

    /**
     *  文本布局退出动画
     */
    private fun textLayoutAnimOut(isPre: Boolean) {
        textLayout?.also {
            // 重置当前偏移
            it.translationX = 0f
            // 透明度渐变
            ObjectAnimator.ofFloat(it, View.ALPHA, if (isPre) 0f else 1f, if (isPre) 1f else 0f)
                    .setDuration(DURATION_TIME)
                    .apply {
                        interpolator = if (isPre) AccelerateInterpolator(1.2f)
                        else DecelerateInterpolator(2f)
                    }.start()
        }
    }

    /**
     *  文本布局进入动画
     */
    private fun textLayoutAnimIn(isPre: Boolean) {
        textLayout?.also {
            // 位移动画
            ObjectAnimator.ofFloat(it, View.TRANSLATION_X, if (isPre) 0f else width.toFloat(), if (isPre) width.toFloat() else 0f)
                    .setDuration(DURATION_TIME)
                    .apply { interpolator = BezierCurveHelper.getInterpolator(BezierCurveHelper.Config.MODE_025F_010F_025F_1F) }
                    .start()
            // 透明度变化
            ObjectAnimator.ofFloat(it, View.ALPHA, if (isPre) 1f else 0f, if (isPre) 0f else 1f)
                    .setDuration(DURATION_TIME)
                    .apply {
                        interpolator = if (isPre) DecelerateInterpolator(2f)
                        else AccelerateInterpolator(0.5f)
                    }.start()
        }
    }

    /**
     *  还原
     */
    private fun reset() {
        translationX = 0f
        textLayout?.alpha = 1f
        mMatrix.setTranslate(0f, 0f)
        lastValue = 0f
        invalidate()
    }
}