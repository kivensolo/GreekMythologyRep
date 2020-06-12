package com.kingz.mvvm.data.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitBuilder {

    private const val BASE_URL_MOCK_API = "https://5e510330f2c0d300147c034c.mockapi.io/"
    private const val BASE_URL_GITHUB_API = "https://api.github.com"

    private fun getMockApiRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL_MOCK_API)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    private fun getGithubApiRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL_GITHUB_API)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val apiService: ApiService = getMockApiRetrofit().create(ApiService::class.java)

    val githubApiService: GitHubApiService = getGithubApiRetrofit().create(GitHubApiService::class.java)

}