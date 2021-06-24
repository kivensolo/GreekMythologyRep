package com.kingz.coroutines.learn.errorhandling.exceptionhandler

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kingz.base.response.ResponseResult
import com.kingz.coroutines.data.api.IUserAPIFunc
import com.kingz.coroutines.data.local.DatabaseHelper
import com.kingz.coroutines.data.model.ApiUser
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch

class ExceptionHandlerViewModel(
    private val apiHelper: IUserAPIFunc,
    private val dbHelper: DatabaseHelper
) : ViewModel() {

    private val users = MutableLiveData<ResponseResult<List<ApiUser>>>()

    private val exceptionHandler = CoroutineExceptionHandler { _, exception ->
        users.postValue(ResponseResult.error("Something Went Wrong：${exception.message}", null))
    }

    init {
        fetchUsers()
    }

    private fun fetchUsers() {
        viewModelScope.launch(exceptionHandler) {
            users.postValue(ResponseResult.loading(null))
            val usersFromApi = apiHelper.getUsersWithError()
            users.postValue(ResponseResult.success(usersFromApi))
        }
    }

    fun getUsers(): LiveData<ResponseResult<List<ApiUser>>> {
        return users
    }

}