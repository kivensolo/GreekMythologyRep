package com.zeke.ktx.player.presenter

import android.content.Context
import com.zeke.kangaroo.utils.ZLog
import com.zeke.ktx.player.contract.DemoContract
import com.zeke.ktx.player.entity.DemoGroup
import com.zeke.ktx.player.entity.TimeTableData
import com.zeke.ktx.player.service.DataApiService
import com.zeke.ktx.player.service.DemoApiServiceImpl

/**
 * author：KingZ
 * date：2020/2/8
 * description：首页Demo模块Presenter
 *
 * 构造函数：Bind View和Presenter的关系
 */
class DemoPresenter(var mView: DemoContract.View) :
        DemoContract.Presenter,
        DataApiService.IDataCallback<DemoGroup>{
    var mService: DataApiService<DemoGroup> = DemoApiServiceImpl()

    override fun getDemoInfo(context: Context) {
        mView.showLoading()
        mService.requestData(context,this)
    }

    override fun onResult(data: MutableList<DemoGroup>?) {
        ZLog.d("Kingz onResult=" + data?.size)
        mView.showDemoInfo(data)
    }

    override fun onTimeTables(data: MutableList<TimeTableData>?) {
    }

    override fun onPlayUrl(url: String) {
    }

}