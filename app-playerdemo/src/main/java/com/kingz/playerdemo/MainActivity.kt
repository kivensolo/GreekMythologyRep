package com.kingz.playerdemo

import android.content.Context
import android.net.nsd.NsdManager
import android.net.nsd.NsdServiceInfo
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_nsd.*
import java.net.InetAddress

/**
 * NSD客户端分为两个步骤：扫描，解析
 */
class MainActivity : AppCompatActivity() {

    private val TAG: String = "MainActivity"
    private val SERVICE_NAME: String = "KingZ-NsdChat"
    private lateinit var mServiceInfo: NsdServiceInfo
    private lateinit var nsdManager: NsdManager

    // 扫描监听器
    private val discoveryListener = object : NsdManager.DiscoveryListener {

        // 在服务发现开始时调用
        override fun onDiscoveryStarted(regType: String) {
            Log.d(TAG, "Service discovery started -----> ")
        }

        /**
         *  在服务找到时回调返回NsdServiceInfo的对象
         *  里面只有NSD服务器的主机名，要解析后才能得到该主机名的其他数据信息
         */
        override fun onServiceFound(service: NsdServiceInfo) {
            Log.d(TAG, "Service discovery success :: $service")
            when {
                // 服务类型是包含此服务的协议和传输层的字符串
                service.serviceType != "_nsdchat._tcp" ->
                    Log.e(TAG, "Unknown Service Type: ${service.serviceType}")
                service.serviceName == SERVICE_NAME ->
                    Log.d(TAG, "Same machine: ${service.serviceName}")
                service.serviceName.contains("NsdChat") ->
                    // 当应用在网络上找到要连接的服务时
                    // 它必须首先使用 resolveService() 方法确定该服务的连接信息
                    nsdManager.resolveService(service, resolveListener)
            }
        }

        override fun onServiceLost(service: NsdServiceInfo) {
            // When the network service is no longer available.
            // Internal bookkeeping code goes here.
            Log.e(TAG, "service lost: $service")
        }

        override fun onDiscoveryStopped(serviceType: String) {
            Log.i(TAG, "Discovery stopped: $serviceType")
        }

        override fun onStartDiscoveryFailed(serviceType: String, errorCode: Int) {
            Log.e(TAG, "Discovery failed: Error code:$errorCode")
            nsdManager.stopServiceDiscovery(this)
        }

        override fun onStopDiscoveryFailed(serviceType: String, errorCode: Int) {
            Log.e(TAG, "Discovery failed: Error code:$errorCode")
            nsdManager.stopServiceDiscovery(this)
        }
    }

    // 解析监听器
    private val resolveListener = object : NsdManager.ResolveListener {

        override fun onResolveFailed(serviceInfo: NsdServiceInfo, errorCode: Int) {
            // Called when the resolve fails. Use the error code to debug.
            Log.e(TAG, "Resolve failed: $errorCode")
        }

        override fun onServiceResolved(serviceInfo: NsdServiceInfo) {
            Log.e(TAG, "Resolve Succeeded. $serviceInfo")

            if (serviceInfo.serviceName == SERVICE_NAME) {
                Log.d(TAG, "Same IP.")
                return
            }
            mServiceInfo = serviceInfo
            val port: Int = serviceInfo.port
            val host: InetAddress = serviceInfo.host
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nsd)
        nsdManager = getSystemService(Context.NSD_SERVICE) as NsdManager

        // 扫描
        btn_search.setOnClickListener {
            discoverServices()
        }
        // 解析
        btn_connect.setOnClickListener {
            nsdManager.resolveService(mServiceInfo, resolveListener)
        }
    }

    /**
     * 发现NSD服务
     */
    private fun discoverServices() {
        nsdManager.discoverServices(
            "_nsdchat._tcp", //要和NSD服务器端定的ServerType一样
            NsdManager.PROTOCOL_DNS_SD, //固定参数
            discoveryListener           //扫描监听器
        )
    }

}
