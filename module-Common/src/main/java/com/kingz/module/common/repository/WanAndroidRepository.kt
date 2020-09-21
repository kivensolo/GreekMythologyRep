package com.kingz.module.common.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.kingz.base.BaseRepository
import com.kingz.base.response.ResponseResult
import com.kingz.database.entity.UserEntity
import com.kingz.module.common.api.WanAndroidApiService
import com.kingz.module.common.bean.ArticleData
import com.kingz.module.common.bean.BannerData
import com.kingz.module.common.service.ApiServiceUtil
import com.kingz.module.common.user.UserInfo
import com.zeke.kangaroo.utils.ZLog

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
    suspend fun getUserInfo(): UserEntity? {
        return UserInfo.getUserInfor()
    }

    suspend fun getBannerData(): BannerData? {
        ZLog.d("get Banner ---> ")
        return ApiServiceUtil.getApiService<WanAndroidApiService>().bannerData()
    }

    /**
     * 获取Banner数据,并返回liveData的数据
     */
    suspend fun getBannerWithLiveData(): LiveData<ResponseResult<BannerData>> {
        ZLog.d("get Banner With LiveData ---> ")
        val liveData: MutableLiveData<ResponseResult<BannerData>> = MutableLiveData()
        liveData.postValue(ResponseResult.loading())
        val service = ApiServiceUtil.getApiService<WanAndroidApiService>()
        try {
            val result = service.bannerData()
            liveData.postValue(ResponseResult.success(result))
        } catch (e: Exception) {
            liveData.postValue(ResponseResult.error(e.toString(), null))
        }
        return liveData
    }

}