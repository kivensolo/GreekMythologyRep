package com.kingz.coroutines.data.api

import com.kingz.coroutines.data.model.ApiUser


interface IUserAPIFunc {

    suspend fun getUsers(): List<ApiUser>

    suspend fun getMoreUsers(): List<ApiUser>

    suspend fun getUsersWithError(): List<ApiUser>

}