package com.zeke.home.wanandroid.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.kingz.module.home.R
import com.kingz.module.wanandroid.bean.BannerItem
import com.youth.banner.adapter.BannerAdapter


/**
 * author：ZekeWang
 * date：2021/1/29
 * description：自定义的BannerAdapter
 * banner库默认的是BannerImageAdapter，只支持了一个ImageView的布局。
 *
 * 继承BannerAdapter，和RecyclerView的Adapter一样（如果只是图片轮播也可以使用默认的）
 */
class HomeBannerAdapter<T> constructor(mDatas: List<T>?) :
    BannerAdapter<T, HomeBannerAdapter.BannerCustomViewHolder>(mDatas) {

    var requestOptions = RequestOptions()
        .placeholder(R.color.gray)
        .fitCenter()
        .diskCacheStrategy(DiskCacheStrategy.DATA)
        .error(R.color.gray)

    override fun onCreateHolder(parent: ViewGroup, viewType: Int): BannerCustomViewHolder {
        val rootView = LayoutInflater.from(parent.context)
            .inflate(R.layout.banner_item_layout, null, false)
        //注意，必须设置为match_parent，这个是viewpager2强制要求的
        val params = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        rootView.layoutParams = params
        return BannerCustomViewHolder(rootView)
    }

    override fun onBindView(
        holder: BannerCustomViewHolder,
        data: T,
        position: Int,
        size: Int) {
        if (data is BannerItem) {
            Glide.with(holder.itemView)
                .load(data.imagePath)
                .apply(requestOptions)
                .transition(DrawableTransitionOptions.withCrossFade(600))
                .thumbnail(0.1f) // 设置缩略图支持：先加载缩略图 然后在加载全图
                // 传了一个 0.1f 作为参数，Glide 将会显示原始图像的10%的大小。
                .into(holder.bannerImageView)

            holder.bannerTitleView.text = data.title
        }
    }


    class BannerCustomViewHolder(@NonNull view: View) : RecyclerView.ViewHolder(view) {
        val bannerImageView: ImageView
            get() {
                return itemView.findViewById(R.id.bannerImage)
            }

        val bannerTitleView: TextView
            get() {
                return itemView.findViewById(R.id.bannerTitle)
            }


    }
}