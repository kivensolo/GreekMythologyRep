package com.kingz.coroutines

import com.kingz.module.common.BuildConfig
import com.kingz.module.common.CommonApp
import com.zeke.kangaroo.zlog.ZLog

/**
 * WanAndroid:
 * https://github.com/leiyun1993/WanAndroid
 */
class DemoApp : CommonApp() {

    override fun onCreate() {
        super.onCreate()
        ZLog.init(this,enableLog = BuildConfig.DEBUG)
        ZLog.d("DemoApp ")
    }
}