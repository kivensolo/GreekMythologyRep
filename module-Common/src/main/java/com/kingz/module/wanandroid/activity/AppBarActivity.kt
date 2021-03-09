package com.kingz.module.wanandroid.activity

import android.os.Bundle
import android.view.LayoutInflater
import androidx.activity.viewModels
import com.alibaba.android.arouter.facade.annotation.Route
import com.kingz.base.BaseVMActivity
import com.kingz.base.factory.ViewModelFactory
import com.kingz.module.common.R
import com.kingz.module.common.router.RPath
import com.kingz.module.wanandroid.WADConstants
import com.kingz.module.wanandroid.viewmodel.WanAndroidViewModelV2
import com.zeke.kangaroo.utils.ToastUtils
import com.zeke.reactivehttp.base.BaseReactiveViewModel

/**
 * author：ZekeWang
 * date：2021/2/28
 * description：带有AppBar的Layout页面
 */
@Route(path = RPath.PAGE_Common)
class AppBarActivity : BaseVMActivity() {
    override val viewModel: BaseReactiveViewModel by viewModels {
        ViewModelFactory.build { WanAndroidViewModelV2() }
    }

    override fun getContentLayout(): Int = R.layout.layout_appbar_recycler_list_page

    override fun initData(savedInstanceState: Bundle?) {
        val intent = intent
        if (intent == null) {
            ToastUtils.show(baseContext, "intent data is null")
            finish()
            return
        }
        val type = intent.getIntExtra(WADConstants.Key.KEY_FRAGEMTN_TYPE, 0)
        if (type == 0) {
            ToastUtils.show(baseContext, "非法类型参数")
            return
        }

        when (type) {
            WADConstants.Type.TYPE_TAB_COLLECT -> {
                //TODO 进行页面填充
                val inflateView =
                    LayoutInflater.from(this).inflate(R.layout.layout_appbar_recyclerview, null)
                addContentView(inflateView, inflateView.layoutParams)
            }
        }
    }
}