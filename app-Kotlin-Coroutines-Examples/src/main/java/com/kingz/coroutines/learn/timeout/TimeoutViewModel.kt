package com.kingz.coroutines.learn.timeout

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kingz.base.response.ResponseResult
import com.kingz.coroutines.data.api.IUserAPIFunc
import com.kingz.coroutines.data.local.DatabaseHelper
import com.kingz.coroutines.data.model.ApiUser
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeout

class TimeoutViewModel(
    private val apiHelper: IUserAPIFunc,
    private val dbHelper: DatabaseHelper
) : ViewModel() {

    private val users = MutableLiveData<ResponseResult<List<ApiUser>>>()

    init {
        fetchUsers()
    }

    private fun fetchUsers() {
        viewModelScope.launch {
            users.postValue(ResponseResult.loading(null))
            try {
                withTimeout(200) {
                    val usersFromApi = apiHelper.getUsers()
                    users.postValue(ResponseResult.success(usersFromApi))
                }
            } catch (e: TimeoutCancellationException) {
                users.postValue(ResponseResult.error("TimeoutCancellationException：${e.message}", null))
            } catch (e: Exception) {
                users.postValue(ResponseResult.error("Something Went Wrong：${e.message}", null))
            }
        }
    }

    fun getUsers(): LiveData<ResponseResult<List<ApiUser>>> {
        return users
    }

}