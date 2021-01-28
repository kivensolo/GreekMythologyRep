package com.kingz.module.wanandroid.repository

import com.kingz.base.BaseRepository
import com.kingz.database.entity.UserEntity
import com.kingz.module.common.api.ApiServiceUtil
import com.kingz.module.common.user.UserInfo
import com.kingz.module.wanandroid.api.WanAndroidApiService
import com.kingz.module.wanandroid.bean.CollectBean
import com.zeke.kangaroo.utils.ZLog

/**
 * author: King.Z <br>
 * date:  2020/8/29 9:59 <br>
 * description: Repository层负责控制数据获取API <br>
 */
open class WanAndroidRepository : BaseRepository() {


    /**
     * 获取用户信息
     */
    suspend fun getUserInfo(): UserEntity? {
        return UserInfo.getUserInfor()
    }

    /**
     * 添加收藏文章
     */
    suspend fun collectArticle(articleId:Int): CollectBean?{
        ZLog.d("collect article: $articleId ---> ")
        return ApiServiceUtil.getApiService<WanAndroidApiService>().collect(articleId)
    }

    /**
     * 取消文章收藏
     */
    suspend fun unCollectArticle(articleId:Int): CollectBean?{
        ZLog.d("uncollect article: $articleId ---> ")
        return ApiServiceUtil.getApiService<WanAndroidApiService>().unCcollect(articleId)
    }
}