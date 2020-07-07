package com.kingz.module.common

import android.content.Context
import android.os.Handler
import com.kingz.database.DatabaseApplication
import com.kingz.module.common.service.GitHubService
import com.kingz.module.common.service.WeatherService
import com.tencent.bugly.crashreport.CrashReport
import com.zeke.kangaroo.utils.ZLog
import com.zeke.network.retrofit.mannager.Api

/**
 * author: King.Z <br>
 * date:  2020/5/17 22:02 <br>
 * description:  <br>
 */
open class CommonApp: DatabaseApplication(){

    private var _appMainHandler: Handler? = null

    companion object {

        private const val TAG = "CommonApp"
        private const val BUGLY_KEY = "34a3209dd6"

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
        initBugly()
    }

    private fun initBugly() {
    //第三个参数为SDK调试模式开关，调试模式的行为特性如下：
    //  输出详细的Bugly SDK的Log；
    //  每一条Crash都会被立即上报；
    //  自定义日志将会在Logcat中输出。
    //  建议在测试阶段建议设置成true，发布时设置为false。
        CrashReport.initCrashReport(applicationContext, BUGLY_KEY, false)
    }


    /**
     * 初始化app网络管理器
     */
    private fun initApiManager() {
        Api.i().registeServer(GitHubService::class.java)
        Api.i().registeServer(WeatherService::class.java)
    }

    private fun initLog() {
         if (BuildConfig.DEBUG) {
            ZLog.isDebug = true
         }
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

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        // 主动加载非主dex
//        MultiDex.install(this)
    }

}