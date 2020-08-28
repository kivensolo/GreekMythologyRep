package com.zeke.home.api

import com.kingz.base.BaseApiService
import com.zeke.home.bean.ArticleData
import com.zeke.home.entity.BannerData
import retrofit2.http.GET
import retrofit2.http.Path

/**
 * Wan android recom data api.
 */
interface RecomApiService: BaseApiService {

    @GET("/article/list/{pageId}/json")
    suspend fun requestArticles(@Path("pageId") pageIndex:String): ArticleData?

    @GET("/banner/json")
    suspend fun bannerData(): BannerData?

}