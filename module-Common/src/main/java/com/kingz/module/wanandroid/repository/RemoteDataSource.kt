package com.kingz.module.wanandroid.repository

import android.text.TextUtils
import com.kingz.base.response.ResponseResult
import com.kingz.database.DatabaseApplication
import com.kingz.database.entity.CookiesEntity
import com.kingz.database.entity.UserEntity
import com.kingz.module.common.user.UserInfo
import com.kingz.module.wanandroid.api.WanAndroidApiService
import com.kingz.module.wanandroid.bean.*
import com.kingz.module.wanandroid.response.WanAndroidResponse
import com.zeke.kangaroo.utils.ZLog
import com.zeke.network.OkHttpClientManager
import com.zeke.network.cookie.ICookiesHandler
import com.zeke.network.interceptor.AddCookiesInterceptor
import com.zeke.network.interceptor.SaveCookiesInterceptor
import com.zeke.reactivehttp.datasource.RemoteExtendDataSource
import com.zeke.reactivehttp.viewmodel.IUIActionEvent
import okhttp3.HttpUrl
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

    /**
     * 业务层的cookie处理类
     */
    class CookiesHander : ICookiesHandler {
        private val PATH_LOGIN = "user/login"
        private val PATH_REGISTER = "user/register"
        override fun setCookies(httpUrl: HttpUrl, cookies: String) {
            ZLog.d("setCookies of: ${httpUrl.host()}")
            if (isUserLoginAPI(httpUrl.toString())) {
//                launchIO {
                    val url = httpUrl.url().toString()
                    val host = httpUrl.host().toString()
                    val urlEntity = CookiesEntity(id = 0, url = url, cookies = cookies)
                    val hostEntity = CookiesEntity(id = 1, url = host, cookies = cookies)
                    val cookiesList: MutableList<CookiesEntity> = ArrayList()
                    cookiesList.add(urlEntity)
                    cookiesList.add(hostEntity)
                    //分别为该url和host设置相同的cookie，其中host可选,这样能使得该cookie的应用范围更广
                    val cookiesDao = DatabaseApplication.getInstance().getCookiesDao()
                    cookiesDao.insert(cookiesList)
                    //FIXME 只添加了一个cookie
//                }
            }
        }

        override fun getCookies(httpUrl: HttpUrl): String? {
            val url = httpUrl.url().toString()
            val hostAddr = httpUrl.host()
            if (!TextUtils.isEmpty(hostAddr) &&
                (url.contains("lg/uncollect") //取消收藏站内文章
                        || url.contains("article") // 获取文章列表
                        || url.contains("lg/collect") // 收藏站内文章
                        || url.contains("lg/todo"))
                || url.contains("coin") //积分 API
            ) {
                val cookies: String
//                launchIO {
                    val cookiesDao = DatabaseApplication.getInstance().getCookiesDao()
                    val queryCookies = cookiesDao.getCookies(hostAddr)
                    cookies = queryCookies?.cookies ?: ""
//                }
                return cookies
            }
            return ""
        }

        /**
         * 是否是用户登录或者注册接口
         */
        private fun isUserLoginAPI(url: String): Boolean {
            return if (TextUtils.isEmpty(url)) {
                false
            } else url.contains(PATH_LOGIN) || url.contains(PATH_REGISTER)
        }
    }

    companion object {
        private val wanAndroidHttpClient: OkHttpClient by lazy {
            createHttpClient()
        }

        private fun createHttpClient(): OkHttpClient {
            val cookiesHander = CookiesHander()
            val okHttpClientManager = OkHttpClientManager.getInstance()
            okHttpClientManager.setBuilderFactory(object :
                OkHttpClientManager.IClientBuilderFactory {
                override fun getPreInterceptors(): MutableList<Interceptor>? {
                    val list = ArrayList<Interceptor>()
                    list.add(AddCookiesInterceptor(cookiesHander))
                    list.add(SaveCookiesInterceptor(cookiesHander))
                    return list
                }

                override fun getPostInterceptors(): MutableList<Interceptor>? {
                    return null
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