package com.kingz.module.wanandroid.viewmodel

import androidx.lifecycle.MutableLiveData
import com.kingz.base.BaseViewModel
import com.kingz.database.entity.UserEntity
import com.kingz.module.common.api.ApiServiceUtil
import com.kingz.module.wanandroid.bean.Article
import com.kingz.module.wanandroid.bean.CollectActionBean
import com.kingz.module.wanandroid.repository.WanAndroidRepository
import com.zeke.kangaroo.utils.ZLog

/**
 * 玩Android的ViewModel
 */
open class WanAndroidViewModel : BaseViewModel<WanAndroidRepository>() {
    override fun createRepository(): WanAndroidRepository {
        return WanAndroidRepository(ApiServiceUtil.getApiService())
    }

    val userInfoLiveData: MutableLiveData<UserEntity> by lazy {
        MutableLiveData<UserEntity>()
    }


    val articalCollectData: MutableLiveData<CollectActionBean> by lazy {
        MutableLiveData<CollectActionBean>()
    }

    fun getUserInfo() {
        launchIO {
            val info = repository.getUserInfo()
            userInfoLiveData.postValue(info)
        }
    }

    /**
     * 进行指定文章的收藏和取消收藏
     */
    fun changeArticleLike(item: Article){
     launchIO {
            try {
                val data = repository.changeArticleLike(item)
                val result = CollectActionBean().apply {
                    actionType = if (item.collect) {
                            CollectActionBean.TYPE.UNCOLLECT
                        } else {
                            CollectActionBean.TYPE.COLLECT
                        }
                    isSuccess = (data.errorCode == 0)
                }
                if(data.errorCode == 0){
                    val collect = item.collect
                    item.collect = !collect
                }else{
                    result.errorMsg = data.errorMsg ?: "unKnow"
                }
                articalCollectData.postValue(result)
            } catch (e: Exception) {
                //java.net.SocketTimeoutException: timeout
                ZLog.e("changeArticleLike on exception: ${e.printStackTrace()}")
                val result = CollectActionBean().apply {
                    actionType = if (item.collect) {
                            CollectActionBean.TYPE.UNCOLLECT
                        } else {
                            CollectActionBean.TYPE.COLLECT
                        }
                    isSuccess = false
                }
                articalCollectData.postValue(result)
            }
        }
    }
}