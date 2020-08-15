package com.zeke.home.repository

import com.kingz.base.BaseRepository
import com.kingz.module.common.service.ApiServiceUtil
import com.zeke.home.api.RecomApiService
import com.zeke.home.entity.ArticleData
import com.zeke.network.retrofit.mannager.Api
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response

class HomePageRepository : BaseRepository() {

    /**
     * 进行文章列表获取
     */
    suspend fun getArticals(pageId: String = "0"): ArticleData? {
        val api =  ApiServiceUtil.getApiService<RecomApiService>()
        // TODO 不用每次都切线程吧？
        return withContext(Dispatchers.IO) {
            api.requestArticles(pageId)
        }
    }
}