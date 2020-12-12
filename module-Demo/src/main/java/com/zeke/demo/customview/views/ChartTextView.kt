package com.zeke.demo.customview.views

import android.animation.Animator
import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.text.TextUtils
import android.util.AttributeSet
import android.view.animation.LinearInterpolator
import androidx.annotation.Keep
import androidx.appcompat.widget.AppCompatTextView
import java.util.*

/**
 * 模拟音乐跳动的随机柱状图效果
 * TODO 增加自定义view的属性配置
 */
class ChartTextView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0)
    : AppCompatTextView(context, attrs, defStyleAttr){
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

    var isPlaying:Boolean = false

    private var rectA = RectF(chartLeft, y + chartTop + Y_OFFSET_MAX, columnWidth, BUTTOM)
    private var rectB = RectF(rectA)
    private var rectC = RectF(rectA)
    var chartPaint = Paint()

    init {
        init()
    }

    companion object{
        const val RESTART_DELAY:Long = 250
        const val DURATION:Long = 2100
        const val DELAY_COLUMN_A:Long = DURATION / 6
        const val chartLeft:Float = 20f
        var chartTop:Float = 20f
        const val Y_OFFSET_MAX:Float = 30f
        const val Y_OFFSET_MID:Float = 10f
        const val Y_OFFSET_MIN:Float = 0f
        const val BUTTOM:Float = 60f

       const val columnWidth:Float = 4f
       const val columnMargin:Float = 4f
       const val columnRightOffset:Float = columnWidth + columnMargin
    }

    private fun init() {
        text = "这只是测试数据啦啦啦啦啦啦啦啦啦啦啦啦啊啦啦啦啦啦只是测试数只是测试数只是测试数只是测试数."
        setTextColor(Color.RED)
        textSize = 28f
        maxLines = 2
        ellipsize = TextUtils.TruncateAt.END
        chartPaint.color = Color.RED
        chartPaint.isAntiAlias = true
        chartPaint.isDither = true
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
        // start()

        postDelayed({
            setPlayerPlaying(true)
        },1500)
    }


    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        setMeasuredDimension(900, 300)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        drawMusicPlayView(canvas)
    }

    private fun drawMusicPlayView(canvas: Canvas) {
        if (isPlaying) {
            rectA.set(x + chartLeft,
                    y + chartTop + offset_y,
                    x + chartLeft + columnMargin,
                    BUTTOM)

            rectB.set(rectA.right + columnMargin,
                    y + chartTop + offset_y2,
                    rectA.right + columnRightOffset,
                    BUTTOM)

            rectC.set(rectB.right + columnMargin,
                    y + chartTop + offset_y3,
                    rectB.right + columnRightOffset,
                    BUTTOM)

            canvas.drawRoundRect(rectA, 3f, 3f, paint)
            canvas.drawRoundRect(rectB, 3f, 3f, paint)
            canvas.drawRoundRect(rectC, 3f, 3f, paint)
            invalidate()
        }
    }

    fun stop(){
        isPlaying = false
        aniColumnA?.cancel()
        aniColumnB?.cancel()
        aniColumnC?.cancel()
    }

    fun start(){
        postDelayed({aniColumnA?.start()},DELAY_COLUMN_A)
        aniColumnB?.start()
        aniColumnC?.start()
    }

    private fun setPlayerPlaying(playing:Boolean){
        isPlaying = true
        if(playing){
            val content = text
            text = "\u3000$content"
            start()
            invalidate()
        }else{
            stop()
        }
    }

    fun recycle(){
        stop()
        aniColumnA = null
        aniColumnB = null
        aniColumnC = null
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        recycle()
    }
}