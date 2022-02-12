package com.kingz.coroutines.data

import com.kingz.coroutines.data.api.UserServiceApi
import com.kingz.coroutines.data.model.ApiUser
import com.zeke.reactivehttp.datasource.RemoteExtendDataSource

/**
 * author：ZekeWang
 * date：2021/6/18
 * description：用户信息的DataSource
 */
class UserInfoRemoteDataSource: RemoteExtendDataSource<UserServiceApi>(
        iActionEvent = null,
        apiServiceClass = UserServiceApi::class.java
    ) {
    override val baseUrl: String = UserServiceApi.BASE_URL
    var apiService = getApiService(baseUrl)

    override fun onExceptionToastShow(msg: String) { }

    suspend fun getUsers(): List<ApiUser>{
        return apiService.getUsers()
    }

    suspend fun getMoreUsers(): List<ApiUser>{
        return apiService.getMoreUsers()
    }

    suspend fun getUsersWithError(): List<ApiUser>{
        return apiService.getUsersWithError()
    }
}