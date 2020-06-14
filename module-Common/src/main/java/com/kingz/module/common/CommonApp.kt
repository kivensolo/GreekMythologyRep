package com.kingz.module.common

import android.os.Handler
import com.kingz.database.DatabaseApplication
import com.kingz.module.common.service.GitHubService
import com.kingz.module.common.service.WeatherService
import com.zeke.kangaroo.utils.ZLog
import com.zeke.network.retrofit.mannager.ApiManager

/**
 * author: King.Z <br>
 * date:  2020/5/17 22:02 <br>
 * description:  <br>
 */
open class CommonApp: DatabaseApplication(){

    private var _appMainHandler: Handler? = null

    companion object {

        private const val TAG = "CommonApp"

        private var INSTANCE: CommonApp? = null

        fun getInstance(): CommonApp {
            return INSTANCE!!
        }
    }


    override fun onCreate() {
        super.onCreate()
        INSTANCE = this
//        _appMainHandler = Handler(mainLooper)
        initApiManager()
        initLog()
    }


    /**
     * 初始化app网络管理器
     */
    private fun initApiManager() {
        ApiManager.i().registeServer(GitHubService::class.java)
        ApiManager.i().registeServer(WeatherService::class.java)
    }

    private fun initLog() {
        // if (BuildConfig.DEBUG) {
            ZLog.isDebug = true
        // }
    }

    private fun initAutoLayout(){
//        AutoLayoutConifg.getInstance()
//                .useDeviceSize()
//                .init(applicationContext)
    }


    fun postToMainLooper(runnable: Runnable) {
        _appMainHandler?.post(runnable)
    }

    fun postDelayToMainLooper(runnable: Runnable, ms: Long) {
        _appMainHandler?.postDelayed(runnable, ms)
    }

}