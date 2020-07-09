package com.zeke.module_login.api

import com.kingz.base.BaseApiService
import com.kingz.base.response.ResponseResult
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST

interface LoginApiService : BaseApiService {
    @FormUrlEncoded
    @POST("/user/login")
    suspend fun userLogin(@Field("username") username: String
                          ,@Field("password") password: String)
            : ResponseResult<String>

    @POST("user/register")
    suspend fun userRegister(): String

    /**
     * 访问了 logout 后，服务端会让客户端清除 Cookie（即cookie max-Age=0），
     * 如果客户端 Cookie 实现合理，可以实现自动清理，
     * 如果本地做了用户账号密码和保存，及时清理。
     */
    @GET("user/logout/json")
    suspend fun userLogout(): String
}
