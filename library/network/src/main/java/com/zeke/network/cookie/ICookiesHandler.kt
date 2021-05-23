package com.zeke.network.cookie

import okhttp3.HttpUrl

/**
 * author：ZekeWang
 * date：2021/5/11
 * description：Cookies 处理类
 */
interface ICookiesHandler {
    /**
     * 储存Cookies信息
     * @param httpUrl: Url of http.
     * @param cookies: cookies 数据
     */
    fun setCookies(httpUrl: HttpUrl, cookies: String)
    /**
     * 获取Cookies
     * @param httpUrl: request url of http.
     */
    fun getCookies(httpUrl:HttpUrl): String?
}