package com.zeke.ktx.modules.player.entity

import com.zeke.ktx.MultiItemEntity

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
        val live: String,
        val name: String
) : MultiItemEntity {
    override fun getItemType(): Int {
        return if (isTitle) 0 else 1
    }

}