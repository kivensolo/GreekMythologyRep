package com.zeke.demo.draw.flash

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.TimeInterpolator
import android.animation.ValueAnimator
import android.content.Context
import android.content.res.TypedArray
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.LinearGradient
import android.graphics.Matrix
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.Shader
import android.graphics.SweepGradient
import android.text.TextUtils
import android.util.AttributeSet
import android.util.Log
import android.view.View
import androidx.core.view.isInvisible
import com.zeke.demo.R
import kotlin.math.cos
import kotlin.math.hypot
import kotlin.math.max
import kotlin.math.min
import kotlin.math.sin


/**
 * author：ZekeWang
 * date：2025/4/14
 * description：增强型光带扫描Render
 * 一个便捷的光带扫描效果自定义渲染控件
 * 支持总时长、间隔时间、运动起始角度、光带渲染模式、渲染次数等功能的控制。
 */
class FlashEnhanceView @JvmOverloads constructor(
    context: Context,
    val attrs: AttributeSet? = null,
    defStyle: Int = 0
) : View(context, attrs, defStyle) {

    private val TAG = "FlashEnhanceView"

    //<editor-fold desc="光带效果模式">
    private val MODE_FLASH_SURFACE = 0x0001

    /**
     * 边缘模式的贪吃风格(类似于贪吃蛇的效果)，1条光带围着View周围转动
     * 目前转动方向固定顺时针，若后续有特殊要求，改动角度变化的计算代码即可。
     */
    private val MODE_FLASH_EDGE_GREEDY = 0x0002

    // 边缘模式的竞赛风格，2条光带从起点出发，到达终点
    private val MODE_FLASH_EDGE_RACE = 0x0003

    private var flashStyleMode = MODE_FLASH_SURFACE
    //</editor-fold>

    //<editor-fold desc="外部可设置的属性值">
    var mFlashStarted = false
    var mMode = ""
    var mFlashColors: IntArray
    /**
     * 光带渐变宽度比例(视图对角线长度的比例值)
     * 有效范围(0,1.0)
     */
    var mGradientRatio = -100f
    /**
     * 光带移动方向起始角度, 基于标准坐标系，
     * 从正右方起始点逆时针方向绕一圈，是0°~360°
     * 默认值为45°(右上角往左下角)
     */
    var mFlashBeginAngle = -100

    var mAnimationDuration = -100
    var mAnimationInterval = -100
    var mRepeatCount = -100
    var mBorderFlashWidth = -100
    //</editor-fold>

    //光带长度(即光带渐变渲染范围宽度)
    private var gradientLength = 0f

    private var flashMovePathLength = 0f

    // 光带起始点的横向偏移量
    private var startOffsetX = 0f

    // 光带起始点的竖向偏移量
    private var startOffsetY = 0f

    // 起始点X轴的方向向量
    private var dirX = 0f

    // 起始点Y轴的方向向量
    private var dirY = 0f

    private var mFlashRect: Rect? = null

    private val mFlashPaint = Paint()

    private lateinit var animator: ValueAnimator

    private var progress = 0f

    private var mTypedArray: TypedArray

    //<editor-fold desc="Edge_greedy模式所需要的特有变量">
    private var matrix: Matrix? = null
    private var viewCenterX: Float = 0f
    private var viewCenterY: Float = 0f
    //</editor-fold>

    init {
        mFlashColors = intArrayOf(
            Color.TRANSPARENT,
            Color.parseColor("#05FFFFFF"),
            Color.parseColor("#10FFFFFF"),
            Color.parseColor("#20FFFFFF"),
            Color.parseColor("#2AFFFFFF"),
            Color.parseColor("#20FFFFFF"),
            Color.parseColor("#10FFFFFF"),
            Color.parseColor("#05FFFFFF"),
            Color.TRANSPARENT
        )

        //TODO obtainStyledAttributes 这个方法进行统一抽离
        mTypedArray = context.theme.obtainStyledAttributes(attrs, R.styleable.flashEnhanceView, defStyle, 0)
    }

    private fun initAnim() {
        if(mRepeatCount == -100){
            mRepeatCount = mTypedArray.getInt(R.styleable.flashEnhanceView_repeatCount, ValueAnimator.INFINITE)
        }

        // 前 animationRatio 时间播放动画，后 interval 时间保持最终值(模拟暂停等待下一次动画的间隔耗时)
        val animationRatio: Float = mAnimationDuration / (mAnimationDuration + mAnimationInterval).toFloat()
        animator = ValueAnimator.ofFloat(0f, 1f)
        animator.setDuration((mAnimationDuration + mAnimationInterval).toLong())
        animator.repeatCount = mRepeatCount
        animator.interpolator =
            TimeInterpolator { input -> min((input / animationRatio).toDouble(), 1.0).toFloat() }
        animator.addUpdateListener {
            progress = it.animatedValue as Float
            invalidate()
        }

        animator.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                super.onAnimationEnd(animation)
                //not invoked when repeat count set to INFINITE.
                mFlashStarted = false
                progress = 0f
            }
        })
    }

    private fun initMode() {
        if(mMode == ""){
            mMode = mTypedArray.getString(R.styleable.flashEnhanceView_flashMode) ?: "surface"
        }
        if ("edge_race" == mMode || "edge_greedy" == mMode) {
            flashStyleMode = if ("edge_race" == mMode) MODE_FLASH_EDGE_RACE else MODE_FLASH_EDGE_GREEDY
            if (isEdgeGreedyMode()) {
                matrix = Matrix()
                if(mBorderFlashWidth == -100){
                    mBorderFlashWidth = mTypedArray.getInt(R.styleable.flashEnhanceView_borderWidth, 4)
                }
            }
        } else {
            flashStyleMode = MODE_FLASH_SURFACE;
        }
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        // 扫光动画部分时长
        if(mAnimationDuration == -100){
            mAnimationDuration = mTypedArray.getInt(R.styleable.flashEnhanceView_animDuration, 2000)
            mAnimationDuration = max(mAnimationDuration, 500)
        }
        // 扫光间隔时长
        if(mAnimationInterval == -100){
            mAnimationInterval = mTypedArray.getInt(R.styleable.flashEnhanceView_animInterval, 1000)
            mAnimationInterval = max(mAnimationInterval,200)
        }
        initAnim()
        initMode()
        initColors()
        initGradientRatio()
        initAngle()
    }

    /**
     * 是否是边缘贪吃蛇效果(一条光带围绕)
     */
    private fun isEdgeGreedyMode(): Boolean {
        return flashStyleMode == MODE_FLASH_EDGE_GREEDY
    }

    private fun initColors() {
        val colorAttr: String? = mTypedArray.getString(R.styleable.flashEnhanceView_flashColors)
        if (TextUtils.isEmpty(colorAttr)) {
            return
        }
        val colors = colorAttr!!.split(",");
        if (colors.isEmpty()) {
            return
        }
        if (isEdgeGreedyMode()) { //Greedy模式不做边缘柔化
            mFlashColors = IntArray(colors.size)
            for (i in colors.indices) {
                mFlashColors[i] = Color.parseColor("#" + colors[i])
            }
        } else {
            mFlashColors = IntArray(colors.size + 2)
            mFlashColors[0] = Color.TRANSPARENT
            mFlashColors[colors.size + 1] = Color.TRANSPARENT
            for (i in colors.indices) {
                mFlashColors[i + 1] = Color.parseColor("#" + colors[i])
            }
        }
    }

    private fun initGradientRatio() {
        if(mGradientRatio == -100f){
            mGradientRatio = mTypedArray.getFloat(R.styleable.flashEnhanceView_gradientRatio, 0.6f)
        }
        if (mGradientRatio == 0f || mGradientRatio >= 1.0f) {
            mGradientRatio = 0.6f
        }
    }

    private fun initAngle() {
        if(mFlashBeginAngle == -100){
            mFlashBeginAngle = mTypedArray.getInt(R.styleable.flashEnhanceView_isAutoRun, 45)
        }
    }

    /**
     * 更新光带运动的相关参数。
     *
     * 以Surface样式的光带为例简单说明：根据角度计算光带路径、起始点等参数，是实现运动轨迹的核心算法函数。
     * 核心步骤：
     * 1. 通过标准坐标系将角度转为弧度(极坐标)
     * 2. 通过极坐标计算起始点的方向向量（注意是起始点）
     * 0° → 右侧水平向左 (dirX=1, dirY=0)
     * 45° → 右上往左下   (dirX=0.5, dirY=0.5)
     * 90° → 从上往下移动 (dirX=0, dirY=1)
     * 180° → 左侧水平向右 (dirX=-1, dirY=0)
     * 225° → 左下往右上   (dirX=-0.5, dirY=0.5)
     * 270° → 从下往上移动 (dirX=0, dirY=-1)
     * 3. 计算光带运动轨迹范围和光带范围长度
     * 4. 根计算初始偏移基准点坐标
     */
    private fun updatePathParameters(viewWidth: Int, viewHeight: Int) {
        val mathRadians = Math.toRadians(mFlashBeginAngle.toDouble())
        dirX = cos(mathRadians).toFloat()
        dirY = sin(mathRadians).toFloat()

        //View对角线长度作为视觉上光带最大移动距离
        val diagonalLine = hypot(viewWidth.toDouble(), viewHeight.toDouble()).toFloat()

        //为保证光带完全移出View区域，设置实际移动距离为对角线的2.2倍
        flashMovePathLength = diagonalLine * 2.2f

        // 按视图比例设置光带长度
        gradientLength = diagonalLine * mGradientRatio

        viewCenterX = viewWidth.toFloat() / 2
        viewCenterY = viewHeight.toFloat() / 2
        // 计算初始偏移基准点
        startOffsetX = viewCenterX + dirX * diagonalLine

        // Y轴的计算需转为Android视图坐标系
        startOffsetY = viewCenterY - dirY * diagonalLine

        if(isEdgeGreedyMode()) {
            matrix?.let {
                it.setRotate(mFlashBeginAngle.toFloat(), viewCenterX, viewCenterY)
                val sweepGradient = SweepGradient(viewCenterX, viewCenterY, mFlashColors, null)
                sweepGradient.setLocalMatrix(it)
                mFlashPaint.setShader(sweepGradient)
            }
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

        if (mFlashRect == null) {
            mFlashRect = Rect(0, 0, width, height)
        }
        Log.d(TAG, "onAttachedToWindow()  mFlashRect=$mFlashRect")
        updatePathParameters(width, height)
        invalidate()
    }

    override fun onDraw(canvas: Canvas) {
        if (isInvisible) {
            return
        }
        super.onDraw(canvas)
        if (!mFlashStarted) {
            return;
        }

        if (mFlashRect == null) {
            return;
        }
        if (!animator.isStarted) {
            animator.start();
        }

        val offset = progress * flashMovePathLength


        // 计算渐变起止点  Y轴的计算需转为Android视图坐标系，
        val startX = startOffsetX - dirX * offset
        val endX = startX - dirX * gradientLength
        val startY = startOffsetY + dirY * offset
        val endY = startY + dirY * gradientLength

        if (isEdgeMode()) {
            mFlashPaint.strokeWidth = mBorderFlashWidth.toFloat()
            mFlashPaint.style = Paint.Style.STROKE
            if (isEdgeGreedyMode()) {
                //In greedy mode, shader already set in updatePathParameters()
                val rotateAngle = progress * 360 + mFlashBeginAngle //顺时针
                matrix?.apply {
                    reset()
                    preRotate(rotateAngle, viewCenterX, viewCenterY)
                    mFlashPaint.shader.setLocalMatrix(this)
                }
            } else {
                mFlashPaint.setShader(LinearGradient(startX, startY, endX, endY, mFlashColors, null, Shader.TileMode.CLAMP))
            }
        } else {
            mFlashPaint.setShader(LinearGradient(startX, startY, endX, endY, mFlashColors, null, Shader.TileMode.CLAMP))
        }
        mFlashRect?.let {
            canvas.drawRect(it, mFlashPaint)
        }
    }

    private fun isEdgeMode(): Boolean {
        return flashStyleMode != MODE_FLASH_SURFACE
    }


    /**
     * 提供给外部UI层的控制方法
     */
    fun startFlash() {
        Log.d(TAG, "startFlash()")
        mFlashStarted = true
        invalidate()
    }

    /**
     * 提供给外部UI层的控制方法
     */
    fun stopFlash() {
        Log.d(TAG, "stopFlash()")
        mFlashStarted = false
        animator.cancel()
        progress = 0f
    }

}