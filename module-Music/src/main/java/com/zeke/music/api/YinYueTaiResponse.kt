package com.zeke.music.api

import com.zeke.reactivehttp.bean.IHttpWrapBean

/**
 * author：ZekeWang
 * description：音悦台数据返回
 */
data class YinYueTaiResponse<T>(
    var data: T
) : IHttpWrapBean<T> {
    override val httpCode: Int
        get() = 200
    override val httpData: T
        get() = data
    override val httpMsg: String
        get() = ""
    override val httpIsSuccess: Boolean
        get() = true
}