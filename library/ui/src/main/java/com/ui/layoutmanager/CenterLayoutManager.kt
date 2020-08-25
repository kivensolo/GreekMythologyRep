package com.ui.layoutmanager

import android.content.Context
import android.graphics.Rect
import android.util.AttributeSet
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.SmoothScroller

class CenterLayoutManager(
    context: Context?,
    attrs: AttributeSet?,
    defStyleAttr: Int,
    defStyleRes: Int
) : LinearLayoutManager(context, attrs, defStyleAttr, defStyleRes) {
    private var mContext: Context? = null
    private var canScroll = false

    override fun onInterceptFocusSearch(
        focused: View,
        direction: Int
    ): View? {
        var currentPosition = getPosition(focusedChild!!)
        val count = itemCount
        val lastVisiblePosition = findLastVisibleItemPosition()
        when (direction) {
            View.FOCUS_RIGHT -> currentPosition++
            View.FOCUS_LEFT -> currentPosition--
            else -> {
            }
        }
        if (currentPosition < 0 || currentPosition > count) {
            return focused
        } else {
            if (currentPosition > lastVisiblePosition) {
                scrollToPosition(currentPosition)
            }
        }
        return super.onInterceptFocusSearch(focused, direction)
    }

    override fun smoothScrollToPosition(
        recyclerView: RecyclerView,
        state: RecyclerView.State,
        position: Int
    ) {
        val smoothScroller: SmoothScroller = CenterSmoothScroller(mContext)
        smoothScroller.targetPosition = position
        startSmoothScroll(smoothScroller)
    }

    override fun requestChildRectangleOnScreen(
        parent: RecyclerView,
        child: View,
        rect: Rect,
        immediate: Boolean,
        focusedChildVisible: Boolean
    ): Boolean {
        return super.requestChildRectangleOnScreen(
            parent,
            child,
            rect,
            immediate,
            focusedChildVisible
        )
    }

    override fun canScrollVertically(): Boolean {
        return canScroll && super.canScrollVertically()
    }

    fun setCanScroll(canScroll: Boolean) {
        this.canScroll = canScroll
    }
}