package com.zeke.ktx.modules.home.http

import com.kingz.net.retrofit.mannager.ApiManager
import com.zeke.ktx.api.GitHubService
import com.zeke.ktx.modules.home.model.GitHubUserInfo
import io.reactivex.Observable

/**
 * author: King.Z <br>
 * date:  2020/4/21 22:28 <br>
 * description:  <br>
 *     TODO 应该提出接口层
 */
object GithubRequest {
    //TODO 为每个请求可以自定义BaseUrl
    private val gitHubService = ApiManager.i().setApi(GitHubService::class.java)

    fun getUserInfo(name:String?): Observable<GitHubUserInfo>? {
        return gitHubService.getUserInfo(name)
    }
}