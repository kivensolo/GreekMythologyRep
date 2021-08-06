package com.zeke.demo.device.bluetooth

import java.util.*

/**
 * author：ZekeWang
 * date：2021/8/4
 * description：
 */
object GattUtils {
    const val leastSigBits:Long = 0x800000800000000L
//    val leastSigBits:Long =0x800000805f9b34fbL

    /**
     *  每一个service 和 characteristic在蓝牙的官网里面都有一个assgined Number
     *  官网里的是行业标准，厂商提供的大部分assignednumber 会和官网相同，
     *  只有少数厂商自定义的Service 和Characteristic 会不一样，这时候就要找厂商索要.
     */
    fun toUuid(assignedNumber: Long): UUID? {
        return UUID(assignedNumber shl 32 or 0x1000, leastSigBits)
    }
}