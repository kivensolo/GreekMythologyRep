package com.view.webview;

import android.graphics.Bitmap;
import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.kingz.utils.ZLog;

/**
 * Copyright(C) 2015, 北京视达科科技有限公司
 * All rights reserved.
 * author: King.Z
 * date:  2016/9/28 21:48
 * description:练习抽象类思想  也可以实现接口  自己控制需要实现的方法
 */
public abstract class ZWebViewClient extends WebViewClient {

    public static final String TAG = "ZWebViewClient";

    @Override
    public void onPageStarted(WebView view, String url, Bitmap favicon) {
        Log.d(TAG, "onPageStarted url = " + url);
        super.onPageStarted(view, url, favicon);
    }

    @Override
    public void onPageFinished(WebView view, String url) {
        Log.d(TAG, "onPageFinished url = " + url);
        super.onPageFinished(view, url);
    }

    @Override
    public void onLoadResource(WebView view, String url) {
        Log.d(TAG, "onLoadResource url = " + url);
        super.onLoadResource(view, url);
    }

    @Override
    public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
        Log.d(TAG, "onReceivedError error = " + errorCode + ";description = " + description);
        onWebReceivedError(view, errorCode, description, failingUrl);
        super.onReceivedError(view, errorCode, description, failingUrl);
    }

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        ZLog.i(TAG, "网页重定向：" + url);
        return super.shouldOverrideUrlLoading(view, url);
    }

    public abstract void onWebReceivedError(WebView view, int errorCode, String description, String failingUrl);

}
