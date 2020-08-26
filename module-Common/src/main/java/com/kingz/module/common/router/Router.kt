package com.kingz.module.common.router

import android.os.Bundle
import android.text.TextUtils
import com.alibaba.android.arouter.launcher.ARouter

/**
 * ARouter跳转工具类
 */
object Router {
    fun startActivity(url: String) {
        if (TextUtils.isEmpty(url)) return
        // Simple jump within application (Jump via URL in 'Advanced usage')
        ARouter.getInstance().build(url).navigation()
    }

    fun startActivity(url: String, bundle: Bundle) {
        if (TextUtils.isEmpty(url)) return
        // Jump with parameters
        ARouter.getInstance().build(url).with(bundle).navigation()
    }

    fun startActivityWithGreen(url: String) {
        if (TextUtils.isEmpty(url)) return
        ARouter.getInstance().build(url).greenChannel().navigation()
    }
}