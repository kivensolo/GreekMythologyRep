package com.kingz.view.webview.js

import android.util.Log
import android.webkit.JavascriptInterface

/**
 * author：ZekeWang
 * date：2024/7/2
 * description：
 * Android侧提供给Js的原生功能函数执行;
 */
open class CommonAndroidObject : IAndroidObject {
    protected val TAG: String = this.javaClass.simpleName

    @JavascriptInterface
    fun log(tag: String?, info: String?) {
        Log.i(tag, info)
    }

    @JavascriptInterface
    fun getVersion(): String {
        return ""
    }

}