package com.kingz.module.common.repository

import com.kingz.base.BaseRepository
import com.kingz.database.entity.UserEntity
import com.kingz.module.common.api.WanAndroidApiService
import com.kingz.module.common.bean.ArticleData
import com.kingz.module.common.service.ApiServiceUtil
import com.kingz.module.common.user.UserInfo

/**
 * author: King.Z <br>
 * date:  2020/8/29 9:59 <br>
 * description:  <br>
 */
open class WanAndroidRepository : BaseRepository() {
    /**
     * 进行文章列表获取
     */
    suspend fun getArticals(pageId: Int = 0): ArticleData? {
        return ApiServiceUtil.getApiService<WanAndroidApiService>().requestArticles(pageId)
    }

    /**
     * 获取用户信息
     */
    suspend fun getUserInfo(): UserEntity?{
        return UserInfo.getUserInfor()
    }

}