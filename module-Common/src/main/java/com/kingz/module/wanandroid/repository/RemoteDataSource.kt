package com.kingz.module.wanandroid.repository

import com.kingz.base.response.ResponseResult
import com.kingz.database.entity.UserEntity
import com.kingz.module.common.user.UserInfo
import com.kingz.module.wanandroid.api.WanAndroidApiService
import com.kingz.module.wanandroid.bean.Article
import com.kingz.module.wanandroid.bean.ArticleData
import com.kingz.module.wanandroid.bean.BannerItem
import com.kingz.module.wanandroid.bean.CollectBean
import com.kingz.module.wanandroid.response.WanAndroidResponse
import com.kingz.module.wanandroid.viewmodel.WanAndroidViewModelV2
import com.zeke.kangaroo.utils.ZLog
import com.zeke.reactivehttp.datasource.RemoteExtendDataSource
import retrofit2.Retrofit

/**
 * author：ZekeWang
 * date：2021/3/5
 * description： 玩Android的DataSource
 *  Repository的概念分为remote-DataSource和local-DataSource
 */
open class WanAndroidRemoteDataSource : RemoteExtendDataSource<WanAndroidApiService>(
    iActionEvent = WanAndroidViewModelV2(),
    apiServiceClass = WanAndroidApiService::class.java
) {
    override val baseUrl: String
        get() = "https://www.wanandroid.com"

    override fun showToast(msg: String) {
        //TODO
    }

    open var apiService = getApiService(baseUrl)

    /**
     * 获取用户信息
     */
    suspend fun getUserInfo(): UserEntity? {
        return UserInfo.getUserInfor()
    }

    /**
     * 重写Retrofit创建，用于自定义的Retrofit
     */
    override fun createRetrofit(baseUrl: String): Retrofit {
//        val client = OkHttpClient.Builder()
//                    .readTimeout(10000L, TimeUnit.MILLISECONDS)
//                    .writeTimeout(10000L, TimeUnit.MILLISECONDS)
//                    .connectTimeout(10000L, TimeUnit.MILLISECONDS)
//                    .retryOnConnectionFailure(true).build()
//        return Retrofit.Builder()
//                    .client(client)
//                    .baseUrl(baseUrl)
//                    .addConverterFactory(GsonConverterFactory.create())
//                    .build()
        return super.createRetrofit(baseUrl)
    }

    /**
     * 文章收藏和取消的接口
     * 当前文章未收藏，则进行收藏.
     * 收藏了则取消收藏.
     */
    suspend fun changeArticleLike(item: Article): WanAndroidResponse<CollectBean> {
        return if (item.collect) {
            ZLog.d("unCcollect article id = (${item.id})")
            apiService.unCollect(item.id)
        } else {
            ZLog.d("collect article id = (${item.id})")
            apiService.collect(item.id)
        }
    }

}

/**
 * 首页相关数据远
 */
class HomeDataSource:WanAndroidRemoteDataSource(){

    /** 进行文章列表获取 */
    suspend fun getArticals(pageId: Int = 0): WanAndroidResponse<ArticleData> {
        return apiService.requestArticles(pageId)
    }

    suspend fun getBannerData(): ResponseResult<MutableList<BannerItem>>? {
        ZLog.d("get Banner ---> ")
        return apiService.bannerData()
    }
}