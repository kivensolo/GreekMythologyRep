package com.kingz.mvvm

import android.app.Application
import com.kingz.module.common.BuildConfig
import com.zeke.kangaroo.utils.ZLog

/**
 * @author zeke.wang
 * @date 2020/5/29
 * @maintainer zeke.wang
 * @copyright 2020 www.xgimi.com Inc. All rights reserved.
 * @desc:
 */
class DemoApp : Application() {

    override fun onCreate() {
        super.onCreate()
        ZLog.isDebug = BuildConfig.DEBUG
    }
}