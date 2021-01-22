package com.kingz.coroutines.data.api.wandroid

import com.kingz.coroutines.demo.entity.ChaptersEntity
import com.kingz.coroutines.demo.entity.LoginEntity
import retrofit2.http.GET

/**
 * @author zeke.wang
 * @date 2020/6/22
 * @maintainer zeke.wang
 * @copyright 2020 www.xgimi.com Inc. All rights reserved.
 * @desc:
 */
interface WAndroidApi {

     @GET("wxarticle/chapters/json")
    suspend fun fetchChapterData(): ChaptersEntity

    suspend fun fetchMockLoginData(): MutableList<LoginEntity>

    companion object {
        const val BASE_URL = "https://wanandroid.com/"
    }
}