package com.zeke.eyepetizer.bean.cards.item

data class TextCard(
		val dataType: String, //TextCard
		val id: Int, //0
		val type: String, //header5
		val text: String, //本周排行
		val subTitle: Any, //null
		val actionUrl: String, //eyepetizer://ranklist/
		val adTrack: Any, //null
		val follow: Any //null
)