package com.kingz.base

import androidx.lifecycle.lifecycleScope
import com.zeke.reactivehttp.base.BaseReactiveViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

abstract class BaseVMFragment< T : BaseReactiveViewModel>
    : BaseLazyFragment() {

    protected abstract val viewModel: T

    override fun initViewModel() {
        super.initViewModel()
//        initViewModelActions()
    }

//    private fun initViewModelActions() {
//        viewModel.statusLiveData.observe(this, Observer { status ->
//            status?.run {
//                when (this) {
//                    CoroutineState.START -> {//协程开始
//                        Log.d("coroutine-status:", "---> START")
//                    }
//                    CoroutineState.REFRESH -> {//协程开始&&进度菊花圈
//                        Log.d("coroutine-status:", "---> REFRESH")
//                        activity?.run {}
//                    }
//                    CoroutineState.FINISH -> {//协程结束
//                        Log.d("coroutine-status:", "<--- FINISH")
//                    }
//                    CoroutineState.ERROR -> {//协程异常
//                        Log.d("coroutine-status:", "<--- ERROR")
//                    }
//                }
//            }
//        })
//    }

    fun launch(blockCode: suspend CoroutineScope.() -> Unit) {
        lifecycleScope.launch {
            try {
                blockCode()
            } catch (e: Exception) {
                //异常处理
            }
        }
    }

    fun launchMain(block: suspend CoroutineScope.() -> Unit) {
        viewModel.launchMain { block() }
    }

    fun launchIO(block: suspend CoroutineScope.() -> Unit) {
        viewModel.launchIO { block() }
    }

    fun launchDefault(block: suspend CoroutineScope.() -> Unit) {
        viewModel.launchCPU { block() }
    }
}