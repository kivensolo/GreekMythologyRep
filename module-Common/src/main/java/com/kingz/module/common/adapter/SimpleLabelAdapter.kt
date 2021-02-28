package com.kingz.module.common.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.kingz.module.common.R

/**
 * author：ZekeWang
 * date：2021/2/28
 * description： 简单的文本itemAdapater，用于RecyclerView的Demo展示
 */
class SimpleLabelAdapter @JvmOverloads constructor(
    data: MutableList<String>? = null
) : BaseQuickAdapter<String, BaseViewHolder>(R.layout.view_text, data) {
    override fun convert(holder: BaseViewHolder, item: String) {
        holder.apply {
            setText(R.id.title,item)
            setText(R.id.desc,item)
        }
    }
}