package com.kingz.module.wanandroid.repository

import com.kingz.base.response.ResponseResult
import com.kingz.database.entity.UserEntity
import com.kingz.module.common.CommonApp
import com.kingz.module.common.user.UserInfo
import com.kingz.module.wanandroid.api.WanAndroidApiService
import com.kingz.module.wanandroid.bean.*
import com.kingz.module.wanandroid.response.WanAndroidResponse
import com.zeke.kangaroo.utils.ZLog
import com.zeke.network.OkHttpClientManager
import com.zeke.network.interceptor.AddCookiesInterceptor
import com.zeke.network.interceptor.SaveCookiesInterceptor
import com.zeke.reactivehttp.datasource.RemoteExtendDataSource
import com.zeke.reactivehttp.viewmodel.IUIActionEvent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

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

    companion object {
        private val wanAndroidHttpClient: OkHttpClient by lazy {
            createHttpClient()
        }

        private fun createHttpClient(): OkHttpClient {
            val okHttpClientManager = OkHttpClientManager.getInstance()
            okHttpClientManager.setBuilderFactory(object :
                OkHttpClientManager.ClientBuilderFactory() {
                override fun getPreInterceptors(): MutableList<Interceptor> {
                    val list = ArrayList<Interceptor>()
                    list.add(AddCookiesInterceptor(CommonApp.getInstance().applicationContext))
                    list.add(SaveCookiesInterceptor(CommonApp.getInstance().applicationContext))
                    return list
                }
            })
            return okHttpClientManager.okHttpClient
        }
    }

    final override val baseUrl: String
        get() = "https://www.wanandroid.com"

    override fun showToast(msg: String) {
        //TODO
    }

    open var apiService = getApiService(baseUrl)

    /**
     * 获取用户信息
     */
    suspend fun getUserInfoFromLocal(): UserEntity? {
        return UserInfo.getUserInfor()
    }

    /**
     * 重写Retrofit创建，用于自定义的Retrofit
     */
    override fun createRetrofit(baseUrl: String): Retrofit {
        return Retrofit.Builder()
                    .client(wanAndroidHttpClient)
                    .baseUrl(baseUrl)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
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

    /**
     * 获取登录用户的收藏文章列表数据
     */
    suspend fun getArticleList(pageIndex: Int): WanAndroidResponse<CollectListBean>{
        return apiService.collectList(pageIndex)
    }

    /************************ 用户相关信息数据  START ************************/
    /**
     * 用户登录
     * @param name: 用户名
     * @param password: 用户密码
     */
    suspend fun userLogin(name: String = "", password: String = ""): UserInfoBean {
        ZLog.d("user Login: name=$name password=******")
        return apiService.userLogin(name, password)
    }

    /**
     * 用户登出
     */
    suspend fun userLogout():UserInfoBean{
        ZLog.e("userLogout()")
        val result = apiService.userLogout()
        if(result.errorCode == 0){
            launchIO {
                UserInfo.clearLocalUserInfo()
            }
        }
        return result
    }

    /**
     * 进行用户注册操作
     * @param name: 用户名
     * @param password: 用户密码
     * @param rePassword: 二次输入的用户密码
     */
    suspend fun userRegister(name: String = "",
                             password: String = "",
                             rePassword: String = ""): WanAndroidResponse<UserInfoBean> {
        return apiService.userRegister(name, password, rePassword)
    }
    /************************ 用户相关信息数据  END************************/

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