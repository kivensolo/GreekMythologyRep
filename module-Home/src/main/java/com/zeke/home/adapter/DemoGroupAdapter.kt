package com.zeke.home.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.graphics.ColorUtils
import androidx.recyclerview.widget.RecyclerView
import com.kingz.module.common.bean.DemoGroup
import com.kingz.module.common.bean.DemoSample
import com.kingz.module.common.setting.SettingUtil
import com.kingz.module.home.R

/**
 * Demo分组列表的 RecyclerView Adapter
 *
 * 将 List<DemoGroup> 拍平为一维 items 列表，通过两种 ViewType 渲染分组头和子项。
 * 只维护 items 一个数据源，展开/折叠通过增量增删子项实现。
 */
class DemoGroupAdapter(
    private val onChildClick: (DemoSample) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    /**
     * 数据源列表，主要分GroupHeader和ChildItem两类
     */
    private val items = mutableListOf<DemoListItem>()

    /**
     * 展开的分组序号记录集合
     */
    private val expandedGroupsSet = mutableSetOf<Int>()
    private var themeColor: Int = SettingUtil.getAppThemeColor()
    private var recyclerView: RecyclerView? = null

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        this.recyclerView = recyclerView
    }

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        super.onDetachedFromRecyclerView(recyclerView)
        this.recyclerView = null
    }

    /**
     * 将主题色转为 HSL，把饱和度降到原来的 35%，
     * 保持色相和明度不变。效果是同色调的灰调版，看起来更柔和
     * @param color 颜色
     * @param saturationFactor 饱和度因子, 值越小越偏灰，越大越接近原色。
     */
    private fun mutedColor(color: Int, saturationFactor: Float = 0.35f): Int {
        val hsl = floatArrayOf(0f, 0f, 0f)
        ColorUtils.colorToHSL(color, hsl)
        hsl[1] *= saturationFactor
        return ColorUtils.HSLToColor(hsl)
    }

    fun setData(groups: List<DemoGroup>) {
        items.clear()
        expandedGroupsSet.clear()
        groups.forEachIndexed { index, group ->
            items.add(DemoListItem.GroupHeader(group, index))
        }
        notifyDataSetChanged()
    }

    fun updateThemeColor(color: Int) {
        themeColor = color
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = items.size

    override fun getItemViewType(position: Int): Int {
        return when (items[position]) {
            is DemoListItem.GroupHeader -> VIEW_TYPE_HEADER
            is DemoListItem.ChildItem -> VIEW_TYPE_CHILD
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            VIEW_TYPE_HEADER -> {
                val view = inflater.inflate(R.layout.item_expand_group, parent, false)
                HeaderViewHolder(view)
            }
            else -> {
                val view = inflater.inflate(R.layout.item_expand_child, parent, false)
                ChildViewHolder(view)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (val item = items[position]) {
            is DemoListItem.GroupHeader -> bindHeader(holder as HeaderViewHolder, item)
            is DemoListItem.ChildItem -> bindChild(holder as ChildViewHolder, item)
        }
    }

    private fun bindHeader(holder: HeaderViewHolder, item: DemoListItem.GroupHeader) {
        val expanded = expandedGroupsSet.contains(item.groupIndex)
        holder.title.text = item.group.title
        holder.arrow.rotation = if (expanded) 90f else 0f
        updateHeaderStyle(holder, expanded)
        holder.itemView.setOnClickListener { toggleGroup(item.groupIndex) }
    }

    private fun updateHeaderStyle(holder: HeaderViewHolder, expanded: Boolean) {
        if (expanded) {
            holder.itemView.setBackgroundColor(mutedColor(themeColor))
            holder.title.setTextColor(ContextCompat.getColor(holder.itemView.context, R.color.white))
        } else {
            holder.itemView.setBackgroundColor(ContextCompat.getColor(holder.itemView.context, R.color.transparent))
            holder.title.setTextColor(ContextCompat.getColor(holder.itemView.context, R.color.black))
        }
    }

    private fun bindChild(holder: ChildViewHolder, item: DemoListItem.ChildItem) {
        holder.title.text = item.sample.name
        holder.itemView.setOnClickListener { onChildClick(item.sample) }
    }

    /**
     * 展开/折叠分组
     * @param groupIndex 分组索引
     */
    private fun toggleGroup(groupIndex: Int) {
        val headerPos = items.indexOfFirst {
            it is DemoListItem.GroupHeader && it.groupIndex == groupIndex
        }
        if (headerPos == -1) return

        val expanded: Boolean
        if (groupIndex in expandedGroupsSet) { //收缩
            expandedGroupsSet.remove(groupIndex)
            expanded = false
            var count = 0
            var pos = headerPos + 1
            while (pos < items.size && items[pos] is DemoListItem.ChildItem
                && (items[pos] as DemoListItem.ChildItem).groupIndex == groupIndex) {
                count++
                pos++
            }
            repeat(count) { items.removeAt(headerPos + 1) }
            notifyItemRangeRemoved(headerPos + 1, count)
        } else { //展开(其实就是在Header后插入子项数据)
            expandedGroupsSet.add(groupIndex)
            expanded = true
            val header = items[headerPos] as DemoListItem.GroupHeader
            // 加载子项数据
            val children = header.group.samples.mapIndexed { childIndex, sample ->
                DemoListItem.ChildItem(sample, groupIndex, childIndex)
            }
            items.addAll(headerPos + 1, children)
            notifyItemRangeInserted(headerPos + 1, children.size)
        }

        // 直接通过 ViewHolder 更新箭头动画和样式，避免 notifyItemChanged 干扰子项
        val holder = recyclerView?.findViewHolderForAdapterPosition(headerPos) as? HeaderViewHolder
        if (holder != null) {
            holder.arrow.animate()
                .rotation(if (expanded) 90f else 0f)
                .setDuration(250)
                .setInterpolator(AccelerateDecelerateInterpolator())
                .start()
            updateHeaderStyle(holder, expanded)
        }
    }

    private class HeaderViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val title: TextView = view.findViewById(R.id.parent_group)
        val arrow: ImageView = view.findViewById(R.id.parent_group_img)
    }

    private class ChildViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val title: TextView = view.findViewById(R.id.child_group)
    }

    companion object {
        private const val VIEW_TYPE_HEADER = 0
        private const val VIEW_TYPE_CHILD = 1
    }
}

/**
 * Demo展示的列表Item数据类型
 */
private sealed class DemoListItem {
    data class GroupHeader(val group: DemoGroup, val groupIndex: Int) : DemoListItem()
    data class ChildItem(val sample: DemoSample, val groupIndex: Int, val childIndex: Int) : DemoListItem()
}
