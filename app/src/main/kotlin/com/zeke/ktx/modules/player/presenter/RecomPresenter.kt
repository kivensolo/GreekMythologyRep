package com.zeke.ktx.modules.player.presenter

import android.content.Context
import com.zeke.kangaroo.utils.ZLog
import com.zeke.ktx.api.DataApiService
import com.zeke.ktx.api.RecomDataServiceImpl
import com.zeke.ktx.api.callback.IDataResponse
import com.zeke.ktx.modules.player.contract.RecomPageContract
import com.zeke.ktx.modules.player.entity.HomeRecomData

/**
 * author：KingZ
 * date：2020/2/8
 * description：首页Demo模块Presenter
 *
 * 构造函数：Bind View和Presenter的关系
 */
class RecomPresenter(var mView: RecomPageContract.View) :
        RecomPageContract.Presenter, IDataResponse<MutableList<HomeRecomData>> {

//    var mService: DataApiService<DemoGroup> = AndroidDemoProvider()
    var mService: DataApiService<MutableList<HomeRecomData>> = RecomDataServiceImpl()

    override fun getPageContent(context: Context) {
        mView.showLoading()
        mService.requestData(context, this)
    }

    override fun onSuccess(data: MutableList<HomeRecomData>) {
        ZLog.d("RecomPresenter", "onResult=" + data.size)
        mView.showRecomInfo(data)
    }

    override fun onError(code: Int, msg: String, data: MutableList<HomeRecomData>) {
    }


}