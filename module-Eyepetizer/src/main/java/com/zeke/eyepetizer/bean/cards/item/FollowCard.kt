package com.zeke.eyepetizer.bean.cards.item

import com.zeke.eyepetizer.bean.Content
import com.zeke.eyepetizer.bean.Header

data class FollowCard(
    val dataType: String, //FollowCard
    val header: Header,
    val content: Content,
    val adTrack: Any //null
)
