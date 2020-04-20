package com.zeke.ktx.modules.player.contract

import android.content.Context
import com.zeke.ktx.base.presenter.IPresenter
import com.zeke.ktx.base.view.IView
import com.zeke.ktx.modules.player.entity.Live
import com.zeke.ktx.modules.player.entity.TimeTableData

/**
 * @description： Contract 将View和Present顶层进行连接
 */
interface LiveContract {

    interface View : IView {// 定义特殊业务需要的UI交互接口
        /**
         * 显示获取到的直播频道信息
         */
        fun showLiveInfo(data: MutableList<Live>?)
        fun showTimeTable(data: MutableList<TimeTableData>)
        /**
         * 显示播放器
         */
        fun showVideo(url: String)
    }

    interface Presenter : IPresenter {
        /**
         * 获取直播频道信息
         */
        fun getLiveInfo(context: Context)
        fun getPlayUrl(context: Context,url: String)
    }
}