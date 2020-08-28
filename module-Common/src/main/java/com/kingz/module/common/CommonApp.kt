package com.kingz.module.common

import android.content.Context
import android.os.Handler
import com.alibaba.android.arouter.launcher.ARouter
import com.kingz.database.DatabaseApplication
import com.kingz.module.common.service.GitHubService
import com.kingz.module.common.service.WeatherService
import com.kingz.module.common.utils.crash.NeverCrash
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
        initARouter()
        initApiManager()
        initLog()
        initBugly()
        initCrashCatcher()
    }

    private fun initCrashCatcher() {
        NeverCrash.init(object : NeverCrash.CrashHandler {
            override fun uncaughtException(thread: Thread?, throwable: Throwable?) {
                if (BuildConfig.DEBUG) {
                    ZLog.e(TAG, "Crash", throwable)
                    CrashReport.postCatchedException(throwable)
                }
            }
        }
        )
    }

    private fun initBugly() {
        //  建议在测试阶段建议设置成true，发布时设置为false。
        if (BuildConfig.DEBUG) {
            //第三个参数为SDK调试模式开关，调试模式的行为特性如下：
            //  输出详细的Bugly SDK的Log；
            //  每一条Crash都会被立即上报；
            //  自定义日志将会在Logcat中输出。
            CrashReport.initCrashReport(applicationContext, BUGLY_KEY, false)
            // 自定义参数---设备串号
            CrashReport.putUserData(this, "DeviceId", "宇宙无敌机皇")
            // 自定义参数---设备机型
            CrashReport.setAppChannel(this, "机型信号")
        }
    }


    /**
     * 初始化app网络管理器
     */
    private fun initApiManager() {
        Api.getInstance().registeServer(GitHubService::class.java)
        Api.getInstance().registeServer(WeatherService::class.java)
    }

    private fun initLog() {
         if (BuildConfig.DEBUG) {
            ZLog.isDebug = true
         }
    }

    private fun initARouter() {
        if (BuildConfig.DEBUG) {   // 这两行必须写在init之前，否则这些配置在init过程中将无效
            ARouter.openLog()      // 打印日志

            // 开启调试模式(如果在InstantRun模式下运行，必须开启调试模式！线上版本需要关闭,否则有安全风险)
            ARouter.openDebug()
        // 因为开启InstantRun之后，很多类文件不会放在原本的dex中，需要单独去加载，
        // ARouter默认不会去加载这些文件，因为安全原因，只有在开启了openDebug之后 ARouter才回去加载InstantRun产生的文件
        }
        ARouter.init(this)// 尽可能早，推荐在Application中初始化
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