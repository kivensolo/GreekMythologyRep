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
//        launchIO {
        val result = CollectActionBean()
        remoteDataSource.enqueue({
            //Ext Fun for API
            if (srcArticle.collect) {
                unCollect(srcArticle.id)
            } else {
                collect(srcArticle.id)
            }
        }) {
            onSuccess {
                // 可能httpCode正确，但业务code错误
                srcArticle.collect = isCollect
                result.apply {
                    result.apply {
                        //change action type
                        actionType = if (srcArticle.collect) {
                            CollectActionBean.TYPE.UNCOLLECT
                        } else {
                            CollectActionBean.TYPE.COLLECT
                        }
                    }
                    srcArticle.collect = isCollect
                   /* isSuccess = (data.errorCode == 0)
                    // 收藏状态更新成功时，改变Item数据
                    if (isSuccess) {
                        srcArticle.collect = isCollect
                    } else {
                        errorMsg = data.errorMsg ?: "unKnow"
                    }*/
                }
            }

            onFailed {
                result.apply {
                    errorMsg = it.errorMessage
                    actionType = if (srcArticle.collect) {
                        CollectActionBean.TYPE.UNCOLLECT
                    } else {
                        CollectActionBean.TYPE.COLLECT
                    }
                    isSuccess = false
                }
            }

            onFinally {
                result.articlePostion = position
                articalCollectData.postValue(result)
            }
        }
          /*  try {
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
            }*/
//        }
    }
}