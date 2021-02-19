package com.zeke.ktx.modules.player.presenter

import com.zeke.ktx.modules.player.contract.GithubContract

/**
 * author: King.Z <br>
 * date:  2020/3/4 21:56 <br>
 * description:  <br>
 *     //TODO 后续增加View对象
 *     增提数据请求架构还需要优化
 */
class GithubPresenter:GithubContract.Presenter{
    override fun getUserInfo(account: String) {
    }

    override fun getUserRepos(account: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}