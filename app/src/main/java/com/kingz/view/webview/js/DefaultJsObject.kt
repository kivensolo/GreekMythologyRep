package com.kingz.view.webview.js

import android.util.Log
import com.kingz.view.webview.core.IWebView
import org.json.JSONArray
import org.json.JSONObject
import java.lang.reflect.Method

/**
 * author：ZekeWang
 * date：2024/7/2
 * description：默认JS Bridge对象实现类
 */
class XJsBridgeImpl(private val webView: IWebView): AbsJsBridgeObject() {

    override fun loadJs(js: String) {}

    override fun reload() {
    }

    /**
     * 执行Android端方法的统一执行入口
     */
    override fun execAndroidFunc(name: String, json: String): String? {
        if(_interfaceObjClass == null){
            return null
        }
        try {
            val paraTypes = ArrayList<Class<*>?>()
            val paraValues = ArrayList<Any>()
            Log.d(TAG, "execAndroidFunc:  $name")
            val jsonArgs = JSONArray(json)
            for (i in 0 until jsonArgs.length()) {
                val arg = jsonArgs.getJSONObject(i)
                val type = arg.getString("type")
                val value = arg.getString("value")
                Log.d(TAG, "param+1:  T=$type, V=$value")
                if ("boolean".equals(type, ignoreCase = true)) {
                    paraTypes.add(Boolean::class.javaPrimitiveType)
                    paraValues.add(value.toBoolean())
                } else if ("number".equals(type, ignoreCase = true)) {
                    paraTypes.add(Double::class.javaPrimitiveType)
                    paraValues.add(value.toDouble())
                } else if ("string".equals(type, ignoreCase = true)) {
                    paraTypes.add(String::class.java)
                    paraValues.add(value)
                }
                else if ("callback".equals(type, ignoreCase = true)) {
                    Log.d(TAG, "callback")
                    val JSCallback: IWebView.JSCallback = IWebView.JSCallback(webView)
                    JSCallback.jsMethodName = value
                    paraTypes.add(JSCallback::class.java)
                    paraValues.add(JSCallback)
                }
                else if ("object".equals(type, ignoreCase = true)) {
                    paraTypes.add(JSONObject::class.java)
                    paraValues.add(arg.getJSONObject("value"))
                } else {
                    throw IllegalArgumentException("error type : $type")
                }
            }
            Log.d(TAG, String.format("name=%s, type=%s, val=%s", name, paraTypes, paraValues))
            val pt = paraTypes.toTypedArray<Class<*>?>()
            val method: Method = _interfaceObjClass!!.getMethod(name, *pt)
            return method.invoke(interfaceObj, *paraValues.toTypedArray())?.toString() ?: "null"
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    override fun setCallbackResult(index: Int, value: String?) {
    }

}