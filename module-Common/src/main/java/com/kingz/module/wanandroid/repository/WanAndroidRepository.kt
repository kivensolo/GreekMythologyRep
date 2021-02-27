package com.kingz.module.wanandroid.repository

import com.kingz.base.BaseRepository
import com.kingz.database.entity.UserEntity
import com.kingz.module.common.user.UserInfo
import com.kingz.module.wanandroid.api.WanAndroidApiService
import com.kingz.module.wanandroid.bean.Article
import com.kingz.module.wanandroid.bean.CollectBean
import com.kingz.module.wanandroid.response.WanAndroidResponse
import com.zeke.kangaroo.utils.ZLog

/**
 * author: King.Z <br>
 * date:  2020/8/29 9:59 <br>
 * description: Repository层负责控制数据获取API <br>
 */
open class WanAndroidRepository(private val apiService: WanAndroidApiService) : BaseRepository() {

    /**
     * 获取用户信息
     */
    suspend fun getUserInfo(): UserEntity? {
        return UserInfo.getUserInfor()
    }

    /**
     * 文章收藏和取消的接口
     * 当前文章未收藏，则进行收藏.
     * 收藏了则取消收藏.
     */
    suspend fun changeArticleLike(item: Article): WanAndroidResponse<CollectBean> {
        return if (item.collect) {
            ZLog.d("unCcollect article id = (${item.id})")
            apiService.unCollect(item.id)
        } else {
            ZLog.d("collect article id = (${item.id})")
            apiService.collect(item.id)
        }
    }
}