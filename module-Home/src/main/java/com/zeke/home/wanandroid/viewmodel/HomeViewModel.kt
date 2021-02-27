package com.zeke.home.wanandroid.viewmodel

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import com.kingz.base.response.ResponseResult
import com.kingz.module.common.api.ApiServiceUtil
import com.kingz.module.wanandroid.bean.ArticleData
import com.kingz.module.wanandroid.bean.BannerItem
import com.kingz.module.wanandroid.viewmodel.WanAndroidViewModel
import com.zeke.home.wanandroid.repository.HomeRepository
import com.zeke.kangaroo.utils.ZLog

/**
 * author：ZekeWang
 * date：2021/1/28
 * description： 首页数据模型
 * 继承自玩Android的ViewModel
 */
class HomeViewModel : WanAndroidViewModel() {
    override fun createRepository(): HomeRepository {
        return HomeRepository(ApiServiceUtil.getApiService())
    }

    val articalLiveData: MutableLiveData<ArticleData> by lazy {
        MutableLiveData<ArticleData>()
    }

    val bannerLiveData: MutableLiveData<ResponseResult<List<BannerItem>>> by lazy {
        MutableLiveData<ResponseResult<List<BannerItem>>>()
    }

    /**
     *  移除当前LiveData中LiveData持有的观察者
     */
    fun cancle(owner: LifecycleOwner) {
        articalLiveData.removeObservers(owner)
        bannerLiveData.removeObservers(owner)
        articalCollectData.removeObservers(owner)
    }

    fun getArticalData(pageId: Int) {
        ZLog.d("getArticalData pageId=$pageId")
        //后续 增加异常情况下延迟重试逻辑
        launchDefault {
            try {
                val result = (repository as HomeRepository).getArticals(pageId)
                articalLiveData.postValue(result.data)
            } catch (e: Exception) {
                //java.net.SocketTimeoutException: timeout
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
                val result = (repository as HomeRepository).getBannerData()
                if(result!!.code == -1){
                    bannerLiveData.postValue(ResponseResult.error(result.message ?:"未知异常"))
                }else{
                    bannerLiveData.postValue(ResponseResult.success(result.data))
                }
            } catch (e: Exception) {
                bannerLiveData.postValue(
                    ResponseResult.error(e.toString(),null)
                )
            }
        }
    }

    fun getTopArticles(){}
}