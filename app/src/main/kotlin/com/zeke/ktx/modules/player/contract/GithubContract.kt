package com.zeke.ktx.modules.player.contract

import com.kingz.module.common.base.IPresenter

interface GithubContract {
    interface Presenter : IPresenter {
        fun getUserInfo(account: String)
        fun getUserRepos(account: String)
    }
}