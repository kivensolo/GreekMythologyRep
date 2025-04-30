package com.zeke.demo.customview.views

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Matrix
import android.graphics.Paint
import android.graphics.SweepGradient
import android.util.AttributeSet
import android.view.View
import android.view.animation.LinearInterpolator
import com.zeke.kangaroo.utils.UIUtils
import java.util.Random
import kotlin.math.cos
import kotlin.math.min
import kotlin.math.sin


/**
 * author：ZekeWang
 * date：2025/4/27
 * description： 雷达扫描效果
 */
class RadarView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : View(context, attrs, defStyle) {
    private val SCAN_COLORS = intArrayOf(
        Color.parseColor("#B200FF00"),
        Color.parseColor("#7F00FF00"),
        Color.parseColor("#4C00FF00"),
        Color.parseColor("#0000FF00"))
    // 修改扫描扇形角度（如120度扫描） 动态扫描角度控制
    private val POSITIONS = floatArrayOf(0f, 0.4f, 0.7f,1f)

    private val radarPaint = Paint()
    private val scanPaint = Paint()
    private val randomDotsPaint = Paint()
    private val matrix: Matrix = Matrix()
    private var animator: ValueAnimator? = null
    private var rotateAngle = 0f
    private var centerX = 0f
    private var centerY = 0f

    // 动态目标
    private val targetPoints = mutableListOf<Pair<Float, Float>>()
    private val handler = android.os.Handler()
    private val updateInterval = 2000L // 2秒更新一次

    init {
        init()

        // 新增定时任务
        startRandomPointUpdater()
    }

    private fun init() {
        // 初始化扫描画笔
        scanPaint.style = Paint.Style.FILL
        scanPaint.isAntiAlias = true

        // 初始化背景画笔
        radarPaint.style = Paint.Style.STROKE
        radarPaint.color = Color.DKGRAY // 雷达网格颜色
        radarPaint.strokeWidth = UIUtils.dip2px(1f).toFloat()
        radarPaint.isAntiAlias = true

        //初始化随机点数
        randomDotsPaint.style = Paint.Style.FILL
        randomDotsPaint.color = Color.RED
        randomDotsPaint.isAntiAlias = true

        // 设置动画
        animator = ValueAnimator.ofFloat(0f, 360f)
        animator?.apply {
            setDuration(2000)
            repeatCount = ValueAnimator.INFINITE
            interpolator = LinearInterpolator()
            addUpdateListener(ValueAnimator.AnimatorUpdateListener { animation: ValueAnimator ->
                rotateAngle = 360f - animation.animatedValue as Float
                invalidate()
            })
        }
    }

    /**
     * 添加随机目标点
     */
    private fun generateRandomPoints(count: Int = 5) {
        targetPoints.clear()
        val random = Random()
        val maxRadius = min(centerX, centerY)

        repeat(count) {
            val angle = random.nextFloat() * 360
            val distance = random.nextFloat() * maxRadius
            val x = centerX + (distance * cos(Math.toRadians(angle.toDouble()))).toFloat()
            val y = centerY + (distance * sin(Math.toRadians(angle.toDouble()))).toFloat()
            targetPoints.add(x to y)
        }
        invalidate()
    }

    private fun startRandomPointUpdater() {
        handler.postDelayed(object : Runnable {
            override fun run() {
                generateRandomPoints()
                handler.postDelayed(this, updateInterval)
            }
        }, updateInterval)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        //View中心坐标
        centerX = (w / 2).toFloat()
        centerY = (h / 2).toFloat()

        // 初始化扫描渐变
        val sweepGradient: SweepGradient = SweepGradient(
            centerX, centerY,
            SCAN_COLORS, POSITIONS
        )
        scanPaint.setShader(sweepGradient)
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        animator?.start()
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        animator?.cancel()
        handler.removeCallbacksAndMessages(null)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        // 绘制雷达背景
        drawRadarBackground(canvas)

        // 应用旋转矩阵
        matrix.reset()
        matrix.preRotate(rotateAngle, centerX, centerY)
        scanPaint.shader.setLocalMatrix(matrix)


        // 绘制扫描效果
        canvas.drawCircle(
            centerX, centerY,
            min(centerX.toDouble(), centerY.toDouble()).toFloat(), scanPaint
        )
    }

    private fun drawRadarBackground(canvas: Canvas) {
        val random: Random = Random()

        val maxRadius = min(centerX.toDouble(), centerY.toDouble()).toInt()
        for (i in 1..4) {
            // 绘制同心圆网格：4层同心圆表达距离感
            val radius = maxRadius * i / 4f
            canvas.drawCircle(centerX, centerY, radius, radarPaint)

            // 添加扫描尾迹
            scanPaint.setAlpha(100);
            canvas.drawCircle(centerX, centerY, radius*0.9f, scanPaint);
            scanPaint.setAlpha(255);
        }

        // 绘制十字准线：增强雷达可视化效果
        canvas.drawLine(centerX, 0f, centerX, height.toFloat(), radarPaint)
        canvas.drawLine(0f, centerY, width.toFloat(), centerY, radarPaint)

        // 绘制随机点
        targetPoints.forEach { (x, y) ->
            canvas.drawCircle(x, y, UIUtils.dip2px(5f).toFloat(), randomDotsPaint)
        }
    }

    /**
     * 可选：设置扫描颜色
     */
    fun setScanColors(colors: IntArray) {
        val newShader: SweepGradient = SweepGradient(
            centerX, centerY,
            colors, POSITIONS
        )
        scanPaint.setShader(newShader)
        invalidate()
    }
}