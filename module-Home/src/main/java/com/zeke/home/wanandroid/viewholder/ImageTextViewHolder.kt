package com.zeke.home.wanandroid.viewholder

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView
import com.kingz.module.home.R

/**
 * author: King.Z <br>
 * date:  2021/1/30 11:34 <br>
 * description: 图片 + 文字的Holer <br>
 */
class ImageTextViewHolder(@NonNull view: View) : RecyclerView.ViewHolder(view) {
    @Suppress("JoinDeclarationAndAssignment")
    var bannerImageView: ImageView
    var bannerTitleView: TextView

    init {
        bannerImageView = view.findViewById(R.id.bannerImage)
        bannerTitleView = itemView.findViewById(R.id.bannerTitle)
    }
}