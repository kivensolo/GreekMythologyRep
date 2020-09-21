package com.zeke.home.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.kingz.base.BaseRepository
import com.kingz.base.response.ResponseResult
import com.kingz.module.common.api.WanAndroidApiService
import com.kingz.module.common.bean.BannerData
import com.kingz.module.common.service.ApiServiceUtil

/**
 * author: King.Z <br>
 * date:  2020/9/21 22:30 <br>
 * description:  <br>
 *
 *   获取LiveData包裹住的真实数据
 */
@Deprecated(message = "使用了WanAndroidRepository代替")
class MainRepository : BaseRepository() {
    /**
     * 获取Banner数据
     */
    suspend fun getBanner(): LiveData<ResponseResult<BannerData>> {
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