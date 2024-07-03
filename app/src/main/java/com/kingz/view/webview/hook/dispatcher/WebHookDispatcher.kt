package com.kingz.view.webview.hook.dispatcher

import android.graphics.Bitmap
import android.net.Uri
import android.webkit.ValueCallback
import android.webkit.WebChromeClient.FileChooserParams
import android.webkit.WebView
import com.kingz.view.webview.hook.SimpleWebHook
import com.kingz.view.webview.hook.WebHook
import java.util.concurrent.CopyOnWriteArrayList


/**
 * author：ZekeWang
 * date：2024/7/3
 * description：
 * Web拦截的分发器, 用于将事件分发给具体的拦截器。
 * 将WebViewClient和WebChromeClient所有方法都封装起来，分发出去，每一个拦截器都负责自己的功能即可。
 *
 * https://juejin.cn/post/7316202809383321609
 */
class WebHookDispatcher : SimpleWebHook(){
    /**
     * 因为shouldInterceptRequest是一个异步的回调，所以这个类需要加锁
     */
    private val webHooks: CopyOnWriteArrayList<WebHook> = CopyOnWriteArrayList()

    fun addWebHook(webHook: WebHook) {
        webHooks.add(webHook)
        if (hasInit) {
            webHook.onWebInit(mWebView)
        }
    }

    fun addWebHooks(webHooks: Collection<WebHook>) {
        this.webHooks.addAll(webHooks)
        if (hasInit) {
            for (webHook in webHooks) {
                webHook.onWebInit(mWebView)
            }
        }
    }

    fun addWebHook(position: Int, webHook: WebHook) {
        webHooks.add(position, webHook)
        if (hasInit) {
            webHook.onWebInit(mWebView)
        }
    }

    fun addWebHooks(position: Int, webHooks: Collection<WebHook>) {
        this.webHooks.addAll(position, webHooks)
        if (hasInit) {
            for (webHook in webHooks) {
                webHook.onWebInit(mWebView)
            }
        }
    }

    fun findWebHookByClass(clazz: Class<out WebHook?>): WebHook? {
        for (webHook in webHooks) {
            if (webHook.javaClass == clazz) {
                return webHook
            }
        }
        return null
    }

    fun removeWebHook(webHook: WebHook?) {
        webHooks.remove(webHook)
    }

    fun getWebHooks(): List<WebHook> {
        return webHooks
    }

    fun clear() {
        webHooks.clear()
    }


    //dispatch method ----------------
    override fun shouldOverrideUrlLoading(webView: WebView?, url: String?): Boolean {
        for (webHook in webHooks) {
            if (webHook.shouldOverrideUrlLoading(webView, url)) {
                return true
            }
        }
        return super.shouldOverrideUrlLoading(webView, url)
    }


    fun onPageFinished(webView: WebView?, url: String?) {
        for (webHook in webHooks) {
            webHook.onPageFinished(webView!!, url)
        }
    }

    fun onReceivedTitle(webView: WebView?, title: String?) {
        for (webHook in webHooks) {
            webHook.onReceivedTitle(webView, title)
        }
    }

    fun onProgressChanged(webView: WebView?, newProgress: Int) {
        for (webHook in webHooks) {
            webHook.onProgressChanged(webView!!, newProgress)
        }
    }

    fun onPageStarted(webView: WebView?, url: String?, favicon: Bitmap?) {
        for (webHook in webHooks) {
            webHook.onPageStarted(webView!!, url, favicon)
        }
    }


    fun onShowFileChooser(
        webView: WebView?, filePathCallback: ValueCallback<Array<Uri?>?>?,
        fileChooserParams: FileChooserParams?
    ): Boolean {
        for (webHook in webHooks) {
            if (webHook.onShowFileChooser(webView, filePathCallback, fileChooserParams)) {
                return true
            }
        }
        return false
    }
//
//    fun onActivityResult(requestCode: Int, resultCode: Int, intent: Intent?): Boolean {
//        for (webHook in webHooks) {
//            if (webHook.onActivityResult(requestCode, resultCode, intent)) {
//                return true
//            }
//        }
//        return false
//    }
//
//
//    fun onReceivedError(webView: WebView?, request: WebResourceRequest?, error: WebResourceError?) {
//        for (webHook in webHooks) {
//            webHook.onReceivedError(webView!!, request, error)
//        }
//    }
//
//    fun shouldInterceptRequest(view: WebView?, request: WebResourceRequest?): WebResourceResponse {
//        for (webHook in webHooks) {
//            val response: WebResourceResponse = webHook.shouldInterceptRequest(view, request)
//            if (response != null) {
//                return response
//            }
//        }
//        return super.shouldInterceptRequest(view, request)
//    }
//
//    fun onBackPressed(): Boolean {
//        for (webHook in webHooks) {
//            if (webHook.onBackPressed()) {
//                return true
//            }
//        }
//        return super.onBackPressed()
//    }
//
//    fun onKeyUp(keyCode: Int, event: KeyEvent?): Boolean {
//        for (webHook in webHooks) {
//            if (webHook.onKeyUp(keyCode, event)) {
//                return true
//            }
//        }
//        return super.onKeyUp(keyCode, event)
//    }
//
//    fun onConsoleMessage(consoleMessage: ConsoleMessage?) {
//        for (webHook in webHooks) {
//            webHook.onConsoleMessage(consoleMessage)
//        }
//    }
//
//    fun onReceivedSslError(view: WebView?, handler: SslErrorHandler?, error: SslError?) {
//        for (webHook in webHooks) {
//            webHook.onReceivedSslError(view, handler, error)
//        }
//        super.onReceivedSslError(view, handler, error)
//    }
//
//    fun onReceivedError(webView: WebView?, url: String?, errorCode: Int, description: String?) {
//        for (webHook in webHooks) {
//            webHook.onReceivedError(webView!!, url, errorCode, description!!)
//        }
//        super.onReceivedError(webView, url, errorCode, description)
//    }
//
//
//    fun onPermissionRequest(request: PermissionRequest?) {
//        super.onPermissionRequest(request)
//        for (webHook in webHooks) {
//            webHook.onPermissionRequest(request)
//        }
//    }
}