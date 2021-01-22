package com.kingz.coroutines.learn.retrofit.single

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kingz.base.response.ResponseResult
import com.kingz.coroutines.data.api.ApiHelper
import com.kingz.coroutines.data.local.DatabaseHelper
import com.kingz.coroutines.data.model.ApiUser
import kotlinx.coroutines.launch

/**
 * 单次网络请求的例子
 */
class SingleNetworkCallViewModel(
        private val apiHelper: ApiHelper,
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
                val usersFromApi = apiHelper.getUsers()
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