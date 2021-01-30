package com.zeke.home.wanandroid.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.kingz.module.home.R
import com.kingz.module.wanandroid.bean.BannerItem
import com.kingz.module.wanandroid.bean.BannerType
import com.kingz.module.wanandroid.bean.STYLE_PIC
import com.kingz.module.wanandroid.bean.STYLE_VID
import com.youth.banner.adapter.BannerAdapter
import com.youth.banner.util.BannerUtils
import com.zeke.home.wanandroid.viewholder.ImageTextViewHolder


/**
 * author：ZekeWang
 * date：2021/1/29
 * description：首页自定义的BannerAdapter
 * 支持 图片 & 视频的模式
 *
 * banner库默认的是BannerImageAdapter，只支持了一个ImageView的布局。
 * 继承BannerAdapter，和RecyclerView的Adapter一样（如果只是图片轮播也可以使用默认的）
 */
class HomeBannerAdapter<T> constructor(mDatas: List<T>?) :
    BannerAdapter<T, RecyclerView.ViewHolder>(mDatas) {

    var requestOptions = RequestOptions()
        .placeholder(R.color.gray)
        .fitCenter()
        .diskCacheStrategy(DiskCacheStrategy.DATA)
        .error(R.color.gray)


    override fun onCreateHolder(parent: ViewGroup, @BannerType viewType: Int)
            : RecyclerView.ViewHolder {
        when (viewType) {
            STYLE_VID -> {
                return ImageTextViewHolder(
                    BannerUtils.getView(parent, R.layout.banner_item_layout)
                )
            }

            STYLE_PIC -> {
                val rootView = LayoutInflater.from(parent.context)
                    .inflate(R.layout.banner_item_layout, null, false)
                //注意，必须设置为match_parent，这个是viewpager2强制要求的
                val params = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
                rootView.layoutParams = params
                return ImageTextViewHolder(rootView)
            }
            else -> {
                return ImageTextViewHolder(
                    BannerUtils.getView(parent,R.layout.banner_item_layout)
                )
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        val data = getData(getRealPosition(position))
        if (data is BannerItem) {
            return data.viewType
        }
        return STYLE_PIC //默认为图片模式
    }

    override fun onBindView(
        holder: RecyclerView.ViewHolder,
        data: T, position: Int, size: Int
    ) {
        when (holder.itemViewType) {
            STYLE_VID, STYLE_PIC -> {
                if (data is BannerItem) {
                    Glide.with(holder.itemView)
                        .load(data.imagePath)
                        .apply(requestOptions)
                        .transition(DrawableTransitionOptions.withCrossFade(600))
                        .thumbnail(0.1f) // 设置缩略图支持：先加载缩略图 然后在加载全图
                        // 传了一个 0.1f 作为参数，Glide 将会显示原始图像的10%的大小。
                        .into((holder as ImageTextViewHolder).bannerImageView)
                    holder.bannerTitleView.text = data.title
                }
            }
        }

    }


}