package com.zeke.demo.device.bluetooth

import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattCallback
import android.bluetooth.BluetoothGattCharacteristic
import android.bluetooth.BluetoothGattDescriptor
import android.bluetooth.le.ScanResult
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.kingz.base.BaseVMActivity
import com.kingz.ble.BleDeviceManager
import com.kingz.ble.BluetoothDeviceWrapper
import com.kingz.ble.response.BleEventCallBack
import com.zeke.demo.R
import com.zeke.demo.databinding.ActivityBluetoothDemoBinding
import com.zeke.kangaroo.zlog.ZLog
import com.zeke.reactivehttp.base.BaseReactiveViewModel
import java.util.UUID


/**
 * author：ZekeWang
 * date：2021/6/18
 * description：BLE蓝牙测试
 * 高版本安卓增加了蓝牙连接的权限请求
 */
class BleDemoActivity : BaseVMActivity() {

    private var bleManager: BleDeviceManager? = null
    private var nrfServiceUUID = UUID.fromString("68680001-0000-5049-484E-4B3201000000")
    //配置特征码UUID
    private val configCharactUuid = UUID.fromString("68680002-0000-5049-484E-4B3201000000")
    //绑定指令（iOS 专用）
    private val notifyCharactUuid = UUID.fromString("68680003-0000-5049-484E-4B3201000000")

    private var bluetoothGattCharacteristicWrite: BluetoothGattCharacteristic? = null
    private var bluetoothGattCharacteristicNotify: BluetoothGattCharacteristic? = null

    var blGatt: BluetoothGatt? = null

    var bleAadapter: BleRecyclerAdapter? = BleRecyclerAdapter()
    private lateinit var viewBind:ActivityBluetoothDemoBinding


