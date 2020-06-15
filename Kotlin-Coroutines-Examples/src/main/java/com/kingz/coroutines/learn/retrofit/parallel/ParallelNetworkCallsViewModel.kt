package com.kingz.coroutines.learn.retrofit.parallel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kingz.coroutines.data.api.ApiHelper
import com.kingz.coroutines.data.local.DatabaseHelper
import com.kingz.coroutines.data.model.ApiUser
import com.kingz.coroutines.utils.Resource
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

/**
 * @author zeke.wang
 * @date 2020/6/15
 * @maintainer zeke.wang
 * @desc: 并行网络请求的ViewModel例子，需等待两个请求都结束后再进行处理
 *
 * Learn how to make network calls in parallel using Kotlin Coroutines.
 * This is useful when you want to make network calls in parallel which are independent of each other.
 */
class ParallelNetworkCallsViewModel(
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
            // coroutineScope is needed, else in case of any network error, it will crash
            try {
                coroutineScope {
                    val usersFromApiDeferred = async {
                        val usersDeferred = apiHelper.getUsers()
                        Log.d("ParallelViewModel","All api done.")
                        usersDeferred
                    }
                    val moreUsersFromApiDeferred = async {
                        val moreUsersDeferred = apiHelper.getMoreUsers()  // suspend obj
                        Log.d("ParallelViewModel","All api done.")
                        moreUsersDeferred
                    }

                    val usersFromApi = usersFromApiDeferred.await()
                    val moreUsersFromApi = moreUsersFromApiDeferred.await()
                    Log.d("ParallelViewModel","All api done.")

                    val allUsersFromApi = mutableListOf<ApiUser>()
                    allUsersFromApi.addAll(usersFromApi)
                    allUsersFromApi.addAll(moreUsersFromApi)

                    users.postValue(Resource.success(allUsersFromApi))
                }
            } catch (e: Exception) {
                users.postValue(Resource.error("Something Went Wrong", null))
            }
        }
    }


    fun getUsers(): LiveData<Resource<List<ApiUser>>> {
        return users
    }
}