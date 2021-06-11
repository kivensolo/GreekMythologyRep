package com.zeke.eyepetizer.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.google.gson.Gson
import com.kingz.module.common.utils.image.GlideLoader
import com.zeke.eyepetizer.bean.Data
import com.zeke.eyepetizer.bean.Item
import com.zeke.eyepetizer.bean.cards.item.VideoSmallCard
import com.zeke.kangaroo.utils.TimeUtils
import com.zeke.moudle_eyepetizer.R

/**
 * author：ZekeWang
 * date：2021/6/8
 * description： 视频播放页的数据适配器
 */
class VideoPlayAdapter : RecyclerView.Adapter<BaseViewHolder>() {
    private var mContext: Context? = null
    //data
    var mHeaderData: Data? = null
    var mRelatedData = ArrayList<Item>()


    enum class ItemType(var value: Int) {
        TYPE_HEADER(0),
        TYPE_RELATED(1),
        TYPE_THE_END(2);
    }

    fun setData(headData: Data, relatedData: List<Item>) {
        this.mHeaderData = headData
        this.mRelatedData = relatedData as ArrayList<Item>
        this.notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return 1 + mRelatedData.size + 1
    }

    // --------------------- Create & Bind View holder ---------------------
    /**
     * 根据UI类型创建Holder
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        mContext = parent.context
        val inflater = LayoutInflater.from(parent.context)

        fun getHolder(@LayoutRes resource: Int): BaseViewHolder {
            return BaseViewHolder(inflater.inflate(resource, parent, false))
        }

        val viewHolder =
            when (viewType) {
                ItemType.TYPE_HEADER.value -> getHolder(R.layout.item_bean_for_client_card)
                ItemType.TYPE_RELATED.value -> getHolder(R.layout.item_video_small_card)
                ItemType.TYPE_THE_END.value -> getHolder(R.layout.template_the_end)
                else -> throw Exception("invalid view type")
            }
        return BaseViewHolder(viewHolder.itemView)
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        when (getItemViewType(position)) {
            ItemType.TYPE_HEADER.value -> initHeaderView(holder)
            ItemType.TYPE_RELATED.value -> initRelatedView(holder, mRelatedData[position-1])
            ItemType.TYPE_THE_END.value -> initTheEndView(holder)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (position) {
            0 -> ItemType.TYPE_HEADER.value
            in 1..mRelatedData.size -> ItemType.TYPE_RELATED.value
            else -> ItemType.TYPE_THE_END.value
        }
    }
    // --------------------- Create & Bind View holder ---------------------


    private fun initHeaderView(holder: BaseViewHolder) {
        //TODO 优化
//
//        //视频标题、内容
//        with(itemBeanForClientCardBinding!!) {
//            tvVideoTitle.text = mHeaderData?.title
//            tvCategory.text = "#${mHeaderData?.category}"
//            tvVideoDescription.text = mHeaderData?.description
//
//
//            tvCollectionCount.text = mHeaderData?.consumption?.collectionCount.toString()
//            tvShare.text = mHeaderData?.consumption?.shareCount.toString()
//            tvReply.text = mHeaderData?.consumption?.replyCount.toString()
//
//            tagsContainer.removeAllViews()
//            mHeaderData?.tags?.forEach { itemTag ->
//                val itemView = android.view.LayoutInflater.from(mContext).inflate(R.layout.video_tag, tagsContainer, false)
//                val ivTag = itemView.findViewById<ImageView>(R.id.ivTag)
//                val tvTag = itemView.findViewById<TextView>(R.id.tvTag)
//
//                tvTag.text = "#" + itemTag.name + "#"
//                ImageLoader.loadNetImageWithCornerAndShade(mContext, ivTag, itemTag.headerImage, placeHolderId = R.drawable.corner_4_solid_dark_light)
//                tagsContainer.addView(itemView)
//            }
//
//
//            tvAuthor.text = mHeaderData?.author?.name
//            tvSlogan.text = mHeaderData?.author?.description
//            ImageLoader.loadNetCircleImage(mContext, ivAvatar, mHeaderData?.author?.icon)
//        }


//        startAnimate(itemBeanForClientCardBinding)

    }

    private fun initRelatedView(holder: BaseViewHolder, item: Item) {
//
//        //init data
        val jsonObject = item.data
        val videoSmallCard = Gson().fromJson(jsonObject, VideoSmallCard::class.java)

        val videoTitle =
            videoSmallCard.title                                                       //视频标题
        val videoFeedUrl =
            videoSmallCard.cover.detail                                              //视频封面Url
        val videoCategory =
            "#" + videoSmallCard?.category                                           //视频类别
        val videoDuration =
            TimeUtils.generateTime(videoSmallCard.duration * 1000.toLong())       //视频时长

        holder.getView<TextView>(R.id.tvTitle)?.apply {
            this.text = videoTitle
            this.setTextColor(resources.getColor(R.color.white))
        }
        holder.getView<TextView>(R.id.tvCatogory).apply {
            this.text = videoCategory
            this.setTextColor(resources.getColor(R.color.white))
        }
        holder.getView<TextView>(R.id.tvVideoDuration).apply {
            this.text = videoDuration
        }

        val ivFeed = holder.getView<ImageView>(R.id.ivFeed)
        GlideLoader.loadNetImageWithCorner(
            mContext!!, ivFeed,
            videoFeedUrl, placeHolderId = R.drawable.corner_4_solid_dark_light
        )

        holder.itemView.setOnClickListener {
            //             changePlayVideo(position)
        }
//        with(itemVideoSmallCardBinding!!) {
//            //init view
//            tvCatogory.setTextColor(mContext.resources.getColor(R.color.white))
//
//
//            //init Event
//            holder.itemView.setOnClickListener {
//                onItemClick.invoke(position - 1)    // -1 是由于Header的存在
//            }
//        }
    }


//    private fun startAnimate(binding: ItemBeanForClientCardBinding) {
//
//
//        //init Animate
//        val translationAnim = TranslateAnimation(0f, 0f, -80f, 0f)
//        translationAnim.duration = 400
//
//        with(binding) {
//            binding.tvShare.startAnimation(translationAnim)
//            binding.tvReply.startAnimation(translationAnim)
//            binding.tvPreload.startAnimation(translationAnim)
//            binding.tvCollectionCount.startAnimation(translationAnim)
//
//            TiaoZiUtil(tvVideoTitle, mHeaderData?.title, 50)
//        }
//    }

    //没有更多数据，到底部的提示ItemView
    private fun initTheEndView(holder: BaseViewHolder) {
          holder.getView<TextView>(R.id.tvEnd).apply {
            this.typeface = android.graphics.Typeface.createFromAsset(mContext!!.assets, "fonts/Lobster-1.4.otf")
            this.setTextColor(resources.getColor(R.color.white))
            (parent as View).setBackgroundColor(android.graphics.Color.TRANSPARENT)
        }
    }

}