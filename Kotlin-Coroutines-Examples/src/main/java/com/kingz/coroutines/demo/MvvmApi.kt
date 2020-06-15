package com.kingz.coroutines.demo

import com.kingz.coroutines.demo.entity.WAZChaptersEntity
import retrofit2.http.GET

/**
 * @author chongyang.zhang
 * @date 2019/11/18
 * @maintainer chongyang.zhang
 * @copyright 2019 www.xgimi.com Inc. All rights reserved.
 * @desc:
 */
interface MvvmApi {

    @GET("wxarticle/chapters/json")
    suspend fun fetchModeData(): WAZChaptersEntity

    companion object {
        const val BASE_URL = "https://wanandroid.com/"
    }
}