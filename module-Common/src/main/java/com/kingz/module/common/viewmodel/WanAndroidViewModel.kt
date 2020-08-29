package com.kingz.module.common.viewmodel

import androidx.lifecycle.MutableLiveData
import com.kingz.base.BaseViewModel
import com.kingz.database.entity.UserEntity
import com.kingz.module.common.bean.ArticleData
import com.kingz.module.common.repository.WanAndroidRepository
import com.zeke.kangaroo.utils.ZLog

open class WanAndroidViewModel : BaseViewModel<WanAndroidRepository>() {
    override fun createRepository(): WanAndroidRepository {
        return WanAndroidRepository()
    }

    val articalLiveData: MutableLiveData<ArticleData> by lazy {
        MutableLiveData<ArticleData>()
    }

    val userInfoLiveData: MutableLiveData<UserEntity> by lazy {
        MutableLiveData<UserEntity>()
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

}