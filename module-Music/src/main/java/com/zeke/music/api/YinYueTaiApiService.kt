package com.zeke.music.api

import com.kingz.base.BaseApiService
import com.zeke.music.bean.RelatedVideoListInfo
import com.zeke.music.bean.VideoInfo
import com.zeke.music.bean.VideoListInfo
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * author：ZekeWang
 * date：2021/5/14
 * description：音乐台数据API接口
 */
interface YinYueTaiApiService: BaseApiService {
    /**
     * 根据影片类型获取数据
     * videoType:
     * 1:内地
     * 2:香港
     * 3:台湾
     * 4:日韩欧美
     * 5:音悦V榜几自制
     */
    @GET("/video/getTypeVideoList")
    suspend fun getVideoList(@Query("videoType") type:Int,
                     @Query("pageNum") pageNum:Int,
                     @Query("pageSize") pageSize:Int):VideoListInfo

    @GET("/video/getVideoInfo")
    suspend fun getVideoInfo(@Query("id") id:Int): VideoInfo

    @GET("/video/getRelatedVideoList")
    suspend fun getRelatedVideoList(@Query("id") id:Int): List<RelatedVideoListInfo>


}