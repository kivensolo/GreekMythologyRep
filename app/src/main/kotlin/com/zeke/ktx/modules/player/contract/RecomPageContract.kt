package com.zeke.ktx.modules.player.contract

import android.content.Context
import com.zeke.ktx.base.presenter.IPresenter
import com.zeke.ktx.base.view.IView
import com.zeke.ktx.modules.player.entity.HomeRecomData

/**
 * @description： Contract 将View和Present顶层进行连接
 */
interface RecomPageContract {

    interface View : IView {// 定义特殊业务需要的UI交互接口
        /** 显示获取到的首页推荐信息 */
        fun showRecomInfo(data: MutableList<HomeRecomData>?)
    }

    interface Presenter : IPresenter {
        /** 获取页面详情信息 */
        fun getPageContent(context: Context)
    }
}