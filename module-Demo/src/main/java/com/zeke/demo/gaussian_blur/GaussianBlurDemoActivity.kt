package com.zeke.demo.gaussian_blur

import android.os.Bundle
import android.view.View
import com.alibaba.android.arouter.facade.annotation.Route
import com.kingz.base.BaseVMActivity
import com.kingz.module.common.router.Router
import com.kingz.module.common.router.RouterConfig
import com.zeke.demo.databinding.ActivityGaussianBlurBinding
import com.zeke.reactivehttp.base.BaseReactiveViewModel

/**
 * author：ZekeWang
 * date：2022/6/23
 * description：图片模糊效果页面
 */
@Route(path = RouterConfig.PAGE_BLUR_LIST)
class GaussianBlurDemoActivity : BaseVMActivity() {
    private lateinit var binding: ActivityGaussianBlurBinding

    override fun getContentView(): View {
        binding = ActivityGaussianBlurBinding.inflate(layoutInflater)
        return binding.root
    }
    override val viewModel: BaseReactiveViewModel
        get() = TODO("Not yet implemented")

    override fun initData(savedInstanceState: Bundle?) {
    }

    override fun initView(savedInstanceState: Bundle?) {
        super.initView(savedInstanceState)
        binding.fiveHundredPx.setOnClickListener {
            Router.startActivity(RouterConfig.PAGE_500px_BLURRING)
        }
        binding.blurryExample.setOnClickListener {
            Router.startActivity(RouterConfig.PAGE_BLURRY)
        }
        binding.jniTestBlur.setOnClickListener {
            Router.startActivity(RouterConfig.PAGE_JNI_TEST_BLUR)
        }
    }
}