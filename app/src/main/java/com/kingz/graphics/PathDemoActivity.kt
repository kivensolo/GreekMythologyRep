package com.kingz.graphics

import android.os.Bundle
import android.view.View
import com.kingz.base.BaseVMActivity
import com.zeke.reactivehttp.base.BaseReactiveViewModel

/**
 * author：ZekeWang
 * date：2021/7/24
 * description：Path绘制
 * drawPath(Path path, Paint paint) 画自定义图形
 * drawPath() 可以绘制自定义图形。
 * 当你要绘制的图形比较特殊，使用前面的那些方法做不到的时候，就可以使用 drawPath() 来绘制
 */
class PathDemoActivity: BaseVMActivity() {

    override val viewModel: BaseReactiveViewModel
        get() = TODO("Not yet implemented")

    override fun getContentView(): View? {
        return PathSampleView(baseContext)
    }

    override fun initView(savedInstanceState: Bundle?) {
        super.initView(savedInstanceState)
    }

    override fun initData(savedInstanceState: Bundle?) {

    }
}