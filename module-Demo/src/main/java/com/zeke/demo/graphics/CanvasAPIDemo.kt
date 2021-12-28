package com.zeke.demo.graphics

import android.os.Bundle
import android.view.View
import com.alibaba.android.arouter.facade.annotation.Route
import com.kingz.base.BaseVMActivity
import com.kingz.module.common.router.RouterConfig
import com.zeke.demo.databinding.ActivityCanvasApiBinding
import com.zeke.reactivehttp.base.BaseReactiveViewModel

/**
 * author：ZekeWang
 * date：2021/12/28
 * description：
 */
@Route(path = RouterConfig.PAGE_CANVAS_API)
class CanvasAPIDemo : BaseVMActivity() {
    private lateinit var binding: ActivityCanvasApiBinding
    override val viewModel: BaseReactiveViewModel
        get() = TODO("Not yet implemented")

    override fun getContentView(): View? {
        binding = ActivityCanvasApiBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun initData(savedInstanceState: Bundle?) {
    }

    override fun initView(savedInstanceState: Bundle?) {
        super.initView(savedInstanceState)

    }

}