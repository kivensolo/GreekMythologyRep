package com.kingz.ble.model

/**
 * author：ZekeWang
 * date：2021/8/10
 * description：蓝牙数据接口,
 * 要进行蓝牙传输的数据，需要实现此接口
 */
interface IBleData{

    /**
     * 序列化: 实体类->byte[]
     */
    fun getBleBytes(): ByteArray

    /**
     * 反序列化: byte[]->实体类
     */
    fun parseBleObject(bytes:ByteArray) :Any?

    /**
     * 检查byte[]是否合法, 长度, 请求头/返回头
     */
    fun checkValid(): Boolean = true

}