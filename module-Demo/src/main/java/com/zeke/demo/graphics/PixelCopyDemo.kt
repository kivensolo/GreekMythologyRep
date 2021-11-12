package com.zeke.demo.graphics

import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.view.PixelCopy
import android.view.View
import com.alibaba.android.arouter.facade.annotation.Route
import com.kingz.base.BaseVMActivity
import com.kingz.module.common.router.RouterConfig
import com.kingz.module.common.utils.UIUtils
import com.zeke.demo.databinding.ActivityPixelcopyBinding
import com.zeke.reactivehttp.base.BaseReactiveViewModel

/**
 * author：ZekeWang
 * date：2021/11/12
 * description：PixelCopy API使用Demo
 * PixelCopy提供了像素复制请求的机制，允许从 Surface 复制像素到 Bitmap
 *
 * 【官方原话】:
 * For screenshots of the UI for feedback reports or
 * unit testing the {@link PixelCopy} API is recommended.
 */
@Route(path = RouterConfig.PAGE_PIXEL_COPY)
class PixelCopyDemo : BaseVMActivity() {
    private var shotBitmap:Bitmap?= null
    private lateinit var binding: ActivityPixelcopyBinding
    override val viewModel: BaseReactiveViewModel
        get() = TODO("Not yet implemented")

    override fun getContentView(): View? {
        binding = ActivityPixelcopyBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun initData(savedInstanceState: Bundle?) {
    }

    override fun initView(savedInstanceState: Bundle?) {
        super.initView(savedInstanceState)
        binding.copyPixel.setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                shotBitmap = UIUtils.viewShotAfterOreo(binding.savePic,0.5f) {
                    if (it == PixelCopy.SUCCESS) {
                        binding.picImageView.setImageBitmap(shotBitmap)
                    }
                }
            }
        }
        binding.savePic.setOnClickListener {
            shotBitmap?.apply {
                UIUtils.saveImageToGallery(this@PixelCopyDemo,this,"测试文件")
            }
        }
    }

}