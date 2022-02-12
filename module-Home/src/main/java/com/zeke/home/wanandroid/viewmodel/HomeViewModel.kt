package com.zeke.home.wanandroid.viewmodel

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import com.kingz.base.response.ResponseResult
import com.kingz.module.wanandroid.bean.ArticleData
import com.kingz.module.wanandroid.bean.BannerItem
import com.kingz.module.wanandroid.bean.KnowledgeTreeBean
import com.kingz.module.wanandroid.repository.HomeDataSource
import com.kingz.module.wanandroid.viewmodel.WanAndroidViewModelV2
import com.zeke.kangaroo.zlog.ZLog
import com.zeke.reactivehttp.exception.NetWorkDisconnectException

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

    val systemLiveData: MutableLiveData<ResponseResult<MutableList<KnowledgeTreeBean>>> by lazy {
        MutableLiveData<ResponseResult<MutableList<KnowledgeTreeBean>>>()
    }

    /**
     *  移除当前LiveData中LiveData持有的观察者
     */
    fun cancle(owner: LifecycleOwner) {
        articalLiveData.removeObservers(owner)
        bannerLiveData.removeObservers(owner)
        articalCollectData.removeObservers(owner)
    }

    fun getArticalData(pageId: Int = 0) {
        ZLog.d("getArticalData pageId=$pageId")
        //TODO 后续 增加异常情况下延迟重试逻辑
        remoteDataSource.enqueue({requestArticles(pageId)}){
            onSuccess {
                articalLiveData.postValue(it)
            }
            onFailed {
                ZLog.e("getArticalData failed: $it")
                if(it is NetWorkDisconnectException){
                    showNoNetworkView()
                }else{
                    articalLiveData.postValue(null)
                }
            }
            onFinally {  }
        }
    }

    /**
     * 获取Banner数据
     * LiveData在Repository中
     */
    fun getBanner() {
   /*     try {
            launchIO {
                val result = remoteDataSource.getBannerData()
                if (result!!.code == -1) {
                    bannerLiveData.postValue(ResponseResult.error(result.message ?: "未知异常"))
                } else {
                    bannerLiveData.postValue(ResponseResult.success(result.data))
                }
            }
        } catch (e: Exception) {
            bannerLiveData.postValue(
                ResponseResult.error(e.toString(), null)
            )
        }*/
    }

    /**
     * 获取知识体系数据
     */
    fun getSystemInfo(){
        launchIO {
            try {
                val result = remoteDataSource.getSystemInfo()
                val response = ResponseResult.response(result.data)
                systemLiveData.postValue(response)
            } catch (e: Exception) {
                systemLiveData.postValue(ResponseResult.error(e.toString(), null))
            }
        }
    }
}