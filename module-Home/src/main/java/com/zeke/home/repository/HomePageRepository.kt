package com.zeke.home.repository

import com.kingz.base.BaseRepository
import com.kingz.database.entity.UserEntity
import com.kingz.module.common.service.ApiServiceUtil
import com.kingz.module.common.user.UserInfo
import com.zeke.home.api.RecomApiService
import com.zeke.home.entity.ArticleData

class HomePageRepository : BaseRepository() {

    /**
     * 进行文章列表获取
     */
    suspend fun getArticals(pageId: String = "0"): ArticleData? {
        return ApiServiceUtil.getApiService<RecomApiService>().requestArticles(pageId)
    }

    /**
     * 获取用户信息
     */
    suspend fun getUserInfo():UserEntity?{
        return UserInfo.getUserInfor()
    }

}