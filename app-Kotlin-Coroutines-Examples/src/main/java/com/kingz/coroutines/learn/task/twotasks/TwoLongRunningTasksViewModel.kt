package com.kingz.coroutines.learn.task.twotasks

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kingz.base.response.ResponseResult
import com.kingz.coroutines.data.api.IUserAPIFunc
import com.kingz.coroutines.data.local.DatabaseHelper
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class TwoLongRunningTasksViewModel(
    private val apiHelper: IUserAPIFunc,
    private val dbHelper: DatabaseHelper
) : ViewModel() {

    private val status = MutableLiveData<ResponseResult<String>>()

    fun startLongRunningTask() {
        viewModelScope.launch {
            status.postValue(ResponseResult.loading(null))
            try {
                // 异步执行多个task, 并等待他们执行完毕   async{} + await()
                val resultOneDeferred = async { doLongRunningTaskOne() }
                val resultTwoDeferred = async { doLongRunningTaskTwo() }
                val combinedResult = resultOneDeferred.await() + resultTwoDeferred.await()

                status.postValue(ResponseResult.success("Task Completed : $combinedResult"))
            } catch (e: Exception) {
                status.postValue(ResponseResult.error("Something Went Wrong", null))
            }
        }
    }

    fun startLongRunningTaskWithCallBack() {
        viewModelScope.launch {
            status.postValue(ResponseResult.loading(null))
            try {
                // 异步执行多个task, 并等待他们执行完毕   async{} + await()
                val resultOneDeferred = async { doLongRunningTaskOneWithCallback() }
                val resultTwoDeferred = async { doLongRunningTaskTwoWithCallback() }
                val combinedResult = resultOneDeferred.await() + "," + resultTwoDeferred.await()

                status.postValue(ResponseResult.success("Task Completed : $combinedResult"))
            } catch (e: Exception) {
                status.postValue(ResponseResult.error("Something Went Wrong", null))
            }
        }
    }

    fun getStatus(): LiveData<ResponseResult<String>> {
        return status
    }

    /**
     * 不带Callback的异步流程模拟
     */
    private suspend fun doLongRunningTaskOne(): String {
        delay(5000)
        return "One"
    }

    /**
     * 带Callback的异步流程模拟
     */
    private suspend fun doLongRunningTaskOneWithCallback(): String {
        var result = ""
        callback({
            result = "One success"
        },{
            result = "One error"
        })
        return result
    }


    /**
     * 不带Callback的异步流程模拟
     */
    private suspend fun doLongRunningTaskTwo(): String {
        delay(8000)
        return "Two"
    }

    /**
     * 带Callback的异步流程模拟
     */
    private suspend fun doLongRunningTaskTwoWithCallback(): String {
        delay(8000)
        var result = ""
        callback({
            result = " Two success"
        },{
            result = " Two error"
        })
        return result
    }

    /**
     * 模拟网络callback
     */
    private suspend fun callback(onSuccess:()->Unit, onError:()->Unit){
        // 这里是挂起函数的调用，如果是java函数，则仍然是异步执行，，无法挂起。那么，如何执行挂起异步任务呢？？？
        delay(1000)
        onSuccess()
    }
}