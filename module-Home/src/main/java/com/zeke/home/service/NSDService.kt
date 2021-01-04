package com.zeke.home.service

import android.content.Context
import android.net.nsd.NsdManager
import android.net.nsd.NsdServiceInfo
import com.zeke.kangaroo.utils.ZLog

/**
 * Copyright (c) 2021, 北京视达科科技有限责任公司 All rights reserved.
 * author：ZekeWang
 * date：2021/1/4
 * description：NSD-DemoCode
 *
 */
class NSDService {
    private var mServiceName: String? = null
    private var nsdManager: NsdManager? = null

    private val registrationListener = object : NsdManager.RegistrationListener {

        override fun onServiceRegistered(NsdServiceInfo: NsdServiceInfo) {
            // Save the service name. Android may have changed it in order to
            // resolve a conflict, so update the name you initially requested
            // with the name Android actually used.
            mServiceName = NsdServiceInfo.serviceName
        }

        override fun onRegistrationFailed(serviceInfo: NsdServiceInfo, errorCode: Int) {
            // Registration failed! Put debugging code here to determine why.
            ZLog.e("onRegistrationFailed errorCode=$errorCode")
        }

        override fun onServiceUnregistered(info: NsdServiceInfo) {
            // Service has been unregistered. This only happens when you call
            // NsdManager.unregisterService() and pass in this listener.
        }

        override fun onUnregistrationFailed(serviceInfo: NsdServiceInfo, errorCode: Int) {
            // Unregistration failed. Put debugging code here to determine why.
            ZLog.e("onUnregistrationFailed errorCode=$errorCode")
        }
    }

    fun init(ctx: Context){
        nsdManager = (ctx.getSystemService(Context.NSD_SERVICE) as NsdManager)
        registerService(5555)
    }

    /**
     * 注册Nsd服务
     * 参数1 ：
     * 服务名称设置为“NsdChat”。
     * 此服务名称是实例名称：
     *  它是对网络上其他设备可见的名称。
     *  网络上使用 NSD 查找本地服务的任何设备都可以看到该名称。
     *  请记住，网络上任何服务的名称都必须是唯一的，并且 Android 会自动处理冲突解决。
     *  如果网络中的两台设备都安装了 NsdChat 应用，则其中一台设备会自动更改服务名称，
     *  如更改为“NsdChat (1)”之类的。
     *
     *  参数2：设置服务类型，指定应用使用的协议和传输层。
     *  语法为“_<protocol>._<transportlayer>”
     */
    private fun registerService(port: Int) {
        // Create the NsdServiceInfo object, and populate it.
        val serviceInfo = NsdServiceInfo().apply {
            // The name is subject to change based on conflicts
            // with other services advertised on the same network.
            serviceName = "NsdChat"
            serviceType = "_nsdchat._tcp"
            setPort(port)
        }
        nsdManager?.apply {
                registerService(
                    serviceInfo,
                    NsdManager.PROTOCOL_DNS_SD,
                    registrationListener
                )
            }
    }
}