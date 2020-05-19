package com.kingz.module.common.model

import com.kingz.module.common.http.GithubRequest
import com.zeke.network.response.IRequestResponse

/**
 * author: King.Z <br>
 * date:  2020/4/21 22:53 <br>
 * description:  <br>
 *     //TODO 需进行基类BaseModel的处理
 */
class HomeGithubModel {

    fun getGithubRepository(listener:IRequestResponse<GitHubUserInfo>){
        val name = "kivensolo"
        GithubRequest.getUserInfo(name)
    }
}