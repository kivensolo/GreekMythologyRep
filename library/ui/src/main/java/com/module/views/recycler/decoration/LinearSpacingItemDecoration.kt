package com.module.views.recycler.decoration

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

/**
 * date：2025/10/28
 * description：线性间距ItemDecoration，用于在RecyclerView中添加间距
 */
class LinearSpacingItemDecoration(
    private val verticalSpace: Int, // 上下间距
    private val horizontalSpace: Int // 左右间距
) : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val position = parent.getChildAdapterPosition(view)
        outRect.left = horizontalSpace
        outRect.right = horizontalSpace
        outRect.bottom = verticalSpace
        if (position == 0) {
            outRect.top = verticalSpace
        }
    }
}