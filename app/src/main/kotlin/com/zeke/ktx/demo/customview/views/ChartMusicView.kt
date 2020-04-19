package com.zeke.ktx.demo.customview.views

import android.animation.Animator
import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.support.annotation.Keep
import android.util.AttributeSet
import android.view.View
import android.view.animation.LinearInterpolator
import java.util.*

/**
 * 模拟音乐跳动的随机柱状图效果
 */
class ChartMusicView @JvmOverloads constructor(context: Context,
      attrs: AttributeSet? = null, defStyleAttr: Int = 0)
    : View(context, attrs, defStyleAttr) {
    var aniColumnC:ObjectAnimator ? = null
    var aniColumnB:ObjectAnimator ? = null
    var aniColumnA:ObjectAnimator ? = null
    var random: Random = Random()
    @Keep
    var offset_y:Float = 0f
    @Keep
    var offset_y2:Float = 0f
    @Keep
    var offset_y3:Float = 0f

    private var rectA = RectF(0f, Y_OFFSET_MAX, columnWidth, height.toFloat())
    private var rectB = RectF(rectA)
    private var rectC = RectF(rectA)
    var paint = Paint()

    init {
        init()
    }

    companion object{
        const val RESTART_DELAY:Long = 200
        const val DURATION:Long = 1800
        const val DELAY_COLUMN_A:Long = DURATION / 6
        const val Y_OFFSET_MAX:Float = 200f
        const val Y_OFFSET_MID:Float = 100f
        const val Y_OFFSET_MIN:Float = 0f

       const val columnWidth:Float = 20f
       const val columnMargin:Float = 20f
       const val columnRightOffset:Float = columnWidth + columnMargin
    }

    private fun init() {
        paint.color = Color.RED
        paint.isAntiAlias = true
        paint.isDither = true
        setBackgroundColor(Color.TRANSPARENT)

        val vHolder1 = PropertyValuesHolder.ofFloat("offset_y", Y_OFFSET_MAX, Y_OFFSET_MIN, Y_OFFSET_MAX, Y_OFFSET_MAX)
        aniColumnA = ObjectAnimator.ofPropertyValuesHolder(this, vHolder1)
        aniColumnA?.duration = DURATION
        aniColumnA?.interpolator = LinearInterpolator()
        // lengthAnimation?.repeatMode = ValueAnimator.RESTART
        // lengthAnimation?.repeatCount = ValueAnimator.INFINITE
        aniColumnA?.addListener(object : Animator.AnimatorListener {
            override fun onAnimationRepeat(animation: Animator?) {
            }
            override fun onAnimationEnd(animation: Animator?) {
                postDelayed({ aniColumnA?.start() },RESTART_DELAY)
            }

            override fun onAnimationCancel(animation: Animator?) {}
            override fun onAnimationStart(animation: Animator?) {}

        })

        val vHolder2 = PropertyValuesHolder.ofFloat("offset_y2", Y_OFFSET_MAX, Y_OFFSET_MID, Y_OFFSET_MID, Y_OFFSET_MAX)
        aniColumnB = ObjectAnimator.ofPropertyValuesHolder(this, vHolder2)
        aniColumnB?.duration = DURATION
        aniColumnB?.interpolator = LinearInterpolator()
        aniColumnB?.addListener(object : Animator.AnimatorListener {
            override fun onAnimationRepeat(animation: Animator?) {}
            override fun onAnimationEnd(animation: Animator?) {
                postDelayed({ aniColumnB?.start() },RESTART_DELAY)
            }
            override fun onAnimationCancel(animation: Animator?) {}
            override fun onAnimationStart(animation: Animator?) {}

        })

        val vHolder3 = PropertyValuesHolder.ofFloat("offset_y3", Y_OFFSET_MAX, Y_OFFSET_MIN, Y_OFFSET_MAX, Y_OFFSET_MAX)
        aniColumnC = ObjectAnimator.ofPropertyValuesHolder(this, vHolder3)
        aniColumnC?.duration = DURATION
        aniColumnC?.interpolator = LinearInterpolator()
        aniColumnC?.addListener(object : Animator.AnimatorListener {
            override fun onAnimationRepeat(animation: Animator?) {}
            override fun onAnimationEnd(animation: Animator?) {
                postDelayed({ aniColumnC?.start() },RESTART_DELAY)
            }
            override fun onAnimationCancel(animation: Animator?) {}
            override fun onAnimationStart(animation: Animator?) {}
        })
        start()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        setMeasuredDimension(300, 300)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        rectA.set(0f,
                offset_y,
                columnMargin,
                measuredHeight.toFloat())

        rectB.set(rectA.right + columnMargin,
                offset_y2,
                rectA.right + columnRightOffset,
                measuredHeight.toFloat())

        rectC.set(rectB.right + columnMargin,
                offset_y3,
                rectB.right + columnRightOffset,
                measuredHeight.toFloat())

        canvas.drawRoundRect(rectA,10f,10f, paint)
        canvas.drawRoundRect(rectB,10f,10f, paint)
        canvas.drawRoundRect(rectC,10f,10f, paint)
        invalidate()
    }

    fun stop(){
        aniColumnA?.cancel()
        aniColumnB?.cancel()
        aniColumnC?.cancel()
    }

    fun start(){
        postDelayed({aniColumnA?.start()},DELAY_COLUMN_A)
        aniColumnB?.start()
        aniColumnC?.start()
    }

    fun destory(){
        stop()
        aniColumnA = null
        aniColumnB = null
        aniColumnC = null
    }

}