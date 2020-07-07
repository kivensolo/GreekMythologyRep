package com.kingz.module.common

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.kingz.module.common.ext.observe
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * description: 基类
 * TODO 抽离出Base模块
 */
// abstract class BaseActivity : AutoLayoutActivity() {
abstract class BaseActivity : AppCompatActivity() {
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


    /**
     * 必须在组合挂起函数中使用
     * 在开始观察前已主动切换至主线程，避免线程错误
     */
    suspend fun <T> LiveData<T>.observeSuspend(
        onChanged: (t: T) -> Unit) = withContext(Dispatchers.Main) {
        observe(this@BaseActivity, Observer { onChanged(it) })
    }

    /**
     * 不要求组合挂起函数
     * 在开始观察前已主动切换至主线程，避免线程错误
     */
    suspend fun <T> LiveData<T>.observe(
        onChanged: (T) -> Unit) = withContext(Dispatchers.Main) {
        observe(this@BaseActivity) { onChanged(it) }
    }
}
