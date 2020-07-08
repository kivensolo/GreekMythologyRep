package com.kingz.base

import android.util.Log
import androidx.lifecycle.Observer

/**
 * @author cx
 */
abstract class BaseVMActivity<V : BaseRepository, T : BaseViewModel<V>> : BaseSimpleActivity() {
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
                        Log.d("coroutine", "开始")
                    }
                    CoroutineState.REFRESH -> {//协程开始&&进度菊花圈
                        Log.d("coroutine", "刷新")
                        //TODO showLoading
                    }
                    CoroutineState.FINISH -> {//协程结束
                        Log.d("coroutine", "结束")
                        //TODO dismissLoading
                    }
                    CoroutineState.ERROR -> {//协程异常
                        Log.e("coroutine", "异常")
                        //TODO dismissLoading
                    }
                }
            }
        })
    }

    abstract fun createViewModel(): T


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