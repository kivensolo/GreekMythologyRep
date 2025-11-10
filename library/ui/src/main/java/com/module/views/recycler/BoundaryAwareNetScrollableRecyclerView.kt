package com.module.views.recycler

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.ViewConfiguration
import androidx.recyclerview.widget.RecyclerView
import kotlin.math.abs

/**
 * author：ZeKeWong
 * date：2025/9/24
 * description：
 * 内部拦截法实现的支持嵌套滑动(垂直方向)且支持边界感知的RecyclerView
 */
open class BoundaryAwareNetScrollableRecyclerView : RecyclerView {
    private var lastX = 0f
    private var lastY = 0f
    private var touchSlop = 0
    protected var parentRecyclerView: RecyclerView? = null
    private var isScrolling = false

    constructor(context: Context) : super(context) {
        init(context)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init(context)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context, attrs, defStyleAttr
    ) {
        init(context)
    }

    private fun init(context: Context) {
        touchSlop = ViewConfiguration.get(context).scaledTouchSlop
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        getParentRecyclerView()
    }

    open fun getParentRecyclerView() {
        parentRecyclerView = parent as? RecyclerView
    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        ev?.apply {
            when (action) {
                MotionEvent.ACTION_DOWN -> {
                    lastX = x
                    lastY = y
                    //  父级RecyclerView不拦截事件
                    parentRecyclerView?.requestDisallowInterceptTouchEvent(true)
                    isScrolling = false
                }
                MotionEvent.ACTION_MOVE -> {
                    val currentX = ev.x
                    val currentY = ev.y

                    val deltaX = abs((currentX - lastX).toDouble()).toFloat()
                    val deltaY = abs((currentY - lastY).toDouble()).toFloat()
                    if (deltaY > touchSlop && deltaY > deltaX) {
                        // 只有在垂直滑动且滑动距离足够时才处理
                        handleVerticalScroll(currentY, lastY)
                    }
//                    else if (deltaX > touchSlop && deltaX > deltaY) {
//                        //水平滑动，不释放给父View处理
//                        //parentRecyclerView?.requestDisallowInterceptTouchEvent(false)
//                    }
                }
                MotionEvent.ACTION_UP,
                MotionEvent.ACTION_CANCEL -> {
                    parentRecyclerView?.requestDisallowInterceptTouchEvent(false)
                    isScrolling = false
                }
            }
        }
        return super.dispatchTouchEvent(ev)
    }


    private fun handleVerticalScroll(currentY: Float, lastY: Float) {
        if (isScrollingOverBoundary(currentY, lastY)) {
            // 已经在边界且继续超越边界滑动，释放给外层RecyclerView
            parentRecyclerView?.requestDisallowInterceptTouchEvent(false)
        } else {
            // 在中间区域或可以继续滚动，保持拦截
            parentRecyclerView?.requestDisallowInterceptTouchEvent(true)
            isScrolling = true
        }
    }

    /**
     * 是否已到边界，且继续滑动超过边界
     */
    private fun isScrollingOverBoundary(currentY: Float, lastY: Float): Boolean {
        // 手指持续滑动的方向
        val deltaY = currentY - lastY
        val isSlideFingerDown = deltaY > 0
        val isSlideFingerUp = deltaY < 0

        // 判断是否到达顶部或底部边界
        val isAtTop = !canScrollVertically(-1)   // 是否不能再向上滚动
        val isAtBottom = !canScrollVertically(1) // 是否不能再向下滚动

        val isScrollingOverTop = isAtTop && isSlideFingerDown
        val isScrollingOverBottom = isAtBottom && isSlideFingerUp
//        AppLog.d("isScrollingOverBoundary? AttachTop=$isScrollingOverTop , AttachBottom=$isScrollingOverBottom")
        return isScrollingOverTop || isScrollingOverBottom
    }

    override fun onScrollStateChanged(state: Int) {
        super.onScrollStateChanged(state)
        isScrolling = state != SCROLL_STATE_IDLE
    }
}
