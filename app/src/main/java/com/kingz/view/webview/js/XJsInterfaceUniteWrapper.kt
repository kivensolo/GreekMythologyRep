package com.kingz.view.webview.js

import android.webkit.JavascriptInterface

/**
 * author：ZekeWang
 * date：2024/7/2
 * description：JsBrige的统一封装类
 * 规定前端调用Android的入口只有一个，内部统一调度。
 * 方便实现拦截操作，如域名校验等。
 */
class XJsInterfaceUniteWrapper {

    private var jsObjectImpl:AbsJsBridgeObject? = null
    fun wrap(impl: AbsJsBridgeObject) {
        jsObjectImpl = impl
    }

    @JavascriptInterface
    fun execAndroidFunc(name: String, json: String): String? { // 将Object的返回值改为了String类型.如果JS回调的方法有无法强制转换为String的一定注意
        return jsObjectImpl?.execAndroidFunc(name, json);
    }

    @JavascriptInterface
    fun setCallbackResult(index: Int, value: String?) {
        // value 多数为undefined，如果以后做其他值的扩展，统一认为web处理。apk只处理value = null的情况
        jsObjectImpl?.setCallbackResult(index, value)
    }

    fun isNull(): Boolean {
        return jsObjectImpl == null
    }
}