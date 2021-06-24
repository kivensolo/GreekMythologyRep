package com.kingz.coroutines.learn.retrofit.single

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.kingz.base.response.ResponseResult
import com.kingz.coroutines.data.model.ApiUser
import com.kingz.coroutines.learn.retrofit.BaseViewModel
import kotlinx.coroutines.launch

/**
 * 单次网络请求的例子
 */
class SingleNetworkCallViewModelV2 : BaseViewModel() {

    private val users = MutableLiveData<ResponseResult<List<ApiUser>>>()
    init {
        fetchUsers()
    }

    private fun fetchUsers() {
        viewModelScope.launch {
            users.postValue(ResponseResult.loading())
            try {
                val usersFromApi = remoteDataSource.getUsers()
                users.postValue(ResponseResult.success(usersFromApi))
            } catch (e: Exception) {
                users.postValue(ResponseResult.error(e.toString(), null))
            }
        }
    }

    fun getUsers(): LiveData<ResponseResult<List<ApiUser>>> {
        return users
    }

}