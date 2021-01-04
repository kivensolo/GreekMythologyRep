package com.kingz.base

import android.util.Log
import androidx.lifecycle.Observer

abstract class BaseVMActivity<BR : BaseRepository,
        VM : BaseViewModel<BR>> : BaseSimpleActivity() {
//    protected val viewModel: VM by lazy {
//        createViewModel()
//    }
//    abstract fun createViewModel(): VM

    protected abstract val viewModel: VM


    override fun initViewModel() {
        super.initViewModel()
        initViewModelStatusActions()
    }

    private fun initViewModelStatusActions() {
        viewModel.statusLiveData.observe(this, Observer { status ->
            status?.run {
                when (this) {
                    CoroutineState.START -> {//协程开始
                        Log.d("Coroutine-status:", "START")
                    }
                    CoroutineState.REFRESH -> {//协程开始&&进度菊花圈
                        Log.d("Coroutine-status:", "REFRESH")
                        showLoading()
                    }
                    CoroutineState.FINISH -> {//协程结束
                        Log.d("Coroutine-status:", "FINISHED")
                        dismissLoading()
                    }
                    CoroutineState.ERROR -> {//协程异常
                        Log.e("Coroutine-status:", "ERROR")
                        dismissLoading()
                    }
                }
            }
        })
    }

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