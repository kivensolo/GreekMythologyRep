package com.kingz.coroutines.data.api

import com.kingz.coroutines.data.api.wandroid.WAndroidApi
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * TODO
 */
@Deprecated("后续进行封装和替换")
object RetrofitBuilder {

    val USER_SERVICE_API: UserServiceApi = buildRetrofit(UserServiceApi.BASE_URL)
            .create(UserServiceApi::class.java)

    val githubApiService: GitHubApiService = buildRetrofit(GitHubApiService.BASE_URL)
            .create(GitHubApiService::class.java)

    val wAndroidApi: WAndroidApi = buildRetrofit(WAndroidApi.BASE_URL)
            .create(WAndroidApi::class.java)

    val loggingInterceptor = HttpLoggingInterceptor(HttpLoggingInterceptor.Logger {})
            .setLevel(HttpLoggingInterceptor.Level.BODY)

    private fun buildRetrofit(baseUrl: String): Retrofit {
        return Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
//                .client(getcutomhttpclient())
                .build()
    }
}