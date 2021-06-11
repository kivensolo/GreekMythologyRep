package com.zeke.eyepetizer.api

import com.kingz.base.BaseApiService
import com.zeke.eyepetizer.bean.Data
import com.zeke.eyepetizer.bean.EyepetizerTabListInfo
import com.zeke.eyepetizer.bean.EyepetizerTabPageData
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.Url

/**
 * Eyepetizer data api.
 */
interface EyepetizerApiService : BaseApiService {

    // 获取【首页】栏目列表
    @GET("v5/index/tab/list")
    suspend fun requestTabList(): EyepetizerTabListInfo

    // 获取指定栏目详情数据
    @GET
    suspend fun requestTabPageData(@Url url: String): EyepetizerTabPageData

    //根据视频Id获取该视频的详细信息    例子：http://baobab.kaiyanapp.com/api/v2/video/127373
    @GET("v2/video/{videoId}")
    suspend fun getVideoDetail(@Path("videoId") videoId: String): Data

    //获取【视频播放页】一些额外信息，如该视频的相关推荐
    @GET("v4/video/related")
    suspend fun getVideoRelated(@Query("id") videoId: String): EyepetizerTabPageData
}