package com.kingz.module.common.bean

data class BannerData(
    val data:MutableList<BannerItem>
)

/**
 * desc :
 * id : 6
 * imagePath : http://www.wanandroid.com/blogimgs/62c1bd68-b5f3-4a3c-a649-7ca8c7dfabe6.png
 * isVisible : 1
 * order : 1
 * title : 我们新增了一个常用导航Tab~
 * type : 0
 * url : http://www.wanandroid.com/navi
 */
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