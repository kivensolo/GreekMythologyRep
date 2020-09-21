package com.kingz.module.common.viewmodel

import androidx.lifecycle.MutableLiveData
import com.kingz.base.BaseViewModel
import com.kingz.base.response.ResponseResult
import com.kingz.database.entity.UserEntity
import com.kingz.module.common.bean.ArticleData
import com.kingz.module.common.bean.BannerData
import com.kingz.module.common.repository.WanAndroidRepository
import com.zeke.kangaroo.utils.ZLog

/**
 * 玩Android的ViewModel
 */
open class WanAndroidViewModel : BaseViewModel<WanAndroidRepository>() {
    override fun createRepository(): WanAndroidRepository {
        return WanAndroidRepository()
    }

    // LiveData在ViewModel中
    val articalLiveData: MutableLiveData<ArticleData> by lazy {
        MutableLiveData<ArticleData>()
    }

    // LiveData在ViewModel中
    val userInfoLiveData: MutableLiveData<UserEntity> by lazy {
        MutableLiveData<UserEntity>()
    }

    val bannerLiveData: MutableLiveData<ResponseResult<BannerData>> by lazy {
        MutableLiveData<ResponseResult<BannerData>>()
    }

    fun getArticalData(pageId: Int) {
        launchDefault {
            try {
                val result = repository.getArticals(pageId)
                articalLiveData.postValue(result)
            } catch (e: Exception) {
                ZLog.e("dologin on exception: ${e.printStackTrace()}")
                articalLiveData.postValue(null)
            }
        }
    }

    fun getUserInfo() {
        launchIO {
            val info = repository.getUserInfo()
            userInfoLiveData.postValue(info)
        }
    }

    /**
     * 获取Banner数据
     * LiveData在Repository中
     */
    fun getBanner() {
        launchIO {
            try {
                val result = repository.getBannerData()
                bannerLiveData.postValue(ResponseResult.success(result))
            } catch (e: Exception) {
                bannerLiveData.postValue(ResponseResult.error(e.toString(), null))
            }
        }
    }

}