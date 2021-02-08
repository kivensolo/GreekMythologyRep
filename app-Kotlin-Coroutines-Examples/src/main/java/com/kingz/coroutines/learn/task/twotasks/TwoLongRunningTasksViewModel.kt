package com.mindorks.example.coroutines.learn.task.twotasks

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kingz.base.response.ResponseResult
import com.kingz.coroutines.data.api.ApiHelper
import com.kingz.coroutines.data.local.DatabaseHelper
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class TwoLongRunningTasksViewModel(
    private val apiHelper: ApiHelper,
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

    fun getStatus(): LiveData<ResponseResult<String>> {
        return status
    }

    private suspend fun doLongRunningTaskOne(): String {
        delay(5000)
        return "One"
    }

    private suspend fun doLongRunningTaskTwo(): String {
        delay(8000)
        return "Two"
    }

}