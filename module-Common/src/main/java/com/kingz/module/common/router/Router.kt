package com.kingz.module.common.router

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.TextUtils
import com.alibaba.android.arouter.launcher.ARouter
import com.kingz.module.common.R
import com.zeke.kangaroo.zlog.ZLog


/**
 * ARouter跳转工具类
 */
object Router {
    fun startActivity(url: String) {
        if (TextUtils.isEmpty(url)) return
        ZLog.d("ARouter startActivity by router: $url")
        // Simple jump within application (Jump via URL in 'Advanced usage')
        ARouter.getInstance().build(url).navigation()
    }

    @SuppressLint("ObsoleteSdkInt")
    fun startActivity(url: String, bundle: Bundle, anim:Boolean = true) {
        if (TextUtils.isEmpty(url)) return
        val postCard = ARouter.getInstance().build(url)
        if(anim){
            postCard.withTransition(R.anim.zoom_enter,R.anim.zoom_exit)
        }
        postCard.with(bundle).navigation()
    }

    fun startActivityWithGreen(url: String) {
        if (TextUtils.isEmpty(url)) return
        ARouter.getInstance().build(url).greenChannel().navigation()
    }
}