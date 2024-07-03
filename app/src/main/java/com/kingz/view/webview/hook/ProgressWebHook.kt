package com.kingz.view.webview.hook

import android.graphics.Bitmap
import android.webkit.WebView
import com.kingz.view.webview.hook.progress.IWebViewLoading

/**
 * author：ZekeWang
 * date：2024/7/3
 * description：实现一个loading的逻辑
 */
class ProgressWebHook(var mWebViewLoading: IWebViewLoading): WebHook() {

    override fun onPageStarted(view: WebView, url: String?, favicon: Bitmap?) {
        super.onPageStarted(view, url, favicon)
        startLoading()
    }
    override fun onPageFinished(view: WebView, url: String?) {
        super.onPageFinished(view, url)
                stopLoading();
    }

    override fun onReceivedError(view: WebView, errorCode: Int, description: String, failingUrl: String) {
        super.onReceivedError(view, errorCode, description, failingUrl)
        stopLoading();
}
    override fun onProgressChanged(view: WebView, newProgress: Int) {
        super.onProgressChanged(view, newProgress)
//        mWebViewLoading.onProgress(getContext(), newProgress);
    }
}