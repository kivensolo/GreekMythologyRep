package com.kingz.coroutines.learn.retrofit.series

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kingz.coroutines.data.api.ApiHelper
import com.kingz.coroutines.data.local.DatabaseHelper
import com.kingz.coroutines.data.model.ApiUser
import com.kingz.coroutines.utils.Resource
import kotlinx.coroutines.launch

/**
 * @desc: 串行网络请求的ViewModel例子，先后执行
 * Learn how to make network calls in series using Kotlin Coroutines.
 * This is useful when you want to make a network call which is dependent on an another network call.
 */
class SeriesNetworkCallsViewModel(
        private val apiHelper: ApiHelper,
        private val dbHelper: DatabaseHelper
) : ViewModel() {

    private val users = MutableLiveData<Resource<List<ApiUser>>>()

    init {
        fetchUsers()
    }

    private fun fetchUsers() {
        viewModelScope.launch {
            users.postValue(Resource.loading(null))
            try {
                Log.d("SeriesViewModel","getUsers() ---->")
                val usersFromApi = apiHelper.getUsers()
                Log.d("SeriesViewModel","getUsers() end.")
                Log.d("SeriesViewModel","getMoreUsers() ---->")
                val moreUsersFromApi = apiHelper.getMoreUsers()
                Log.d("SeriesViewModel","getMoreUsers() end.")

                val allUsersFromApi = mutableListOf<ApiUser>()
                allUsersFromApi.addAll(usersFromApi)
                allUsersFromApi.addAll(moreUsersFromApi)
                users.postValue(Resource.success(allUsersFromApi))
            } catch (e: Exception) {
                users.postValue(Resource.error("Something Went Wrong", null))
            }
        }
    }

    fun getUsers(): LiveData<Resource<List<ApiUser>>> {
        return users
    }

}