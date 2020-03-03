package com.zeke.ktx

import android.app.Activity
import android.app.Application
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.StrictMode
import android.support.multidex.MultiDexApplication
import android.util.Log
import com.core.logic.GlobalCacheCenter
import com.github.xulcache.CacheCenter
import com.github.xulcache.CacheDomain
import com.kingz.customdemo.BuildConfig
import com.takt.FpsTools
import com.zeke.kangaroo.utils.AppInfoUtils
import com.zeke.kangaroo.utils.ZLog
import com.zhy.autolayout.config.AutoLayoutConifg
import java.io.InputStream
import java.util.concurrent.TimeUnit


/**
 * author: King.Z
 * date:  2016/5/10 23:25
 * Rfactor in 2020/2/15 by kotlin.
 */
class App : MultiDexApplication() {
    private var lifecycleHandler: LifecycleHandler? = null

    val TAG = "Application"
    private var _appMainHandler: Handler? = null
    private val STRICT_MODE = false


    // 全局缓存设置
    private val CACHE_DOMAIN_ID_APP = 0x1001
    private val CACHE_MAX_SIZE = 128 * 1024 * 1024L    // 最大保存128M
    private val CACHE_LIFETIME = TimeUnit.DAYS.toMillis(7)    // 最多保存7天
    protected var _appCacheDomain: CacheDomain? = null


    val BDApiKey = "467e6d8f8b06b8811b7a6fb939c8ad5e"

    companion object {
        @JvmField var instance: App? = null
        @JvmField var SCREEN_WIDTH = 1280
        @JvmField var SCREEN_HEIGHT = 720
    }

    val aliveAcNum: Int
        get() = lifecycleHandler!!.aliveActivityNum

    override fun onCreate() {
        super.onCreate()
        instance = this
        _appMainHandler = Handler(mainLooper)
        initCacheCenter()
        initAPPScreenParms()
        init()
        initLog()
//        initFpsDebugView()
        initStrictListenner()
        //        STCFrameChecker.getInstance()
//                .setLogFileSize(50)
//                .setFrameThreshold(2)
//                .start();
        //Bmob.initialize(this, "fea19b87f0795833b30de91f46f1465c");
    }

    private fun init() {
        lifecycleHandler = LifecycleHandler()
        registerActivityLifecycleCallbacks(lifecycleHandler)
        AutoLayoutConifg.getInstance()
                .useDeviceSize()
                .init(applicationContext)
    }

    private fun initFpsDebugView() {
        //Takt.stock(this).size(20f).play();
        FpsTools.init(this)
                .size(30f)
                .interval(2000)
                .color(Color.WHITE)
                .play()
    }

    private fun initAPPScreenParms() {
        Log.i(TAG, "SCREEN_WIDTH = " + resources.displayMetrics.widthPixels
                + "   SCREEN_HEIGHT = " + resources.displayMetrics.heightPixels)
        SCREEN_WIDTH = resources.displayMetrics.widthPixels
        SCREEN_HEIGHT = resources.displayMetrics.heightPixels
    }


    private fun initStrictListenner() {
        @Suppress("ConstantConditionIf")
        if (STRICT_MODE) {
            //https://blog.csdn.net/meegomeego/article/details/45746721
            //https://www.jianshu.com/p/113b9c54b5d1
            //设置StrictMode监听那些潜在问题，出现问题时可以对屏幕闪红色，也可以输出错误日志
            //设置线程方面的策略
            // 主要用于发现在UI线程中是否有读写磁盘的操作，是否有网络操作，
            // 以及检查UI线程中调用的自定义代码是否执行得比较慢。
            StrictMode.setThreadPolicy(StrictMode.ThreadPolicy.Builder()
                    .detectDiskReads()          //磁盘读写检查
                    .detectDiskWrites()
                    .detectCustomSlowCalls()    //帮助开发者发现UI线程调用的那些方法执行得比较慢
                    .detectNetwork()            // or .detectAll() for all detectable problems
                    .penaltyLog()               //在Logcat 中打印违规异常信息
                    .build())
            //设置VM方面的策略
            //主要用于发现内存问题，比如 Activity内存泄露， SQL 对象内存泄露，
            //资源未释放，能够限定某个类的最大对象数。
            StrictMode.setVmPolicy(StrictMode.VmPolicy.Builder()
                    .detectLeakedSqlLiteObjects()
                    .detectLeakedClosableObjects()
                    .penaltyLog()
                    .penaltyDeath()  //当触发违规条件时，直接Crash掉当前应用程序
                    .build())
        }
    }

    private fun initLog() {
        if (BuildConfig.DEBUG) {
            ZLog.isDebug = true
        }
    }


    fun hasActivities(): Boolean {
        return lifecycleHandler!!.visibleActivityNum > 0
    }


    override fun onTerminate() {
        //Takt.finish()
        FpsTools.finish()
        super.onTerminate()
    }

    fun postToMainLooper(runnable: Runnable) {
        _appMainHandler?.post(runnable)
    }

    fun postDelayToMainLooper(runnable: Runnable, ms: Long) {
        _appMainHandler?.postDelayed(runnable, ms)
    }

    // --------------------全局缓存---------------------- Start
    private fun initCacheCenter() {
        GlobalCacheCenter.getInstance().init(applicationContext)

        CacheCenter.setRevision(AppInfoUtils.getAppVersion(instance?.applicationContext,BuildConfig.APPLICATION_ID))
        CacheCenter.setRevision(1)
        CacheCenter.setVersion("test_version")

        _appCacheDomain = CacheCenter.buildCacheDomain(CACHE_DOMAIN_ID_APP,instance?.applicationContext)
                .setDomainFlags(CacheCenter.CACHE_FLAG_FILE
                        or CacheCenter.CACHE_FLAG_REVISION_LOCAL)
                .setLifeTime(CACHE_LIFETIME)
                .setMaxFileSize(CACHE_MAX_SIZE)
                .build()
    }

    /**
     * 缓存app全局数据
     */
    fun storeCachedData(path: String, stream: InputStream): Boolean {
        _appCacheDomain?.put(path, stream)
        return true
    }

    /**
     * 根据指定key,获取app全局缓存数据
     */
    fun loadCachedData(path: String): InputStream {
        return _appCacheDomain!!.getAsStream(path)
    }
    // --------------------全局缓存---------------------- End



    /**
     * 自己监测生命周期，辅助判断应用是否在前台
     */
    @Deprecated("")
    internal inner class LifecycleHandler : Application.ActivityLifecycleCallbacks {
        var visibleActivityNum = 0
            private set
        var aliveActivityNum = 0
            private set

        override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
            aliveActivityNum++
        }

        override fun onActivityStarted(activity: Activity) {
            visibleActivityNum++
        }

        override fun onActivityResumed(activity: Activity) {}

        override fun onActivityPaused(activity: Activity) {}

        override fun onActivityStopped(activity: Activity) {
            visibleActivityNum--
        }

        override fun onActivityDestroyed(activity: Activity) {
            aliveActivityNum--
        }

        override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {}
    }
}
