package com.kingz.module.wanandroid.response

import com.zeke.reactivehttp.bean.IHttpWrapBean

/**
 * author：ZekeWang
 * date：2021/1/28
 * description：玩Android数据返回
 */
data class WanAndroidResponse<T>(
    var data: T?,
    var errorCode: Int = 0,
    val errorMsg: String? = null
) : IHttpWrapBean<T> {
    override val httpCode: Int
        get() = 200
    override val httpMsg: String
        get() = errorMsg ?: ""
    override val httpData: T?
        get() = data
    override val httpIsSuccess: Boolean
        get() = errorCode == 0
}