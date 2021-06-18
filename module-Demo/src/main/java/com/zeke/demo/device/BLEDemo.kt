package com.zeke.demo.device

import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import com.hjq.permissions.OnPermissionCallback
import com.hjq.permissions.XXPermissions
import com.kingz.base.BaseVMActivity
import com.zeke.demo.R
import com.zeke.demo.device.BLEDemo
import com.zeke.kangaroo.utils.ToastUtils
import com.zeke.kangaroo.utils.ZLog
import com.zeke.reactivehttp.base.BaseReactiveViewModel
import kotlinx.android.synthetic.main.activity_bluetooth_demo.*


/**
 * author：ZekeWang
 * date：2021/6/18
 * description：BLE蓝牙测试
 */
class BLEDemo : BaseVMActivity() {

    private var mBluetoothAdapter: BluetoothAdapter? = null

    // <editor-fold defaultstate="collapsed" desc="蓝牙扫描结果广播接收器">
    // 新建一个 BroadcastReceiver来接收ACTION_FOUND&ACTION_DISCOVERY_FINISHED广播
    private var scanFilter: IntentFilter = IntentFilter().apply {
        addAction(BluetoothDevice.ACTION_FOUND)
        addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED)
    }
    var devicesListInfo:String = ""
    private val mReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent) {
            val action = intent.action
            if (BluetoothDevice.ACTION_FOUND == action) {
                //获得 BluetoothDevice
                val device: BluetoothDevice = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE)
                //device.bondState == BluetoothDevice.BOND_BONDED // 配对过的状态
                ZLog.d("onReceive findDevice=${device.name}  address=${device.address}")
                // FIXME 有些蓝牙设备的name为null  暂无解决方案
                devicesListInfo+="[${device.name}: ${device.address}] \n"
            }else if(BluetoothAdapter.ACTION_DISCOVERY_FINISHED == action){
                ble_device_list.text = devicesListInfo
                mBluetoothAdapter?.cancelDiscovery()
            }
        }
    }
    // </editor-fold>

    override val viewModel: BaseReactiveViewModel
        get() = TODO("not implemented") //To change initializer of created properties use File | Settings | File Templates.

    override fun getContentLayout(): Int = R.layout.activity_bluetooth_demo

    override fun initData(savedInstanceState: Bundle?) {
        lContext!!.registerReceiver(mReceiver,scanFilter)
    }

    override fun initView(savedInstanceState: Bundle?) {
        super.initView(savedInstanceState)

        checkPermissions()
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
        if (mBluetoothAdapter == null) {
            Toast.makeText(this, "当前设备不支持蓝牙！", Toast.LENGTH_SHORT).show()
        }else{
            Toast.makeText(this, "当前设备支持蓝牙！", Toast.LENGTH_SHORT).show()
        }

        find_bluetooth_device.setOnClickListener {
            ZLog.d("find_bluetooth_device onclicked.")
            // Check Bluetooth state
            if (!isBluetoothOpen()) {
                return@setOnClickListener
            }
            startFindDevices()
        }
        change_bluetooth.setOnClickListener {
//            mBluetoothAdapter?.
               val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
            startActivityForResult(enableBtIntent, 1)
        }

        //连接蓝牙设备
        connect_bluetooth.setOnClickListener {
            //fisrt cancel
            mBluetoothAdapter?.cancelDiscovery()
//            注 ：停止搜索一般需要一定的时间来完成，最好调用停止搜索函数之后加以100ms的延时，
//            保证系统能够完全停止搜索蓝牙设备。停止搜索之后启动连接过程。

        }


    }

    private fun checkPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // Check Location permision
            XXPermissions.with(this@BLEDemo)
                .permission(Manifest.permission.ACCESS_COARSE_LOCATION)
                .permission(Manifest.permission.ACCESS_FINE_LOCATION)
                .request(object : OnPermissionCallback {
                    override fun onGranted(permissions: MutableList<String>?, all: Boolean) {
                        if (all) {
                            ToastUtils.show(baseContext, "获取到所有位置权限")
                        } else {
                            ToastUtils.show(baseContext, "获取部分权限成功，但部分权限未正常授予")
                        }
                    }

                    override fun onDenied(permissions: MutableList<String>?, never: Boolean) {
                        super.onDenied(permissions, never)
                        if (never) {
                            ToastUtils.show(baseContext, "被永久拒绝授权，请手动授予储存权限")
                            // 如果是被永久拒绝就跳转到应用权限系统设置页面
                            XXPermissions.startPermissionActivity(this@BLEDemo, permissions)
                        } else {
                            ToastUtils.show(baseContext, "获取位置权限")
                        }
                    }
                })
        }
    }

    /**
     * 开始查找设备
     * 老版本用startLeScan(callback)的方式
     *
     * 在连接设备之前要停止搜索蓝牙
     */
    private fun startFindDevices() {
        //会有12秒的时间来进行查询扫描
        val isSuccess = mBluetoothAdapter?.startDiscovery()
        ZLog.d("startFindDevices success? = $isSuccess")
    }

    private fun isBluetoothOpen(): Boolean {
        if (mBluetoothAdapter?.isEnabled != true) {
            val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
            startActivityForResult(enableBtIntent, 1)
            return false
        }
        return true
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        ZLog.d("onActivityResult code=$resultCode")
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                Toast.makeText(this, "蓝牙已经开启", Toast.LENGTH_SHORT).show();
            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(this, "没有蓝牙权限", Toast.LENGTH_SHORT).show();
                finish()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mBluetoothAdapter?.cancelDiscovery()
        mBluetoothAdapter = null
    }
}