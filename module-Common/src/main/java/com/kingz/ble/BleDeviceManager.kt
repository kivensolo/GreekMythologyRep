@file:Suppress("PrivatePropertyName")

package com.kingz.ble

import BleWriteTask
import android.Manifest
import android.annotation.SuppressLint
import android.app.Application
import android.bluetooth.*
import android.bluetooth.le.ScanFilter
import android.bluetooth.le.ScanSettings
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.os.Message
import com.hjq.permissions.OnPermissionCallback
import com.hjq.permissions.XXPermissions
import com.kapplication.beidou.watch.ble.dispatch.BlueToothDataDispatcher
import com.kingz.ble.cons.BleMsg
import com.kingz.ble.controller.GattCharacteristicController
import com.kingz.ble.dispatch.IBleManagerDelegate
import com.kingz.ble.dispatch.IBleResponseCallBack
import com.kingz.ble.exception.BleWriteException
import com.kingz.ble.model.IBleData
import com.kingz.ble.response.BleEventCallBack
import com.kingz.module.common.utils.getTimeFormatString
import com.kingz.module.common.utils.patternFormatChinese
import com.zeke.kangaroo.utils.ToastUtils
import com.zeke.kangaroo.zlog.ZLog
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*
import kotlin.collections.HashMap


/**
 * author：ZekeWang
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
public object BleDeviceManager {
    private const val TAG = "BleDeviceManager"

    // <editor-fold defaultstate="collapsed" desc="老版Android蓝牙GATT Server事件广播action">
    val ACTION_GATT_CONNECTED = "com.example.bluetooth.le.ACTION_GATT_CONNECTED"
    val ACTION_GATT_SERVICES_DISCOVERED = "com.example.bluetooth.le.ACTION_GATT_SERVICES_DISCOVERED"
    val ACTION_GATT_DISCONNECTED = "com.example.bluetooth.le.ACTION_GATT_DISCONNECTED"
    val ACTION_DATA_AVAILABLE = "com.example.bluetooth.le.ACTION_DATA_AVAILABLE"
    val EXTRA_DATA = "com.example.bluetooth.le.EXTRA_DATA"
    // </editor-fold>

  // <editor-fold defaultstate="collapsed" desc="配置参数">
    /**
     * 蓝牙配置文件的类型 : HID
     */
    private const val INPUT_PROFILE = 4

    /**
     * 对话框显示连接成功的持续时间
     */
    private const val CONNECT_SUCCEED_SHOWING_TIME = 2000

    /**
     * 最大连接时间
     */
    private const val MAX_CONNECT_TIME = 30000

    /**
     * 最大搜索时间
     */
    const val MAX_SCAN_TIMEOUT_MILLIS = 15 * 1000

    /**
     * 自动重连等待时间
     */
    private const val AUTO_RECONNECT_DELAY_TIME = 2000
  // </editor-fold>


 // <editor-fold defaultstate="collapsed" desc="蓝牙状态码">
    private const val STATE_DEFAULT = 0               //默认状态，什么都没干
    private const val STATE_BONDING = 1               //正在绑定
    private const val STATE_CONNECTING = 2            //正在连接
    private const val STATE_CONNECTED = 3             //已连接
    private const val STATE_DISCONNECTED = 4          //已断开连接
    private const val STATE_DISCONNECTING = 5         //正在断开连接
    private const val STATE_REMOVE_BONDING = 6        //正在删除绑定
    private const val STATE_SEARCHING = 7             //正在扫描
    private const val STATE_SEARCH_STOPPING = 8       //正在停止扫描
    /**
     * Current State
     */
    var currentState = STATE_DEFAULT

 // </editor-fold>

    /************************************************/
    lateinit var context: Application

    //远程蓝牙设备数据封装
    @Volatile
    var selectedDevice: BluetoothDeviceWrapper? = null

    /**
     * List of devices found
     */
    var foundDeviceList: MutableList<BluetoothDeviceWrapper> = ArrayList()

    /**
     * Map of devices connected.
     * Key:mac
     */
    var currentConnectedDeviceMap: MutableMap<String, BluetoothDeviceWrapper> = HashMap()

    //    private val bluetoothAdapter: BluetoothAdapter? by lazy(LazyThreadSafetyMode.NONE){
