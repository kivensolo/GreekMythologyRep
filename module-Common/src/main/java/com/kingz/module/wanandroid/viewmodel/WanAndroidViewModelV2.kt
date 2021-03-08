package com.kingz.module.wanandroid.viewmodel

import androidx.lifecycle.MutableLiveData
import com.kingz.database.entity.UserEntity
import com.kingz.module.wanandroid.bean.Article
import com.kingz.module.wanandroid.bean.CollectActionBean
import com.kingz.module.wanandroid.repository.WanAndroidRemoteDataSource
import com.zeke.kangaroo.utils.ZLog
import com.zeke.reactivehttp.base.BaseReactiveViewModel
import kotlinx.coroutines.Job

/**
 * 玩Android的ViewModel
 */
open class WanAndroidViewModelV2 : BaseReactiveViewModel() {

    val userInfoLiveData: MutableLiveData<UserEntity> by lazy {
        MutableLiveData<UserEntity>()
    }


    val articalCollectData: MutableLiveData<CollectActionBean> by lazy {
        MutableLiveData<CollectActionBean>()
    }

    open val dataSource: WanAndroidRemoteDataSource = WanAndroidRemoteDataSource()

    fun getUserInfo() {
        launchIO {
            val info = dataSource.getUserInfo()
            userInfoLiveData.postValue(info)
        }
    }

    /**
     * 进行指定文章的收藏和取消收藏
     */
    fun changeArticleLike(item: Article) {
        launchIO {
            try {
                val data = dataSource.changeArticleLike(item)
                val result = CollectActionBean().apply {
                    actionType = if (item.collect) {
                        CollectActionBean.TYPE.UNCOLLECT
                    } else {
                        CollectActionBean.TYPE.COLLECT
                    }
                    isSuccess = (data.errorCode == 0)
                }
                if (data.errorCode == 0) {
                    val collect = item.collect
                    item.collect = !collect
                } else {
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

    // 顶层接口方法
    override fun showLoading(job: Job?) {
        super.showLoading(job)
    }

    override fun dismissLoading() {
        super.dismissLoading()
    }

    override fun showToast(msg: String) {
        super.showToast(msg)
    }

    override fun finishView() {
        super.finishView()
    }
}