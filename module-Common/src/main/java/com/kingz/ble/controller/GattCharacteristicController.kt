@file:Suppress("PrivatePropertyName")

package com.kingz.ble.controller

import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattCharacteristic
import android.bluetooth.BluetoothGattDescriptor
import com.kingz.ble.model.IBleData
import com.kingz.ble.response.BleEventCallBack
import com.zeke.kangaroo.zlog.ZLog
import kotlinx.coroutines.delay
import java.util.*

/**
 * author：ZekeWang
 * date：2021/9/3
 * description：蓝牙Gatt特征控制模块
 * 【Service】:
 *      一个低功耗蓝牙设备可以定义许多 Service, Service 可以理解为一个功能的集合。
 *  设备中每一个不同的 Service 都有一个 128 bit 的 UUID 作为这个 Service 的独立标志。
 *  蓝牙核心规范制定了两种不同的UUID:
 *     一种是基本的UUID，[0000xxxx-0000-1000-8000-00805F9B34FB]
 *     一种是代替基本UUID的16位UUID。
 *     为了进一步简化基本UUID，每一个蓝牙技术联盟定义的属性有一个唯一的16位UUID（也有的叫assigned number），
 * 例如，心率测量特性使用0X2A37作为它的16位UUID，因此它完整的128位UUID为：
 *     0x00002A37-0000-1000-8000-00805F9B34FB
 * 【Characteristic】
 *      在Service中，又包括了许多的独立数据项，这些独立的数据项称作 Characteristic。
 *      每一个 Characteristic 也有一个唯一的 UUID 作为标识符。
 *      在 Android 开发中，建立蓝牙连接后，通过蓝牙发送数据给外围设备
 *      就是往这些 Characteristic 中的 Value 字段写入数据；
 *      外围设备发送数据给手机就是监听这些 Charateristic 中的 Value 字段有没有变化，
 *      如果发生了变化，手机的 BLE API 就会收到一个监听的回调。
 */
class GattCharacteristicController {
    companion object{
        private const val TAG = "ChannelController"
    }

    // <editor-fold defaultstate="collapsed" desc="Server --- 标准蓝牙">
    //蓝牙标准NOTIFY
    private val UUID_OF_NOTIFY: UUID =
        UUID.fromString("00000014-0000-1000-8000-00805F9B34FB")
    //Bluetooth Generic Access Profile
    private val UUID_OF_AccessProfile: UUID =
        UUID.fromString("00001800-0000-1000-8000-00805F9B34FB")

    //Bluetooth Generic Attribute Profile
    private val UUID_OF_AttributeProfile: UUID =
        UUID.fromString("00001801-0000-1000-8000-00805F9B34FB")

    //indicate通道(BLE-->App) uuid
    private val UUID_OF_INDICATE: UUID =
        UUID.fromString("00000015-0000-1000-8000-00805F9B34FB")
    private val UUID_OF_NOTIFICATION: UUID =
        UUID.fromString("00002902-0000-1000-8000-00805f9b34fb")
    // </editor-fold>

    private var eventCallback: BleEventCallBack?= null

    /**
     * Init all gatt characteristics.
     * @return Init finish flag.
     */
    suspend fun initCharacteristics(
        gatt: BluetoothGatt?, eventCallBack: BleEventCallBack?
    ): Boolean {
        ZLog
            .d(TAG, "initCharacteristics by gatt[$gatt]")
        eventCallback = eventCallBack
        gatt?.apply {
            val service = getService(UUID_OF_NOTIFY)
            //获取configCharacter： service?.getCharacteristic(UUID_OF_CONFIG_CHARACTERISTIC)
            //默认直接打开通知
            enableNotification(this,true)
            return enableNotification(gatt, true)
        }
        return false
    }

    /**
     * Open or Close notification channle of all characteristics.
     * 【NOTICE】: Befor read data, must enable notification
     * @param gatt    BluetoothGatt
     * @param enable  notify enable
     */
    @Suppress("MemberVisibilityCanBePrivate")
    suspend fun enableNotification(gatt: BluetoothGatt?, enable: Boolean): Boolean {
        if (gatt == null) {
            ZLog.e(TAG, "bluetoothGatt is null.")
            return false
        }

        // 异步进行Notification设置，每个特征处理要间隔一定时间(500ms)
        for (characteristic in getAllCharacter()) {
            if (!gatt.setCharacteristicNotification(characteristic, enable)) {
                ZLog.e(TAG, "Notification status was set failed.")
                continue
            }
            characteristic?.descriptors?.apply {
                forEach { descriptor ->
                    if (enable) {
                        descriptor.value = BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE
                    } else {
                        descriptor.value = BluetoothGattDescriptor.DISABLE_NOTIFICATION_VALUE
                    }
                    gatt.writeDescriptor(descriptor)
                    ZLog.w(TAG, "Notification writeDescriptor  +1.")
                    delay(500)
                }
            }
        }
        return true
    }

    /**
     * 获取业务上使用的所有蓝牙Character
     */
    private fun getAllCharacter():MutableList<BluetoothGattCharacteristic?>{
        return mutableListOf()
    }


    // <editor-fold defaultstate="collapsed" desc="数据写入方法">

    /**
     * Write data through the Gatt object
     * @param gatt BluetoothGatt
     * @param senData Data of implement the IBleData interface.
     */
    fun writeData(gatt: BluetoothGatt?, senData: IBleData) {
//        ZLog.d(TAG, "Send data with [${senData.getType()}]......")
        val bleBytes = senData.getBleBytes()
        //FIXME 写入数据传入指定的character
        sendDataWithCharacter(gatt,null,bleBytes)
    }

    private fun sendDataWithCharacter(
        gatt: BluetoothGatt?,
        character: BluetoothGattCharacteristic?,
        data: ByteArray
    ) {
        character?.apply {
            value = data
            gatt?.writeCharacteristic(this)
        }

    }
    // </editor-fold>
}