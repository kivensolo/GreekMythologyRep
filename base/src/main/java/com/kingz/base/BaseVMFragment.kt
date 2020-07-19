package com.kingz.base

import android.util.Log
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch

abstract class BaseVMFragment<V : BaseRepository, T : BaseViewModel<V>>
    : BaseSimpleFragment() {

    protected val viewModel: T by lazy {
        createViewModel()
    }

    override fun initViewModel() {
        super.initViewModel()
        initViewModelActions()
    }

    private fun initViewModelActions() {
        viewModel.statusLiveData.observe(this, Observer { status ->
            status?.run {
                when (this) {
                    CoroutineState.START -> {//协程开始
                        Log.d("coroutine-status:", "开始")
                    }
                    CoroutineState.REFRESH -> {//协程开始&&进度菊花圈
                        Log.d("coroutine-status:", "刷新")
                        activity?.run {
                            //TODO showloading
                        }
                    }
                    CoroutineState.FINISH -> {//协程结束
                        Log.d("coroutine-status:", "结束")
                        //TODO Dismiss loading
                    }
                    CoroutineState.ERROR -> {//协程异常
                        Log.d("coroutine-status:", "异常")
                        //TODO Dismiss loading
                    }
                }
            }
        })
    }

    abstract fun createViewModel(): T

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