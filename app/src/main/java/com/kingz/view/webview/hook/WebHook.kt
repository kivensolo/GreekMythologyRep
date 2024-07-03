package com.kingz.view.webview.hook

import android.graphics.Bitmap
import android.net.Uri
import android.net.http.SslError
import android.webkit.SslErrorHandler
import android.webkit.ValueCallback
import android.webkit.WebChromeClient
import android.webkit.WebView

/**
 *
 * 学习掘金一篇文章对webview进行封装的，但是文档只讲了思想，没详细的代码
 * https://juejin.cn/post/7316202809383321609
 *
 *
 * author：ZekeWang
 * date：2024/7/3
 * description：网页拦截器基类，所有拦截器必须继承此类
 *
 * 思路：
 * 由于WebView不可以设置多个Client，那么就使用拦截器，将WebViewClient和WebChromeClient所有方法都封装起来
 * 每一个拦截器都负责自己的功能即可
 */
open class WebHook {
    open fun onWebInit(webView: WebView?) {

    }

    open fun onPageStarted(view: WebView, url: String?, favicon: Bitmap?) {

    }

    open fun onPageFinished(view: WebView, url: String?) {

    }

    open fun onReceivedError(view: WebView, errorCode: Int, description: String, failingUrl: String) {
    }

    open fun onReceivedSslError(view: WebView?, handler: SslErrorHandler?, error: SslError?) {
    }

    open fun onReceivedTitle(view: WebView?, title: String?) {}
    open fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
        return false
    }

    open fun onProgressChanged(view: WebView, newProgress: Int) {}

    open fun onShowFileChooser(
        webView: WebView?, filePathCallback: ValueCallback<Array<Uri?>?>?,
        fileChooserParams: WebChromeClient.FileChooserParams?
    ): Boolean {
        return false
    }

    fun startLoading() {

    }

    fun stopLoading() {

    }
}