package com.kingz.mvvm.data.api

import com.kingz.mvvm.data.model.ApiUser


interface ApiHelper {

    suspend fun getUsers(): List<ApiUser>

    suspend fun getMoreUsers(): List<ApiUser>

    suspend fun getUsersWithError(): List<ApiUser>

}