package com.zeke.ktx.base

import android.content.Intent
import android.os.Bundle
import com.zeke.ktx.AppLifeCycle

import com.zhy.autolayout.AutoLayoutActivity

/**
 * description: 基类
 */
abstract class BaseActivity : AutoLayoutActivity() {
    private val TAG = BaseActivity::class.java.simpleName

    //  属性声明为private set 代替java代码的 boolean isActivityShow(){ return isShow;}
    var isActivityShow: Boolean = false
        private set

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycle.addObserver(AppLifeCycle(TAG))
        if (!isTaskRoot && intent != null) {
            val intentAction = intent.action
            if (intent.hasCategory(Intent.CATEGORY_LAUNCHER)
                    && intentAction != null
                    && intentAction == Intent.ACTION_MAIN) {
                finish()
            }
        }
    }

    override fun onPause() {
        super.onPause()
        isActivityShow = false
    }

    override fun onResume() {
        super.onResume()
        isActivityShow = true
    }

    //避免getActivity为空,不保存fragment的实例
    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
    }

}
