package com.zeke.home.wanandroid.repository

import com.kingz.base.response.ResponseResult
import com.kingz.module.wanandroid.api.WanAndroidApiService
import com.kingz.module.wanandroid.bean.ArticleData
import com.kingz.module.wanandroid.bean.BannerItem
import com.kingz.module.wanandroid.repository.WanAndroidRepository
import com.kingz.module.wanandroid.response.WanAndroidResponse
import com.zeke.kangaroo.utils.ZLog

/**
 * author：ZekeWang
 * date：2021/1/28
 * description：首页数据的Repository
 */
@Deprecated(message = "被HomeDataSource代替")
class HomeRepository(private val apiServer:WanAndroidApiService) : WanAndroidRepository(apiServer) {

    /** 进行文章列表获取 */
    suspend fun getArticals(pageId: Int = 0): WanAndroidResponse<ArticleData> {
        return apiServer.requestArticles(pageId)
    }

    suspend fun getBannerData(): ResponseResult<MutableList<BannerItem>>? {
        ZLog.d("get Banner ---> ")
        return apiServer.bannerData()
    }

}