package com.zeke.eyepetizer.bean

import com.google.gson.JsonObject

/**
 * author: King.Z <br></br>
 * date:  2021/5/23 19:52 <br></br>
 * description: 开眼栏目详情数据
 */
data class EyepetizerTabPageData(
    val itemList: List<Item>,
    val count: Int, // 16
    val total: Int, // 0
    val nextPageUrl: String, //http://baobab.kaiyanapp.com/api/v5/index/tab/discovery?start=0
    val adExist: Boolean //false
)

data class Item(
    var type: String, //horizontalScrollCard
    //不解析该字段，因为该字段代表的数据结构会随着type的变化而变化，
    // 保留原始数据，在具体解析时根据type的不同值进行相应类的类型转换
    var data: JsonObject,
    var tag: Any, //null
    var id: Int, //0
    var adIndex: Int //-1
)