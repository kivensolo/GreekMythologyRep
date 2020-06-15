package com.kingz.coroutines.data.api

import com.kingz.coroutines.data.model.ApiUser
import retrofit2.http.GET

interface ApiService {

    //---------------------- Mock api
    @GET("users")
    suspend fun getUsers(): List<ApiUser>

    @GET("more-users")
    suspend fun getMoreUsers(): List<ApiUser>

    @GET("error")
    suspend fun getUsersWithError(): List<ApiUser>

}