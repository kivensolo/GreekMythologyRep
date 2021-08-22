package com.zeke.demo.jetpack.paging

import com.kingz.base.BaseApiService
import okhttp3.OkHttpClient
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import java.util.concurrent.TimeUnit

/**
 * author: King.Z <br>
 * date:  2021/8/22 18:59 <br>
 * description:  <br>
 */
interface ReqresApiService : BaseApiService {

    /**
     * getListData可以在协程中异步进行分页请求
     */
    @GET("api/users")
    suspend fun getUserListData(
        @Query("page") pageNumber: Int
    ): Response<ReqresUserList>


    companion object {
        private val defaultOkHttpClient by lazy {
            OkHttpClient.Builder()
                .readTimeout(10000L, TimeUnit.MILLISECONDS)
                .writeTimeout(10000L, TimeUnit.MILLISECONDS)
                .connectTimeout(10000L, TimeUnit.MILLISECONDS)
                .retryOnConnectionFailure(true).build()
        }

        fun getApiService() = Retrofit.Builder()
            .baseUrl("https://reqres.in/")
            .client(defaultOkHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ReqresApiService::class.java)
    }
}