package com.zeke.eyepetizer.bean.cards.item

import com.zeke.eyepetizer.bean.Header
import com.zeke.eyepetizer.bean.Item


/**
 * 文件： SquareCardCollection
 * 描述：
 * 作者： YangJunQuan   2018-8-22.
 */

data class SquareCardCollection(
        val dataType: String, //ItemCollection
        val header: Header,
        val itemList: List<Item>,
        val count: Int, //5
        val adTrack: Any //null
)


