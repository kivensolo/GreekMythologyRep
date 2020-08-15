package com.zeke.home.viewmodel

import androidx.lifecycle.MutableLiveData
import com.kingz.base.BaseViewModel
import com.zeke.home.entity.ArticleData
import com.zeke.home.repository.HomePageRepository
import com.zeke.kangaroo.utils.ZLog

class MainPageViewModel : BaseViewModel<HomePageRepository>() {
    override fun createRepository(): HomePageRepository {
        return HomePageRepository()
    }

    val articalLiveData: MutableLiveData<ArticleData> by lazy {
        MutableLiveData<ArticleData>()
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

}