//          BluetoothAdapter.getDefaultAdapter()
//    }

    // <editor-fold defaultstate="collapsed" desc="北斗手表蓝牙属性">
    //拥有基本的蓝牙操作，例如开启蓝牙扫描等等
    private var bluetoothAdapter: BluetoothAdapter? = null
    private var bluetoothGatt: BluetoothGatt? = null
    private val characteristicController = GattCharacteristicController()
    // </editor-fold>

    private var bleEventCallback: BleEventCallBack? = null

    // <editor-fold defaultstate="collapsed" desc="BluetoothProfiles">
    private var bluetoothHealth: BluetoothHealth? = null
    private val profileListener = object : BluetoothProfile.ServiceListener {
        override fun onServiceConnected(profile: Int, proxy: BluetoothProfile?) {
            ZLog.e(TAG,"onServiceConnected: profile=$profile  proxy=$proxy")
            if (profile == BluetoothProfile.HEALTH) {
                bluetoothHealth = proxy as BluetoothHealth
            }
        }

        override fun onServiceDisconnected(profile: Int) {
            ZLog.e(TAG,"onServiceDisconnected: ")
            if (profile == BluetoothProfile.HEALTH) {
                bluetoothHealth = null
            }
        }

    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="扫描控制">
    private var isAutoConnectAfterScanResult = false
    private val uiHandler = BLEManagerHandler()

    private val dataDispatcher = BlueToothDataDispatcher(object : IBleManagerDelegate {
        override fun onWriteDataDispatch(task: BleWriteTask) {
            characteristicController.writeData(bluetoothGatt, task.data)
        }

        override fun onWriteDataFinish() {
        }
    })

    class BLEManagerHandler : Handler(Looper.getMainLooper()) {
        override fun handleMessage(msg: Message) {
            when (msg.what) {
                BleMsg.MSG_OF_SCAN_TIMEOUT,
                BleMsg.MSG_CONNECTED -> {
                    stopScan()
                }
                BleMsg.MSG_DISCOVER_SUCCESS -> {
                    CoroutineScope(Dispatchers.IO).launch {
                        initCharacters()
                    }
                }
                BleMsg.MSG_DISCOVER_FAIL -> {}
            }
            super.handleMessage(msg)
        }

        private suspend fun initCharacters() {
            val initSuccess = characteristicController.initCharacteristics(bluetoothGatt, bleEventCallback)
            if(initSuccess){
                requestMTU(256)
            }else{
                //FIXME 初始化失败如何处理?
            }
        }
    }


    // </editor-fold>

// <editor-fold defaultstate="collapsed" desc="Server --- 标准蓝牙">
//    private val UUID_STANDARD_BASE_SERVICE: UUID =
//        UUID.fromString("0000xxxx-0000-1000-8000-00805F9B34FB") // 用设备assigned number替换xxxx即得到UUID
    //蓝牙标准NOTIFY
    private val UUID_OF_NOTIFY: UUID = UUID.fromString("00000014-0000-1000-8000-00805F9B34FB")
    //Bluetooth Generic Access Profile
    private val UUID_OF_AccessProfile: UUID =
        UUID.fromString("00001800-0000-1000-8000-00805F9B34FB")

    //Bluetooth Generic Attribute Profile
    private val UUID_OF_AttributeProfile: UUID =
        UUID.fromString("00001801-0000-1000-8000-00805F9B34FB")

    //indicate通道(BLE-->App) uuid
    private val UUID_OF_INDICATE: UUID = UUID.fromString("00000015-0000-1000-8000-00805F9B34FB")
    private val UUID_OF_NOTIFICATION: UUID = UUID.fromString("00002902-0000-1000-8000-00805f9b34fb")
    // </editor-fold>



    // <editor-fold defaultstate="collapsed" desc="初始化设置">
    fun init(context: Application) {
        BleDeviceManager.context = context
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
        if (bluetoothAdapter == null) {
            ZLog.e(TAG,"Current Device can't find Bluetooth binder.")
        } else {
            ZLog.d(TAG,"Current Device support Bluetooth.")
        }
    }

    fun setScanCallBack(callback: BleEventCallBack) {
        bleEventCallback = callback
    }
    // </editor-fold>

// <editor-fold defaultstate="collapsed" desc="公共Get方法">


    // <editor-fold defaultstate="collapsed" desc="蓝牙数据发送">

    fun sendData(data: IBleData, callback: IBleResponseCallBack? = null){
        sendData(data, true, callback)
    }

    /**
     * 蓝牙数据统一发送API
     * @param data 蓝牙协议数据封装对象
     * @param trySliceMode 尝试以分片模式发送数据，
     *        默认尝试开启分片模式(当数据量小于MTU时，则不需要分片)
     * @param callback 响应回调
     *  注意: 此Callback为数据写入成功后，腕表的Response响应，不是数据写入成功的响应
     */
    fun sendData(
        data: IBleData,
        trySliceMode: Boolean = true,
        callback: IBleResponseCallBack? = null
    ) {
        if(currentState != STATE_CONNECTED){
            ZLog.e(TAG, "Connect is not ready, send drop.")
            //FIXME 会引起APP崩溃
            callback?.onError(
                BleWriteException(
                    BleMsg.MSG_OF_WRITE_FAILED_WHEN_NOT_CONNECT,
                    "Device is not connected yet!"
                )
            )
            return
        }
        dataDispatcher.startSendData(data, trySliceMode, callback)
    }

    /**
     * 清空发送队列的数据
     */
    fun clearSendData(){
        dataDispatcher.clearWriteQueue()
    }

    fun getSendDataFromHead(){
        dataDispatcher.getWriteHeadData()
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="蓝牙控制方法">

    /**
     * 获取Profile
     * @param profile eg: BluetoothProfile.HEALTH
     */
    fun getProfile(profile: Int, lsr: BluetoothProfile.ServiceListener) {
        bluetoothAdapter?.getProfileProxy(context, lsr, profile)
    }

    /**
     * ChangeMTU
     */
    private fun requestMTU(newMtu: Int) {
        ZLog.d(TAG, "requestMTU: $newMtu")
        if (newMtu in 24..511) { // 只有蓝牙5.0才支持512大小的MTU
            bluetoothGatt?.requestMtu(newMtu)
        } else {
            ZLog.e(TAG, "无效mtu值: $newMtu")
            mtuIsChanged(-1, BleMsg.MTU_CHANGE_ILLEGAL)
        }
    }

    private fun mtuIsChanged(mtu: Int, status: Int) {
        dataDispatcher.onMtuChanged(mtu, status)
        //MTU改变是最后一个步骤，完毕后，则分发AllReady
        bleEventCallback?.onGattAllReady()
    }

    /**
     * Get the current MTU value.
     */
    fun getMTU():Int{
        return dataDispatcher.getCurrentMTU()
    }

    @Deprecated("老版本startDiscovery扫描设备")
    fun startDiscovery() {
        val isSuccess = bluetoothAdapter?.startDiscovery()
        ZLog.d(TAG, "startFindDevices success? = $isSuccess")
    }

    /**
     *  Scan for Bluetooth devices
     * 【NOTICE】: Stop searching for Bluetooth before connect.
     * @param scanTime Scan timeout duration
     */
    @Synchronized
    fun startScanning(scanTime: Int = MAX_SCAN_TIMEOUT_MILLIS) {
        ZLog.d(TAG, "startScanning()")
        currentState = STATE_SEARCHING
//        如果想扫描特定类型的外围设备，则可使用
//        startLeScan(UUID[], BluetoothAdapter.LeScanCallback)，
//        提供一组 UUID 对象，用于指定App支持的 GATT 服务。

        val scanner = bluetoothAdapter?.bluetoothLeScanner
        val filter = ScanFilter.Builder().build()
        val settings = ScanSettings.Builder()
//            .setCallbackType(ScanSettings.CALLBACK_TYPE_ALL_MATCHES)
//            .setNumOfMatches(ScanSettings.MATCH_NUM_ONE_ADVERTISEMENT)
            .setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY)
            .setReportDelay(0)
            .build()
        scanner?.startScan(listOf(filter), settings, bleEventCallback)

        val msg = uiHandler.obtainMessage(BleMsg.MSG_OF_SCAN_TIMEOUT)
        uiHandler.sendMessageDelayed(msg, scanTime.toLong())
    }

    /**
     * Stop scan
     */
    @SuppressLint("ObsoleteSdkInt")
    @Synchronized
    fun stopScan() {
        if (currentState != STATE_SEARCHING) {
            return
        }
        ZLog.d(TAG, "Stop ble Scan....")
        currentState = STATE_SEARCH_STOPPING
        uiHandler.removeCallbacksAndMessages(null)
        bluetoothAdapter?.apply {
            if (enable()) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    //防止“BT Adapter is not turned ON”等异常
                    try{
                        bluetoothLeScanner?.stopScan(bleEventCallback)
                    }catch (e:Exception){
                        e.printStackTrace()
                    }
                } else {
                    //TODO 低版本适配
//                    stopLeScan(bleEventCallback2)
                }
                bleEventCallback?.onScanStop()
            }
        }
    }

    /**
     * Connect BLE devices with GATT
     * @param target Wrapper of BluetoothDevice
     * @return BluetoothGatt
     */
    @Synchronized
    fun connectDevice(target: BluetoothDeviceWrapper) {
        selectedDevice = target
        ZLog.e(TAG, "To connect gatt server: ${target.device?.name}")
        stopScan()  //Stop first
        bluetoothGatt = target.device?.connectGatt(
            context,
            false, //false: 可用时不自动连接到 BLE 设备
            BleGattCallback()
        )
    }

    /**
     * Whether bluetooth is enabled
     */
    fun isEnable(): Boolean {
        return bluetoothAdapter?.isEnabled ?: false
    }

    /**
     * 切换蓝牙状态
     */
    fun enableBluetooth(enable: Boolean) {
        if (enable) {
            bluetoothAdapter?.enable()
        } else {
            stopScan()
            bluetoothAdapter?.disable()
        }
    }

    /**
     * 获取绑定设备列表
     */
    fun getBoundedDevices(): Set<BluetoothDevice>? {
        return bluetoothAdapter?.bondedDevices
    }
    // </editor-fold>

    @Synchronized
    fun disconnect() {
        ZLog.e(TAG, "disconnect()")
        bluetoothGatt?.apply {
            disconnect() //先断开连接，但是不释放资源
        }
    }
