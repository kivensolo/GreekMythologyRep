package com.zeke.network.interceptor

import com.zeke.kangaroo.utils.ZLog
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

/**
 * date：2019/9/17
 * description：http logging 拦截器
 */
class LoggingInterceptor : Interceptor {
    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val t1 = System.nanoTime()
        ZLog.d("INFO", String.format("Sending request %s on %s%n%s",
                request.url(), chain.connection(), request.headers()))
        val response = chain.proceed(request)
        val t2 = System.nanoTime()
        ZLog.d("INFO", String.format("Received response for %s in %.1fms%n%s",
                response.request().url(), (t2 - t1) / 1e6, response.headers()))
        return response
    }
}