package com.module.drawable;

import android.animation.ValueAnimator
import android.graphics.Canvas
import android.graphics.ColorFilter
import android.graphics.DashPathEffect
import android.graphics.LinearGradient
import android.graphics.Matrix
import android.graphics.Paint
import android.graphics.Path
import android.graphics.PathMeasure
import android.graphics.PixelFormat
import android.graphics.Rect
import android.graphics.RectF
import android.graphics.Shader
import android.graphics.drawable.Animatable
import android.graphics.drawable.Drawable
import android.view.animation.LinearInterpolator

/**
 * Copyright (c) 2026, 北京视达科科技有限责任公司 All rights reserved.
 * author：ZekeWang
 * date：2026/5/21
 * description： 跑马灯边框 Drawable
 * 继承 Drawable + 实现 Animatable，可作为任意 View 的 background/foreground 使用，组合性强。
 *
 * 整体流程
 * ValueAnimator 每帧 → applyFrame(fraction)
 *                       ├── LinearGradient 平移 dx → 颜色流动
 *                       ├── DashPathEffect phase=-dx → 可见段移动
 *                       └── invalidateSelf() → draw() → canvas.drawPath()
 *  最终效果：一条占边框 40% 长度的彩色光带（蓝→红→黄→绿渐变），
 *  以 animDuration 秒为周期匀速沿View的边框循环流动，其余 60% 为透明，露出底层的背景。
 */
class MarqueeBorderDrawable(
    private val cornerRadius: Float = 4f,    // 圆角半径
    private val borderWidth: Float = 4f,     // 边框线宽
    private val animDuration: Long = 4000L,   // 一个完整循环的时长
    private val autoRun:Boolean = false
) : Drawable(), Animatable {

    // 描边画笔，本身不设颜色，颜色由后面的 Shader 提供
    private val borderPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
        strokeWidth = borderWidth
    }

    private val path = Path()
    private val rect = RectF()
    private var pathLength = 0f

    // 首尾添加透明色(alpha=0)，实现光带头尾亮度渐隐
    private val colors = intArrayOf(
        0x4C4285F4.toInt(), // Blue (30%透明 - 头部渐隐)
        0xFF4285F4.toInt(), // Blue
        0xFFEA4335.toInt(), // Red
        0xFFFBBC05.toInt(), // Yellow
        0xFF34A853.toInt(), // Green
        0xFF4285F4.toInt(), // Blue
        0x4C4285F4.toInt()  // Blue (30%透明 - 尾部渐隐)
    )

    // positions 控制每个颜色在渐变中的位置占比，首尾 15% 为渐隐区
    private val positions = floatArrayOf(
        0.0f,
        0.15f,
        0.33f,
        0.5f,
        0.67f,
        0.85f,
        1.0f
    )

    // 值动画，每帧回调 applyFrame()
    private val animator = ValueAnimator.ofFloat(0f, 1f).apply {
        duration = animDuration
        repeatCount = ValueAnimator.INFINITE
        interpolator = LinearInterpolator()
        addUpdateListener {
            applyFrame(it.animatedValue as Float)
            invalidateSelf()
        }
    }

    /**
     * 每帧更新, 由 ValueAnimator 每帧回调
     * 核心机制：双重偏移同步驱动。
     * 1. LinearGradient + Matrix 平移
     *      创建水平渐变（蓝→红→黄→绿），使用 TileMode.MIRROR使渐变镜像重复覆盖整条路径。
     *      每帧通过 Matrix.postTranslate(dx) 将渐变向右平移，产生颜色流动效果。
     * 2. DashPathEffect 负相位偏移
     *      将路径分为 40% 可见段 + 60% 空白段，每帧通过 DashPathEffect(floatArrayOf(dashLen,gapLen), -dx)
     *      让可见段沿路径前进方向移动。负的 phase 值使可见段随 dx 增大而向前滑动。
     * 3. Path 路径约束
     *      用 addRoundRect() 创建圆角矩形路径，PathMeasure 计算总周长，canvas.drawPath() 将渲染约束在边框路径上。
     *
     * 三者协作：Shader 平移负责颜色流动，DashPathEffect 负责可见区域移动，Path 负责形状约束，
     * 三者 offset 同步（均为 fraction * pathLength），形成光带沿边框循环流动的效果。
     *
     * @param fraction 移动的进度：0→1 线性变化
     */
    private fun applyFrame(fraction: Float) {
        if (pathLength <= 0f) return
        // 光带和DashPath的偏移量 = 进度 × 周长
        val dx = fraction * pathLength // 0 → pathLength

        val shader = LinearGradient(
            0f, 0f, pathLength * 0.5f, 0f, // 渐变跨度 = 周长的一半
            colors, positions,
            Shader.TileMode.MIRROR // TileMode.MIRROR 让渐变在超出范围时镜像重复，覆盖整条路径
        )
        val matrix = Matrix()
        //平移渐变起始位置,使渐变整体在屏幕水平方向往右移动
        matrix.postTranslate(dx, 0f)
        shader.setLocalMatrix(matrix)
        borderPaint.shader = shader

        val dashLen = pathLength * 0.4f // 可见段 = 40% 周长
        val gapLen = pathLength * 0.6f // 空白段 = 60% 周长
        // 虚线可见段沿路径滑动（负的 phase 让可见段沿路径前进方向移动）phase 从 0 减小到 -pathLength，完成一整圈
        borderPaint.pathEffect = DashPathEffect(floatArrayOf(dashLen, gapLen), -dx)
    }

    /**
     * onBoundsChange 在 Drawable 被设置到 View 上时自动调用
     * 用 PathMeasure 算出边框路径的总长度（周长），后续动画和 Dash 的长度都基于这个值。
     */
    override fun onBoundsChange(bounds: Rect) {
        super.onBoundsChange(bounds)
        val w = bounds.width().toFloat()
        val h = bounds.height().toFloat()
        val half = borderWidth / 2
        // 内缩半个线宽，避免被裁切
        rect.set(half, half, w - half, h - half)
        path.reset()
        // 圆角矩形路径设置
        path.addRoundRect(rect, cornerRadius, cornerRadius, Path.Direction.CW)
        // 计算路径总周长
        pathLength = PathMeasure(path, false).length
        if(autoRun){
            start()
        }
    }

    override fun draw(canvas: Canvas) {
        if (pathLength <= 0f) return
        //用配置好的 Paint（带 Shader + DashPathEffect）画圆角矩形路径
        canvas.drawPath(path, borderPaint)
//        canvas.drawRect(RectF(bounds), borderPaint)
    }

    override fun setAlpha(alpha: Int) {
        borderPaint.alpha = alpha
    }

    override fun setColorFilter(colorFilter: ColorFilter?) {
        borderPaint.colorFilter = colorFilter
    }

    @Deprecated("Deprecated in Java")
    override fun getOpacity(): Int = PixelFormat.TRANSLUCENT

    override fun start() {
        if (!isRunning) animator.start()
    }

    override fun stop() {
        animator.cancel()
    }

    override fun isRunning(): Boolean = animator.isRunning
}
