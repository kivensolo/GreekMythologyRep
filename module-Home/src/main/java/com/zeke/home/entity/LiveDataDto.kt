package com.zeke.home.entity

/**
 * @description：
 * 直播频道数据类
 */
data class LiveDataDto(
        val lives: MutableList<Live>?
)

data class Live(
        val icon: String,
        val isTitle: Boolean,
        val live: String,  //playUrl
        val name: String
) : MultiItemEntity {
    override fun getItemType(): Int {
        return if (isTitle) 0 else 1
    }

}