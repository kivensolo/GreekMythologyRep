package com.zeke.home.viewmodel

import androidx.lifecycle.MutableLiveData
import com.kingz.base.BaseViewModel
import com.kingz.database.entity.UserEntity
import com.zeke.home.bean.ArticleData
import com.zeke.home.repository.HomePageRepository
import com.zeke.kangaroo.utils.ZLog

class MainPageViewModel : BaseViewModel<HomePageRepository>() {
    override fun createRepository(): HomePageRepository {
        return HomePageRepository()
    }

    val articalLiveData: MutableLiveData<ArticleData> by lazy {
        MutableLiveData<ArticleData>()
    }

    val userInfoLiveData: MutableLiveData<UserEntity> by lazy {
        MutableLiveData<UserEntity>()
    }

    fun getArticalData(pageId: String) {
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