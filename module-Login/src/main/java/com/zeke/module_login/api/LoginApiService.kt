package com.zeke.module_login.api

import com.kingz.base.response.BaseResponse
import com.kingz.base.response.BaseService
import com.zeke.module_login.entity.Chapters
import retrofit2.http.GET

interface LoginApiService : BaseService {
    @GET("/wxarticle/chapters/json")
    suspend fun getChapters(): BaseResponse<List<Chapters>>
}