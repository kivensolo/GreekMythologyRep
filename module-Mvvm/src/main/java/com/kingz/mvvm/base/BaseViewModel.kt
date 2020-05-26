package com.kingz.mvvm.base

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
abstract class BaseViewModel: ViewModel() {
    /**
     * 统一的Throwable
     * 多个订阅该异常时，请注意
     */
    val exception: MutableLiveData<Throwable> = MutableLiveData()

    fun launchMain(block: suspend CoroutineScope.() -> Unit) {
        viewModelScope.launch(Dispatchers.Main) { block() }
    }

    fun launchIO(block: suspend CoroutineScope.() -> Unit) {
        viewModelScope.launch(Dispatchers.IO) { block() }
    }

    fun launchDefault(block: suspend CoroutineScope.() -> Unit) {
        viewModelScope.launch(Dispatchers.Default) { block() }
    }

    suspend fun switchToIO(block: suspend CoroutineScope.() -> Unit) {
        withContext(Dispatchers.IO) {
            block()
        }
    }

    suspend fun switchToMain(block: suspend CoroutineScope.() -> Unit) {
        withContext(Dispatchers.Main) {
            block()
        }
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