package com.kingz.module.wanandroid.viewmodel

import androidx.lifecycle.MutableLiveData
import com.kingz.base.BaseViewModel
import com.kingz.database.entity.UserEntity
import com.kingz.module.wanandroid.repository.WanAndroidRepository

/**
 * 玩Android的ViewModel
 */
open class WanAndroidViewModel : BaseViewModel<WanAndroidRepository>() {
    override fun createRepository(): WanAndroidRepository {
        return WanAndroidRepository()
    }

    val userInfoLiveData: MutableLiveData<UserEntity> by lazy {
        MutableLiveData<UserEntity>()
    }

    fun getUserInfo() {
        launchIO {
            val info = repository.getUserInfo()
            userInfoLiveData.postValue(info)
        }
    }
}