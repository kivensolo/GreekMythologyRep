package com.kingz.module.wanandroid.response

/**
 * author：ZekeWang
 * date：2021/1/28
 * description：玩Android数据返回
 */
data class WanAndroidResponse<T>(
    var data: T,
    var errorCode: Int = 0,
    val errorMsg: String? = null
)