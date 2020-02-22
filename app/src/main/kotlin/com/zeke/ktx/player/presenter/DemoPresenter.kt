package com.zeke.ktx.player.presenter

import android.content.Context
import android.view.View
import com.zeke.ktx.api.AndroidDemoProvider
import com.zeke.ktx.api.DataApiService
import com.zeke.ktx.api.callback.IDataResponse
import com.zeke.ktx.player.contract.DemoContract
import com.zeke.ktx.player.entity.DemoGroup

/**
 * author：KingZ
 * date：2020/2/22
 * description：Android Demo页面的Presenter
 */
class DemoPresenter(var mView: DemoContract.View) :
        DemoContract.Presenter, IDataResponse<MutableList<DemoGroup>> {

    var provider: DataApiService<MutableList<DemoGroup>> = AndroidDemoProvider()

    override fun onSucess(data: MutableList<DemoGroup>) {
        mView.showDemoInfo(data)
    }

    override fun onError(code: Int, msg: String,
                         data: MutableList<DemoGroup>) {
        mView.showError(View.OnClickListener {

        })
    }

    override fun getDemoInfo(context: Context) {
        mView.showLoading()
        provider.requestData(context,this)
    }
}