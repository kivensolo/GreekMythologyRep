package com.kingz.coroutines.data.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitBuilder {

    private const val BASE_URL_MOCK_API = "https://5e510330f2c0d300147c034c.mockapi.io/"
    private const val BASE_URL_GITHUB_API = "https://api.github.com"

    val apiService: ApiService = buildRetrofit(BASE_URL_MOCK_API)
            .create(ApiService::class.java)

    val githubApiService: GitHubApiService = buildRetrofit(BASE_URL_GITHUB_API)
            .create(GitHubApiService::class.java)

    private fun buildRetrofit(baseUrl: String): Retrofit {
        return Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
    }
}