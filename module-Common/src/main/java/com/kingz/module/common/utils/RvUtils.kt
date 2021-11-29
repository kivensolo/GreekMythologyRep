package com.kingz.module.common.utils

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

/**
 * author：ZekeWang
 * date：2021/2/23
 * description：RecyclerView 工具类
 */
object RvUtils {
    /**
     * 平滑滚动到第1个元素
     */
    fun smoothScrollTop(rv: RecyclerView) {
        val layoutManager: RecyclerView.LayoutManager? = rv.layoutManager
        if (layoutManager is LinearLayoutManager) {
            val linearLayoutManager: LinearLayoutManager = layoutManager
            val first: Int = linearLayoutManager.findFirstVisibleItemPosition()
            val last: Int = linearLayoutManager.findLastVisibleItemPosition()
            val visibleCount = last - first + 1
            val scrollIndex = visibleCount * 2 - 1
            if (first > scrollIndex) {
                rv.scrollToPosition(scrollIndex)
            }
        }
        rv.smoothScrollToPosition(0)
    }
}