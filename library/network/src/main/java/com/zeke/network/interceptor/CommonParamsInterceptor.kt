
package com.zeke.network.interceptor

import android.text.TextUtils
import okhttp3.*
import okio.Buffer
import java.io.IOException


/**
 * author：ZekeWang
 * date：2021/6/5
 * description：
 * 公共接口参数网络配置拦截器
 * 支持Params和Header全局添加
 * 使用Builder模式(Kotlin)
 */
@Suppress("unused")
class CommonParamsInterceptor private constructor() : Interceptor {
    // 添加到 URL 末尾，Get Post 方法都使用
    var queryParamsMap: HashMap<String, String> = HashMap()
    // 添加到公共参数到消息体，适用 Post 请求
    var postParamsMap: HashMap<String, String> = HashMap()
    // 公共 Headers 添加
    var headerParamsMap: HashMap<String, String> = HashMap()
    // 消息头 集合形式，一次添加一行
    var headerLinesList: MutableList<String> = ArrayList()

    override fun intercept(chain: Interceptor.Chain): Response {
        var request: Request = chain.request()
        val requestBuilder = request.newBuilder()
        val headerBuilder: Headers.Builder = request.headers().newBuilder()

        injectHeaderParams(headerBuilder, requestBuilder)
        injectQueryParmas(request, requestBuilder)
        injectPostBodyParmas(request, requestBuilder)

        request = requestBuilder.build()
        return chain.proceed(request)
    }

    /**
     * Post模式下添加Body参数
     */
    private fun injectPostBodyParmas(request: Request, requestBuilder: Request.Builder) {
        if (postParamsMap.isNotEmpty()) {
            if (canInjectIntoBody(request)) {
                val formBodyBuilder = FormBody.Builder()
                for ((key, value) in postParamsMap) {
                    formBodyBuilder.add(key, value)
                }
                val formBody: RequestBody = formBodyBuilder.build()
                var postBodyString: String = bodyToString(request.body())
                val prefex = if (postBodyString.isNotEmpty()) "&" else ""
                postBodyString += prefex + bodyToString(formBody)
                requestBuilder.post(
                    RequestBody.create(
                        MediaType.parse("application/x-www-form-urlencoded;charset=UTF-8"),
                        postBodyString
                    )
                )
            }
        }
    }

    /**
     * 添加消息头
     */
    private fun injectHeaderParams(
        headerBuilder: Headers.Builder,
        requestBuilder: Request.Builder
    ) {
        // 以 Entry 添加消息头
        if (headerParamsMap.isNotEmpty()) {
            val iterator: Iterator<*> = headerParamsMap.entries.iterator()
            while (iterator.hasNext()) {
                val entry = iterator.next() as Map.Entry<*, *>
                headerBuilder.add(entry.key as String, entry.value as String)
            }
            requestBuilder.headers(headerBuilder.build())
        }

        // 以 String 形式添加消息头
        if (headerLinesList.isNotEmpty()) {
            for (line in headerLinesList) {
                headerBuilder.add(line)
            }
            requestBuilder.headers(headerBuilder.build())
        }
    }

    //添加查询参数 Get|Post
    private fun injectQueryParmas(request: Request?, requestBuilder: Request.Builder) {
        if (queryParamsMap.isNotEmpty()) {
            injectParamsIntoUrl(
                request!!.url().newBuilder(),
                requestBuilder,
                queryParamsMap
            )
        }
    }

    /**
     * 注入公共参数到Url中，储存在requestBuilder中
     */
    private fun injectParamsIntoUrl(
        httpUrlBuilder: HttpUrl.Builder,
        requestBuilder: Request.Builder,
        paramsMap: Map<String, String>
    ){
        if (paramsMap.isNotEmpty()) {
            val iterator: Iterator<*> = paramsMap.entries.iterator()
            while (iterator.hasNext()) {
                val entry = iterator.next() as Map.Entry<*, *>
                httpUrlBuilder.addQueryParameter(
                    entry.key as String,
                    entry.value as String
                )
            }
            requestBuilder.url(httpUrlBuilder.build())
        }
    }

    /**
     * 确认是否是 post 请求
     * @param request 发出的请求
     * @return true 需要注入公共参数
     */
    private fun canInjectIntoBody(request: Request?): Boolean {
        if (request == null) {
            return false
        }
        if (!TextUtils.equals(request.method(), "POST")) {
            return false
        }
        val body = request.body() ?: return false
        val mediaType = body.contentType() ?: return false
        return TextUtils.equals(mediaType.subtype(), "x-www-form-urlencoded")
    }

    private fun bodyToString(request: RequestBody?): String {
        return try {
            val buffer = Buffer()
            request?.writeTo(buffer)
            buffer.readUtf8()
        } catch (e: IOException) {
            "did not work"
        }
    }
// <editor-fold defaultstate="collapsed" desc="对外提供的Builder构造器">
    class Builder {
        private var interceptor: CommonParamsInterceptor =
            CommonParamsInterceptor()

        // <editor-fold defaultstate="collapsed" desc="添加公共参数到 post 消息体">
        fun addPostParam(key: String, value: String): Builder {
            interceptor.postParamsMap[key] = value
            return this
        }

        fun addPostParamsMap(paramsMap: Map<String, String>): Builder {
            interceptor.postParamsMap.putAll(paramsMap)
            return this
        }
        // </editor-fold>

       // <editor-fold defaultstate="collapsed" desc="添加公共参数到消息头">
        fun addHeaderParam(key: String?,value: String?): Builder {
            interceptor.headerParamsMap[key!!] = value!!
            return this
        }

        fun addHeaderParamsMap(headerParamsMap: Map<String, String>) : Builder {
            interceptor.headerParamsMap.putAll(headerParamsMap)
            return this
        }

        fun addHeaderLine(headerLine: String): Builder {
            val index = headerLine.indexOf(":")
            require(index != -1) { "Unexpected header: $headerLine" }
            interceptor.headerLinesList.add(headerLine)
            return this
        }

        fun addHeaderLinesList(headerLinesList: List<String>): Builder {
            for (headerLine in headerLinesList) {
                val index = headerLine.indexOf(":")
                require(index != -1) { "Unexpected header: $headerLine" }
                interceptor.headerLinesList.add(headerLine)
            }
            return this
        }
       // </editor-fold>

      // <editor-fold defaultstate="collapsed" desc="添加公共参数到URL">
        fun addQueryParam(key: String, value: String): Builder {
            interceptor.queryParamsMap[key] = value
            return this
        }

        fun addQueryParamsMap(
            queryParamsMap: Map<String, String>): Builder {
            interceptor.queryParamsMap.putAll(queryParamsMap)
            return this
        }
      // </editor-fold>

        fun build(): CommonParamsInterceptor {
            return interceptor
        }
    }
// </editor-fold>
}