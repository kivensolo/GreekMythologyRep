package com.zeke.network.interceptor

import okhttp3.Interceptor
import okhttp3.MediaType
import okhttp3.RequestBody
import okhttp3.Response
import okio.BufferedSink
import okio.GzipSink
import okio.buffer
import java.io.IOException

/**
 * author：KingZ
 * date：2019/9/17
 * description：This interceptor compresses the HTTP request body.
 * Many webservers can't handle this!
 */
class GzipRequestInterceptor : Interceptor {
    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        if (originalRequest.body() == null ||
                originalRequest.header("Content-Encoding") != null) {
            return chain.proceed(originalRequest)
        }
        val compressedRequest = originalRequest.newBuilder()
                .header("Content-Encoding", "gzip")
                .method(originalRequest.method(), gzip(originalRequest.body()))
                .build()
        return chain.proceed(compressedRequest)
    }

    private fun gzip(body: RequestBody?): RequestBody {
        return object : RequestBody() {
            override fun contentType(): MediaType? {
                return body!!.contentType()
            }

            override fun contentLength(): Long {
                return -1 // We don't know the compressed length in advance!
            }

            @Throws(IOException::class)
            override fun writeTo(sink: BufferedSink) {
                val gzipSink = GzipSink(sink).buffer()
                body!!.writeTo(gzipSink)
                gzipSink.close()
            }
        }
    }
}