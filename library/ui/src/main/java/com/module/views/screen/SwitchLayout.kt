package com.module.views.screen

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout


/**
 * Author: liliangyi
 * Maintainer: liliangyi
 * Date: 2020/7/21
 * Desc:
 */
class SwitchLayout : FrameLayout {

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    private var mCurrentIndex = 0

    private var isPlaying = false

    init {
        isChildrenDrawingOrderEnabled = true
    }

    fun showNext() {
        if (childCount < 2) return
        if (isPlaying) return
        moveOut(false)
        mCurrentIndex++
        if (mCurrentIndex >= childCount) {
            mCurrentIndex = 0
        }
        showIn(false)
    }

    fun showPre() {
        if (childCount < 2) return
        if (isPlaying) return
        showIn(true)
        mCurrentIndex--
        if (mCurrentIndex < 0) {
            mCurrentIndex = childCount - 1
        }
        moveOut(true)
    }

    private fun moveOut(pre: Boolean) {
        val childAt = getChildAt(mCurrentIndex)
        if (childAt is SwitchView) {
            isPlaying = true
            if (pre) invalidate()
            childAt.animOut(pre) {
                isPlaying = false
                invalidate()
            }
        }
    }

    private fun showIn(pre: Boolean) {
        val childAt = getChildAt(mCurrentIndex)
        if (childAt is SwitchView) {
            childAt.animIn(pre)
        }
    }

    override fun getChildDrawingOrder(childCount: Int, drawingPosition: Int): Int {
        if (childCount < 2) return drawingPosition
        // 将当前的View绘制层级提到最后绘制
        return when {
            // 如果是最后一个，需要将倒数第2个和第0个位置交换
            mCurrentIndex == childCount - 1 -> {
                when (drawingPosition) {
                    0 -> {
                        childCount - 2
                    }
                    childCount - 2 -> {
                        0
                    }
                    else -> {
                        drawingPosition
                    }
                }
            }
            drawingPosition < mCurrentIndex -> {
                drawingPosition
            }
            else -> {
                mCurrentIndex + childCount - 1 - drawingPosition
            }
        }
    }

    companion object {
        private const val TAG = "SwitchLayout"
    }
}