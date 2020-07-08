package com.kingz.base

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.*

/**
 * @author zeke.wang
 * @date 2020/5/26
 * @maintainer zeke.wang
 * @desc:
 *
 * viewModelScope: 与ViewModel绑定的CoroutineScope
 */
abstract class BaseViewModel<T : BaseRepository> : ViewModel() {

    protected val repository by lazy {
        createRepository()
    }

    abstract fun createRepository(): T

    /**
     * 协程状态管理
     *
     * 开始 CoroutineState.START
     * 刷新 CoroutineState.REFRESH
     * 结束 CoroutineState.FINISH
     * 异常 CoroutineState.ERROR
     */
    val statusLiveData: MutableLiveData<CoroutineState> by lazy {
        MutableLiveData<CoroutineState>()
    }

    /**
     * 统一的Throwable 多个订阅该异常时，请注意
     */
    val exception: MutableLiveData<Throwable> = MutableLiveData()

    fun <T> launch(
        refresh: Boolean = true,
        netBlock: NetBlock<T>,
        error: Error? = null,
        success: Success<T>) {
        viewModelScope.launch {
            try {
                if (refresh) {
                    statusLiveData.value = CoroutineState.REFRESH
                } else {
                    statusLiveData.value = CoroutineState.START
                }
                success(netBlock())
                statusLiveData.value = CoroutineState.FINISH
            } catch (e: Exception) {
                if (error == null) statusLiveData.value = CoroutineState.ERROR else error(e)
            }
        }
    }


    fun launchMain(refresh: Boolean = true, block: BlockCode) {
        viewModelScope.launch(Dispatchers.Main) {
            exeBlock(refresh, block)
        }
    }

    fun launchIO(refresh: Boolean = true, block: BlockCode) {
        viewModelScope.launch(Dispatchers.IO) {
            exeBlock(refresh, block)
        }
    }

    fun launchDefault(refresh: Boolean = true, block: BlockCode) {
        viewModelScope.launch(Dispatchers.Default) {
            exeBlock(refresh, block)
        }
    }

    private suspend fun exeBlock(refresh: Boolean, block: BlockCode) {
        coroutineScope {
            try {
                if (refresh) {
                    statusLiveData.value = CoroutineState.REFRESH
                } else {
                    statusLiveData.value = CoroutineState.START
                }
                block()
                statusLiveData.value = CoroutineState.FINISH
            } catch (e: Exception) {
                statusLiveData.value = CoroutineState.ERROR
                //处理协程异常
            }
        }
    }

    suspend fun switchToIO(block: BlockCode) {
        withContext(Dispatchers.IO) { block() }
    }

    suspend fun switchToMain(block: BlockCode) {
        withContext(Dispatchers.Main) { block()  }
    }

    /**
     * 当这个ViewModel不再使用并将被销毁时，将调用此方法。
     * 当ViewModel观察到一些数据并且需要清除该订阅以防止该ViewModel的泄漏时，它非常有用。
     */
    override fun onCleared() {
        super.onCleared()
        viewModelScope.cancel()
    }
}

/**
 * 协程状态
 */
enum class CoroutineState {
    START,
    REFRESH,
    FINISH,
    ERROR
}
