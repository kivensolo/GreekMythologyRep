package com.kingz.module.common.http

import com.kingz.module.common.api.GitHubService
import com.kingz.module.common.model.GitHubUserInfo
import com.zeke.network.retrofit.mannager.Api
import io.reactivex.Single

/**
 * author: King.Z <br>
 * date:  2020/4/21 22:28 <br>
 * description:  <br>
 *     TODO 应该提出接口层
 */
object GithubRequest {
    //TODO 为每个请求可以自定义BaseUrl
    private val gitHubService = Api.getInstance().registeServer(GitHubService::class.java)

    fun getUserInfo(name:String?): Single<GitHubUserInfo>? {
//        return gitHubService.getUserInfo(name)
        return null
    }
}