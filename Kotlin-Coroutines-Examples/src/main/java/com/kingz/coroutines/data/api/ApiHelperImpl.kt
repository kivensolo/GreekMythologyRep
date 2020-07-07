package com.kingz.coroutines.data.api

class ApiHelperImpl(private val userServiceApi: UserServiceApi) : ApiHelper {

    override suspend fun getUsers() = userServiceApi.getUsers()

    override suspend fun getMoreUsers() = userServiceApi.getMoreUsers()

    override suspend fun getUsersWithError() = userServiceApi.getUsersWithError()

}