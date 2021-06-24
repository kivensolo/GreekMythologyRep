package com.zeke.demo.device.bluetooth

import android.Manifest
import android.bluetooth.*
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanFilter
import android.bluetooth.le.ScanResult
import android.bluetooth.le.ScanSettings
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Handler
import android.os.Message
import android.widget.Toast
import com.hjq.permissions.OnPermissionCallback
import com.hjq.permissions.XXPermissions
import com.zeke.kangaroo.utils.ToastUtils
import com.zeke.kangaroo.utils.ZLog
import java.util.*
import kotlin.collections.HashMap


/**
 * author：ZekeWang
 * date：2021/6/23
 * description：
 * BLE蓝牙设备管理器
 *
 * <!-- 蓝牙所需权限 -->
 *  <uses-permission android:name="android.permission.BLUETOOTH" />  //允许程序连接到已配对的蓝牙设备
 *  <uses-permission android:name="android.permission.BLUETOOTH_ADMIN" /> //允许程序发现和配对蓝牙设备
 *  //定位权限
 *  <uses-permission android:name="android.permission.ACCESS_FINE_LOCATION"/>
 *  <uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION"/>
 *
 * >= 4.3
 * <uses-feature
 *      android:name="android.hardware.bluetooth_le"
 *      android:required="true" />
 *      //true 应用只能在支持BLE的Android设备上安装运行
 *      //为false时，Android设备均可正常安装运行
 *
 * 坑点记录:
 *  1.通知开启后，才能读到数据，否则读不到。
 *  2.发送数据时，如果一包数据超过20字节，需要分包发送，一次最多发送二十字节。
 *    接受也是。
 *  3.每次发送数据或者数据分包发送时， 操作间要有至少15ms的间隔
 *  4.有的蓝牙产品，发现获取不到需要的特征，后来打断点，发现他们蓝牙设备的通知特征根本没有，是他们给错协议了。。。
 *     所以建议各位开发的时候，如果一直连接失败，也可以查看一下写特征和通知特征是否为空，
 *     是不是卖家搞错了，协议和产品不匹配。（当然，这样马虎的卖家估计是少数）。
 *  5.蓝牙如果出现扫描不到的情况，那是因为手机没有开启定位权限，清单文件中写上定位权限，代码中在动态获取下就OK了。
 *
 */
class BLEDeviceManager(val context: Context) {

    val ACTION_GATT_CONNECTED = "com.example.bluetooth.le.ACTION_GATT_CONNECTED"
    val ACTION_GATT_SERVICES_DISCOVERED =
        "com.example.bluetooth.le.ACTION_GATT_SERVICES_DISCOVERED"
    val ACTION_GATT_DISCONNECTED = "com.example.bluetooth.le.ACTION_GATT_DISCONNECTED"
    val ACTION_DATA_AVAILABLE = "com.example.bluetooth.le.ACTION_DATA_AVAILABLE"
    val EXTRA_DATA = "com.example.bluetooth.le.EXTRA_DATA"

    /**
     * 蓝牙配置文件的类型 : HID
     */
    private val INPUT_PROFILE = 4

    /**
     * 对话框显示连接成功的持续时间
     */
    private val CONNECT_SUCCEED_SHOWING_TIME = 2000

    /**
     * 最大连接时间
     */
    private val MAX_CONNECT_TIME = 30000

    /**
     * 最大搜索时间
     */
    private val MAX_SCAN_TIMEOUT_MILLIS = 15 * 1000L
    /**
     * 自动重连等待时间
     */
    private val AUTO_RECONNECT_DELAY_TIME = 2000

    /**
     * 搜索超时事件值
     */
    private val CODE_MSG_SCAN_TIMEOUT = 0


    /**************  当前状态定义区 *****************/
    private val STATE_DEFAULT = 0 //默认状态，什么都没干
    private val STATE_BONDING = 1 //正在绑定
    private val STATE_CONNECTING = 2    //正在连接
    private val STATE_CONNECTED = 3     //已连接
    private val STATE_DISCONNECTED = 4  //已断开连接
    private val STATE_REMOVE_BONDING = 5 //正在删除绑定
    private val STATE_SEARCHING = 6
    /**
     * Current State
     */
    private var currentState = STATE_DEFAULT
    /************************************************/


    protected var targetDevice: BluetoothDeviceWrapper? = null
    /**
     * List of devices found
     */
    protected var foundDeviceList: MutableList<BluetoothDeviceWrapper> = ArrayList()
    /**
     * Map of devices connected.
     */
    protected var currentConnectedDeviceMap: MutableMap<String, BluetoothDeviceWrapper> = HashMap()

    //    private val bluetoothAdapter: BluetoothAdapter? by lazy(LazyThreadSafetyMode.NONE){
//          BluetoothAdapter.getDefaultAdapter()
//    }
    private var bluetoothAdapter: BluetoothAdapter? = null

    private val BluetoothAdapter.isDisabled: Boolean
        get() = !isEnabled

    private var leScanCallback: BleScanCallBack? = null


    private val mHandler = BLEManagerHandler()

