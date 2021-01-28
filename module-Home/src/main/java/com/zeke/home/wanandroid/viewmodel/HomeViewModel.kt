package com.zeke.home.wanandroid.viewmodel

import androidx.lifecycle.MutableLiveData
import com.kingz.base.BaseViewModel
import com.kingz.base.response.ResponseResult
import com.kingz.module.wanandroid.bean.ArticleData
import com.kingz.module.wanandroid.bean.BannerData
import com.zeke.home.wanandroid.repository.HomeRepository
import com.zeke.kangaroo.utils.ZLog

/**
 * author：ZekeWang
 * date：2021/1/28
 * description： 首页数据模型
 */
class HomeViewModel : BaseViewModel<HomeRepository>() {
    override fun createRepository(): HomeRepository {
        return HomeRepository()
    }

    val articalLiveData: MutableLiveData<ArticleData> by lazy {
        MutableLiveData<ArticleData>()
    }

    val bannerLiveData: MutableLiveData<ResponseResult<BannerData>> by lazy {
        MutableLiveData<ResponseResult<BannerData>>()
    }

    fun getArticalData(pageId: Int) {
        ZLog.d("getArticalData pageId=$pageId")
        launchDefault {
            try {
                val result = repository.getArticals(pageId)
                articalLiveData.postValue(result.data)
            } catch (e: Exception) {
                ZLog.e("getArticalData on exception: ${e.printStackTrace()}")
                articalLiveData.postValue(null)
            }
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
                bannerLiveData.postValue(
                    ResponseResult.success(
                        result?.data
                    )
                )
            } catch (e: Exception) {
                bannerLiveData.postValue(
                    ResponseResult.error(
                        e.toString(),
                        null
                    )
                )
            }
        }
    }
    //TODO
    fun collect(){}
    fun unCollect(){}
    fun getTopArticles(){}
}