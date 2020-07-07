package com.zeke.network.interceptor

import com.zeke.kangaroo.utils.ZLog
import okhttp3.Interceptor
import okhttp3.Response
import okio.Buffer
import java.io.IOException
import java.nio.charset.Charset

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
            ZLog.d("request.body() == null")
        }
        val t1 = System.nanoTime()
        ZLog.d("LoggingInterceptor", String.format("Sending request %s on %s%n%s",
                request.url(), chain.connection(), request.headers()))
        val response = chain.proceed(request)

        //获得返回的body，注意此处不要使用responseBody.string()获取返回数据，
        // 原因在于这个方法会消耗返回结果的数据(buffer),只能使用一次
        val t2 = System.nanoTime()
        ZLog.d("LoggingInterceptor",
                String.format("Received response for %s in %.1fms%n%s",
                response.request().url(),
                (t2 - t1) / 1e6,  // 等同于除1后面6个0
                response.headers()))

        //为了不消耗buffer，这里使用source先获得buffer对象，然后clone()后使用，避免直接消耗
        val responseBody = response.body()
        val source = responseBody!!.source()
        source.request(Long.MAX_VALUE) // Buffer the entire body.
        val buffer = source.buffer
        ZLog.json(buffer.clone().readString(Charset.forName("UTF-8")))

        return response
    }
}