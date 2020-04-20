package com.zeke.ktx.modules.player.contract

import com.zeke.ktx.base.presenter.IPresenter

interface GithubContract {
    interface Presenter : IPresenter {
        fun getUserInfo(account: String)
        fun getUserRepos(account: String)
    }
}