package com.kingz.module.wanandroid.adapter

import android.text.Html
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.module.LoadMoreModule
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.kingz.module.common.R
import com.kingz.module.wanandroid.bean.KnowledgeTreeBean

/**
 * author：ZekeWang
 * date：2022/1/28
 * description：知识体系的数据适配器
 */
class KnowledgeTreeAdapter :
    BaseQuickAdapter<KnowledgeTreeBean, BaseViewHolder>(R.layout.item_article)
    , LoadMoreModule {
    override fun convert(holder: BaseViewHolder, item: KnowledgeTreeBean) {
        holder.setText(R.id.tvTitle, item.name)
        item.children.let {
            //
            val tags = it?.joinToString("    ", transform = { child ->
                Html.fromHtml(child.name)
            })
            holder.setText(R.id.tvDesc, tags)
        }
    }
}