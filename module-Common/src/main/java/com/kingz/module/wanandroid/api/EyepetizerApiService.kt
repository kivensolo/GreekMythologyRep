package com.kingz.module.wanandroid.api

import com.kingz.base.BaseApiService
import com.kingz.module.github.bean.EyepetizerTabListInfo
import retrofit2.http.GET

/**
 * Eyepetizer recom data api.
 */
interface EyepetizerApiService : BaseApiService {

    /**
     * 分类Tab数据
     * @return
     */
    @GET("/api/v5/index/tab/list")
    suspend fun requestTabList(): EyepetizerTabListInfo  // TODO 后续换成 ResponseResult

}