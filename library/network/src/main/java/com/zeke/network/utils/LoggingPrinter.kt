package com.zeke.network.utils

import com.zeke.kangaroo.utils.ZLog
import okhttp3.Interceptor
import okhttp3.Protocol
import okhttp3.Response
import java.nio.charset.Charset

/**
 * @author zeke.wang
 * @date 2020/7/25
 * @maintainer zeke.wang
 * @desc: 网络日志打印辅助类
 */
object LoggingPrinter {
    private const val TAG = "LoggingInterceptor"
    private const val CONTENT_TYPE = "Content-Type: "
    private const val CONTENT_LENGTH = "Content-Length: "
    private var startTime = 0L

    fun printRequest(chain: Interceptor.Chain){
        startTime = System.nanoTime()
        val request = chain.request()
        val url = request.url()
        val connection = chain.connection()
        val protocolVersion = connection?.protocol() ?: Protocol.HTTP_1_1

        // ---------Request-Line Info:
        //  "Get wwww.nininin.com HTTP/1.1(xxx-byte body)"
        var requestStart = "${request.method()}   $url   $protocolVersion"
        val requestBody = request.body()
        val hasRequestBody = requestBody != null
        if(hasRequestBody){
            requestStart += "(${requestBody!!.contentLength()}-byte body)"
        }
        requestStart += "\n"

        //---------Headers Info:
        val headerBuffer = StringBuffer()
        if (hasRequestBody) {
            if (requestBody!!.contentType() != null) {
                headerBuffer.append(CONTENT_TYPE + requestBody.contentType()).append("\n")
            }
            if (requestBody.contentLength() != -1L) {
                headerBuffer.append(CONTENT_LENGTH + requestBody.contentLength()).append("\n")
            }
        }

        val headers = request.headers()
        val count=headers.size()
        for (index in (0 until count)){
            val name = headers.name(index)
            // Skip headers from the request body as they are explicitly logged above.
            if(!CONTENT_TYPE.equals(name, ignoreCase = true) &&
                !CONTENT_LENGTH.equals(name,ignoreCase = true)){
                headerBuffer.append("${name}: ${headers.value(index)}").append("\n")
            }
        }

        val requestMessage = requestStart + headerBuffer.toString()
        ZLog.d(TAG,requestMessage)
    }

    /**
     * Don't get response data by responseBody.string().
     * Because this method will waste buffer data.
     */
    fun printResponse(response: Response) {
        val endTime = System.nanoTime()
        val headers = response.headers()
        ZLog.d(TAG,
            String.format(
                "<--- %.1fms%n%s",
                (endTime - startTime) / 1e6,  // aka: 1x10^6
                response.headers()))

        //Get BufferedSource object and clone it.
        val responseBody = response.body()
        val source = responseBody!!.source()
        // Buffer the entire body.
        source.request(Long.MAX_VALUE)
        val buffer = source.buffer
        val bodyString = buffer.clone().readString(Charset.forName("UTF-8"))
        if(headers["Content-Type"]?.contains("json") == true){
            ZLog.json(bodyString)
        }else{
            ZLog.d(bodyString)
        }
    }
}