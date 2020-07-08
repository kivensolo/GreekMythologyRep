package com.kingz.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

abstract class BaseSimpleActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getContentView())
        initViewModel()
        initImmersionBar()
        initView(savedInstanceState)
        initData(savedInstanceState)
    }

    private fun initImmersionBar() {
//        immersionBar {
//            barColor(R.color.colorPrimary)
//        }
    }

    open fun initViewModel(){

    }

    abstract fun getContentView(): Int

    abstract fun initData(savedInstanceState: Bundle?)

    abstract fun initView(savedInstanceState: Bundle?)


    /**
     * 必须在组合挂起函数中使用
     * 在开始观察前已主动切换至主线程，避免线程错误
     */
    suspend fun <T> LiveData<T>.observeSuspend(onChanged: (t: T) -> Unit) = withContext(Dispatchers.Main) {
        observe(this@BaseSimpleActivity, Observer { onChanged(it) })
    }
}