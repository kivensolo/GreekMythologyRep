package com.zeke.network.interceptor

import com.zeke.kangaroo.utils.ZLog
import com.zeke.network.utils.LoggingPrinter
import okhttp3.Interceptor
import okhttp3.Response
import okio.Buffer
import java.io.IOException

/**
 * date：2019/9/17
 * description：http logging 拦截器
 */
class LoggingInterceptor : Interceptor {
    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val requestBuffer = Buffer()
        if (request.body() != null) {
            request.body()!!.writeTo(requestBuffer)
        } else {
            ZLog.w("request.body() == null")
        }
        LoggingPrinter.printRequest(chain)
        val response = chain.proceed(request)
        LoggingPrinter.printResponse(response)
        return response
    }
}