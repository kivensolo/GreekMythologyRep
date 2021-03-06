package com.kingz.module.wanandroid.repository

import com.kingz.base.response.ResponseResult
import com.kingz.database.entity.UserEntity
import com.kingz.module.common.user.UserInfo
import com.kingz.module.wanandroid.api.WanAndroidApiService
import com.kingz.module.wanandroid.bean.*
import com.kingz.module.wanandroid.response.WanAndroidResponse
import com.zeke.kangaroo.utils.ZLog
import com.zeke.reactivehttp.datasource.RemoteExtendDataSource
import com.zeke.reactivehttp.viewmodel.IUIActionEvent
import retrofit2.Retrofit

/**
 * author：ZekeWang
 * date：2021/3/5
 * description： 玩Android的DataSource
 *  Repository的概念分为remote-DataSource和local-DataSource
 */
open class WanAndroidRemoteDataSource(iActionEvent: IUIActionEvent?)
    : RemoteExtendDataSource<WanAndroidApiService>(
    iActionEvent = iActionEvent,
    apiServiceClass = WanAndroidApiService::class.java
) {

//    companion object {
//        private val httpClient: OkHttpClient by lazy {
//            createHttpClient()
//        }
//
//        private fun createHttpClient(): OkHttpClient {
//            val builder = OkHttpClient.Builder()
//                    .readTimeout(1000L, TimeUnit.MILLISECONDS)
//                    .writeTimeout(1000L, TimeUnit.MILLISECONDS)
//                    .connectTimeout(1000L, TimeUnit.MILLISECONDS)
//                    .retryOnConnectionFailure(true)
//            return builder.build()
//        }
//    }

    final override val baseUrl: String
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
//        return Retrofit.Builder()
//                    .client(httpClient)
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
class HomeDataSource(iActionEvent: IUIActionEvent?) : WanAndroidRemoteDataSource(iActionEvent) {

    /** 进行文章列表获取 */
    suspend fun getArticals(pageId: Int = 0): WanAndroidResponse<ArticleData> {
        return apiService.requestArticles(pageId)
    }

    suspend fun getBannerData(): ResponseResult<MutableList<BannerItem>>? {
        ZLog.d("get Banner ---> ")
        return apiService.bannerData()
    }
}

/**
 * 用户相关信息数据源
 * 登录 & 注册等
 */
class LoginDataSource(iActionEvent: IUIActionEvent?) : WanAndroidRemoteDataSource(iActionEvent) {
    /**
     * 进行用户登录操作
     */
    suspend fun userLogin(name: String = "", password: String = ""): UserInfoBean {
        return apiService.userLogin(name, password)
    }

    /**
     * 进行用户注册操作
     */
    suspend fun userRegister(name: String = "",
                             password: String = "",
                             rePassword: String = ""): WanAndroidResponse<UserInfoBean> {
        return apiService.userRegister(name, password, rePassword)
    }
}