package com.kingz.module.common.api

import com.kingz.base.BaseApiService
import com.kingz.module.common.bean.*
import retrofit2.http.Field
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

/**
 * Wan android recom data api.
 */
interface WanAndroidApiService : BaseApiService {

    /**
     * 获取文章列表
     * @param pageId 页码从0开始
     */
    @GET("/article/list/{pageId}/json")
    suspend fun requestArticles(@Path("pageId") pageIndex: Int): ArticleData?

    /**
     * 首页Banner
     */
    @GET("/banner/json")
    suspend fun bannerData(): BannerData?

    /**
     * 搜索热词
     */
    @GET("hotkey/json")
    suspend fun getHotKey(): HotKeyBean?

    /**
     * 知识体系
     */
    @GET("tree/json")
    suspend fun getknowlegeSystem(): TreeBean?

    /**
     * 收藏站内文章
     */
    @POST("lg/collect/{id}/json")
    suspend fun collect(@Path("id") id:Int): CollectBean?

    /**
     * 收藏站外文章
     */
    @GET("lg/collect/add/json")
    suspend fun collect(@Field("title") title:String,
                        @Field("author") author:String,
                        @Field("link") link:String): CollectBean?
}