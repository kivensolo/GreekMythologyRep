package com.zeke.demo.customview.views

import android.animation.Animator
import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import android.view.animation.LinearInterpolator
import androidx.annotation.Keep
import com.zeke.demo.R
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
    var columnColor:Int = 0

    var pauseTime:Long = 200
    var duration:Long = 1800
    var delayStartOfColumnA:Long = duration / 6  //这个除6 不能变
    private var offsetYMAX:Float = 200f
    private var offsetYMID:Float = 100f
    private var offsetYMIN:Float = 0f

    var columnWidth:Float = 20f
    var columnMargin:Float = 20f
    var columnRightOffset:Float = columnWidth + columnMargin

    private var rectA = RectF(0f, offsetYMAX, columnWidth, height.toFloat())
    private var rectB = RectF(rectA)
    private var rectC = RectF(rectA)
    var paint = Paint()

    init {
        getCustomArray(context, attrs, defStyleAttr)
        init()
    }

    private fun getCustomArray(context: Context, attrs: AttributeSet?, defStyle: Int) {
        val typedArray = context.theme.obtainStyledAttributes(attrs, R.styleable.chartMusicView, defStyle, 0)
        val typeCount = typedArray.indexCount
        for (i in 0 until typeCount) {
            when(val attr = typedArray.getIndex(i)){
                R.styleable.chartMusicView_columnWidth -> {
                    columnWidth = typedArray.getDimension(attr,
                            TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX,20f, resources.displayMetrics)
                    )
                }
                R.styleable.chartMusicView_columnMargin -> {
                    columnMargin = typedArray.getDimension(attr,
                            TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX,20f, resources.displayMetrics)
                    )
                }
                R.styleable.chartMusicView_columnColor ->{
                    columnColor = typedArray.getColor(attr, Color.RED)
                }
                R.styleable.chartMusicView_duration -> {
                    duration = typedArray.getInteger(attr, 1800).toLong()
                }
                R.styleable.chartMusicView_pauseTime -> {
                    pauseTime = typedArray.getInteger(attr, 200).toLong()
                }
            }
        }
        typedArray.recycle()
    }


    private fun init() {
        paint.color = columnColor
        paint.isAntiAlias = true
        paint.isDither = true
        setBackgroundColor(Color.TRANSPARENT)

        val vHolder1 = PropertyValuesHolder.ofFloat("offset_y", offsetYMAX, offsetYMIN, offsetYMAX, offsetYMAX)
        aniColumnA = ObjectAnimator.ofPropertyValuesHolder(this, vHolder1)
        aniColumnA?.duration = duration
        aniColumnA?.interpolator = LinearInterpolator()
        // lengthAnimation?.repeatMode = ValueAnimator.RESTART
        // lengthAnimation?.repeatCount = ValueAnimator.INFINITE
        aniColumnA?.addListener(object : Animator.AnimatorListener {
            override fun onAnimationRepeat(animation: Animator?) {
            }
            override fun onAnimationEnd(animation: Animator?) {
                postDelayed({ aniColumnA?.start() },pauseTime)
            }

            override fun onAnimationCancel(animation: Animator?) {}
            override fun onAnimationStart(animation: Animator?) {}

        })

        val vHolder2 = PropertyValuesHolder.ofFloat("offset_y2", offsetYMAX, offsetYMID, offsetYMID, offsetYMAX)
        aniColumnB = ObjectAnimator.ofPropertyValuesHolder(this, vHolder2)
        aniColumnB?.duration = duration
        aniColumnB?.interpolator = LinearInterpolator()
        aniColumnB?.addListener(object : Animator.AnimatorListener {
            override fun onAnimationRepeat(animation: Animator?) {}
            override fun onAnimationEnd(animation: Animator?) {
                postDelayed({ aniColumnB?.start() },pauseTime)
            }
            override fun onAnimationCancel(animation: Animator?) {}
            override fun onAnimationStart(animation: Animator?) {}

        })

        val vHolder3 = PropertyValuesHolder.ofFloat("offset_y3", offsetYMAX, offsetYMIN, offsetYMAX, offsetYMAX)
        aniColumnC = ObjectAnimator.ofPropertyValuesHolder(this, vHolder3)
        aniColumnC?.duration = duration
        aniColumnC?.interpolator = LinearInterpolator()
        aniColumnC?.addListener(object : Animator.AnimatorListener {
            override fun onAnimationRepeat(animation: Animator?) {}
            override fun onAnimationEnd(animation: Animator?) {
                postDelayed({ aniColumnC?.start() },pauseTime)
            }
            override fun onAnimationCancel(animation: Animator?) {}
            override fun onAnimationStart(animation: Animator?) {}
        })

        // 自动启动动画
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
        postDelayed({aniColumnA?.start()},delayStartOfColumnA)
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