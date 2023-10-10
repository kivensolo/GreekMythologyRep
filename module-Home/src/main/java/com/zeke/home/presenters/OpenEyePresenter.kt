package com.zeke.home.presenters

import android.content.Context
import com.kingz.module.common.api.DataApiService
import com.zeke.home.api.RecomDataServiceImpl
import com.zeke.home.contract.RecomPageContract
import com.zeke.home.entity.TemplatePageData
import com.zeke.kangaroo.zlog.ZLog
import com.zeke.network.response.IRequestResponse

/**
 * author：KingZ
 * date：2021/5/23
 * description：
 * 首页开眼视频模块Presenter
 */
class OpenEyePresenter(var mView: RecomPageContract.View) :
        RecomPageContract.Presenter, IRequestResponse<MutableList<TemplatePageData>> {

//    var mService: DataApiService<DemoGroup> = AndroidDemoProvider()
    var mService: DataApiService<MutableList<TemplatePageData>> = RecomDataServiceImpl()

    override fun getPageContent(context: Context) {
        mView.showLoading()
        mService.requestData(context, this)
    }

    override fun onSuccess(data: MutableList<TemplatePageData>) {
        ZLog.d("RecomPresenter", "onResult=" + data.size)
        mView.showRecomInfo(data)
    }

    override fun onError(code: Int, msg: String, data: MutableList<TemplatePageData>) {
    }


}