package com.zeke.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.kingz.module.common.adapter.IDelegateAdapter
import com.kingz.module.home.R
import com.zeke.home.entity.TemplatePageData
import com.zeke.kangaroo.adapter.CommonRecyclerAdapter

/**
 * author: King.Z <br>
 * date:  2020/5/24 13:22 <br>
 * description: 3个海报的代理Adapter <br>
 */
class ThreePicDelegateAdapter: IDelegateAdapter<TemplatePageData> {
    override fun isForViewType(dataType: TemplatePageData): Boolean {
        return dataType.type == "three_pic"
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.news_three_pic_item, parent, false)
        return CommonRecyclerAdapter.ViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, dataType: TemplatePageData) {
        if(holder is CommonRecyclerAdapter.ViewHolder){
            holder.getView<TextView>(R.id.tv_content).text = dataType.name
            holder.getView<TextView>(R.id.tv_source).text = "澎湃新闻"
            holder.getView<TextView>(R.id.tv_time).text = "07:33"
            // GlideApp().with(holder.itemView.getContext()).load(news.imgUrls.get(0)).into(viewHolder.ivPic);
            val drawable = holder.itemView.context.resources.getDrawable(R.mipmap.ic_launcher)
            holder.getView<ImageView>(R.id.iv_pic1).background = drawable
            holder.getView<ImageView>(R.id.iv_pic2).background = drawable
            holder.getView<ImageView>(R.id.iv_pic3).background = drawable
        }
    }
}