package com.kingz.view.webview.js

/**
 * author：ZekeWang
 * date：2024/7/2
 */
interface IJsBridgeObject {
    fun loadJs(js: String)
    fun reload()

    /**
     * Js invoke Android
     */
    fun execAndroidFunc( name:String, json: String ):String?

    /**
     * Android invoke Js
     */
    fun setCallbackResult(index:Int ,  value:String?)
}