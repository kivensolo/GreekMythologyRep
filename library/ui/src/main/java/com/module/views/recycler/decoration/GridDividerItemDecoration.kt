import android.content.Context
import android.graphics.Rect
import android.view.View
import androidx.core.view.marginEnd
import androidx.core.view.marginStart
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import kotlin.math.max


/**
 * author：ZeKeWong
 * date：2025/9/30
 * description：支持item均分的网格布局装饰器
 * 支持以下特征
 * - 可自定义首列、尾列与父容器的间隙
 * - 默认模式下，行/列间距将动态分配
 * - 行间距可自定义
 *
 * 通过 mRecyclerView.addItemDecoration(new GridDividerItemDecoration(this)) 使用
 */
class GridDividerItemDecoration(
    private val mContext: Context,
    private val firstAndLastColumnSpace: Int = 0, //第1列和最后1列与父控件的间隔；
    private val rowSpace: Int = -1, // 自定义的item行间距,不设置时，与自动计算的columnSpace一致
    private val firstRowTopMargin: Int = 0, // 第1行顶部的自定义间隔(优先级大于rowSpace)
    private val lastRowBottomMargin: Int = 0// 最后1行底部的自定义间隔(优先级大于rowSpace)
) : RecyclerView.ItemDecoration() {
    private var mSpanCount = 0
    private var mScreenW = 0

     override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)

        var top = 0
        val left: Int
        val right: Int
        var bottom: Int

        val itemPosition = (view.layoutParams as RecyclerView.LayoutParams).viewLayoutPosition
        mSpanCount = getSpanCount(parent)
        val childCount = parent.adapter!!.itemCount

        val maxDividerWidth: Int = getMaxDividerWidth(view,parent)
        //首尾两列与父布局之间的间隔
        val spaceWidth: Int = firstAndLastColumnSpace
        //除去首尾两列的间隔后，计算item与item之间的距离
        val eachItemWidth: Int = maxDividerWidth / mSpanCount
        val dividerItemWidth: Int =
            (maxDividerWidth - 2 * spaceWidth) / (mSpanCount - 1) //item与item之间的距离

        left = itemPosition % mSpanCount * (dividerItemWidth - eachItemWidth) + spaceWidth
        right = eachItemWidth - left
        bottom = if(rowSpace < 0){
            dividerItemWidth
        }else{
            rowSpace
        }

        // 首行
        if (firstRowTopMargin > 0 && isFirstRow(parent, itemPosition, mSpanCount, childCount)) {
            top = firstRowTopMargin
        }
        // 尾行
        if (isLastRow(parent, itemPosition, mSpanCount, childCount)) {
            bottom = max(0, lastRowBottomMargin)
        }
        outRect.set(left, top, right, bottom)
    }

    /**
     * 获取屏幕上可分配的最大间隔宽度
     * 并根据 “屏幕宽度 - View的宽度*spanCount - recycler的左右margin” 得到屏幕剩余空间
     * FIXME 应该优化为“父级recyclerView的宽度 - View的宽度*spanCount”
     *
     * @param view
     * @return 屏幕上可分配的最大间隔宽度
     */
    private fun getMaxDividerWidth(view: View, parent: RecyclerView): Int {
        val itemWidth = view.layoutParams.width
        val itemHeight = view.layoutParams.height
        val screenWidth: Int = getScreenWidth()
        var maxDividerWidth =
            screenWidth - (itemWidth * mSpanCount) - (parent.marginStart + parent.marginEnd)

        if (itemHeight < 0 || itemWidth < 0 || maxDividerWidth <= (mSpanCount - 1) * firstAndLastColumnSpace) {
            // Item View的大小未指定则自动分配空间
            view.layoutParams.width = getAttachCloumnWidth()
            view.layoutParams.height = getAttachCloumnWidth()
            maxDividerWidth = screenWidth - view.layoutParams.width * mSpanCount
        }

        return maxDividerWidth
    }

    private fun getScreenWidth(): Int {
        if (mScreenW > 0) {
            return mScreenW
        }
        val wPixels = mContext.resources.displayMetrics.widthPixels
        val hPixels = mContext.resources.displayMetrics.heightPixels

        mScreenW = if (wPixels > hPixels) hPixels  else wPixels
        return mScreenW
    }

    /**
     * 根据屏幕宽度和item数量分配 item View的width和height
     *
     * @return
     */
    private fun getAttachCloumnWidth(): Int {
        var itemWidth = 0
        var spaceWidth = 0
        try {
            val width = getScreenWidth()
            spaceWidth = 2 * firstAndLastColumnSpace
            itemWidth = (width - spaceWidth) / mSpanCount - 40
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return itemWidth
    }

    /**
     * 判读是否是第一列
     *
     * @param parent
     * @param pos
     * @param spanCount
     * @return
     */
    private fun isFirstColumn(parent: RecyclerView, pos: Int, spanCount: Int): Boolean {
        val layoutManager = parent.layoutManager
        if (layoutManager is GridLayoutManager) {
            if (pos % spanCount == 0) {
                return true
            }
        } else if (layoutManager is StaggeredGridLayoutManager) {
            val orientation = layoutManager.orientation
            if (orientation == StaggeredGridLayoutManager.VERTICAL) {
                if (pos % spanCount == 0) { // 第一列
                    return true
                }
            } else {
            }
        }
        return false
    }

    /**
     * 判断是否是最后一列
     *
     * @param parent
     * @param pos
     * @param spanCount
     * @param childCount
     * @return
     */
    private fun isLastColumn(parent: RecyclerView, pos: Int, spanCount: Int): Boolean {
        val layoutManager = parent.layoutManager
        if (layoutManager is GridLayoutManager) {
            if ((pos + 1) % spanCount == 0) { // 如果是最后一列，则不需要绘制右边
                return true
            }
        } else if (layoutManager is StaggeredGridLayoutManager) {
            val orientation = layoutManager.orientation
            if (orientation == StaggeredGridLayoutManager.VERTICAL) {
                if ((pos + 1) % spanCount == 0) { // 最后一列
                    return true
                }
            } else {
            }
        }
        return false
    }

    /**
     * 判读是否是最后一行
     *
     * @param parent
     * @param pos
     * @param spanCount
     * @param childCount
     * @return
     */
    private fun isLastRow(
        parent: RecyclerView, pos: Int, spanCount: Int, childCount: Int
    ): Boolean {
        val layoutManager = parent.layoutManager
        if (layoutManager is GridLayoutManager) {
            val lines =
                if (childCount % spanCount == 0) childCount / spanCount else childCount / spanCount + 1
            return lines == pos / spanCount + 1
        }
        return false
    }

    /**
     * 判断当前item的位置是否在第一行
     *
     * @param parent
     * @param pos
     * @param spanCount
     * @param childCount
     * @return
     */
    private fun isFirstRow(
        parent: RecyclerView, pos: Int, spanCount: Int, childCount: Int
    ): Boolean {
        val layoutManager = parent.layoutManager
        if (layoutManager is GridLayoutManager) {
            return (pos / spanCount + 1) == 1
        }
        return false
    }

    /**
     * 获取布局管理器的列数
     *
     * @param parent
     * @return
     */
    private fun getSpanCount(parent: RecyclerView): Int {
        var spanCount = -1
        val layoutManager = parent.layoutManager
        if (layoutManager is GridLayoutManager) {
            spanCount = layoutManager.spanCount
        } else if (layoutManager is StaggeredGridLayoutManager) {
            spanCount = layoutManager.spanCount
        }
        return spanCount
    }
}
