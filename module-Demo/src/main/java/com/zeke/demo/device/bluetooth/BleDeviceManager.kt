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
    private val STATE_DEFAULT = 0               //默认状态，什么都没干
    private val STATE_BONDING = 1               //正在绑定
    private val STATE_CONNECTING = 2            //正在连接
    private val STATE_CONNECTED = 3             //已连接
    private val STATE_DISCONNECTED = 4          //已断开连接
    private val STATE_DISSTATE_CONNECTING = 5   //已断开连接
    private val STATE_REMOVE_BONDING = 6        //正在删除绑定
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
    var configCharacter: BluetoothGattCharacteristic? = null
    var controlCharacter: BluetoothGattCharacteristic? = null
    var uartCharacter: BluetoothGattCharacteristic? = null
    var flashCharacter: BluetoothGattCharacteristic? = null

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

    // ---------- 标准蓝牙 标准蓝牙service
    private val UUID_STANDARD_SERVICE: UUID = UUID.fromString("0000xxxx-0000-1000-8000-00805F9B34FB")


    //北斗手表NRF_SERVICE
    private val UUID_OF_NRF_SERVICE: UUID = UUID.fromString("68680001-0000-5049-484E-4B3201000000")
    private val UUID_OF_CONFIG_CHARACT: UUID = UUID.fromString("68680002-0000-5049-484E-4B3201000000")
    private val UUID_OF_CONTROL_CHARACT: UUID = UUID.fromString("68680003-0000-5049-484E-4B3201000000")

    //北斗手表APOLLO_SERVICE
    private val UUID_OF_APOLLO_SERVICE: UUID = UUID.fromString("68680001-0000-5049-484E-4B3202000000")
    private val UUID_OF_UARTCHARACT: UUID = UUID.fromString("68680002-0000-5049-484E-4B3202000000")
    private val UUID_OF_FLASHCHARACT: UUID = UUID.fromString("68680003-0000-5049-484E-4B3202000000")

    //蓝牙标准NOTIFY
    private val UUID_NOTIFY: UUID = UUID.fromString("00000014-0000-1000-8000-00805F9B34FB")
    //indicate通道(BLE-->App) uuid
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
     * 切换蓝牙状态
     */
    fun enableBluetooth(enable: Boolean) {
        if (enable) bluetoothAdapter?.enable() else bluetoothAdapter?.disable()
    }

    /**
     * 获取绑定设备列表
     */
    fun getBoudedDevices(): Set<BluetoothDevice>? {
        return bluetoothAdapter?.bondedDevices
    }

    fun disconectDevice(){
        bluetoothGatt?.apply {
            disconnect() //触发BluetoothGattCallback#onConnectionStateChange()的回调 回调断开连接信息，
            close()
        }
    }

    fun readBleVersionCharacter(){
        //写入需要传递给外设的特征值（即传递给外设的信息）
        val byteArray = ByteArray(20)
        //模拟寻找腕表
        byteArray[0] = 0x16
        byteArray[1] = 0x00000001
        controlCharacter?.value = byteArray
        bluetoothGatt?.writeCharacteristic(controlCharacter)
//        val byteArray = ByteArray(20).apply {
//            this[0] = 0x07
//        }
//        configCharacter?.value = byteArray
//        bluetoothGatt?.writeCharacteristic(configCharacter)
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
        val filter = ScanFilter.Builder().build()
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

        /**
         *  Callback when a BLE advertisement has been found.
         */
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
     * Open notification channle.
     * 【NOTICE】: Befor read data, must enable notification
     */
    private fun enableNotification( enable: Boolean,
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
     * BLE设备向客户端传递信息的回调
     * TODO 开一个Service
     */
    inner class BleGattCallback : BluetoothGattCallback() {


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
                    ZLog.e("Connected to GATT server.")
                    ZLog.d("Attempting to start service discovery: ${bluetoothGatt?.discoverServices()}")
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

        /**
         * 特征值读取成功回调
         */
        override fun onCharacteristicRead(
            gatt: BluetoothGatt?,
            characteristic: BluetoothGattCharacteristic?,
            status: Int
        ) {
            ZLog.d("数据读取成功 ")
            super.onCharacteristicRead(gatt, characteristic, status)
            when (status) {
                BluetoothGatt.GATT_SUCCESS -> {
                }
            }
        }

        /**
         * 特征值写入成功回调
         */
        override fun onCharacteristicWrite(
            gatt: BluetoothGatt?,
            characteristic: BluetoothGattCharacteristic?,
            status: Int
        ) {
            ZLog.d("数据写入成功 ${characteristic?.value}")
            if (status == BluetoothGatt.GATT_SUCCESS) {
                //获取写入到外设的特征值
                ZLog.d("Data is ${characteristic?.value}")
            }
            super.onCharacteristicWrite(gatt, characteristic, status)
        }

        /**
         * 监听notify通道数据
         */
        override fun onCharacteristicChanged(
            gatt: BluetoothGatt?,
            characteristic: BluetoothGattCharacteristic?
        ) {
            //TODO 手表发送查找手机的数据， [19,1,0,0....]   [19,2,0,0....]
            ZLog.d("onCharacteristicChanged() command:${characteristic?.value}")
            super.onCharacteristicChanged(gatt, characteristic)
        }

        override fun onServicesDiscovered(gatt: BluetoothGatt?, status: Int) {
            super.onServicesDiscovered(gatt, status)
            ZLog.d("onServicesDiscovered() status=${status}")
            if (gatt == null) {
                ZLog.e("onServicesDiscovered() gatt == null")
            }
            gatt?.apply {
                val characteristicConfig =
                    getService(UUID_OF_NRF_SERVICE)?.getCharacteristic(UUID_OF_CONFIG_CHARACT)
                val characteristicControl =
                    getService(UUID_OF_NRF_SERVICE)?.getCharacteristic(UUID_OF_CONTROL_CHARACT)

                enableNotification(true, characteristicConfig)
                enableNotification(true, characteristicControl)

                readCharacteristic(characteristicConfig)
                readCharacteristic(characteristicControl)
            }

            when (status) {
                BluetoothGatt.GATT_SUCCESS -> {
                    val serviceList = gatt?.services
                    serviceList?.forEach { service ->
                        if (UUID_OF_NRF_SERVICE == service.uuid) {
                            val characterList = service.characteristics
                            characterList.forEach { character ->
                                when (character.uuid) {
                                    UUID_OF_CONFIG_CHARACT -> {
                                        configCharacter = character
                                        ZLog.e(
                                            """Get Config Character:
                                             serviceUUID=${service.uuid}  characterUuid=${character.uuid}  
                                            serviceType=${service.type} characterValue=${character.value}"""
                                        )
                                    }
                                    UUID_OF_CONTROL_CHARACT -> {
                                        controlCharacter = character
                                         ZLog.e(
                                            """Get Control Character: 
                                            serviceUUID=${service.uuid}  characterUuid=${character.uuid}  
                                            serviceType=${service.type} characterValue=${character.value}"""
                                        )
                                    }
                                    else ->{
                                       ZLog.d(
                                            """Get Other Character: 
                                            serviceUUID=${service.uuid}  characterUuid=${character.uuid}  
                                            serviceType=${service.type} characterValue=${character.value}"""
                                        )
                                    }
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