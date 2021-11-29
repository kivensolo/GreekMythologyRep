package com.kingz.ble.receiver

import android.bluetooth.BluetoothAdapter
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.zeke.kangaroo.zlog.ZLog

/**
 * author：ZekeWang
 * date：2021/9/25
 * description：手机蓝牙状态接受器
 *
 * 8.0后需要动态注册：
 * val intentFilter = IntentFilter()
 * //intentFilter.addAction("android.bluetooth.a2dp.profile.action.CONNECTION_STATE_CHANGED");
 * //intentFilter.addAction(BluetoothDevice.ACTION_ACL_CONNECTED);
 * //intentFilter.addAction(BluetoothDevice.ACTION_ACL_DISCONNECTED);
 * intentFilter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED)
 * intentFilter.addAction("android.bluetooth.BluetoothAdapter.STATE_OFF")
 * intentFilter.addAction("android.bluetooth.BluetoothAdapter.STATE_ON")
 * baseContext.registerReceiver(BLEStatusReceiver(), intentFilter)
 */
class BLEStatusReceiver: BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        ZLog.d("BLEStatusReceiver onReceive, action:${intent.action}")
        when(intent.action){
            BluetoothAdapter.ACTION_STATE_CHANGED -> {
                // ZLog.d("BLEStatusReceiver STATE_CHANGED:$blueState")
                when (intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, 0)) {
                    BluetoothAdapter.STATE_TURNING_ON -> {}
                    BluetoothAdapter.STATE_ON -> {
                        ZLog.d("BLE state on.")
                    }

                    BluetoothAdapter.STATE_TURNING_OFF -> {}
                    BluetoothAdapter.STATE_OFF -> {
                        ZLog.d("BLE state off.")
                    }
                }
            }
        }
    }
}