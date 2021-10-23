package com.zeke.home.wanandroid.viewmodel

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import com.kingz.base.response.ResponseResult
import com.kingz.module.wanandroid.bean.ArticleData
import com.kingz.module.wanandroid.bean.BannerItem
import com.kingz.module.wanandroid.repository.HomeDataSource
import com.kingz.module.wanandroid.viewmodel.WanAndroidViewModelV2
import com.zeke.kangaroo.zlog.ZLog

/**
 * author：ZekeWang
 * date：2021/1/28
 * description： 首页数据模型
 * 继承自玩Android的ViewModel
 */
class HomeViewModel : WanAndroidViewModelV2() {

    override val remoteDataSource by lazy {
        HomeDataSource(this)
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
        //TODO 后续 增加异常情况下延迟重试逻辑
        launchCPUWithCatch({
            val result = remoteDataSource.getArticals(pageId)
            articalLiveData.postValue(result.data)
        }){
            //java.net.SocketTimeoutException: timeout
            ZLog.e("getArticalData on exception: ${it.printStackTrace()}")
            articalLiveData.postValue(null)
        }
    }

    /**
     * 获取Banner数据
     * LiveData在Repository中
     */
    fun getBanner() {
        launchIO {
            try {
                val result = remoteDataSource.getBannerData()
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
}