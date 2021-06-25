package com.zeke.demo.device.bluetooth

import android.Manifest
import android.bluetooth.*
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanFilter
import android.bluetooth.le.ScanResult
import android.bluetooth.le.ScanSettings
import android.content.Context
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
 *    接收也是。
 *  3.每次发送数据或者数据分包发送时， 操作间要有至少15ms的间隔
 *  4.有的蓝牙产品，发现获取不到需要的特征，后来打断点，发现他们蓝牙设备的通知特征根本没有，是他们给错协议了。。。
 *     所以建议各位开发的时候，如果一直连接失败，也可以查看一下写特征和通知特征是否为空，
 *     是不是卖家搞错了，协议和产品不匹配。（当然，这样马虎的卖家估计是少数）。
 *  5.蓝牙如果出现扫描不到的情况，那是因为手机没有开启定位权限，清单文件中写上定位权限，代码中在动态获取下就OK了。
 *
 */
class BleDeviceManager(val context: Context) {

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
    private val STATE_DISSTATE_CONNECTING = 5  //已断开连接
    private val STATE_REMOVE_BONDING = 6 //正在删除绑定
    private val STATE_SEARCHING = 7
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
    /**
     * Support API for the Bluetooth GATT Profile
     */
    private var bluetoothGatt: BluetoothGatt? = null
    var writeCharacter: BluetoothGattCharacteristic? = null
    var readCharacter: BluetoothGattCharacteristic? = null

    //------------- Some BluetoothProfiles
    private var bluetoothHealth: BluetoothHealth? = null
    private val profileListener = object : BluetoothProfile.ServiceListener{
        override fun onServiceConnected(profile: Int, proxy: BluetoothProfile?) {
            ZLog.e("onServiceConnected: profile=$profile  proxy=$proxy")
            if (profile == BluetoothProfile.HEALTH){
                bluetoothHealth = proxy as BluetoothHealth
            }
        }

        override fun onServiceDisconnected(profile: Int) {
            ZLog.e("onServiceDisconnected: ")
            if (profile == BluetoothProfile.HEALTH){
                bluetoothHealth = null
            }
        }

    }
    private var leScanCallback: BleScanCallBack? = null

    private val mHandler = BLEManagerHandler()


    //TODO 按照具体情况设置UUID
    private val UUID_SERVICE: UUID = UUID.fromString("0000D459-0000-1000-8000-00805F9B34FB")
    private val UUID_WRITE: UUID = UUID.fromString("00000013-0000-1000-8000-00805F9B34FB")
    private val UUID_NOTIFY: UUID = UUID.fromString("00000014-0000-1000-8000-00805F9B34FB")
    private val UUID_INDICATE: UUID = UUID.fromString("00000015-0000-1000-8000-00805F9B34FB")


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

