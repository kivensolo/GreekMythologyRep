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
import android.graphics.PointF
import android.graphics.Rect
import android.graphics.Shader
import android.graphics.SweepGradient
import android.text.TextUtils
import android.util.AttributeSet
import android.util.Log
import android.view.View
import androidx.core.view.isInvisible
import com.zeke.demo.R
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.hypot
import kotlin.math.max
import kotlin.math.min
import kotlin.math.sin


/**
 * author：ZekeWang
 * date：2025/4/14
 * description：增强型光带扫描效果自定义View控件
 * 一个便捷的光带扫描效果自定义渲染控件
 * 支持总时长、间隔时间、运动起始角度、光带渲染模式、渲染次数等功能的控制。
 *
 * 支持3种光带模式：
 * -  Surface 模式
 *      通过角度计算路径起终点，每帧用 progress 在路径上插值得到光带中心位置，
 *      再根据方向向量和光带长度计算渐变起止坐标，直接创建 LinearGradient 渲染。
 * - edge_race: 边缘竞赛效果，2条光带从起点出发
 *      与 Surface 模式类似但使用 Paint.Style.STROKE，光带沿边缘移动。
 * - edge_greedy: 边缘贪吃蛇效果，光带围绕View边缘顺时针转动. (使用SweepGradient + Matrix旋转实现环绕效果)
 *      SweepGradient 以 View 中心为原点创建扫描渐变，
 *      通过 Matrix.preRotate(rotateAngle) 旋转 Shader实现光带环绕边框旋转。
 *
 * 使用 ValueAnimator 驱动动画。
 * 通过 updatePathParameters 计算路径参数
 */
