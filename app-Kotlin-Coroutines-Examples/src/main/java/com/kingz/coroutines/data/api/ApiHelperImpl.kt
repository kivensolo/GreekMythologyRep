package com.kingz.coroutines.data.api

@Deprecated("后续被dataSource替换")
class ApiHelperImpl(private val userServiceApi: UserServiceApi) : IUserAPIFunc {

    override suspend fun getUsers() = userServiceApi.getUsers()

    override suspend fun getMoreUsers() = userServiceApi.getMoreUsers()

    override suspend fun getUsersWithError() = userServiceApi.getUsersWithError()

}