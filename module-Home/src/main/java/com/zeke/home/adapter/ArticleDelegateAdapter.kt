package com.zeke.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.kingz.module.common.adapter.IDelegateAdapter
import com.kingz.module.home.R
import com.zeke.home.bean.ArticleData
import com.zeke.kangaroo.adapter.CommonRecyclerAdapter

/**
 * author: King.Z <br>
 * date:  2020/5/24 13:22 <br>
 * description: 文章类型模板UI的代理Adapter <br>
 */
class ArticleDelegateAdapter: IDelegateAdapter<ArticleData.DataBean.ArticleItem> {
    override fun isForViewType(dataType: ArticleData.DataBean.ArticleItem): Boolean {
        return dataType.contentType == "artical"
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.article_normal_item, parent, false)
        return CommonRecyclerAdapter.ViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int, dataType: ArticleData.DataBean.ArticleItem) {
        if(holder is CommonRecyclerAdapter.ViewHolder){
            holder.getView<TextView>(R.id.tv_content).text = dataType.title
            holder.getView<TextView>(R.id.tv_source).text = dataType.superChapterName
            holder.getView<TextView>(R.id.tv_time).text = dataType.niceDate
            // GlideApp().with(holder.itemView.getContext()).load(news.imgUrls.get(0)).into(viewHolder.ivPic);
//            val drawable = holder.itemView.context.resources.getDrawable(R.mipmap.ic_launcher)
//            holder.getView<ImageView>(R.id.iv_pic1).background = drawable
//            holder.getView<ImageView>(R.id.iv_pic2).background = drawable
//            holder.getView<ImageView>(R.id.iv_pic3).background = drawable
        }
    }
}