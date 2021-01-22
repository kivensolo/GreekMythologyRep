package com.kingz.coroutines.demo.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.kingz.module.common.ext.observe
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * @author zeke.wang
 * @date 2020/5/26
 * @maintainer zeke.wang
 */
abstract class BaseActivity : AppCompatActivity() {

    abstract val layoutOfContent: Int
    abstract val binding: ViewDataBinding?

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if(binding != null){
            setContentView(binding!!.root)
        }else{
            setContentView(layoutOfContent)
        }
        setupViewModel()
        initImmersionBar()
        setupView(savedInstanceState)
        initData(savedInstanceState)
    }

   private fun initImmersionBar() {
//        immersionBar {
//            barColor(R.color.colorPrimary)
//        }
    }

    open fun setupViewModel(){

    }

    abstract fun initData(savedInstanceState: Bundle?)

    abstract fun setupView(savedInstanceState: Bundle?)


    /**
     * 必须在组合挂起函数中使用
     * 在开始观察前已主动切换至主线程，避免线程错误
     */
    suspend fun <T> LiveData<T>.observeSuspend(onChanged: (t: T) -> Unit) = withContext(Dispatchers.Main) {
        observe(this@BaseActivity, Observer { onChanged(it) })
    }

    /**
     * 不要求组合挂起函数
     * 在开始观察前已主动切换至主线程，避免线程错误
     */
    suspend fun <T> LiveData<T>.observe(onChanged: (T) -> Unit) = withContext(Dispatchers.Main) {
        observe(this@BaseActivity) { onChanged(it) }
    }

}