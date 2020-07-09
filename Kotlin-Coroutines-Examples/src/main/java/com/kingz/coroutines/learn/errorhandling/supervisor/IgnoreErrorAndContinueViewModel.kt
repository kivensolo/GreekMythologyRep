package com.kingz.coroutines.learn.errorhandling.supervisor

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kingz.base.response.ResponseResult
import com.kingz.coroutines.data.api.ApiHelper
import com.kingz.coroutines.data.local.DatabaseHelper
import com.kingz.coroutines.data.model.ApiUser
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope

class IgnoreErrorAndContinueViewModel(
    private val apiHelper: ApiHelper,
    private val dbHelper: DatabaseHelper
) : ViewModel() {

    private val users = MutableLiveData<ResponseResult<List<ApiUser>>>()

    companion object{
        const val TAG = "IgnoreErrorAndContinue"
    }

    init {
        fetchUsers()
    }

    private fun fetchUsers() {
        viewModelScope.launch {
            users.postValue(ResponseResult.loading(null))
            try {
                // supervisorScope is needed, so that we can ignore error and continue
                // here, more than two child jobs are running in parallel under a supervisor,
                // one child job gets failed, we can continue with other.
                supervisorScope {
                    // supervisorScope 可以保证某个job失败，其他job可以继续执行。
                    val usersFromApiDeferred = async { apiHelper.getUsersWithError() }
                    val moreUsersFromApiDeferred = async { apiHelper.getMoreUsers() }

                    val usersFromApi = try {
                        usersFromApiDeferred.await()
                    } catch (e: Exception) {
                        Log.e(TAG,"usersFromApiDeferred failed.")
                        emptyList<ApiUser>()
                    }

                    val moreUsersFromApi = try {
                        moreUsersFromApiDeferred.await()
                    } catch (e: Exception) {
                        Log.e(TAG,"moreUsersFromApiDeferred failed.")
                        emptyList<ApiUser>()
                    }

                    val allUsersFromApi = mutableListOf<ApiUser>()
                    allUsersFromApi.addAll(usersFromApi)
                    allUsersFromApi.addAll(moreUsersFromApi)
                    Log.d(TAG,"All deferred done. postValue~~")

                    users.postValue(ResponseResult.success(allUsersFromApi))
                }
            } catch (e: Exception) {
                Log.e(TAG,"All Deferred failed.")
                users.postValue(ResponseResult.error("Something Went Wrong", null))
            }
        }
    }

    fun getUsers(): LiveData<ResponseResult<List<ApiUser>>> {
        return users
    }

}