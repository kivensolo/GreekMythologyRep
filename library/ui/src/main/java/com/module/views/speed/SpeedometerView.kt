package com.module.views.speed

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.graphics.RectF
import android.graphics.Typeface
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import com.module.views.R
import java.util.Locale
import kotlin.math.cos
import kotlin.math.min
import kotlin.math.sin

/**
 * date: 2025/9/30
 * description: 速率仪表盘自定义View
 *
 * 整体绘制的坐标体系如下：
 *            270°
 *             ↑
 *             |
 *             |
 * 180° — — — — — — — — —> 0°
 *             |
 *             |
 *             |
 *            90°
 * 故起始角度为135°，表盘扫描范围为270°，终点角度为405°
 */
class SpeedometerView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val pointerPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val path = Path()
    private val rectF = RectF()

    // 默认参数
    private var minValue = 0
    private var maxValue = 100
    // 当前速度对应的UI进度值[min,max]
    private var currentProgressValue = 0F
    // 当前速度值>=min
    private var currentSpeedValue = 0F

    private var padding = 0f
    // 刻度速率文本大小
    private var scaleTextSize = spToPx(11f)
    // 中心速率文本大小
    private var centerTextSize = spToPx(20f)

    // 颜色配置
    private var pointerColor = Color.BLACK
    private var progressColor = Color.RED
    private var progressBgColor = Color.parseColor("#E0E0E0") // 浅灰色
    private var currentValueTextColor = Color.BLACK
    private var dialColor = Color.BLACK // 表盘颜色

    // 动画
    private var animationDuration = 500
    private var animator: ValueAnimator? = null

    // 角度计算相关 - 根据图片调整角度范围
    private val startAngle = 135f  // 起始角度 (7点钟位置)
    private val sweepAngle = 270f  // 扫描角度 (从7点到4点位置)

    // 圆弧区域宽度
    private var arcWidth = dpToPx(13f)

    // 仪表盘的中心坐标和半径
    private var centerX = 0f
    private var centerY = 0f
    private var radius = 0f

    init {
        attrs?.let {
            val typedArray = context.obtainStyledAttributes(it, R.styleable.SpeedometerView)
            minValue = typedArray.getInt(R.styleable.SpeedometerView_minValue, 0)
            maxValue = typedArray.getInt(R.styleable.SpeedometerView_maxValue, 100)
            val currentValue = typedArray.getInt(R.styleable.SpeedometerView_currentValue, minValue)
            currentSpeedValue = currentValue.toFloat()
            currentProgressValue = currentValue.coerceIn(minValue, maxValue).toFloat()

            pointerColor = typedArray.getColor(R.styleable.SpeedometerView_pointerColor, Color.BLACK)
            progressColor = typedArray.getColor(R.styleable.SpeedometerView_progressColor, Color.RED)
            progressBgColor = typedArray.getColor(R.styleable.SpeedometerView_progressBgColor, Color.parseColor("#E0E0E0"))
            currentValueTextColor = typedArray.getColor(R.styleable.SpeedometerView_currentValueTextColor, Color.BLACK)
            dialColor = typedArray.getColor(R.styleable.SpeedometerView_dialColor, Color.BLACK)
            animationDuration = typedArray.getInt(R.styleable.SpeedometerView_animationDuration, 1000)
            typedArray.recycle()
        }

        // 初始化指针画笔
        pointerPaint.color = pointerColor
        pointerPaint.strokeWidth = dpToPx(3f)
        pointerPaint.typeface = Typeface.DEFAULT
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val width = width
        val height = height
        centerX = width / 2f
        centerY = height / 2f
        radius = min(width, height) / 2f - padding

        drawProgress(canvas)
        drawScales(canvas)
        drawPointer(canvas)
        drawCenterText(canvas, centerX, centerY)
    }

    /**
     * 绘制进度和进度的背景
     */
    private fun drawProgress(canvas: Canvas) {
        val startAngle = valueToAngle(minValue.toFloat())
        //Draw progress background
        val bkgEndAngle = valueToAngle(maxValue.toFloat())
        val bkgSweepAngle = bkgEndAngle - startAngle
        drawRoundedArc(canvas, startAngle, bkgSweepAngle, progressBgColor)

        //Draw progress background
        if(currentProgressValue == minValue.toFloat()){
           return
        }
        val endAngle = valueToAngle(currentProgressValue)
        val sweepAngle = endAngle - startAngle
        drawRoundedArc(canvas,startAngle, sweepAngle, progressColor)
    }

    private fun drawRoundedArc(
        canvas: Canvas,
        startAngle: Float,
        sweepAngle: Float,
        color: Int
    ) {
        // 重置路径
        path.reset()

        // 计算圆弧的矩形边界
        val outerRadius = radius
        val innerRadius = radius - arcWidth

        // 绘制圆弧主体
        rectF.set(centerX - outerRadius, centerY - outerRadius, centerX + outerRadius, centerY + outerRadius)
        path.arcTo(rectF, startAngle, sweepAngle)

        // 连接到内圆弧
        rectF.set(centerX - innerRadius, centerY - innerRadius, centerX + innerRadius, centerY + innerRadius)
        path.arcTo(rectF, startAngle + sweepAngle, -sweepAngle)

        // 闭合路径
        path.close()

        // 填充路径
        paint.color = color
        paint.style = Paint.Style.FILL
        paint.typeface = Typeface.DEFAULT
        canvas.drawPath(path, paint)

        // 绘制圆头端帽
        drawRoundedCap(canvas, centerX, centerY, outerRadius, innerRadius, startAngle, color)
        drawRoundedCap(canvas, centerX, centerY, outerRadius, innerRadius, startAngle + sweepAngle, color)
    }

    private fun drawRoundedCap(
        canvas: Canvas,
        centerX: Float,
        centerY: Float,
        outerRadius: Float,
        innerRadius: Float,
        angle: Float,
        color: Int
    ) {
        val angleRad = Math.toRadians(angle.toDouble()).toFloat()
        val capCenterX = centerX + (outerRadius + innerRadius) / 2 * cos(angleRad)
        val capCenterY = centerY + (outerRadius + innerRadius) / 2 * sin(angleRad)
        val capRadius = (outerRadius - innerRadius) / 2

        paint.color = color
        paint.style = Paint.Style.FILL
        paint.typeface = Typeface.DEFAULT
        canvas.drawCircle(capCenterX, capCenterY, capRadius, paint)
    }

    /**
     * 绘制表盘主刻度和数字
     */
    private fun drawScales(canvas: Canvas) {
        val radius = this.radius
        val centerX = this.centerX
        val centerY = this.centerY

        paint.color = dialColor
        paint.typeface = Typeface.DEFAULT
        val steps = 10
        // 主刻度线条坐标间隔值(越大越靠近中心), 起点靠内，终点靠外
        val majorStart = dpToPx(30f)
        val majorEnd = dpToPx(17f)
        // 刻度文本的坐标间隔值(越大越靠近中心)
        val majorTextX = dpToPx(43f)
        val majorTextY = dpToPx(5f)

        val smallStart = dpToPx(27f)
        val smallEnd = dpToPx(18f)
        for (i in 0..steps) {
            val value = minValue + (maxValue - minValue) * i * 1.0f / steps
            val angle = valueToAngle(value)

            // 计算主刻度起点和终点
            val startX = centerX + (radius - majorStart) * cos(Math.toRadians(angle.toDouble())).toFloat()
            val startY = centerY + (radius - majorStart) * sin(Math.toRadians(angle.toDouble())).toFloat()
            val endX = centerX + (radius - majorEnd) * cos(Math.toRadians(angle.toDouble())).toFloat()
            val endY = centerY + (radius - majorEnd) * sin(Math.toRadians(angle.toDouble())).toFloat()

            // 绘制主刻度线
            paint.strokeWidth = dpToPx(3f)
            canvas.drawLine(startX, startY, endX, endY, paint)

            // 绘制主刻度数字
            paint.textSize = scaleTextSize
            paint.strokeWidth = dpToPx(1.5f)
            paint.textAlign = Paint.Align.CENTER

            val textX = centerX + (radius - majorTextX) * cos(Math.toRadians(angle.toDouble())).toFloat()
            val textY = centerY + (radius - majorTextX) * sin(Math.toRadians(angle.toDouble())).toFloat() + majorTextY

            canvas.drawText(value.toInt().toString(), textX, textY, paint)
        }

        // 绘制细分刻度 (每总范围的1/50作为一个小刻度)
        paint.strokeWidth = 2f
        val totalRange = maxValue - minValue
        val smallStep = totalRange / 50f  // 总共约50个小刻度
        val majorStep = totalRange / 10f   // 主刻度间隔
        var value: Float = minValue.toFloat()
        while (value <= maxValue) {
            // 跳过主刻度位置
            val majorTickValue = ((value - minValue) / majorStep).toInt() * majorStep + minValue
            if (Math.abs(value - majorTickValue) > smallStep * 0.1f) { // 不是主刻度位置
                val angle = valueToAngle(value)

                val startX = centerX + (radius - smallStart) * cos(Math.toRadians(angle.toDouble())).toFloat()
                val startY = centerY + (radius - smallStart) * sin(Math.toRadians(angle.toDouble())).toFloat()
                val endX = centerX + (radius - smallEnd) * cos(Math.toRadians(angle.toDouble())).toFloat()
                val endY = centerY + (radius - smallEnd) * sin(Math.toRadians(angle.toDouble())).toFloat()

                canvas.drawLine(startX, startY, endX, endY, paint)
            }
            value += smallStep
        }
    }

    /**
     * 绘制指针
     */
    private fun drawPointer(canvas: Canvas) {
        val radius = this.radius
        val centerX = this.centerX
        val centerY = this.centerY

        // 计算指针角度
        val angle = valueToAngle(currentProgressValue)
        // 绘制指针
        pointerPaint.style = Paint.Style.STROKE

        // 计算指针的终点坐标。margin越大，指针越短
        val pointerRadiusMargin = radius * 0.45f
        val endX = centerX + (radius - pointerRadiusMargin) * cos(Math.toRadians(angle.toDouble())).toFloat()
        val endY = centerY + (radius - pointerRadiusMargin) * sin(Math.toRadians(angle.toDouble())).toFloat()
        canvas.drawLine(centerX, centerY, endX, endY, pointerPaint)

        // 绘制指针中心圆点
        pointerPaint.style = Paint.Style.FILL
        canvas.drawCircle(centerX, centerY, dpToPx(5f), pointerPaint)
    }

    /**
     *  绘制中心数字
     */
    private fun drawCenterText(canvas: Canvas, centerX: Float, centerY: Float) {
        paint.color = currentValueTextColor
        paint.textSize = centerTextSize
        paint.textAlign = Paint.Align.CENTER
        paint.style = Paint.Style.FILL
        paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)

        val offsetY = centerY + (height / 2 - padding) / 2
        val centerText = if(currentProgressValue > 0f){
            String.format(Locale.getDefault(), "%.1f", currentSpeedValue)
        }else{
            "0"
        }
        canvas.drawText(centerText, centerX, offsetY, paint)
    }

    /**
     * 将当前数值转换为旋转目标角度
     * min值对应135度，max值对应405度
     * @param value 当前数值
     */
    private fun valueToAngle(value: Float): Float {
        val valueAbsMaxValue = maxValue - minValue
        val sweepPercent = (value - minValue) / valueAbsMaxValue
        return startAngle + sweepPercent * sweepAngle
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        animator?.cancel()
        animator = null
    }

    fun reset() {
        setValue(minValue.toFloat())
        postDelayed({
            animator?.cancel()
            animator = null
        }, 1500)
    }

    // 公共接口：设置当前值（带动画）
    fun setValue(value: Float) {
        currentSpeedValue = value
        val newProgressValue = value.coerceIn(minValue.toFloat(), maxValue.toFloat())

        animator?.cancel()
        animator = ValueAnimator.ofFloat(currentProgressValue, newProgressValue).apply {
            duration = animationDuration.toLong()
            interpolator = android.view.animation.DecelerateInterpolator()
            addUpdateListener { animation ->
                currentProgressValue = animation.animatedValue as Float
                invalidate()
            }
            start()
        }
    }

    // 公共接口：设置数值范围
    fun setValueRange(min: Int, max: Int) {
        require(min < max) { "最小值必须小于最大值" }

        this.minValue = min
        this.maxValue = max

        // 调整当前值在合理范围内
        currentProgressValue = currentProgressValue.coerceIn(min.toFloat(), max.toFloat())

        invalidate()
    }

    // 公共接口：设置正常区域颜色
    fun setProgressBgColor(color: Int) {
        this.progressBgColor = color
        invalidate()
    }

    // 公共接口：设置警示区域颜色
    fun setProgressColor(color: Int) {
        this.progressColor = color
        invalidate()
    }

    // 公共接口：设置指针颜色
    fun setPointerColor(color: Int) {
        this.pointerColor = color
        invalidate()
    }

    // 获取当前值
    fun getCurrentValue():Float = currentSpeedValue

    /**
     * DP转PX
     */
    private fun dpToPx(dp: Float): Float {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dp,
            resources.displayMetrics
        )
    }

    /**
     * SP转PX
     */
    private fun spToPx(sp: Float): Float {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_SP,
            sp,
            resources.displayMetrics
        )
    }
}