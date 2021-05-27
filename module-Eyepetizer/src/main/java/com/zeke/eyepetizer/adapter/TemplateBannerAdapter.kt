package com.zeke.eyepetizer.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.kingz.module.wanandroid.bean.BannerType
import com.kingz.module.wanandroid.bean.STYLE_EYE_BANNER_CARD
import com.kingz.module.wanandroid.bean.STYLE_PIC
import com.kingz.module.wanandroid.bean.STYLE_VID
import com.youth.banner.adapter.BannerAdapter
import com.youth.banner.util.BannerUtils
import com.zeke.eyepetizer.ImageTextViewHolder
import com.zeke.eyepetizer.bean.ItemTypeBanner
import com.zeke.moudle_eyepetizer.R


/**
 * author：ZekeWang
 * date：2021/1/29
 * description：
 * 开眼视频Banner模板的Adapter
 * 支持 图片 & 视频的模式
 *
 * banner库默认的是BannerImageAdapter，只支持了一个ImageView的布局。
 * 继承BannerAdapter，和RecyclerView的Adapter一样（如果只是图片轮播也可以使用默认的）
 *
 * TODO 对Banner的Adapter进行抽离
 */
class TemplateBannerAdapter<T> constructor(mDatas: List<T>?) :
    BannerAdapter<T, RecyclerView.ViewHolder>(mDatas) {

    // <editor-fold defaultstate="collapsed" desc="图片加载配置 Image load option">
    private var requestOptions = RequestOptions()
        .placeholder(R.color.whitesmoke)
        .fitCenter()
        .diskCacheStrategy(DiskCacheStrategy.DATA)
        .error(R.color.whitesmoke)
    // </editor-fold>


    override fun onCreateHolder(parent: ViewGroup, @BannerType viewType: Int)
            : RecyclerView.ViewHolder {
        val bannerItemView = BannerUtils.getView(parent, R.layout.banner_image_label)

        return when (viewType) {
            STYLE_VID -> ImageTextViewHolder(bannerItemView)
            STYLE_PIC -> {
                //注意，必须设置为match_parent，这个是viewpager2强制要求的
                val params = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
                bannerItemView.layoutParams = params
                ImageTextViewHolder(bannerItemView)
            }
            else -> {
                ImageTextViewHolder(bannerItemView)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        val data = getData(getRealPosition(position))
        if(data is ItemTypeBanner){
            //开眼视频模式
            return STYLE_EYE_BANNER_CARD
        }

        return STYLE_PIC //默认为图片模式
    }

    override fun onBindView(
        holder: RecyclerView.ViewHolder,
        data: T, position: Int, size: Int
    ) {
        when (holder.itemViewType) {
            STYLE_VID,
            STYLE_PIC,
            STYLE_EYE_BANNER_CARD -> {
                if (data is ItemTypeBanner) {
                    Glide.with(holder.itemView)
                        .load(data.image)
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