package com.kingz.view.webview.js

/**
 * author：ZekeWang
 * date：2024/7/2
 * description：
 * JsBridge对象，外部可实现自定义的JsBridge对象；
 * 内部持有本地Android对象，用于执行Js想在Android端的具体方法调用；
 */
abstract class AbsJsBridgeObject : IJsBridgeObject {
    protected val TAG: String = this.javaClass.simpleName

    protected var interfaceObj: IAndroidObject? = null
    protected var _interfaceObjClass: Class<*>? = null

    fun bindInterfaceObject(androidObject: CommonAndroidObject?) {
        interfaceObj = androidObject
        _interfaceObjClass = interfaceObj?.javaClass
    }

}