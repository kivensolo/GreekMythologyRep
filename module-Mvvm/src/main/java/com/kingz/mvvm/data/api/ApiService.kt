package com.kingz.mvvm.data.api

import com.kingz.mvvm.data.model.ApiUser
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