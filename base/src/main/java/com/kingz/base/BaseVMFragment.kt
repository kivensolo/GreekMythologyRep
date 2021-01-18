package com.kingz.base

import android.util.Log
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch

abstract class BaseVMFragment<V : BaseRepository, T : BaseViewModel<V>>
    : BaseSimpleFragment() {

    protected abstract val viewModel: T

    override fun initViewModel() {
        super.initViewModel()
        initViewModelActions()
    }

    private fun initViewModelActions() {
        viewModel.statusLiveData.observe(this, Observer { status ->
            status?.run {
                when (this) {
                    CoroutineState.START -> {//协程开始
                        Log.d("coroutine-status:", "---> START")
                    }
                    CoroutineState.REFRESH -> {//协程开始&&进度菊花圈
                        Log.d("coroutine-status:", "---> REFRESH")
                        activity?.run {
                            //TODO showloading
                        }
                    }
                    CoroutineState.FINISH -> {//协程结束
                        Log.d("coroutine-status:", "<--- FINISH")
                        //TODO Dismiss loading
                    }
                    CoroutineState.ERROR -> {//协程异常
                        Log.d("coroutine-status:", "<--- ERROR")
                        //TODO Dismiss loading
                    }
                }
            }
        })
    }

    fun launch(blockCode: BlockCode) {
        lifecycleScope.launch {
            try {
                blockCode()
            } catch (e: Exception) {
                //异常处理
            }
        }
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