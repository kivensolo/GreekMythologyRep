package com.kingz.coroutines.data.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitBuilder {

    val apiService: ApiService = buildRetrofit(ApiService.BASE_URL)
            .create(ApiService::class.java)

    val githubApiService: GitHubApiService = buildRetrofit(GitHubApiService.BASE_URL)
            .create(GitHubApiService::class.java)

    val wAndroidApi:WAndroidApi = buildRetrofit(WAndroidApi.BASE_URL)
            .create(WAndroidApi::class.java)

    private fun buildRetrofit(baseUrl: String): Retrofit {
        return Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
    }
}