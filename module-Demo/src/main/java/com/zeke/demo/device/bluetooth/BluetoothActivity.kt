package com.starcor.mydemo.page.bluetooth

import android.Manifest
import android.app.Activity
import android.bluetooth.*
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanResult
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.starcor.mydemo.R
import com.starcor.mydemo.adapter.SimpleRecycleViewAdapter
import com.starcor.mydemo.data.ConstFiled.Companion.ACTION_GATT_DISCONNECTED
import com.starcor.mydemo.databinding.ActivityBluetoothBinding
import com.starcor.mydemo.utils.ProtocalUtil
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.*
import kotlin.collections.ArrayList


class BluetoothActivity : AppCompatActivity() {

    private val TAG = javaClass.simpleName
    private val UUID_SERVICE:UUID = UUID.fromString("0000D459-0000-1000-8000-00805F9B34FB")
    private val UUID_WRITE:UUID = UUID.fromString("00000013-0000-1000-8000-00805F9B34FB")
    private val UUID_NOTIFY:UUID = UUID.fromString("00000014-0000-1000-8000-00805F9B34FB")
    private val UUID_INDICATE:UUID = UUID.fromString("00000015-0000-1000-8000-00805F9B34FB")

    private var bluetoothHealth:BluetoothHealth? = null
    private val bluetoothAdapter:BluetoothAdapter? = BluetoothAdapter.getDefaultAdapter()
    lateinit var binding:ActivityBluetoothBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBluetoothBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initPermission()
        initListView()
    }

    // 搜索蓝牙的类型  // 传统蓝牙  BLE
    var searchType:Int = TYPE_TRADITION_BT
    var connectionState:Int = STATE_DISCONNECTED

    var recycleView:RecyclerView? = null
    var adapter:SimpleRecycleViewAdapter<BluetoothDevice>? = null
    private val dataSet: ArrayList<BluetoothDevice> = ArrayList()
    private fun initListView() {
        val listener = object : SimpleRecycleViewAdapter.ISimpleRecycleViewAdapter<BluetoothDevice> {
            override fun onBindViewHolder(
                holder: SimpleRecycleViewAdapter.SimpleViewHolder,
                position: Int,
                data: BluetoothDevice
            ) {
                val container = holder.itemView.findViewById<ViewGroup>(R.id.item_container)
                val deviceName = holder.itemView.findViewById<TextView>(R.id.device_name)
                val deviceAddress = holder.itemView.findViewById<TextView>(R.id.device_address)
                val deviceType = holder.itemView.findViewById<TextView>(R.id.device_type)
                val deviceBondState = holder.itemView.findViewById<TextView>(R.id.device_bond_state)
                deviceName.text = data.name?:"N/A"
                deviceAddress.text = "Mac:${data.address?:"N/A"}"
                deviceType.text = "Type:${data.type}"
                deviceBondState.text = "BondState:${data.bondState}"
                container.setOnClickListener{
                    when(searchType){
                        TYPE_TRADITION_BT ->
                            connectDevice(data)
                        TYPE_LOW_ENERGY_BT -> connectBLEDevice(data)
                    }
                }
            }

        }

        recycleView = binding.recycleView
        recycleView?.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        adapter = SimpleRecycleViewAdapter(
            R.layout.item_simple_recycle_view,
            dataSet,
            listener
        )
        recycleView?.adapter = adapter
    }

    private fun initPermission() {
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) {
            Log.i(TAG, "sdk < 28 Q")
            if (ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                val strings = arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
                ActivityCompat.requestPermissions(this, strings, 1)
            }
        } else {
            if (ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_BACKGROUND_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                val strings = arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_BACKGROUND_LOCATION
                )
                ActivityCompat.requestPermissions(this, strings, 2)
            }
        }

    }

    // region 点击事件
    fun openBlueTooth(view: View){
        if (!isDeviceSupportBluetooth()){
            return
        }
        if (bluetoothAdapter?.isEnabled == false){
            bluetoothAdapter.enable()
        }else{
            showText("蓝牙已连接")
        }
        bluetoothAdapter?.getProfileProxy(this, profileListener, BluetoothProfile.HEALTH)
    }

    fun closeBlueTooth(view: View){
        Log.d(TAG, "closeBlueTooth: ")
        if (!isDeviceSupportBluetooth()){
            return
        }
        adapter?.clear()
        bluetoothAdapter?.cancelDiscovery()
        bluetoothAdapter?.closeProfileProxy(BluetoothProfile.HEALTH, bluetoothHealth)
        if (bluetoothAdapter?.isEnabled == true){
            bluetoothAdapter.disable()
        }
    }

    fun searchBlueTooth(view: View){
        if (!isDeviceSupportBluetooth()){
            return
        }
        searchType = 0
        adapter?.clear()
        if (bluetoothAdapter?.isDiscovering == true) {
            bluetoothAdapter.cancelDiscovery()
        }
        if (bluetoothAdapter?.startDiscovery() == false){
            Log.i(TAG, "searchBlueTooth: 扫描失败")
        }
    }

    var deviceList = ArrayList<BluetoothDevice>()
    fun searchBLE(view: View){
        if (!isDeviceSupportBluetooth()){
            return
        }
        searchType = 1
        adapter?.clear()
        deviceList.clear()
        val callback = BluetoothAdapter.LeScanCallback { device, rssi, scanRecord ->
            if (deviceList.contains(device)){
                return@LeScanCallback
            }
            Log.d(
                TAG,
                "searchBLE: device: name=${device.name}\taddress=${device.address}\tuuids=${device.uuids}"
            )
            adapter?.addData(device)
            deviceList.add(device)
        }
        val callback1 = object : ScanCallback() {
            override fun onScanResult(callbackType: Int, result: ScanResult?) {
                super.onScanResult(callbackType, result)
                val device = result?.device
                var record = result?.scanRecord
                record?.bytes
                Log.d(TAG,"result=${record.toString()}")

                adapter?.addData(device)
            }

            override fun onBatchScanResults(results: MutableList<ScanResult>?) {
                super.onBatchScanResults(results)
                Log.d(TAG, "onBatchScanResults: ")
            }

            override fun onScanFailed(errorCode: Int) {
                super.onScanFailed(errorCode)
                Log.d(TAG, "onScanFailed: ")
            }
        }

        if (bluetoothAdapter?.isDiscovering == true) {
            bluetoothAdapter.cancelDiscovery()
        }
        if (bluetoothAdapter?.isEnabled == false){
            // bluetoothAdapter?.stopLeScan(callback)
            bluetoothAdapter?.bluetoothLeScanner?.stopScan(callback1)
            return
        }

        GlobalScope.launch {
            delay(3000)
            bluetoothAdapter?.bluetoothLeScanner?.stopScan(callback1)
        }
        bluetoothAdapter?.bluetoothLeScanner?.startScan(callback1)
    }

    fun searchPairedBlueTooth(view: View){
        Log.d(TAG, "searchPairedBlueTooth: ")
        if (!isDeviceSupportBluetooth()){
            return
        }
        adapter?.clear()
        val pairedDevices:Set<BluetoothDevice>? = bluetoothAdapter?.bondedDevices
        pairedDevices?.forEach{
            val deviceName = it.name
            val deviceHardwareAddress = it.address
            Log.d(TAG, "searchBlueTooth: deviceName=$deviceName   Mac=$deviceHardwareAddress")
        }
    }

    // 通过蓝牙暴露此设备
    fun exposeBlueTooth(view: View){
        val discoverableIntent: Intent = Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE)
        discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300)
        startActivity(discoverableIntent)
    }

    // 绑定设备
    fun bindDevice(view: View){
        Log.d(TAG, "bindDevice: ")
        val service = bluetoothGatt?.getService(UUID.fromString("0000D459-0000-1000-8000-00805F9B34FB"))
        val characteristicWrite = service?.getCharacteristic(UUID.fromString("00000013-0000-1000-8000-00805F9B34FB"))
        val requestData = ProtocalUtil.getBindRequest(
            0x00,
            System.currentTimeMillis()/100,
            "18867115506"
        )
        requestData.forEach {
            characteristicWrite?.value = it
            bluetoothGatt?.writeCharacteristic(characteristicWrite)
        }

    }

    // 超级绑定
    fun exBindDevice(view: View){
        Log.d(TAG, "exBindDevice: ")
        writeCharacter?.value = ProtocalUtil.getSuperBindRequest(
            0x00,
            System.currentTimeMillis()/100,
            ""
        )[0]
        bluetoothGatt?.writeCharacteristic(writeCharacter)
    }

    // 解绑定设备
    fun unBindDevice(view: View){
        Log.d(TAG, "unBindDevice: ")
    }

    // 执行设备指令
    fun executeDeviceCommand(view: View){
        Log.d(TAG, "executeDeviceCommand: ")

    }
    // endregion

    private var bluetoothGatt:BluetoothGatt? = null
    var writeCharacter: BluetoothGattCharacteristic? = null
    var readCharacter: BluetoothGattCharacteristic? = null

    // 设备连接
    fun connectDevice(bluetoothDevice: BluetoothDevice){
        // 连接前关闭设备发现
        bluetoothAdapter?.cancelDiscovery()

        GlobalScope.launch {
            // bluetoothSocket = bluetoothDevice.createRfcommSocketToServiceRecord(uuid)
            // bluetoothSocket?.connect()
        }
    }

    // 设备连接
    fun connectBLEDevice(bluetoothDevice: BluetoothDevice){
        bluetoothAdapter?.bluetoothLeScanner?.stopScan(object : ScanCallback() {
            override fun onScanResult(callbackType: Int, result: ScanResult?) {
                super.onScanResult(callbackType, result)
                Log.d(TAG, "onScanResult: close")
            }

            override fun onBatchScanResults(results: MutableList<ScanResult>?) {
                super.onBatchScanResults(results)
                Log.d(TAG, "onBatchScanResults: ")
            }

            override fun onScanFailed(errorCode: Int) {
                super.onScanFailed(errorCode)
                Log.d(TAG, "onScanFailed: ")
            }
        })
        bluetoothAdapter?.cancelDiscovery()
        bluetoothGatt = bluetoothDevice.connectGatt(
            this,
            false,
            object : BluetoothGattCallback() {
                override fun onConnectionStateChange(
                    gatt: BluetoothGatt?,
                    status: Int,
                    newState: Int
                ) {
                    super.onConnectionStateChange(gatt, status, newState)
                    println("current state:$newState")
                    val intentAction: String
                    when (newState) {
                        BluetoothProfile.STATE_CONNECTED -> {
                            editViewText(binding.btState, "连接成功")
                            connectionState = STATE_CONNECTED
                            Log.i(TAG, "Connected to GATT server.")
                            Log.i(
                                TAG, "Attempting to start service discovery: " +
                                        gatt?.discoverServices()
                            )
                            runOnUiThread {
                                binding.deviceContainer.visibility = View.VISIBLE
                            }
                        }
                        BluetoothProfile.STATE_CONNECTING -> {
                            editViewText(binding.btState, "连接中")
                            Log.i(TAG, "Connecting from GATT server.")
                        }
                        BluetoothProfile.STATE_DISCONNECTED -> {
                            editViewText(binding.btState, "连接断开")
                            intentAction = ACTION_GATT_DISCONNECTED
                            connectionState = STATE_DISCONNECTED
                            Log.i(TAG, "Disconnected from GATT server.")
                            runOnUiThread {
                                binding.deviceContainer.visibility = View.GONE
                            }
                        }
                        BluetoothProfile.STATE_DISCONNECTING -> {
                            editViewText(binding.btState, "连接断开中")
                            Log.i(TAG, "Disconnecting from GATT server.")
                        }
                    }
                }

                override fun onServicesDiscovered(gatt: BluetoothGatt?, status: Int) {
                    super.onServicesDiscovered(gatt, status)


                    val characteristicNotify =
                        gatt?.getService(UUID_SERVICE)?.getCharacteristic(UUID_NOTIFY)
                    val characteristicRead1 =
                        gatt?.getService(UUID_SERVICE)?.getCharacteristic(UUID_INDICATE)

                    if (!enableNotification(true, characteristicNotify)) {
                        Log.e(TAG, "onServicesDiscovered: enable false")
                    }

                    if (bluetoothGatt?.readCharacteristic(characteristicRead1) == false) {
                        Log.e(TAG, "onServicesDiscovered: read2 false")
                    }

                    when (status) {
                        BluetoothGatt.GATT_SUCCESS -> {
                            val serviceList = gatt?.services
                            serviceList?.forEach { service ->
                                if (UUID_SERVICE.toString().equals(service.uuid)) {
                                    val characterList = service.characteristics
                                    characterList.forEach { character ->
                                        if (UUID_WRITE.toString().equals(character.uuid)) {
                                            writeCharacter = character
                                            Log.d(
                                                TAG, "onServicesDiscovered(writeCharacter):" +
                                                        """serviceUUID=${service.uuid}  serviceType=${service.type}
                                            characterUuid=${character.uuid}  characterValue=${character.value}"""
                                            )
                                        } else if (UUID_INDICATE.toString().equals(character.uuid)
                                        ) {
                                            readCharacter = character
                                            Log.d(
                                                TAG, "onServicesDiscovered(readCharacter):" +
                                                        """serviceUUID=${service.uuid}  serviceType=${service.type}
                                            characterUuid=${character.uuid}  characterValue=${character.value}"""
                                            )
                                        }
                                    }
                                }
                            }
                        }
                        else -> Log.w(TAG, "onServicesDiscovered received: $status")
                    }
                }

                override fun onCharacteristicWrite(
                    gatt: BluetoothGatt?,
                    characteristic: BluetoothGattCharacteristic?,
                    status: Int
                ) {
                    super.onCharacteristicWrite(gatt, characteristic, status)
                    Log.d(TAG, "onCharacteristicWrite: ")
                }

                override fun onCharacteristicRead(
                    gatt: BluetoothGatt?,
                    characteristic: BluetoothGattCharacteristic?,
                    status: Int
                ) {
                    super.onCharacteristicRead(gatt, characteristic, status)
                    Log.d(
                        TAG, "onCharacteristicRead: characteristic=$characteristic"
                    )
                    when (status) {
                        BluetoothGatt.GATT_SUCCESS -> {
                            // broadcastUpdate(ACTION_DATA_AVAILABLE, characteristic)
                        }
                    }
                }

                /**
                 * 监听notify通道数据
                 */
                override fun onCharacteristicChanged(
                    gatt: BluetoothGatt?,
                    characteristic: BluetoothGattCharacteristic?
                ) {
                    super.onCharacteristicChanged(gatt, characteristic)
                    Log.d(TAG, "onCharacteristicChanged: ")
                    Log.e(TAG, "命令： ${characteristic?.value}")
                    val values = characteristic?.value

                }
            })
    }

    // 开启Notification通道
    private fun enableNotification(
        enable: Boolean,
        characteristic: BluetoothGattCharacteristic?
    ): Boolean {
        if (bluetoothGatt == null || characteristic == null) return false
        if (bluetoothGatt?.setCharacteristicNotification(characteristic, enable) == false) return false
        val clientConfig =
            characteristic.getDescriptor(UUID.fromString("00002902-0000-1000-8000-00805f9b34fb"))
                ?: return false
        if (enable) {
            clientConfig.value = BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE
        } else {
            clientConfig.value = BluetoothGattDescriptor.DISABLE_NOTIFICATION_VALUE
        }
        return bluetoothGatt?.writeDescriptor(clientConfig)?:false
    }


    private fun editViewText(textView: TextView, text: String) {
        runOnUiThread { textView.text = text }
    }

    private fun isDeviceSupportBluetooth():Boolean{
        if (bluetoothAdapter == null){
            Log.e(TAG, "isDeviceSupportBluetooth: this device can not use bluetooth")
            return false
        }
        return true
    }

    private val profileListener = object : BluetoothProfile.ServiceListener{
        override fun onServiceConnected(profile: Int, proxy: BluetoothProfile?) {
            Log.d(TAG, "onServiceConnected: profile=$profile  proxy=$proxy")
            if (profile == BluetoothProfile.HEALTH){
                bluetoothHealth = proxy as BluetoothHealth
            }
        }

        override fun onServiceDisconnected(profile: Int) {
            Log.d(TAG, "onServiceDisconnected: ")
            if (profile == BluetoothProfile.HEALTH){
                bluetoothHealth = null
            }
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_ENABLE_BT){
            if (resultCode == Activity.RESULT_OK){
                Log.i(TAG, "onActivityResult: 启动蓝牙成功")
            }else {
                Log.i(TAG, "onActivityResult: 启动蓝牙失败")
            }
        }
        if (requestCode == REQUEST_DISENABLE_BT){
            if (resultCode == Activity.RESULT_OK){
                Log.i(TAG, "onActivityResult: 关闭蓝牙成功")
            }else {
                Log.i(TAG, "onActivityResult: 关闭蓝牙失败")
            }
        }

    }

    private fun showText(text: String){
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
    }

    companion object {
        private const val REQUEST_ENABLE_BT = 0x1000
        private const val REQUEST_DISENABLE_BT = 0x1001

        private const val TYPE_TRADITION_BT = 0
        private const val TYPE_LOW_ENERGY_BT = 1

        private const val STATE_DISCONNECTED = 0
        private const val STATE_CONNECTING = 1
        private const val STATE_CONNECTED = 2


    }
}



