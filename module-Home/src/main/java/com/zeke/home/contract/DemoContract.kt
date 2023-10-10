package com.zeke.home.contract

import android.content.Context
import com.kingz.module.common.IView
import com.kingz.module.common.base.IPresenter
import com.kingz.module.common.bean.DemoGroup

/**
 * author：KingZ
 * date：2020/2/22
 * description：demo数据的连接层
 */
interface DemoContract {
    interface View : IView {// 定义特殊业务需要的UI交互接口
        /** 显示获取到的首页推荐信息 */
        fun showDemoInfo(data: MutableList<DemoGroup>)
    }

    interface Presenter : IPresenter {
        /** 获取页面详情信息 */
        fun getDemoInfo(context: Context)
    }
}