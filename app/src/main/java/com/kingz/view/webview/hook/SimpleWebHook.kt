package com.kingz.view.webview.hook

import android.webkit.WebView

/**
 * author：ZekeWang
 * date：2024/7/3
 * description：
 */
open class SimpleWebHook {
    var hasInit: Boolean = false
    var mWebView: WebView? = null
    open fun shouldOverrideUrlLoading(webView: WebView?, url: String?): Boolean {
        return false
    }
}