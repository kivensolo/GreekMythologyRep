package com.kingz.ble.exception

import com.kingz.ble.response.IResponseData


abstract class BleException(
    open var code:Int = 0,
    open var reason:String)
    : Exception(), IResponseData {

    override fun getData(): ByteArray {
        return byteArrayOf(-1)
    }
}