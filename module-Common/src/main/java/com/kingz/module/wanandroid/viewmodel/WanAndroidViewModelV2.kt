package com.kingz.module.wanandroid.viewmodel

import androidx.lifecycle.MutableLiveData
import com.kingz.database.entity.UserEntity
import com.kingz.module.wanandroid.bean.Article
import com.kingz.module.wanandroid.bean.CollectActionBean
import com.kingz.module.wanandroid.bean.CollectListBean
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

    val userCollectArticalListLiveData: MutableLiveData<CollectListBean> by lazy {
        MutableLiveData<CollectListBean>()
    }

    open val remoteDataSource by lazy {
        WanAndroidRemoteDataSource(this)
    }

    fun getUserInfo() {
        launchIO {
            val info = remoteDataSource.getUserInfo()
            userInfoLiveData.postValue(info)
        }
    }

    /**
     * 进行指定文章的收藏和取消收藏
     * @param srcArticle: 原文章数据
     * @param isCollect: 是否收藏
     */
    fun changeArticleLike(srcArticle: Article, position: Int, isCollect:Boolean) {
        launchIO {
            var result:CollectActionBean ?= null
            try {
                val data = remoteDataSource.changeArticleLike(srcArticle)
                result = CollectActionBean().apply {
                    actionType = if (srcArticle.collect) {
                        CollectActionBean.TYPE.UNCOLLECT
                    } else {
                        CollectActionBean.TYPE.COLLECT
                    }
                    isSuccess = (data.errorCode == 0)
                    // 收藏状态更新成功时，改变Item数据
                    if (isSuccess) {
                        srcArticle.collect = isCollect
                    } else {
                        errorMsg = data.errorMsg ?: "unKnow"
                    }
                }
            } catch (e: Exception) {
                //java.net.SocketTimeoutException: timeout
                ZLog.e("changeArticleLike on exception: ${e.printStackTrace()}")
                result = CollectActionBean().apply {
                    actionType = if (srcArticle.collect) {
                        CollectActionBean.TYPE.UNCOLLECT
                    } else {
                        CollectActionBean.TYPE.COLLECT
                    }
                    isSuccess = false
                }
            }finally {
                result?.articlePostion = position
                articalCollectData.postValue(result)
            }
        }
    }

    /**
     * 获取登录用户自身的文章集合
     * @param pageIndex: 页面index id
     */
    fun getMineCollectArticalList(pageIndex: Int) {
        launchIO {
            var result: CollectListBean? = null
            try {
                val articleList = remoteDataSource.getArticleList(pageIndex)
                result = articleList.data
            } catch (e: Exception) {
                ZLog.e("getMineCollectArticalList on exception: ${e.printStackTrace()}")
            } finally {
                userCollectArticalListLiveData.postValue(result)
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