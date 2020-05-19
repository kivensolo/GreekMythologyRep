package com.kingz.module.common.http

import com.kingz.module.common.model.GitHubUserInfo
import com.kingz.module.common.service.GitHubService
import com.zeke.network.retrofit.mannager.ApiManager
import io.reactivex.Single

/**
 * author: King.Z <br>
 * date:  2020/4/21 22:28 <br>
 * description:  <br>
 *     TODO 应该提出接口层
 */
object GithubRequest {
    //TODO 为每个请求可以自定义BaseUrl
    private val gitHubService = ApiManager.i().setApi(GitHubService::class.java)

    fun getUserInfo(name:String?): Single<GitHubUserInfo>? {
        return gitHubService.getUserInfo(name)
    }
}