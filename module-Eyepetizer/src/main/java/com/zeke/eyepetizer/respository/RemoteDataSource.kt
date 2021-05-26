package com.zeke.eyepetizer.respository

import com.zeke.eyepetizer.api.EyepetizerApiService
import com.zeke.eyepetizer.bean.EyepetizerTabListInfo
import com.zeke.eyepetizer.bean.EyepetizerTabPageData
import com.zeke.reactivehttp.datasource.RemoteExtendDataSource
import com.zeke.reactivehttp.viewmodel.IUIActionEvent
import retrofit2.http.Url

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
    suspend fun getTabPageDetail(@Url url:String): EyepetizerTabPageData {
        return apiService.requestTabPageData(url)
    }

}