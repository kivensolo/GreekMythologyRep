package com.zeke.home.presenters

import android.content.Context
import com.kingz.module.common.api.DataApiService
import com.kingz.module.common.bean.DemoGroup
import com.zeke.home.api.AndroidDemoProvider
import com.zeke.home.contract.DemoContract
import com.zeke.network.response.IRequestResponse

/**
 * author：KingZ
 * date：2020/2/22
 * description：Android Demo页面的Presenter
 */
class DemoPresenter(var mView: DemoContract.View) :
        DemoContract.Presenter, IRequestResponse<MutableList<DemoGroup>> {

    var provider: DataApiService<MutableList<DemoGroup>> = AndroidDemoProvider()

    override fun onSuccess(data: MutableList<DemoGroup>) {
        mView.showDemoInfo(data)
    }

    override fun onError(code: Int, msg: String,
                         data: MutableList<DemoGroup>) {
        mView.showError()
    }

    override fun getDemoInfo(context: Context) {
        mView.showLoading()
        provider.requestData(context,this)
    }
}