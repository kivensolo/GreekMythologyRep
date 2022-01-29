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
     * 滚动到第1个元素
     * 若可视的第一个元素，超过了2页数据，则直接滚动，否则平滑滚动
     */
    fun smoothScrollTop(rv: RecyclerView) {
        val layoutManager: RecyclerView.LayoutManager? = rv.layoutManager
        if (layoutManager is LinearLayoutManager) {
            val linearLayoutManager: LinearLayoutManager = layoutManager
            val topPosition: Int = linearLayoutManager.findFirstVisibleItemPosition()
            val bottomPosition: Int = linearLayoutManager.findLastVisibleItemPosition()
            val visibleCount = bottomPosition - topPosition + 1
            val directlyScrollRange = visibleCount * 2 - 1
            if (topPosition > directlyScrollRange) {
                rv.scrollToPosition(directlyScrollRange)
            }
        }
        rv.smoothScrollToPosition(0)
    }
}