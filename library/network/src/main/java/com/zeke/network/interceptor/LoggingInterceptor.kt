package com.zeke.network.interceptor

import com.zeke.kangaroo.zlog.ZLog
import com.zeke.network.BuildConfig
import okhttp3.Interceptor
import okhttp3.MediaType
import okhttp3.Protocol
import okhttp3.Response
import okio.Buffer
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.ByteArrayOutputStream
import java.io.Closeable
import java.io.EOFException
import java.io.InputStream
import java.net.URLDecoder
import java.nio.charset.Charset
import java.util.zip.GZIPInputStream

/**
 * date：2019/9/17
 * description：http logging 拦截器
 */
class LoggingInterceptor : Interceptor {

    private var startTime = 0L

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val requestBuffer = Buffer()
        if (request.body() != null) {
            request.body()!!.writeTo(requestBuffer)
        } else {
            ZLog.w("Request body is empty.")
        }

        val infoBuilder = StringBuilder("\n[REQUEST]:\n")
        dumpRequest(infoBuilder, chain)
        val response = chain.proceed(request)
        infoBuilder.append("[RESPONSE]:\n")
        dumpResponse(infoBuilder, response)
        ZLog.d(infoBuilder.toString())
        return response
    }

    private fun dumpRequest(builder:StringBuilder, chain: Interceptor.Chain) {
        val request = chain.request()
        val url = URLDecoder.decode(request.url().toString(), "UTF-8")
        val connection = chain.connection()
        val protocolVersion = connection?.protocol() ?: Protocol.HTTP_1_1

        // ---------Request-Line Info:
        // eg: "Get wwww.nininin.com http/1.1(xxx-byte body)"
//        var requestStart = "\n${request.method()} $url $protocolVersion"

        builder.append("URL: ").append(url).append("\n")
        builder.append("Method: ").append(request.method()).append("\n")
        builder.append("Version: ").append(protocolVersion).append("\n")
        val requestBody = request.body()
        val hasBody = requestBody != null
        var bodyTitle = ""
        if (hasBody) {
            bodyTitle = "(${requestBody!!.contentLength()}-bytes body)"
        }
        //---------Headers Info:
        builder.append("Headers: ").append(bodyTitle).append("\n")
        val headers = request.headers()
        val count = headers.size()
        for (index in (0 until count)) {
            val name = headers.name(index)
            builder.append("    ").append("${name}: ${headers.value(index)}")
            if (index != count - 1) {
                builder.append("\n")
            }
        }
        builder.append("\n──────────────────────────────────────────────────────────\n")
        startTime = System.nanoTime()
    }

    /**
     * Don't get response data by responseBody.string().
     * Because this method will waste buffer data.
     */
    private fun dumpResponse(builder:StringBuilder, response: Response) {
        val headers = response.headers()
        builder.append("Time: ").append((System.nanoTime() - startTime) / 1e6).append("(ms)\n")  // aka: 1x10^6)
        builder.append("HttpCode: ").append(response.code()).append("\n")
        builder.append("isFromCache: ").append(response.networkResponse() == null).append("\n")
        builder.append("Headers: \n")
        val count = headers.size()
        for (index in (0 until count)) {
            val name = headers.name(index)
            builder.append("    ").append("${name}: ${headers.value(index)}")
            if (index != count - 1) {
                builder.append("\n")
            }
        }
        builder.append("\n")
        val charset = Charset.forName("UTF-8")
        val responseBody = response.body()
        if (responseBody != null) {
            val contentLength = responseBody.contentLength()
            val bodySize = if (contentLength != -1L) "$contentLength-byte" else "unknown-length"
            builder.append("Size: ").append(bodySize).append("\n")
            val contentTYpe = responseBody.contentType()
            if (isText(contentTYpe) && BuildConfig.DEBUG) {
                val source = responseBody.source()
                val buffer = source.buffer
                source.request(Long.MAX_VALUE)
                var responseStr = ""
                if("gzip".equals(response.header("Content-Encoding"), ignoreCase = true)){
                    val gzipInputStream = GZIPInputStream(buffer.clone().inputStream())
                    readDataFromStream(gzipInputStream)?.let {
                        responseStr = String(it)
                    }
                }
                if (contentTYpe.toString().contains("json")) {
                    responseStr = buffer.clone().readString(charset)
                }
//                builder.append("data->").append(formatJson(responseStr)).append("\n")
                builder.append("data->").append(formatJson(responseStr)).append("\n")
            }
        }
        builder.append("──────────────────────────────────────────────────────────\n")
    }

    /***
     * 判断是否是文字
     * @param buffer 缓存
     * @return
     */
    private fun isPlaintext(buffer: Buffer): Boolean {
        return try {
            val prefix = Buffer()
            val byteCount = if (buffer.size < 64) buffer.size else 64
            buffer.copyTo(prefix, 0, byteCount)
            for (i in 0..15) {
                if (prefix.exhausted()) {
                    break
                }
                val codePoint = prefix.readUtf8CodePoint()
                if (Character.isISOControl(codePoint) && !Character.isWhitespace(codePoint)) {
                    return false
                }
            }
            true
        } catch (e: EOFException) {
            false
        }
    }

    /***
     * 判断MediaType是否是文本
     * @param mediaType contentType
     * @return True|False
     */
    private fun isText(mediaType: MediaType?): Boolean {
        if (mediaType == null) {
            return false
        }
        val contentType = mediaType.toString()
        return contentType.startsWith("text/plain")
                || contentType.startsWith( "application/json")
                || contentType.startsWith("text/html")
    }

     /***
     * 从流中读取数据
     * @param inputStream 读取流
     * @return 读取的数据
     */
    private fun readDataFromStream(inputStream: InputStream?): ByteArray? {
        if (inputStream == null) {
            return byteArrayOf()
        }
        try {
            ByteArrayOutputStream().use { bos ->
                var len = 0
                val buffer = ByteArray(1024)
                while (inputStream.read(buffer).also { len = it } > 0) {
                    bos.write(buffer, 0, len)
                }
                return bos.toByteArray()
            }
        } catch (e: Exception) {
            e.printStackTrace()
            return byteArrayOf()
        } finally {
            closeStream(inputStream)
        }
    }

    /**
     * 格式化字符串数据
     * FIXME 文本过长时，格式化不全
     */
    private fun formatJson(jsonStr: String): String {
        try {
            val json = jsonStr.trim { it <= ' ' }
            if (json.startsWith("{")) {
                val jsonObject = JSONObject(json)
                return jsonObject.toString(2)
            }
            if (json.startsWith("[")) {
                val jsonArray = JSONArray(json)
                return jsonArray.toString(2)
            }
            return jsonStr
        } catch (e: JSONException) {
            return jsonStr
        }
    }

     private fun closeStream(stream: Closeable?) {
        if (stream != null) {
            try {
                stream.close()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}