    // <editor-fold defaultstate="collapsed" desc="蓝牙扫描结果广播接收器">
    // 新建一个 BroadcastReceiver来接收ACTION_FOUND&ACTION_DISCOVERY_FINISHED广播
    private var scanFilter: IntentFilter = IntentFilter().apply {
        addAction(BluetoothDevice.ACTION_FOUND)
        addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED)
    }
    var devicesListInfo: String = ""
    private val mReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent) {
            val action = intent.action
            if (BluetoothDevice.ACTION_FOUND == action) {
                //获得 BluetoothDevice
                val device: BluetoothDevice? =
                    intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE)
                device?.apply {
                    //device.bondState == BluetoothDevice.BOND_BONDED // 配对过的状态
                    if (ActivityCompat.checkSelfPermission(
                            this@BleDemoActivity,
                            Manifest.permission.BLUETOOTH_CONNECT
                        ) != PackageManager.PERMISSION_GRANTED
                    ) {
                        // TODO: Consider calling ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                        return
                    }
                    ZLog.d("onReceive findDevice=${this.name}  address=${this.address}  uuids=${this.uuids}")
                    // FIXME 有些蓝牙设备的name为null  暂无解决方案
                    bluetoothClass
                    devicesListInfo += "[${this.name}: ${this.address}] \n"
                    if (this.name != null && this.name.startsWith("CM")) {
                        // 进行设备连接
                        blGatt = this.connectGatt(context, false, object : BluetoothGattCallback() {
                            override fun onReadRemoteRssi(
                                gatt: BluetoothGatt?,
                                rssi: Int,
                                status: Int
                            ) {
                                super.onReadRemoteRssi(gatt, rssi, status)
                            }

                            /**
                             * 接受BLE上行数据
                             */
                            override fun onCharacteristicRead(
                                gatt: BluetoothGatt?,
                                characteristic: BluetoothGattCharacteristic?,
                                status: Int
                            ) {
                                super.onCharacteristicRead(gatt, characteristic, status)
                                if (status == BluetoothGatt.GATT_SUCCESS) {
                                    // 收到数据
                                    val receiveByte = characteristic?.value
                                }
                            }

                            /**
                             * 收到BLE从机写入数据回调
                             */
                            override fun onCharacteristicWrite(
                                gatt: BluetoothGatt?,
                                characteristic: BluetoothGattCharacteristic?,
                                status: Int
                            ) {
                                super.onCharacteristicWrite(gatt, characteristic, status)
                                if (status == BluetoothGatt.GATT_SUCCESS) {
                                    // 发送成功

                                } else {
                                    // 发送失败
                                }
                            }

                            override fun onServicesDiscovered(gatt: BluetoothGatt?, status: Int) {
                                super.onServicesDiscovered(gatt, status)
                                //此回调说明发现当前设备了。然后就可以在里面去获取BluetoothGattService和BluetoothGattCharacteristic
                                ZLog.d("status=$status")
                                if (status == BluetoothGatt.GATT_SUCCESS) {
                                    //发现设备，遍历服务，初始化特征
                                    initBleCharacteristics(gatt)
                                } else {
                                    ZLog.d("onServicesDiscovered fail-->$status")
                                }

                            }

                            override fun onPhyUpdate(
                                gatt: BluetoothGatt?,
                                txPhy: Int,
                                rxPhy: Int,
                                status: Int
                            ) {
                                super.onPhyUpdate(gatt, txPhy, rxPhy, status)
                            }

                            override fun onMtuChanged(gatt: BluetoothGatt?, mtu: Int, status: Int) {
                                super.onMtuChanged(gatt, mtu, status)
                            }

                            override fun onReliableWriteCompleted(
                                gatt: BluetoothGatt?,
                                status: Int
                            ) {
                                super.onReliableWriteCompleted(gatt, status)
                            }

                            override fun onDescriptorWrite(
                                gatt: BluetoothGatt?,
                                descriptor: BluetoothGattDescriptor?,
                                status: Int
                            ) {
                                super.onDescriptorWrite(gatt, descriptor, status)
                            }

                            override fun onCharacteristicChanged(
                                gatt: BluetoothGatt?,
                                characteristic: BluetoothGattCharacteristic?
                            ) {
                                super.onCharacteristicChanged(gatt, characteristic)
                            }

                            override fun onDescriptorRead(
                                gatt: BluetoothGatt?,
                                descriptor: BluetoothGattDescriptor?,
                                status: Int
                            ) {
                                super.onDescriptorRead(gatt, descriptor, status)
                            }

                            override fun onPhyRead(
                                gatt: BluetoothGatt?,
                                txPhy: Int,
                                rxPhy: Int,
                                status: Int
                            ) {
                                super.onPhyRead(gatt, txPhy, rxPhy, status)
                            }

                            override fun onConnectionStateChange(
                                gatt: BluetoothGatt?,
                                status: Int,
                                newState: Int
                            ) {
                                ZLog.d("status=$status, newState=$newState")
                                if (status == BluetoothGatt.STATE_CONNECTED) {
                                    ZLog.d("STATE_CONNECTED")
                                    // 连接成功  //获取服务
                                    if (ActivityCompat.checkSelfPermission(
                                            this@BleDemoActivity,
                                            Manifest.permission.BLUETOOTH_CONNECT
                                        ) != PackageManager.PERMISSION_GRANTED
                                    ) {
                                        // TODO: Consider calling
                                        //    ActivityCompat#requestPermissions
                                        // here to request the missing permissions, and then overriding
                                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                        //                                          int[] grantResults)
                                        // to handle the case where the user grants the permission. See the documentation
                                        // for ActivityCompat#requestPermissions for more details.
                                        return
                                    }
                                    blGatt?.discoverServices()
                                } else if (status == BluetoothGatt.STATE_DISCONNECTED) {
                                    ZLog.e("STATE_DISCONNECTED")
                                    //断开连接
                                }
                                super.onConnectionStateChange(gatt, status, newState)
                            }
                        })
                    }
                }


            } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED == action) {
//                ble_device_list.text = devicesListInfo

                bleManager?.stopScan()
            }
        }
    }
    // </editor-fold>

    override val viewModel: BaseReactiveViewModel
        get() = TODO("not implemented") //To change initializer of created properties use File | Settings | File Templates.

    override fun getContentLayout(): Int = R.layout.activity_bluetooth_demo

    override fun initData(savedInstanceState: Bundle?) {
        lContext!!.registerReceiver(mReceiver, scanFilter)
    }

    var deviceMap: HashMap<String, ScanResult> = HashMap<String, ScanResult>()

    override fun initView(savedInstanceState: Bundle?) {
        super.initView(savedInstanceState)
        viewBind = ActivityBluetoothDemoBinding.inflate(layoutInflater)
        bleAadapter?.apply {
            clickListener = object : BleRecyclerAdapter.DeviceClickListener {
                override fun onClick(item: BluetoothDeviceWrapper, adapterPosition: Int) {
                    bleManager?.connectDevice(item)
                }
            }
        }

        bleManager = BleDeviceManager.apply {
            setScanCallBack(object : BleEventCallBack() {
                override fun onScanResult(callbackType: Int, result: ScanResult) {
                    super.onScanResult(callbackType, result)
                    val deviceWrapper = BluetoothDeviceWrapper(
                        result.rssi,
                        result.device
                    )
                    bleAadapter?.apply {
                        runOnUiThread {
                            for (datum in data) {
                                if(TextUtils.equals(datum.device?.address,
                                        result.device.address)){
                                    return@runOnUiThread
                                }
                            }
                            //没有设备 添加
                            addData(deviceWrapper)
                            notifyDataSetChanged()
                        }
                    }
                }
            })
            requestLocalPermission()
        }
        
        viewBind.deviceFindRecycler.apply {
            layoutManager = LinearLayoutManager(lContext)
            adapter = bleAadapter
        }
        
        viewBind.findBluetoothDevice.setOnClickListener {
            ZLog.d("viewBind.findBluetoothDevice onclicked.")
            // Check Bluetooth state
            bleManager?.takeIf { !it.isEnable() }?.apply {
                val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
                // 高版本做的权限检查
                if (ActivityCompat.checkSelfPermission(
                        this@BleDemoActivity, Manifest.permission.BLUETOOTH_CONNECT
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    return@apply
                }
                startActivityForResult(enableBtIntent, 1)
                return@setOnClickListener
            }
            startScanDevices()
        }
        viewBind.changeBluetooth.setOnClickListener {
            if(bleManager?.isEnable() == true){
                bleManager?.enableBluetooth(false)
            }else{
                val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
                startActivityForResult(enableBtIntent, 1)
            }
        }

        //连接蓝牙设备
        viewBind.connectBluetooth.setOnClickListener {
            //fisrt cancel
            bleManager?.stopScan()
        }

        viewBind.disconnectBluetooth.setOnClickListener {
            bleManager?.disconnect()
        }

        viewBind.readConfig.setOnClickListener {
        }
    }


    /**
     * 初始化读写特征
     */
    fun initBleCharacteristics(gatt: BluetoothGatt?) {
        if (gatt == null) {
            return
        }
        //遍历所有服务
        for (service in gatt.services) {
            ZLog.d("BluetoothGattService --->$service ")

            //遍历所有特征
            for (charactertistic in service.characteristics) {
                ZLog.d("BluetoothGattCharacteristic --->$charactertistic ")
                when (charactertistic.uuid) {
                    configCharactUuid -> {
                        //根据写UUID找到写特征
                        bluetoothGattCharacteristicWrite = charactertistic
                    }
                    notifyCharactUuid -> {
                        //根据通知UUID找到通知特征
                        bluetoothGattCharacteristicNotify = charactertistic
                    }
                }
            }
        }
    }

    private fun sendData() {
        bluetoothGattCharacteristicWrite?.apply {
            val byteArray = ByteArray(2048)
            val k2 =
                byteArrayOf(
                    0x00,   //数据帧序列号  首条0x00
                    0x01,           //数据帧总数量
                    0xaa.toByte(),  //当前帧的CommandId
                    0x01,           //当前帧的Key值
                    0x00 //是否加密  0加密 1不加密

                )
        }

        //通过GATT协议 传送数据
        blGatt?.apply {
            // 开启通知【必须】  onCharacteristicRead才能回调
            if (ActivityCompat.checkSelfPermission(
                    this@BleDemoActivity,
                    Manifest.permission.BLUETOOTH_CONNECT
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return
            }
            setCharacteristicNotification(bluetoothGattCharacteristicNotify, true)
            writeCharacteristic(bluetoothGattCharacteristicWrite)
        }
    }


    /**
     * 开始查找LE设备
     * 在连接设备之前要停止搜索蓝牙
     */
    private fun startScanDevices() {
        bleManager?.startScanning()
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
        bleManager?.stopScan()
        bleManager = null

        //释放
        blGatt?.apply {
            if (ActivityCompat.checkSelfPermission(
                    this@BleDemoActivity,
                    Manifest.permission.BLUETOOTH_CONNECT
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return
            }
            disconnect()
            close()
        }
        blGatt = null
    }
}