package com.zeke.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.kingz.module.common.adapter.IDelegateAdapter
import com.kingz.module.home.R
import com.zeke.home.entity.HomeRecomData
import com.zeke.kangaroo.adapter.CommonRecyclerAdapter

/**
 * author: King.Z <br>
 * date:  2020/5/24 13:22 <br>
 * description: 单独一个海报的代理Adapter <br>
 */
class OnePicDelegateAdapter: IDelegateAdapter<HomeRecomData> {
    override fun isForViewType(dataType: HomeRecomData): Boolean {
        return dataType.type == "one_pic"
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.news_one_pic_item, parent, false)
        return CommonRecyclerAdapter.ViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int, dataType: HomeRecomData) {
        if(holder is CommonRecyclerAdapter.ViewHolder){
            holder.getView<TextView>(R.id.tv_content)?.text = dataType.name
            // GlideApp().with(holder.itemView.getContext()).load(news.imgUrls.get(0)).into(viewHolder.ivPic);
            val drawable = holder.itemView.context.resources.getDrawable(R.drawable.cat_m)
            holder.getView<ImageView>(R.id.iv_pic).background = drawable

        }
    }
}