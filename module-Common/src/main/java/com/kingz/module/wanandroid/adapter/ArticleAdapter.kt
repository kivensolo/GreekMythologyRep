package com.kingz.module.wanandroid.adapter

import android.content.Context
import android.text.TextUtils
import android.view.ViewGroup
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.kingz.module.common.R
import com.kingz.module.wanandroid.bean.Article
import com.like.LikeButton
import com.like.OnLikeListener


/**
 * author: King.Z <br>
 * date:  2020/5/24 13:05 <br>
 * description: 支持多种viewType的文章Adapter <br>
 *
 */
open class ArticleAdapter @JvmOverloads constructor(
    data: MutableList<Article>? = null,
    var mType:Int? = TYPE_COMMON
) : BaseQuickAdapter<Article, BaseViewHolder>(R.layout.item_article, data) {
    private var mContext: Context? = null
    var likeListener: LikeListener? = null

    open interface LikeListener {
        fun liked(item: Article, adapterPosition: Int)
        fun unLiked(item: Article, adapterPosition: Int)
    }

    companion object {
        const val TYPE_COMMON = 0x0001
        const val TYPE_COLLECTION = 0x0002
    }

    public override fun getDefItemCount(): Int {
        return super.getDefItemCount()
    }

    override fun getDefItemViewType(position: Int): Int {
        return TYPE_COMMON
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        mContext = parent.context
        return super.onCreateViewHolder(parent, viewType)
    }

    override fun convert(holder: BaseViewHolder, item: Article, payloads: List<Any>) {

    }

    override fun convert(holder: BaseViewHolder, item: Article) {
        holder.apply {
            val userName = item.author ?: item.shareUser ?: "匿名"
            setText(R.id.tvAuthor, userName)     //要转html Html.fromHtml(str).toString()
            setText(R.id.tvDesc, item.title)
            setText(R.id.tvDate, item.niceDate)
            setText(R.id.tvTitle, item.title)

            setGone(R.id.tvTagTop, item.isTop)
            setGone(R.id.tvTagNew, item.isFresh)
            setGone(R.id.tvTagQa, TextUtils.equals(item.superChapterName, "问答"))
            setGone(R.id.tvDesc, TextUtils.isEmpty(item.desc))
            setGone(R.id.ivPoster, TextUtils.isEmpty(item.envelopePic))
            setText(R.id.tvType, String.format("%s/%s", item.superChapterName, item.chapterName))
            val ivLike = getView<LikeButton>(R.id.ivLike)
            ivLike.setOnLikeListener(object : OnLikeListener {
                override fun liked(likeButton: LikeButton?) {
                    likeListener?.liked(item, holder.adapterPosition)
                }

                override fun unLiked(likeButton: LikeButton?) {
                    likeListener?.unLiked(item, holder.adapterPosition)
                }
            })
            when(mType){
                TYPE_COLLECTION -> {
                    ivLike.isLiked = true
                }
                TYPE_COMMON ->{
                    ivLike.isLiked = item.collect
                }
            }
        }

        val options = RequestOptions().placeholder(R.drawable.placeholder_wanandroid)
            .error(mContext!!.resources.getDrawable(R.color.red))
            .transform(RoundedCorners(20))

        val ivProject: ImageView = holder.itemView.findViewById(R.id.ivPoster)
        if (!TextUtils.isEmpty(item.envelopePic)) {
            Glide.with(ivProject)
                .load(item.envelopePic)
                .apply(options)
                .into(ivProject)
        }

    }

    /**
     * 重置指定位置数据的收藏按钮状态
     */
    fun resetCollectState(position: Int){
        val likeButton = getViewByPosition(position, R.id.ivLike) ?: return
        (likeButton as LikeButton).isLiked = likeButton.isLiked
        notifyItemChanged(position)
    }
}