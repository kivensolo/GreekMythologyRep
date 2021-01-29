package com.zeke.home.wanandroid.adapter

import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.kingz.module.home.R
import com.kingz.module.wanandroid.bean.BannerItem
import com.youth.banner.adapter.BannerImageAdapter
import com.youth.banner.holder.BannerImageHolder


/**
 * author：ZekeWang
 * date：2021/1/29
 * description：自定义Banner布局，下面是常见的图片样式，更多实现可以看demo，可以自己随意发挥
 * 继承BannerAdapter，和RecyclerView的Adapter一样（如果只是图片轮播也可以使用默认的）
 */
class ImageBannerAdapter(mDatas: List<BannerItem?>?) :
    BannerImageAdapter<BannerItem>(mDatas) {

    var requestOptions = RequestOptions()
        .placeholder(R.color.gray)
        .fitCenter()
        .diskCacheStrategy(DiskCacheStrategy.DATA)
        .error(R.color.gray)

    override fun onBindView(
        holder: BannerImageHolder,
        data: BannerItem,
        position: Int,
        size: Int) {
        Glide.with(holder.imageView)
            .load(data.imagePath)
            .apply(requestOptions)
            .transition(DrawableTransitionOptions.withCrossFade(600))
            .thumbnail(0.1f) // 设置缩略图支持：先加载缩略图 然后在加载全图
            // 传了一个 0.1f 作为参数，Glide 将会显示原始图像的10%的大小。
            .into(holder.imageView)
    }
}