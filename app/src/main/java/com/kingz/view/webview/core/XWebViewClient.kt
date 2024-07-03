package com.kingz.view.webview.core

import android.graphics.Bitmap
import android.net.http.SslError
import android.os.SystemClock
import android.text.TextUtils
import android.util.Log
import android.webkit.SslErrorHandler
import android.webkit.WebView
import android.webkit.WebViewClient
import com.zeke.kangaroo.zlog.ZLog.Companion.i
import java.net.URL
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException

/**
 * author: King.Z
 * date:  2016/9/28 21:48
 * description:练习抽象类思想  也可以实现接口  自己控制需要实现的方法
 */
abstract class XWebViewClient : WebViewClient() {
    private val consumeMap = HashMap<String, Long>()

    override fun onPageStarted(view: WebView, url: String?, favicon: Bitmap?) {
        super.onPageStarted(view, url, favicon)
        if (TextUtils.isEmpty(url)) {
            return
        }
        Log.d(TAG, "onPageStarted url = $url")
        consumeMap[getUrlMD5(url)] = SystemClock.uptimeMillis()
    }

    override fun onPageFinished(view: WebView, url: String?) {
        super.onPageFinished(view, url)
        if (TextUtils.isEmpty(url)) {
            return
        }
        Log.d(TAG, "onPageFinished url = $url")
        val loadConsuming = consumeMap.remove(getUrlMD5(url)) ?: return
        Log.i(TAG, "Load url:(" + (SystemClock.uptimeMillis() - loadConsuming) + ")ms  url=" + url)
    }

    override fun onLoadResource(view: WebView, url: String) {
        Log.d(TAG, "onLoadResource url = $url")
        super.onLoadResource(view, url)
    }

    /**
     * 【注意】：这个方法会有些报错是无用的不影响用户使用，需要进行过滤。
     * 过滤方案：
     *  1. 加载失败的url跟WebView里的url不是同一个url，过滤
     *  2. errorCode=-1，表明是 ERROR_UNKNOWN 的错误，为了保证不误判，过滤
     *  3. failingUrl=null&errorCode=-12，由于错误的url是空而不是 ERROR_BAD_URL，过滤
     */
    override fun onReceivedError(view: WebView, errorCode: Int, description: String, failingUrl: String) {
        Log.e(TAG, "onReceivedError error = $errorCode;description = $description")
        super.onReceivedError(view, errorCode, description, failingUrl)
    }

    override fun onReceivedSslError(view: WebView?, handler: SslErrorHandler?, error: SslError?) {
        Log.e(TAG, "onReceivedSslError error = $error")
        super.onReceivedSslError(view, handler, error)
    }

    override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
        i(TAG, "网页重定向：$url")
        return super.shouldOverrideUrlLoading(view, url)
    }

    companion object {
        const val TAG: String = "ZWebViewClient"
        fun getUrlMD5(urlString: String?): String {
            try {
                // 创建URL对象
                val url = URL(urlString)
                // 获取URL的字符串形式并转换为字节数组
                val urlBytes = url.toString().toByteArray()
                // 获取MD5消息摘要实例
                val md = MessageDigest.getInstance("MD5")
                // 计算MD5摘要
                val digest = md.digest(urlBytes)
                // 将字节转换为16进制字符串
                val hexString = StringBuilder()
                for (b in digest) {
                    hexString.append(String.format("%02x", b))
                }
                return hexString.toString()
            } catch (e: NoSuchAlgorithmException) {
                throw RuntimeException("MD5 algorithm not found", e)
            } catch (e: Exception) {
                throw RuntimeException("Error while converting URL to MD5", e)
            }
        }
    }
}
