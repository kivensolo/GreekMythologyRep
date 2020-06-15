package com.kingz.coroutines.learn.room

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kingz.coroutines.data.api.ApiHelper
import com.kingz.coroutines.data.local.DatabaseHelper
import com.kingz.coroutines.data.local.entity.User
import com.kingz.coroutines.utils.Resource
import kotlinx.coroutines.launch

class RoomDBViewModel(
        private val apiHelper: ApiHelper,
        private val dbHelper: DatabaseHelper)
    : ViewModel() {
    private val users = MutableLiveData<Resource<List<User>>>()

    companion object {
        private const val TAG = "RoomDBViewModel"
    }

    init {
        fetchUsers()
    }

    private fun fetchUsers() {
        viewModelScope.launch {
            Log.d(TAG, "fetchUsers by database first.")
            users.postValue(Resource.loading(null))
            try {
                val usersFromDb = dbHelper.getUsers()
                if (usersFromDb.isEmpty()) {
                    Log.d(TAG, "db is empty. request data with network.")
                    val usersFromApi = apiHelper.getUsers()
                    val usersToInsertInDB = mutableListOf<User>()

                    for (apiUser in usersFromApi) {
                        val user = User(
                                apiUser.id,
                                apiUser.name,
                                apiUser.email,
                                apiUser.avatar
                        )
                        usersToInsertInDB.add(user)
                    }

                    dbHelper.insertAll(usersToInsertInDB)
                    users.postValue(Resource.success(usersToInsertInDB))
                } else {
                    Log.d(TAG, "Find cache data from local-db.")
                    users.postValue(Resource.success(usersFromDb))
                }
            } catch (e: Exception) {
                Log.e(TAG, "Something Went Wrong:${e.message}")
                users.postValue(Resource.error(
                        "Something Went Wrong:${e.message}"
                        , null))
            }
        }
    }

    fun getUsers(): LiveData<Resource<List<User>>> {
        return users
    }
}