// </editor-fold>

    /**
     * BLE设备向客户端传递信息的回调
     */
    class BleGattCallback : BluetoothGattCallback() {
        /**
         * 连接状态发生改变时回调:
         */
        override fun onConnectionStateChange(gatt: BluetoothGatt, status: Int, newState: Int) {
            ZLog.d(TAG, "onConnectionStateChange -status=$status -newState=$newState  ")
            when (newState) {
                BluetoothProfile.STATE_CONNECTED -> {
                    ZLog.e(TAG, "[蓝牙已连接]Connected to GATT server.")
                    currentState = STATE_CONNECTED
                    selectedDevice?.apply {
//                        currentConnectedDeviceMap[device?.address] = this
                        extInfo = Date().getTimeFormatString(patternFormatChinese)
                        showName = device?.name ?: "Unknow"
                    }
                    uiHandler.post {
                        bleEventCallback?.onDeviceConnected(selectedDevice!!)
                    }
                    val success = bluetoothGatt?.discoverServices()
                    ZLog.d(TAG, "Start discovery services: $success")
                }
                BluetoothProfile.STATE_DISCONNECTED -> {
                    onBleDisconnect()
                }
                BluetoothProfile.STATE_CONNECTING -> {
                    currentState = STATE_CONNECTING
                    ZLog.d(TAG, "Connecting from GATT server.....")
                }

                BluetoothProfile.STATE_DISCONNECTING -> {
                    currentState = STATE_DISCONNECTING
                    ZLog.d(TAG, "Disconnecting from GATT server......")
                }

            }
        }

        override fun onReadRemoteRssi(gatt: BluetoothGatt?, rssi: Int, status: Int) {
            ZLog.d(TAG, "onReadRemoteRssi() ")
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
            ZLog.d(TAG, "数据读取成功 ")
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
            super.onCharacteristicWrite(gatt, characteristic, status)
            dataDispatcher.onCharacteristicWrite(gatt, characteristic, status)
        }

        /**
         * Listen for data on the notify channel
         * A callback when a watch sends a message to a mobile phone.
         */
        override fun onCharacteristicChanged(
            gatt: BluetoothGatt,
            characteristic: BluetoothGattCharacteristic) {
            super.onCharacteristicChanged(gatt, characteristic)
            dataDispatcher.onCharacteristicChanged(characteristic)
        }

        override fun onServicesDiscovered(gatt: BluetoothGatt, status: Int) {
            super.onServicesDiscovered(gatt, status)
            ZLog.d(TAG,"onServicesDiscovered() status=${status} \n currentThread: ${Thread.currentThread().name}")
            bluetoothGatt = gatt
            when (status) {
                BluetoothGatt.GATT_SUCCESS -> {
                    uiHandler.apply {
                        removeMessages(BleMsg.MSG_OF_SCAN_TIMEOUT)

                        val msgDiscover = obtainMessage(BleMsg.MSG_DISCOVER_SUCCESS)
                        sendMessage(msgDiscover)

                        //Delay Stop scan
                        val msg = obtainMessage(BleMsg.MSG_CONNECTED)
                        sendMessageDelayed(msg, 800)
                    }
                }
                else -> {
                    uiHandler.apply {
                        val msgDiscover = obtainMessage(BleMsg.MSG_DISCOVER_FAIL)
                        sendMessage(msgDiscover)
                    }
                }
            }

        }

        override fun onPhyUpdate(gatt: BluetoothGatt?, txPhy: Int, rxPhy: Int, status: Int) {
            ZLog.d(TAG, "onPhyUpdate() ")
            super.onPhyUpdate(gatt, txPhy, rxPhy, status)
        }

        override fun onPhyRead(gatt: BluetoothGatt?, txPhy: Int, rxPhy: Int, status: Int) {
            ZLog.d(TAG, "onPhyRead() ")
            super.onPhyRead(gatt, txPhy, rxPhy, status)
        }

        override fun onMtuChanged(gatt: BluetoothGatt?, mtu: Int, status: Int) {
            mtuIsChanged(mtu, status)
        }

        override fun onReliableWriteCompleted(gatt: BluetoothGatt?, status: Int) {
            ZLog.d(TAG, "onReliableWriteCompleted() ")
            super.onReliableWriteCompleted(gatt, status)
        }

        override fun onDescriptorWrite(gatt: BluetoothGatt?, descriptor: BluetoothGattDescriptor?, status: Int) {
            ZLog.d(TAG, "onDescriptorWrite() ")
            super.onDescriptorWrite(gatt, descriptor, status)
        }

        override fun onDescriptorRead(gatt: BluetoothGatt?, descriptor: BluetoothGattDescriptor?, status: Int) {
            ZLog.d(TAG, "onDescriptorRead() ")
            super.onDescriptorRead(gatt, descriptor, status)
        }

    }

    fun onBleDisconnect(data: Any? = null) {
        ZLog.e(TAG, "[蓝牙已断开] Disconnected from GATT server. ")
        stopScan()
        currentConnectedDeviceMap.remove(selectedDevice?.device?.address)
        selectedDevice = null
        bluetoothGatt?.close() //释放gatt资源
        bluetoothGatt = null
        currentState = STATE_DISCONNECTED
        uiHandler.post {
            bleEventCallback?.onDeviceDisconnected()
        }
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
}