    /**
     * 获取Profile
     * @param profile eg: BluetoothProfile.HEALTH
     */
    fun getProfile(profile: Int, lsr: BluetoothProfile.ServiceListener) {
        bluetoothAdapter?.getProfileProxy(context, lsr, profile)
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
     * Connect BLE devices with GATT
     * @param target Wrapper of BluetoothDevice
     * @return BluetoothGatt
     */
    fun connectDevice(target: BluetoothDeviceWrapper) {
        ZLog.e("connectGattServer: ${target.device?.name}")
        //Stop first
        bluetoothAdapter?.bluetoothLeScanner?.stopScan(leScanCallback)

        //false: 可用时不自动连接到 BLE 设备
        bluetoothGatt = target.device?.connectGatt(context, false, BleGattCallback())
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
         * Open notification channle.
         * NOTICE: Befor read data, must enable notification
         */
        private fun enableNotification(
            enable: Boolean,
            characteristic: BluetoothGattCharacteristic?
        ): Boolean {
            if (bluetoothGatt == null) {
                ZLog.e("bluetoothGatt is null.")
                return false
            }
            if (bluetoothGatt?.setCharacteristicNotification(characteristic, enable) == false) {
                ZLog.e("Notification status was set failed.")
                return false
            }
            //蓝牙标准Notification UUID
            val clientConfig =
                characteristic?.getDescriptor(UUID.fromString("00002902-0000-1000-8000-00805f9b34fb"))
                    ?: return false
            if (enable) {
                clientConfig.value = BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE
            } else {
                clientConfig.value = BluetoothGattDescriptor.DISABLE_NOTIFICATION_VALUE
            }
            return bluetoothGatt?.writeDescriptor(clientConfig) ?: false
        }

        /**
         * 连接状态发生改变时回调:
         * Connect Steps:
         *  registerApp()
         *  |_onClientRegistered() - status=0 clientIf=7
         *  |__onClientConnectionState - status=0 clientIf=7 device=D4:5E:EC:D1:CC:21
         *
         *  BLEDeviceManager.kt#OnConnectionStateChange: onConnectionStateChange()
         *
         */
        override fun onConnectionStateChange(gatt: BluetoothGatt?, status: Int, newState: Int) {
            ZLog.d("onConnectionStateChange() ")
            when (newState) {
                BluetoothProfile.STATE_CONNECTED -> {
                    currentState = STATE_CONNECTED
                    ZLog.d("Connected to GATT server.")
                    ZLog.d(
                        "Attempting to start service discovery: " +
                                bluetoothGatt?.discoverServices()
                    )
                }

                BluetoothProfile.STATE_CONNECTING -> {
                    currentState = STATE_CONNECTING
                    ZLog.d("Connecting from GATT server.")

                }

                BluetoothProfile.STATE_DISCONNECTING -> {
                    currentState = STATE_DISSTATE_CONNECTING
                    ZLog.d("Disconnecting from GATT server.")
                }

                BluetoothProfile.STATE_DISCONNECTED -> {
                    ZLog.e("Disconnected from GATT server. ")
                    currentState = STATE_DISCONNECTED
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
            when (status) {
                BluetoothGatt.GATT_SUCCESS -> {
                }
            }
        }

        override fun onCharacteristicWrite(
            gatt: BluetoothGatt?,
            characteristic: BluetoothGattCharacteristic?,
            status: Int
        ) {
            ZLog.d("onCharacteristicWrite() ")
            super.onCharacteristicWrite(gatt, characteristic, status)
        }

        /**
         * 监听notify通道数据
         */
        override fun onCharacteristicChanged(
            gatt: BluetoothGatt?,
            characteristic: BluetoothGattCharacteristic?
        ) {
            ZLog.d("onCharacteristicChanged() command:${characteristic?.value}")
            super.onCharacteristicChanged(gatt, characteristic)
        }

        override fun onServicesDiscovered(gatt: BluetoothGatt?, status: Int) {
            super.onServicesDiscovered(gatt, status)
            ZLog.d("onServicesDiscovered() ")
            if (gatt == null) {
                ZLog.e("onServicesDiscovered() gatt == null")
            }
            var characteristicNotify: BluetoothGattCharacteristic?
            var characteristicRead: BluetoothGattCharacteristic?
            gatt?.apply {
                characteristicNotify = getService(UUID_SERVICE)?.getCharacteristic(UUID_NOTIFY)
                characteristicRead = getService(UUID_SERVICE)?.getCharacteristic(UUID_INDICATE)
                enableNotification(true, characteristicNotify)
                readCharacteristic(characteristicRead)
            }

            when (status) {
                BluetoothGatt.GATT_SUCCESS -> {
                    val serviceList = gatt?.services
                    serviceList?.forEach { service ->
                        if (UUID_SERVICE == service.uuid) {
                            val characterList = service.characteristics
                            characterList.forEach { character ->
                                if (UUID_WRITE == character.uuid) {
                                    writeCharacter = character
                                    ZLog.d(
                                        "onServicesDiscovered(writeCharacter):" +
                                                """serviceUUID=${service.uuid}  serviceType=${service.type}
                                            characterUuid=${character.uuid}  characterValue=${character.value}"""
                                    )
                                } else if (UUID_INDICATE == character.uuid) {
                                    readCharacter = character
                                    ZLog.d(
                                        "onServicesDiscovered(readCharacter):" +
                                                """serviceUUID=${service.uuid}  serviceType=${service.type}
                                            characterUuid=${character.uuid}  characterValue=${character.value}"""
                                    )
                                }
                            }
                        }
                    }
                }
                else -> ZLog.d("onServicesDiscovered received: $status")
            }

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

    inner class BLEManagerHandler : Handler() {
        override fun handleMessage(msg: Message) {
            when (msg.what) {
                CODE_MSG_SCAN_TIMEOUT -> stopScan()
            }
            super.handleMessage(msg)
        }
    }

}