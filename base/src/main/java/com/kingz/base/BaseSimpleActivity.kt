package com.kingz.base

import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.gyf.immersionbar.ImmersionBar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Top level abstract activity.
 * [MVVM]
 */
abstract class BaseSimpleActivity : AppCompatActivity() {

    //优化思路: 全局单例weakRefebceDialog,显示的地方直接show,隐藏的地方直接dismiss
    private var progress: View? = null
    var INVALID_LAYOUT_ID:Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initContentView()
        initViewModel()
        val actionBar = supportActionBar
        actionBar?.setDisplayShowTitleEnabled(false)
        actionBar?.setDisplayHomeAsUpEnabled(true)
        initImmersionBar()
        initView(savedInstanceState)
        initData(savedInstanceState)
    }

    private fun initContentView() {
        val contentLayout = getContentLayout()
        if (contentLayout != INVALID_LAYOUT_ID) {
            setContentView(contentLayout)
        } else {
            setContentView(getContentView())
        }
    }

    /**
     * Get layout id.
     */
    abstract fun getContentLayout(): Int

    open fun getContentView():View? {return null}

    /**
     * Init immersion style bar.
     */
    open fun initImmersionBar() {
        ImmersionBar.with(this)
             //如果当前设备支持状态栏字体变色，会设置状态栏字体为黑色
            .statusBarDarkFont(true)
            .init()
    }

    /**
     * Init viewmodel
     */
    open fun initViewModel() {}

    /**
     * Init data logic.
     */
    abstract fun initData(savedInstanceState: Bundle?)

    /**
     * Init View.
     * 默认会添加一个Progress
     */
    open fun initView(savedInstanceState: Bundle?) {
        progress = ProgressBar(baseContext)
        val progressParams = FrameLayout.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        progressParams.gravity = Gravity.CENTER
        progress?.visibility = View.GONE
        addContentView(progress, progressParams)
    }

    fun showLoading() {
        progress?.let {
            if (!it.isShown) {
                it.visibility = View.VISIBLE
            }
        }
    }

    fun dismissLoading() {
        progress?.let {
            if (it.isShown) {
                it.visibility = View.GONE
            }
        }
    }


    fun showToast(msg: String) {
        if (msg.isNotBlank()) {
            Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        dismissLoading()
    }

    /**
     * 必须在组合挂起函数中使用
     * 在开始观察前已主动切换至主线程，避免线程错误
     */
    suspend fun <T> LiveData<T>.observeSuspend(onChanged: (t: T) -> Unit) =
        withContext(Dispatchers.Main) {
            observe(this@BaseSimpleActivity, Observer { onChanged(it) })
        }

     /**
     * 不要求组合挂起函数
     * 在开始观察前已主动切换至主线程，避免线程错误
     */
    suspend fun <T> LiveData<T>.observe(onChanged: (T) -> Unit) =
        withContext(Dispatchers.Main) {
            observe(this@BaseSimpleActivity, Observer { onChanged(it) })
        }
}