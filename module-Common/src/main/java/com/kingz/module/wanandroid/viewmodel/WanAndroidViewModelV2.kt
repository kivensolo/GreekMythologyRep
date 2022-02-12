package com.kingz.module.wanandroid.viewmodel


import androidx.lifecycle.MutableLiveData
import com.kingz.database.AppDatabase
import com.kingz.database.entity.UserEntity
import com.kingz.module.common.CommonApp
import com.kingz.module.wanandroid.bean.Article
import com.kingz.module.wanandroid.bean.CollectActionBean
import com.kingz.module.wanandroid.repository.WanAndroidRemoteDataSource
import com.zeke.kangaroo.zlog.ZLog
import com.zeke.reactivehttp.base.BaseReactiveViewModel

/**
 * 玩Android的ViewModel
 */
open class WanAndroidViewModelV2 : BaseReactiveViewModel() {
    protected val db: AppDatabase?

    init {
        db = CommonApp.getInstance().getDataBase()
    }

    val userInfoLiveData: MutableLiveData<UserEntity> by lazy {
        MutableLiveData<UserEntity>()
    }


    val articalCollectData: MutableLiveData<CollectActionBean> by lazy {
        MutableLiveData<CollectActionBean>()
    }

    // 需限定访问权限为protected 防止view层直接访问Model层的DataSource对象
    protected open val remoteDataSource by lazy {
        WanAndroidRemoteDataSource(this)
    }

    fun getUserInfo() {
        launchIO {
            ZLog.d(TAG,"Get user info from data base.")
            val info = remoteDataSource.getUserInfoFromLocal()
            userInfoLiveData.postValue(info)
        }
    }

    fun userLogout(){
        launchIO{
            ZLog.e(TAG,"User logout")
            val result = remoteDataSource.userLogout()
            ZLog.d(TAG,"User logout result = $result")
            if(result.errorCode == 0){
                userInfoLiveData.postValue(null)
            }
        }
    }

    /**
     * 进行指定文章的收藏和取消收藏
     * @param srcArticle: 原文章数据
     * @param isCollect: 是否收藏
     */
    fun changeArticleLike(srcArticle: Article, position: Int, isCollect:Boolean) {
        val result = CollectActionBean()
        val type = if (srcArticle.collect) {
            CollectActionBean.TYPE.UNCOLLECT //已收藏，则取消收藏
        } else {
            CollectActionBean.TYPE.COLLECT //已取消收藏，则收藏
        }
        remoteDataSource.enqueue({
            //Ext Fun for API
            if (srcArticle.collect) {
                unCollect(srcArticle.id)
            } else {
                collect(srcArticle.id)
            }
        }) {
            onSuccess {
                ZLog.d("Change article collect success. result=${it}, Is collected? $isCollect")
                srcArticle.collect = isCollect
                result.apply {
                    //change action type
                    actionType = type
                    isSuccess = true
                }
            }

            onFailed {
                result.apply {
                    errorMsg = it.errorMessage
                    actionType = type
                    isSuccess = false
                }
            }

            onFinally {
                ZLog.d("Change artical like onFinally.")
                result.articlePostion = position
                articalCollectData.postValue(result)
            }
        }
    }
}