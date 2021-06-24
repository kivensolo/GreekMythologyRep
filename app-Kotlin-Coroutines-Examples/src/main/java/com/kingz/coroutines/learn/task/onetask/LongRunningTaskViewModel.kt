package com.kingz.coroutines.learn.task.onetask

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kingz.base.response.ResponseResult
import com.kingz.coroutines.data.api.IUserAPIFunc
import com.kingz.coroutines.data.local.DatabaseHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LongRunningTaskViewModel(
    private val apiHelper: IUserAPIFunc,
    private val dbHelper: DatabaseHelper
) : ViewModel() {

    private val status = MutableLiveData<ResponseResult<String>>()

    fun startLongRunningTask() {
        viewModelScope.launch {
            status.postValue(ResponseResult.loading(null))
            try {
                // do a long running task
                doLongRunningTask()
                status.postValue(ResponseResult.success("Task Completed"))
            } catch (e: Exception) {
                status.postValue(ResponseResult.error("Something Went Wrong", null))
            }
        }
    }

    fun getStatus(): LiveData<ResponseResult<String>> {
        return status
    }

    private suspend fun doLongRunningTask() {
        withContext(Dispatchers.Default) {
            // your code for doing a long running task
            // Added delay to simulate
            delay(5000)
        }
    }

}