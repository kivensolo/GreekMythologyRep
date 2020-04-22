package com.zeke.ktx.modules.player.presenter

import android.content.Context
import com.zeke.ktx.api.AndroidDemoProvider
import com.zeke.ktx.api.DataApiService
import com.zeke.ktx.modules.player.contract.DemoContract
import com.zeke.ktx.modules.player.entity.DemoGroup
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