package com.zeke.ktx.api

import com.zeke.ktx.modules.home.model.GitHubRepo
import com.zeke.ktx.modules.home.model.GitHubUserInfo
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Path

/**
 * 定义一个接口配置网络请求;
 * 把网络接口统一放到一个接口类中，
 * 让Retrofit创建接口实例类来方便调用
 */
interface GitHubService {
    @GET("users/{user}")
    fun getUserInfo(@Path("user") user: String?): Observable<GitHubUserInfo>?

    @GET("users/{user}/repos")
    fun listRepos(@Path("user") user: String?): Observable<List<GitHubRepo>?>?
}