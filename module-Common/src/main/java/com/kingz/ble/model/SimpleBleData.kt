package com.kingz.ble.model


/**
 * author：ZekeWang
 * date：2021/8/10
 * description：蓝牙简单数据包装类
 */
class SimpleBleData(
    private var bytes:ByteArray
) : IBleData{
    var realTaskName:String = "Default"

    override fun getBleBytes(): ByteArray {
       return bytes
    }

    override fun parseBleObject(bytes: ByteArray): Any? {
        return null
    }
}