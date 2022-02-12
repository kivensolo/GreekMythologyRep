package com.zeke.music.repository

import com.zeke.music.api.YinYueTaiApiService
import com.zeke.music.bean.RelatedVideoInfo
import com.zeke.music.bean.VideoInfo
import com.zeke.music.bean.VideoListInfo
import com.zeke.network.OkHttpClientManager
import com.zeke.reactivehttp.datasource.RemoteExtendDataSource
import com.zeke.reactivehttp.viewmodel.IUIActionEvent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * author：ZekeWang
 * date：2021/5/14
 * description：音悦台 远程数据源
 */
class YinYueTaiRemoteDataSource(iActionEvent: IUIActionEvent?) :
    RemoteExtendDataSource<YinYueTaiApiService>(
        iActionEvent = iActionEvent,
        apiServiceClass = YinYueTaiApiService::class.java
    ) {

    override val baseUrl: String
        get() = "https://data.yinyuetai.com"

    override fun onExceptionToastShow(msg: String) {
        //TODO
    }


     companion object {
        private val yinYueTaiHttpClient: OkHttpClient by lazy {
            createHttpClient()
        }

        private fun createHttpClient(): OkHttpClient {
            return OkHttpClientManager.getInstance().okHttpClient
        }
    }

    /**
     * Override create retrofit.
     * Custom Retrofit.
     */
    override fun createRetrofit(baseUrl: String): Retrofit {
        return Retrofit.Builder()
                    .client(yinYueTaiHttpClient)
                    .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        }

    private var apiService = getApiService(baseUrl)

    /**
     * Get video list of yingyuetai
     * @param type Type code of music category.
     * @param pageNum  Number of page.
     * @param pageSize Data size of every page.
     */
    suspend fun getVideoList(type: Int, pageNum: Int = 1,pageSize:Int = 20): VideoListInfo {
        return apiService.getVideoList(type, pageNum, pageSize)
    }

    /**
     * Get video info by videoId
     * @param id video id.
     */
    suspend fun getVideoInfo(id: Int): VideoInfo {
        return apiService.getVideoInfo(id)
    }

     /**
     * Get related video info by videoId
     * @param id video id.
     */
    suspend fun getRelatedVideoList(id: Int): List<RelatedVideoInfo> {
        return apiService.getRelatedVideoList(id)
    }

}