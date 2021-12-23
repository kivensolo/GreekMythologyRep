package com.zeke.demo.widget

import android.os.Bundle
import com.alibaba.android.arouter.facade.annotation.Route
import com.kingz.module.common.router.RouterConfig
import com.kingz.module.wanandroid.activity.AppBarActivity

/**
 * author：ZekeWang
 * date：2021/12/5
 * description：ToolBar效果测试
 */
@Route(path = RouterConfig.PAGE_TOOLBAR)
class ToolBarTestActivity: AppBarActivity() {
    override fun initView(savedInstanceState: Bundle?) {
        super.initView(savedInstanceState)
    }

    override fun initData(savedInstanceState: Bundle?) {
        defaultFragmentName = ToolBarFragment::class.java.name
        pageTitle = "ToolBar"
        super.initData(savedInstanceState)
    }
}