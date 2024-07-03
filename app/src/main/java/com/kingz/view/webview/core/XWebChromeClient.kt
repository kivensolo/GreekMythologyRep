package com.kingz.view.webview.core

import android.net.Uri
import android.view.View
import android.webkit.ConsoleMessage
import android.webkit.JsPromptResult
import android.webkit.JsResult
import android.webkit.PermissionRequest
import android.webkit.ValueCallback
import android.webkit.WebChromeClient
import android.webkit.WebView
import com.kingz.view.webview.hook.dispatcher.WebHookDispatcher


/**
 * Copyright (c) 2024, 北京视达科科技有限责任公司 All rights reserved.
 * author：ZekeWang
 * date：2024/7/3
 * description：
 */
class XWebChromeClient(webHookDispatcher: WebHookDispatcher) : WebChromeClient() {
    private val mWebHookDispatcher = webHookDispatcher

    override fun onPermissionRequest(request: PermissionRequest?) {
        mWebHookDispatcher.onPermissionRequest(request)
    }

    override fun onReceivedTitle(view: WebView?, title: String?) {
        super.onReceivedTitle(view, title)
        mWebHookDispatcher.onReceivedTitle(view, title)
    }

    override fun onProgressChanged(view: WebView?, newProgress: Int) {
        super.onProgressChanged(view, newProgress)
        mWebHookDispatcher.onProgressChanged(view, newProgress)
    }

    // For Android >= 5.0
    override fun onShowFileChooser(
        webView: WebView?, filePathCallback: ValueCallback<Array<Uri?>?>?,
        fileChooserParams: FileChooserParams?
    ): Boolean {
        return mWebHookDispatcher.onShowFileChooser(webView, filePathCallback, fileChooserParams)
    }

    override fun onConsoleMessage(consoleMessage: ConsoleMessage?): Boolean {
        mWebHookDispatcher.onConsoleMessage(consoleMessage)
        return super.onConsoleMessage(consoleMessage)
    }

    override fun onJsAlert(view: WebView?, url: String?, message: String?, result: JsResult?): Boolean {
        if (mWebHookDispatcher.onJsAlert(view, url, message, result)) {
            return true
        }
        return super.onJsAlert(view, url, message, result)
    }

    override fun onJsPrompt(
        view: WebView?,
        url: String?,
        message: String?,
        defaultValue: String?,
        result: JsPromptResult?
    ): Boolean {
        if (mWebHookDispatcher.onJsPrompt(view, url, message, defaultValue, result)) {
            return true
        }
        return super.onJsPrompt(view, url, message, defaultValue, result)
    }

    override fun onJsBeforeUnload(view: WebView?, url: String?, message: String?, result: JsResult?): Boolean {
        if (mWebHookDispatcher.onJsBeforeUnload(view, url, message, result)) {
            return true
        }
        return super.onJsBeforeUnload(view, url, message, result)
    }

    override fun onShowCustomView(view: View?, callback: CustomViewCallback?) {
        super.onShowCustomView(view, callback)
        mWebHookDispatcher.onShowCustomView(view, callback)
    }

    override fun onHideCustomView() {
        super.onHideCustomView()
        mWebHookDispatcher.onHideCustomView()
    }
}