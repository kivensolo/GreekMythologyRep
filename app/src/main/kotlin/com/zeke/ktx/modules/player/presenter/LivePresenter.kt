package com.zeke.ktx.modules.player.presenter

import android.content.Context
import com.zeke.ktx.api.DataApiService
import com.zeke.ktx.api.LiveDataProvider
import com.zeke.ktx.api.callback.LiveDataResponse
import com.zeke.ktx.modules.player.contract.LiveContract
import com.zeke.ktx.modules.player.entity.Live
import com.zeke.ktx.modules.player.entity.TimeTableData

/**
 * author：KingZ
 * date：2020/2/8
 * description：直播模块Presenter
 *
 * 构造函数：Bind View和Presenter的关系
 */
class LivePresenter(var mView: LiveContract.View) :
        LiveContract.Presenter,
        LiveDataResponse {

    var mService: DataApiService<MutableList<Live>> = LiveDataProvider()


    override fun getLiveInfo(context: Context) {
        mView.showLoading()
        mService.requestData(context,this)
    }

    override fun getPlayUrl(context: Context, url: String) {
        mView.showVideo(url)
        mView.hideLoading()
    }

    override fun onSuccess(data: MutableList<Live>) {
        mView.showLiveInfo(data)
    }

    override fun onError(code: Int, msg: String, data: MutableList<Live>) {
//        mView.showError()
    }

    override fun onTimeTables(data: MutableList<TimeTableData>?) {
    }

    override fun onPlayUrl(url: String) {
    }

}