class FlashEnhanceView @JvmOverloads constructor(
    context: Context,
    val attrs: AttributeSet? = null,
    defStyle: Int = 0
) : View(context, attrs, defStyle) {

    companion object{
        val MODE_SURFACE = "surface"
        val MODE_EDGE_RACE = "edge_race"
        val MODE_EDGE_GREEDY = "edge_greedy"

    }
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

    // 起始点X轴的方向向量
    private var dirX = 0f

    // 起始点Y轴的方向向量
    private var dirY = 0f

    // 动画开始时光带中心坐标点（View 外一侧）
    private var pathStartCenterPoint: PointF = PointF()
    // 动画结束时光带中心坐标点（View 外另一侧）
    private var pathEndCenterPoint: PointF = PointF()

    private var mFlashRect: Rect? = null

    private val mFlashPaint = Paint()

    private lateinit var animator: ValueAnimator

    private var progress = 0f

    private var mTypedArray: TypedArray

// <editor-fold defaultstate="collapsed" desc="渐变效果">
    private var sweepGradient: SweepGradient? = null
    //基准渐变
    private var linearGradient: LinearGradient? = null
    //渐变矩阵    基准渐变 + 渐变矩阵 = 任意位置/角度的渐变效果
    private val gradientMatrix = Matrix()
// </editor-fold>

    //<editor-fold desc="Edge_greedy模式所需要的特有变量">
    private var matrix: Matrix? = null
    private var viewCenterPoint: PointF = PointF()
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
            mFlashBeginAngle = mTypedArray.getInt(R.styleable.flashEnhanceView_beginAngle, 45)
        }
    }

    /**
     * 更新光带运动的相关参数。
     *
     * 根据角度计算光带路径起终点等参数，是实现运动轨迹的核心算法函数。
     * 核心步骤：
     * 1. 通过标准坐标系将角度转为弧度(极坐标)，再转为方向向量 (dirX, dirY)
     * 2. 根据方向向量和 View 尺寸，计算路径起终点坐标 (pathStart/pathEnd)
     *    路径长度 = 对角线 + 光带长度，保证光带完全进出 View
     * 3. edge_greedy 模式额外初始化 SweepGradient
     */
    private fun updatePathParameters(viewWidth: Int, viewHeight: Int) {
        // 极坐标转换：将角度（0°~360°）转为方向向量 (dirX, dirY)
        val mathRadians = Math.toRadians(mFlashBeginAngle.toDouble())
        dirX = cos(mathRadians).toFloat()
        dirY = sin(mathRadians).toFloat()

        val diagonalLine = hypot(viewWidth.toDouble(), viewHeight.toDouble()).toFloat()
        gradientLength = diagonalLine * mGradientRatio

        viewCenterPoint.apply {
            x = viewWidth.toFloat() / 2
            y = viewHeight.toFloat() / 2
        }
        val viewCenterX = viewCenterPoint.x
        val viewCenterY = viewCenterPoint.y

        // 路径半长 = (对角线 + 光带长度) / 2，保证光带完全进出 View
        val halfPath = (diagonalLine + gradientLength) / 2f

        // 路径起终点：从角度反方向一侧进入，沿角度方向移出
        // Android Y轴向下为正，标准坐标系 sin 向上为正，所以 Y 分量取反
        val pathStartX = viewCenterX - dirX * halfPath
        val pathStartY = viewCenterY + dirY * halfPath
        pathStartCenterPoint.apply {
            x = pathStartX
            y = pathStartY
        }

        val pathEndX = viewCenterX + dirX * halfPath
        val pathEndY = viewCenterY - dirY * halfPath
        pathEndCenterPoint.apply {
            x = pathEndX
            y = pathEndY
        }

        if (mFlashColors.size < 2) {
            return
        }
        if(isEdgeGreedyMode()) {
            matrix?.let {
                sweepGradient = SweepGradient(viewCenterX, viewCenterY, mFlashColors, null)
                it.setRotate(mFlashBeginAngle.toFloat(), viewCenterX, viewCenterY)
                sweepGradient?.setLocalMatrix(it)
                mFlashPaint.shader = sweepGradient
            }
        } else {
            linearGradient = LinearGradient(
                -gradientLength / 2f, 0f,
                gradientLength / 2f, 0f,
                mFlashColors, null, Shader.TileMode.CLAMP
            )
            mFlashPaint.shader = linearGradient
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

        val viewCenterX = viewCenterPoint.x
        val viewCenterY = viewCenterPoint.y
        val pathStartX = pathStartCenterPoint.x
        val pathStartY = pathStartCenterPoint.y
        val pathEndX = pathEndCenterPoint.x
        val pathEndY = pathEndCenterPoint.y

        if (isEdgeGreedyMode()) {
            mFlashPaint.strokeWidth = mBorderFlashWidth.toFloat()
            mFlashPaint.style = Paint.Style.STROKE
            val rotateAngle = progress * 360 + mFlashBeginAngle
            matrix?.apply {
                reset()
                preRotate(rotateAngle, viewCenterX, viewCenterY)
                sweepGradient?.setLocalMatrix(this)
            }
        } else {
            // Surface 和 Edge_Race：通过 progress 插值得到光带中心，用 Matrix 定位预创建的渐变
            val cx = pathStartX + (pathEndX - pathStartX) * progress
            val cy = pathStartY + (pathEndY - pathStartY) * progress

            /*
             * 计算路径方向在屏幕坐标系中的角度
             * (pathEndX - pathStartX, pathEndY - pathStartY) 就是路径从起点到终点的方向向量。
             * atan2 算出这个方向相对于正 X 轴的角度，
             * Math.toDegrees 转为度数，范围 [-180°, 180°]
             *
             * 0°   左-->右   dx正  dy=0 atan2 = 0°
             * 45°  左下→右上  dx正  dy负 atan2 = -45°
             * 90°  下-->上   dx>0  dy负 atan2 = -90°
             * 180° 右-->左   dx>负 dy=0 atan2 = 180°
              */
            val moveAngle = Math.toDegrees(
                atan2((pathEndY - pathStartY).toDouble(), (pathEndX - pathStartX).toDouble())
            ).toFloat()

            gradientMatrix.reset()
            //先将渐变旋转到运动方向
            gradientMatrix.postRotate(moveAngle)
            //再移到当前位置
            gradientMatrix.postTranslate(cx, cy)
            linearGradient?.setLocalMatrix(gradientMatrix)

            if (isEdgeMode()) {
                mFlashPaint.strokeWidth = mBorderFlashWidth.toFloat()
                mFlashPaint.style = Paint.Style.STROKE
            }
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