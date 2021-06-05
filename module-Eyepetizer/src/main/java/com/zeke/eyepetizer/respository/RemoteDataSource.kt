package com.zeke.eyepetizer.respository

import com.zeke.eyepetizer.api.EyepetizerApiService
import com.zeke.eyepetizer.bean.EyepetizerTabListInfo
import com.zeke.eyepetizer.bean.EyepetizerTabPageData
import com.zeke.network.interceptor.CommonParamsInterceptor
import com.zeke.network.interceptor.LoggingInterceptor
import com.zeke.reactivehttp.datasource.RemoteExtendDataSource
import com.zeke.reactivehttp.viewmodel.IUIActionEvent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Url
import java.util.concurrent.TimeUnit

/**
 * 开眼视频远程数据源
 */
open class EyepetizerRemoteDataSource(iActionEvent: IUIActionEvent?)
    : RemoteExtendDataSource<EyepetizerApiService>(
    iActionEvent = iActionEvent,
    apiServiceClass = EyepetizerApiService::class.java
){
    override val baseUrl: String = "http://baobab.kaiyanapp.com"

    open var apiService = getApiService(baseUrl)

    companion object {
        private val eyepetizerOkHttpClient: OkHttpClient by lazy {
            //添加公共请求参数, 基于开眼APK版本:4.3.381
            // https://github.com/1136535305/Eyepetizer/wiki/%E5%BC%80%E7%9C%BC-API-%E6%8E%A5%E5%8F%A3%E5%88%86%E6%9E%90
            val commonParamsInterceptor = CommonParamsInterceptor.Builder()
                .addQueryParam("udid", "435865baacfc49499632ea13c5a78f944c2f28aa")
                .addQueryParam("vc", "381")
                .addQueryParam("vn", "4.3")
                .addQueryParam("deviceModel", "DUK-AL20")
                .addQueryParam("first_channel", "eyepetizer_360_market")
                .addQueryParam("last_channel", "eyepetizer_360_market")
                .addQueryParam("system_version_code", "26")
                .build()

            OkHttpClient.Builder()
                .readTimeout(10000L, TimeUnit.MILLISECONDS)
                .writeTimeout(10000L, TimeUnit.MILLISECONDS)
                .connectTimeout(10000L, TimeUnit.MILLISECONDS)
                .retryOnConnectionFailure(true)
                .addInterceptor(commonParamsInterceptor)
                .addInterceptor(LoggingInterceptor())
                .build()
        }
    }

    override fun createRetrofit(baseUrl: String): Retrofit {
        return Retrofit.Builder()
            .client(eyepetizerOkHttpClient)
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    override fun showToast(msg: String) {
    }

    /**
     * 获取分类列表
     */
    suspend fun getTabList(): EyepetizerTabListInfo {
        return apiService.requestTabList()
    }

    /**
     * 获取分类的页面详情数据
     */
    suspend fun getTabPageDetail(@Url url: String): EyepetizerTabPageData {
        return apiService.requestTabPageData(url)
    }

}