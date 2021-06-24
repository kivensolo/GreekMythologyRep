package com.zeke.demo.device.bluetooth

import android.bluetooth.BluetoothDevice
import kotlin.math.abs
import kotlin.math.pow


/**
 * author：ZekeWang
 * date：2021/6/24
 * description：蓝牙设备包装类，加了一个信号强度，以及距离，距离计算因子
 */
class BluetoothDeviceWrapper(
    rssi: Int,  //信号强度
    var mDevice: BluetoothDevice? = null
) : Comparable<BluetoothDeviceWrapper> {

    /**
     * 距离
     */
    var mDistance = 0.0

    /**
     * 发射端和接收端相隔1米时的信号强度 A
     * 需要根据实际环境进行检测得出
     */
    var A_Value = 60.0
    /**
     * 环境衰减因子n, 需要根据实际环境进行检测得出
     */
    var n_Value = 2.5

    init {
        mDistance = getDistance(rssi)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        val wrapper = other as BluetoothDeviceWrapper
        return mDevice?.equals(wrapper.mDevice)?:false
    }

    override fun hashCode(): Int {
        return mDevice.hashCode()
    }

    /**
     * 根据Rssi获得返回的距离,返回数据单位为m
     * 计算公式：
     *   d = 10^((abs(RSSI) - A) / (10 * n))
     * @param rssi 接收信号强度（负值）
     */
    private fun getDistance(rssi: Int): Double {
        val iRssi = abs(rssi)
        val power = (iRssi - A_Value) / (10 * n_Value)
        return 10.0.pow(power)
    }

    override fun compareTo(other: BluetoothDeviceWrapper): Int {
        return when {
            mDistance < other.mDistance -> {
                -1
            }
            mDistance > other.mDistance -> {
                1
            }
            else -> {
                0
            }
        }
    }
}