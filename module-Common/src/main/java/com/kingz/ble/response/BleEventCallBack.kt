package com.kingz.ble.response

import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanResult
import androidx.annotation.CallSuper
import com.kingz.ble.BluetoothDeviceWrapper
import com.zeke.kangaroo.zlog.ZLog

/**
 * author：ZekeWang
 * date：2021/8/3
 * description：ble蓝牙事件回调
 */
open class BleEventCallBack : ScanCallback() {
    open fun onScanStop() {}

    /**
     *  Callback when a BLE advertisement has been found.
     */
    override fun onScanResult(callbackType: Int, result: ScanResult) {
        //TODO 检测到已绑定设备缓存

//            val deviceWrapper = BluetoothDeviceWrapper(
//                result.rssi,
//                result.device
//            )
//            if(!foundDeviceList.contains(deviceWrapper)){
//                ZLog.d("found new device : $deviceWrapper")
//                foundDeviceList.add(deviceWrapper)
//            }
    }

    override fun onScanFailed(errorCode: Int) {
        super.onScanFailed(errorCode)
    }

    override fun onBatchScanResults(results: MutableList<ScanResult>?) {
        super.onBatchScanResults(results)
    }

    /**
     * 蓝牙已连接事件，但GATT未准备就绪，不可在此事件接受后立刻进行通信。
     * 此事件可用于UI更新.
     * Callback in UI thread
     */
    @CallSuper
    open fun onDeviceConnected(target: BluetoothDeviceWrapper) {
        ZLog.d("BleEventCallBack", "BLE device is connected !")
    }

    /**
     * Callback in UI thread
     */
    open fun onDeviceDisconnected(){
        ZLog.d("BleEventCallBack", "BLE device is disconnected !")
    }

    /**
     * 蓝牙连接后，GATT初始化准备就绪，表明可以进行蓝牙数据通信
     * Callback in UI thread
     */
    open fun onGattAllReady(){
        ZLog.d("BleEventCallBack", "BLE gatt all ready!")
    }
}