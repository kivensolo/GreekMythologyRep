package com.zeke.music.adapter

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
import com.zeke.music.bean.RelatedVideoInfo


/**
 * author: King.Z <br>
 * description: 支持多种viewType的影片推荐数据Adapter <br>
 */
open class VideoRecomAdapter @JvmOverloads constructor(
    data: MutableList<RelatedVideoInfo>? = null,
    var mType:Int? = TYPE_COMMON
) : BaseQuickAdapter<RelatedVideoInfo, BaseViewHolder>(R.layout.item_video_recom, data) {
    private var mContext: Context? = null

    companion object {
        const val TYPE_COMMON = 0x0001
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

    override fun convert(holder: BaseViewHolder, item: RelatedVideoInfo, payloads: List<Any>) {

    }

    override fun convert(holder: BaseViewHolder, item: RelatedVideoInfo) {
        holder.apply {
            val videoName = item.videoName
            setText(R.id.recom_video_name, videoName)
        }

        val options = RequestOptions().placeholder(R.drawable.pic_video_default)
            .error(mContext!!.resources.getDrawable(R.color.red))
            .transform(RoundedCorners(20))

        val ivProject: ImageView = holder.itemView.findViewById(R.id.recom_video_poster)
        if (!TextUtils.isEmpty(item.videoImg)) {
            Glide.with(ivProject)
                .load(item.videoImg)
                .apply(options)
                .into(ivProject)
        }

    }
}