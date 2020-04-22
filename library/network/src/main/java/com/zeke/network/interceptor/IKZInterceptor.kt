package com.zeke.network.interceptor

import okhttp3.Interceptor
import java.util.*

/**
 * author: King.Z <br>
 * date:  2020/4/21 22:41 <br>
 * description: 拦截器顶层接口 <br>
 */
interface IKZInterceptor {

    interface Set<Builder> {
        fun addInterceptor(vararg interceptors: Interceptor): Builder
        fun addHeader(var1: Map<String?, String?>?): Builder
        fun addDefaultHeader(var1: Map<String?, String?>?): Builder
        fun setLogLevel(var1: Boolean, var2: Int, var3: String?): Builder
        fun setLogLevel(var1: Boolean, var2: Int): Builder
    }

    interface Get {
        fun getLogInterceptor():Interceptor
        val headerInterceptor: Interceptor?
        val interceptors: ArrayList<Any?>?
    }
}