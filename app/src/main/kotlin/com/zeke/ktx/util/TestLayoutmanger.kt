package com.zeke.ktx.util

import android.content.Context
import android.util.AttributeSet
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView


/**
 * author: King.Z <br>
 * date:  2020/5/17 19:41 <br>
 * description: Layoutmanger测试  <br>
 */
class TestLayoutmanger @JvmOverloads constructor(context: Context?,
                       attrs: AttributeSet?,
                       defStyleAttr: Int,
                       defStyleRes: Int) :
        GridLayoutManager(context, attrs, defStyleAttr, defStyleRes) {


    /**
     * 关键函数, 给子Item View 使用的，返回
     * ViewGroup.LayoutParams.FILL_PARENT(宽/高)也是可以
     * 具体参考需要修改的样式以及业务要求
     */
    override fun generateDefaultLayoutParams(): RecyclerView.LayoutParams {
        return RecyclerView.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT)
    }


    /**
     * LayoutManager 的主入口。
     * 由RecyclerView在以下流程中调用:
     *   -> onMeasure()
     *     -> dispatchLayoutStep2()
     *      -> mState.mItemCount = mAdapter.getItemCount();
     *         mLayout.onLayoutChildren(mRecycler, mState);
     *
     *
     * 它会在初始化布局时调用， 当适配器的数据改变时(或者整个适配器被换掉时)会再次调用。
     * 它的作用就是在初始化的时候放置item，直到填满布局为止。
     * 类似 ViewGroup 的 onLayout() 方法.
     */
    override fun onLayoutChildren(recycler: RecyclerView.Recycler, state: RecyclerView.State) {
        // 所有的View先从RecyclerView中detach掉，
        // 然后标记为"Scrap"状态，放入 Scrap Heap或Recycle Pool 来提高复用效率.
        detachAndScrapAttachedViews(recycler)
        // 处理需要显示的的控件
        val itemCount  = itemCount

        for (i in 0..itemCount) {
            // 从缓存 或 创建 返回一个 view.
            val scrap = recycler.getViewForPosition(i)
            addView(scrap)
            measureChildWithMargins(scrap, 0, 0);
            // layoutDecoratedWithMargins(scrap, ... ...)
        }

        // 对于detachAndScrapAttachedViews与getViewForPosition 缓存的了解可以参考这篇文章：
        // https://blog.csdn.net/qw85525006/article/details/91127988
    }

    override fun onLayoutCompleted(state: RecyclerView.State?) {
        super.onLayoutCompleted(state)
    }
}