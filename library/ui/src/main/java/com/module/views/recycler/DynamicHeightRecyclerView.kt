package com.module.views.recycler

import android.content.Context
import android.util.AttributeSet
import androidx.recyclerview.widget.RecyclerView
import com.module.UIUtil

/**
 * Copyright (c) 2025, 北京视达科科技有限责任公司 All rights reserved.
 * author：ZeKeWong
 * date：2025/9/24
 * description：
 * item 数量超过指定个数时，动态限制绘制高度的RecyclerView
 */
 class DynamicHeightRecyclerView : TxSlideRecyclerView {
    // 定义最大高度（可根据需要调整）
    private val limitMaxHeight = 557F // 对应原来XML中的maxHeight值
//    private val limitMaxHeight = 250F

    companion object {
        // 定义触发滚动的最大项目数
        private const val MAX_ITEMS_BEFORE_SCROLL = 2 + 1 // dataSize + tail
    }

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context, attrs, defStyleAttr
    )

    override fun getParentRecyclerView() {
        parentRecyclerView = parent.parent as? RecyclerView
    }

    override fun onMeasure(widthSpec: Int, heightSpec: Int) {
        val itemCount = adapter?.itemCount ?: 0
        if (itemCount <= MAX_ITEMS_BEFORE_SCROLL) {
            // 当项目数量小于等于2时，设置为展开模式
            super.onMeasure(
                widthSpec, MeasureSpec.makeMeasureSpec(
                    Int.MAX_VALUE shr 2, MeasureSpec.AT_MOST
                )
            )
        } else {
            // 当项目数量大于2时，限制最大高度，启用滚动
            super.onMeasure(
                widthSpec, MeasureSpec.makeMeasureSpec(
                    UIUtil.dip2px(context,limitMaxHeight), MeasureSpec.AT_MOST
                )
            )
        }
    }
}
