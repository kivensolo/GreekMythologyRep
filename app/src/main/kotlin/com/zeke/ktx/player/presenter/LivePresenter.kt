package com.zeke.ktx.player.presenter

import android.content.Context
import com.zeke.ktx.player.contract.LiveContract
import com.zeke.ktx.player.entity.Live
import com.zeke.ktx.player.entity.TimeTableData
import com.zeke.ktx.player.service.DataApiService
import com.zeke.ktx.player.service.LiveApiServiceImpl

/**
 * author：KingZ
 * date：2020/2/8
 * description：直播模块Presenter
 *
 * 构造函数：Bind View和Presenter的关系
 */
class LivePresenter(var mView: LiveContract.View) :
        LiveContract.Presenter,
        DataApiService.IDataCallback<Live>{

    var mService: DataApiService<Live> = LiveApiServiceImpl()


    override fun getLiveInfo(context: Context) {
        mView.showLoading()
        mService.requestData(context,this)
    }

    override fun getPlayUrl(context: Context, url: String) {
        mView.showVideo(url)
        mView.hideLoading()
    }

    override fun onResult(data: MutableList<Live>?) {
        mView.showLiveInfo(data)
    }

    override fun onTimeTables(data: MutableList<TimeTableData>?) {
    }

    override fun onPlayUrl(url: String) {
    }

}