package com.zeke.home.contract

import android.content.Context
import com.kingz.module.common.IView
import com.kingz.module.common.base.IPresenter
import com.zeke.home.entity.TemplatePageData

/**
 * @description： Contract 将View和Present顶层进行连接
 */
interface RecomPageContract {

    interface View : IView {// 定义特殊业务需要的UI交互接口
        /** 显示获取到的首页推荐信息 */
        fun showRecomInfo(data: MutableList<TemplatePageData>?)
    }

    interface Presenter : IPresenter {
        /** 获取页面详情信息 */
        fun getPageContent(context: Context)
    }
}