package com.kingz.coroutines.demo.base

import android.util.Log
import androidx.lifecycle.Observer

/**
 * @author zeke.wang
 * @date 2020/5/26
 * @maintainer zeke.wang
 * @copyright 2020 www.xgimi.com Inc. All rights reserved.
 * @desc:
 */
abstract class BaseVMActivity<VM : BaseViewModel> : BaseActivity() {

    protected abstract val viewModel: VM

    override fun setupViewModel() {
        super.setupViewModel()
//        viewModel = createViewModel()
        initViewModelActions()
    }

    private fun initViewModelActions() {
        viewModel.statusLiveData.observe(this, Observer { status ->
            status?.run {
                when (this) {
                    CoroutineState.START -> {//协程开始
                        Log.d("coroutine", "开始")
                    }
                    CoroutineState.REFRESH -> {//协程开始&&进度菊花圈
                        Log.d("coroutine", "刷新")
                        // ShowLoadding
                    }
                    CoroutineState.FINISH -> {//协程结束
                        Log.d("coroutine", "结束")
                        // Dismiss Loadding
                    }
                    CoroutineState.ERROR -> {//协程异常
                        // Dismiss Loadding
                        Log.d("coroutine", "异常")
                    }
                }
            }
        })
    }

//    abstract fun createViewModel(): VM

    fun launchMain(block: BlockCode) {
        viewModel.launchMain { block() }
    }

    fun launchIO(block: BlockCode) {
        viewModel.launchIO { block() }
    }

    fun launchDefault(block: BlockCode) {
        viewModel.launchDefault { block() }
    }

}