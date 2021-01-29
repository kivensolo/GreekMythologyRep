package com.zeke.home.wanandroid.repository

import com.kingz.base.BaseRepository
import com.kingz.base.response.ResponseResult
import com.kingz.module.common.api.ApiServiceUtil
import com.kingz.module.wanandroid.api.WanAndroidApiService
import com.kingz.module.wanandroid.bean.ArticleData
import com.kingz.module.wanandroid.bean.BannerItem
import com.kingz.module.wanandroid.response.WanAndroidResponse
import com.zeke.kangaroo.utils.ZLog

/**
 * author：ZekeWang
 * date：2021/1/28
 * description：首页数据的Repository
 */
class HomeRepository : BaseRepository() {
    /**
     * 进行文章列表获取
     */
    suspend fun getArticals(pageId: Int = 0): WanAndroidResponse<ArticleData> {
        return ApiServiceUtil.getApiService<WanAndroidApiService>().requestArticles(pageId)
    }

    suspend fun getBannerData(): ResponseResult<MutableList<BannerItem>>? {
        ZLog.d("get Banner ---> ")
        return ApiServiceUtil.getApiService<WanAndroidApiService>().bannerData()
    }
}