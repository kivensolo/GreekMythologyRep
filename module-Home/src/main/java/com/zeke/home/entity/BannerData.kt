package com.zeke.home.entity

data class BannerData(
    val data:MutableList<BannerItem>
)

data class BannerItem(
    val contentType:String = "banner",
    val desc:String,       // 简单描述
    val id:Int,            // 文章id
    val imagePath:String,  // 图片地址
    val isVisible:Int,     // 是否可见 1:可见
    val order:Int,         // 序号
    val title:String,      // Title
    val type:Int,          // 类型  不知道是啥用
    val url:String
)