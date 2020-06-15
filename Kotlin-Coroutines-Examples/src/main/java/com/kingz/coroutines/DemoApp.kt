package com.kingz.coroutines

import com.kingz.module.common.BuildConfig
import com.kingz.module.common.CommonApp
import com.zeke.kangaroo.utils.ZLog

class DemoApp : CommonApp() {

    override fun onCreate() {
        super.onCreate()
        ZLog.isDebug = BuildConfig.DEBUG
    }
}