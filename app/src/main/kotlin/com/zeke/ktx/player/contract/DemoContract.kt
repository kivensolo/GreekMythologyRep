package com.zeke.ktx.player.contract

import android.content.Context
import com.zeke.ktx.base.presenter.IPresenter
import com.zeke.ktx.base.view.IView
import com.zeke.ktx.player.entity.DemoGroup

/**
 * @description： Contract 将View和Present顶层进行连接
 */
interface DemoContract {

    interface View : IView {// 定义特殊业务需要的UI交互接口
        /** 显示获取到的Demo信息 */
        fun showDemoInfo(data: MutableList<DemoGroup>?)
    }

    interface Presenter : IPresenter {
        /** 获取Demo列表信息 */
        fun getDemoInfo(context: Context)
    }
}