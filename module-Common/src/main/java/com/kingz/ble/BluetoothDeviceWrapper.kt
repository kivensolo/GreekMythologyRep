package com.kingz.ble

import android.bluetooth.BluetoothDevice
import android.text.TextUtils
import kotlin.math.abs
import kotlin.math.pow


/**
 * author：ZekeWang
 * date：2021/6/24
 * description：蓝牙设备包装类，加了一个信号强度，以及距离，距离计算因子
 */
class BluetoothDeviceWrapper(
    var rssi: Int = -1,  //信号强度
    var device: BluetoothDevice?=null,
    var showName:String ?= "",
    var extInfo:String ?= "",
    var syncTime:String ?= ""
) : Comparable<BluetoothDeviceWrapper> {

    /**
     * 距离
     */
    var distance = 0.0

    /**
     * 发射端和接收端相隔1米时的信号强度 A
     * 需要根据实际环境进行检测得出
     */
    var aValue = 60.0
    /**
     * 环境衰减因子n, 需要根据实际环境进行检测得出
     */
    var nValue = 2.5

    init {
        distance = getDistance(rssi)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        val wrapper = other as BluetoothDeviceWrapper
        return TextUtils.equals(device?.address, wrapper.device?.address)
    }

    override fun hashCode(): Int {
        return device.hashCode()
    }

    /**
     * 根据Rssi获得返回的距离,返回数据单位为m
     * 计算公式：
     *   d = 10^((abs(RSSI) - A) / (10 * n))
     * @param rssi 接收信号强度（负值）
     */
    private fun getDistance(rssi: Int): Double {
        val iRssi = abs(rssi)
        val power = (iRssi - aValue) / (10 * nValue)
        return 10.0.pow(power)
    }

    override fun compareTo(other: BluetoothDeviceWrapper): Int {
        return when {
            distance < other.distance -> -1
            distance > other.distance ->  1
            else -> 0
        }
    }
}