    init {
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
        if (bluetoothAdapter == null) {
            Toast.makeText(context, "当前设备不支持蓝牙！", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(context, "当前设备支持蓝牙！", Toast.LENGTH_SHORT).show()
        }
    }

    fun setScanCallBack(callback: BleScanCallBack) {
        leScanCallback = callback
    }

    fun requestLocalPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // Check Location permision
            XXPermissions.with(context)
                .permission(Manifest.permission.ACCESS_COARSE_LOCATION)
                .permission(Manifest.permission.ACCESS_FINE_LOCATION)
                .request(object : OnPermissionCallback {
                    override fun onGranted(permissions: MutableList<String>?, all: Boolean) {
                        if (all) {
                            ToastUtils.show(context, "获取到所有位置权限")
                        } else {
                            ToastUtils.show(context, "获取部分权限成功，但部分权限未正常授予")
                        }
                    }

                    override fun onDenied(permissions: MutableList<String>?, never: Boolean) {
                        super.onDenied(permissions, never)
                        if (never) {
                            ToastUtils.show(context, "被永久拒绝授权，请手动授予储存权限")
                            // 如果是被永久拒绝就跳转到应用权限系统设置页面
                            XXPermissions.startPermissionActivity(context, permissions)
                        } else {
                            ToastUtils.show(context, "获取位置权限")
                        }
                    }
                })
        }
    }

    /**
     * 蓝牙是否被打开
     */
    fun isEnable(): Boolean {
        return bluetoothAdapter?.isEnabled ?: false
    }

    /**
     * 获取绑定设备列表
     */
    fun getBoudedDevices(): Set<BluetoothDevice>? {
        return bluetoothAdapter?.bondedDevices
    }

    fun startDiscovery() {
        val isSuccess = bluetoothAdapter?.startDiscovery()
        ZLog.d("startFindDevices success? = $isSuccess")
    }

    /**
     * 扫描搜索蓝牙设备
     */
    fun startScanning() {
        currentState = STATE_SEARCHING
//        如果想扫描特定类型的外围设备，则可使用
//        startLeScan(UUID[], BluetoothAdapter.LeScanCallback)，
//        它会提供一组 UUID 对象，用于指定App支持的 GATT 服务。

        val scanner = bluetoothAdapter?.bluetoothLeScanner
        //如何设置过滤？
        val filter = ScanFilter.Builder().build()
        //如何配置？
        val settings = ScanSettings.Builder()
//            .setCallbackType(ScanSettings.CALLBACK_TYPE_ALL_MATCHES)
//            .setNumOfMatches(ScanSettings.MATCH_NUM_ONE_ADVERTISEMENT)
            .setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY)
            .setReportDelay(0)
            .build()
        scanner?.startScan(listOf(filter), settings, leScanCallback)

        val msg = mHandler.obtainMessage(CODE_MSG_SCAN_TIMEOUT)
        mHandler.sendMessageDelayed(msg, MAX_SCAN_TIMEOUT_MILLIS)
    }

    fun stopScan() {
        leScanCallback?.onScanStop()
        bluetoothAdapter?.bluetoothLeScanner?.stopScan(leScanCallback)
    }

    /**
     * 连接到 GATT 服务器
     * @return BluetoothGatt
     */
    fun connectGattServer(target: BluetoothDeviceWrapper): BluetoothGatt? {
        ZLog.e("connectGattServer: ${target.mDevice?.name}")
        //false: 可用时不自动连接到 BLE 设备
        return target.mDevice?.connectGatt(context, false, BleGattCallback())
    }

    open class BleScanCallBack : ScanCallback() {
        open fun onScanStop() {}

        override fun onScanResult(callbackType: Int, result: ScanResult) {
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
    }

    /**
     * BLE设备向客户端传递信息的回调
     * TODO 开一个Service
     */
    inner class BleGattCallback : BluetoothGattCallback() {

        /**
         * 连接状态发生改变时回调:
         * Zlog: [ (BLEDeviceManager.kt:218)#ConnectGattServer ] connectGattServer: MITV-1CC21
         * BluetoothGatt: connect() - device: D4:5E:EC:D1:CC:21, auto: false
         * BluetoothGatt: registerApp()
         * BluetoothGatt: registerApp() - UUID=1fa61be8-9f0c-435e-8958-b63a1b64b0e6
         * BluetoothGatt: onClientRegistered() - status=0 clientIf=7
         * BluetoothGatt: onClientConnectionState() - status=0 clientIf=7 device=D4:5E:EC:D1:CC:21
         * Zlog: [ (BLEDeviceManager.kt:321)#OnConnectionStateChange ] onConnectionStateChange()
         */
        override fun onConnectionStateChange(gatt: BluetoothGatt?, status: Int, newState: Int) {
            ZLog.d("onConnectionStateChange() ")
            val intentAction: String
            when (newState) {
                BluetoothProfile.STATE_CONNECTED -> {
//                    intentAction = ACTION_GATT_CONNECTED
                    currentState = STATE_CONNECTED
//                    broadcastUpdate(intentAction)
//                    Log.i(TAG, "Connected to GATT server.")
//                    Log.i(
//                        TAG, "Attempting to start service discovery: " +
//                                bluetoothGatt?.discoverServices()
//                    )
                }
                BluetoothProfile.STATE_DISCONNECTED -> {
//                    intentAction = Companion.ACTION_GATT_DISCONNECTED
                    currentState = STATE_DISCONNECTED
//                    Log.i(TAG, "Disconnected from GATT server.")
//                    broadcastUpdate(intentAction)
                }
            }
        }

        override fun onReadRemoteRssi(gatt: BluetoothGatt?, rssi: Int, status: Int) {
            ZLog.d("onReadRemoteRssi() ")
            super.onReadRemoteRssi(gatt, rssi, status)
        }

        override fun onCharacteristicRead(
            gatt: BluetoothGatt?,
            characteristic: BluetoothGattCharacteristic?,
            status: Int
        ) {
            ZLog.d("onCharacteristicRead() ")
            super.onCharacteristicRead(gatt, characteristic, status)
        }

        override fun onCharacteristicWrite(
            gatt: BluetoothGatt?,
            characteristic: BluetoothGattCharacteristic?,
            status: Int
        ) {
            ZLog.d("onCharacteristicWrite() ")
            super.onCharacteristicWrite(gatt, characteristic, status)
        }

        override fun onCharacteristicChanged(
            gatt: BluetoothGatt?,
            characteristic: BluetoothGattCharacteristic?
        ) {
            ZLog.d("onCharacteristicChanged() ")
            super.onCharacteristicChanged(gatt, characteristic)
        }

        override fun onServicesDiscovered(gatt: BluetoothGatt?, status: Int) {
            ZLog.d("onServicesDiscovered() ")
            super.onServicesDiscovered(gatt, status)
        }

        override fun onPhyUpdate(gatt: BluetoothGatt?, txPhy: Int, rxPhy: Int, status: Int) {
            ZLog.d("onPhyUpdate() ")
            super.onPhyUpdate(gatt, txPhy, rxPhy, status)
        }

        override fun onPhyRead(gatt: BluetoothGatt?, txPhy: Int, rxPhy: Int, status: Int) {
            ZLog.d("onPhyRead() ")
            super.onPhyRead(gatt, txPhy, rxPhy, status)
        }

        override fun onMtuChanged(gatt: BluetoothGatt?, mtu: Int, status: Int) {
            ZLog.d("onMtuChanged() ")
            super.onMtuChanged(gatt, mtu, status)
        }

        override fun onReliableWriteCompleted(gatt: BluetoothGatt?, status: Int) {
            ZLog.d("onReliableWriteCompleted() ")
            super.onReliableWriteCompleted(gatt, status)
        }

        override fun onDescriptorWrite(
            gatt: BluetoothGatt?,
            descriptor: BluetoothGattDescriptor?,
            status: Int
        ) {
            ZLog.d("onDescriptorWrite() ")
            super.onDescriptorWrite(gatt, descriptor, status)
        }

        override fun onDescriptorRead(
            gatt: BluetoothGatt?,
            descriptor: BluetoothGattDescriptor?,
            status: Int
        ) {
            ZLog.d("onDescriptorRead() ")
            super.onDescriptorRead(gatt, descriptor, status)
        }

    }

    private fun broadcastUpdate(action: String) {
        val intent = Intent(action)
        context.sendBroadcast(intent)
    }

    private fun broadcastUpdate(action: String, characteristic: BluetoothGattCharacteristic) {
        val intent = Intent(action)

        // This is special handling for the Heart Rate Measurement profile. Data
        // parsing is carried out as per profile specifications.
        when (characteristic.uuid) {
//            UUID_HEART_RATE_MEASUREMENT -> {
//                val flag = characteristic.properties
//                val format = when (flag and 0x01) {
//                    0x01 -> {
//                        Log.d(TAG, "Heart rate format UINT16.")
//                        BluetoothGattCharacteristic.FORMAT_UINT16
//                    }
//                    else -> {
//                        Log.d(TAG, "Heart rate format UINT8.")
//                        BluetoothGattCharacteristic.FORMAT_UINT8
//                    }
//                }
//                val heartRate = characteristic.getIntValue(format, 1)
//                Log.d(TAG, String.format("Received heart rate: %d", heartRate))
//                intent.putExtra(Companion.EXTRA_DATA, (heartRate).toString())
//            }
//            else -> {
//                // For all other profiles, writes the data formatted in HEX.
//                val data: ByteArray? = characteristic.value
//                if (data?.isNotEmpty() == true) {
//                    val hexString: String = data.joinToString(separator = " ") {
//                        String.format("%02X", it)
//                    }
//                    intent.putExtra(Companion.EXTRA_DATA, "$data\n$hexString")
//                }
//            }

        }
        context.sendBroadcast(intent)
    }

    inner class BLEManagerHandler : Handler() {
        override fun handleMessage(msg: Message) {
            when (msg.what) {
                CODE_MSG_SCAN_TIMEOUT -> stopScan()
            }
            super.handleMessage(msg)
        }
    }

}