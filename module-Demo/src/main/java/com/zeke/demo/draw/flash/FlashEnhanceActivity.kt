package com.zeke.demo.draw.flash

import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.alibaba.android.arouter.facade.annotation.Route
import com.kingz.base.BaseVMActivity
import com.kingz.module.common.databinding.IncludeBaseRecyclerViewBinding
import com.kingz.module.common.router.RouterConfig
import com.zeke.demo.R
import com.zeke.reactivehttp.base.BaseReactiveViewModel

/**
 * author：ZekeWang
 * date：2025/4/14
 * description：增强型光带扫描Render 的DEMO页面
 */
@Route(path = RouterConfig.PAGE_FLASH_SCAN)
class FlashEnhanceActivity :BaseVMActivity() {
    private lateinit var rcViewBinding: IncludeBaseRecyclerViewBinding
    var mRecyclerView: RecyclerView? = null;
    override val viewModel: BaseReactiveViewModel
        get() = TODO("Not yet implemented")

    override fun initData(savedInstanceState: Bundle?) {
    }
    override fun getContentView(): View? {
        rcViewBinding = IncludeBaseRecyclerViewBinding.inflate(layoutInflater)
        return rcViewBinding.root
    }

    override fun initView(savedInstanceState: Bundle?) {
        super.initView(savedInstanceState)

        mRecyclerView = findViewById(R.id.recycler_view)
        mRecyclerView?.apply {
            this.layoutManager = GridLayoutManager(this@FlashEnhanceActivity, 2)
            this.adapter = FlashDemoRecyclerAdapter(R.layout.item_flash_view, createDataList())
            this.setBackgroundColor(Color.DKGRAY)
        }
    }

    //生成测试数据List
    private fun createDataList(): MutableList<FlashAttrsBean> {
        val colorsArray = intArrayOf(
            Color.TRANSPARENT,
            Color.parseColor("#39FF0000"),
            Color.parseColor("#BF00FF00"),
            Color.parseColor("#390000FF"),
            Color.TRANSPARENT
        )
        val list: MutableList<FlashAttrsBean> = ArrayList()
        list.add(FlashAttrsBean(mode = "surface", colors = colorsArray, gradientRatio = 0.5f, angle = 0))
        list.add(FlashAttrsBean(mode = "surface", colors = colorsArray, gradientRatio = 0.5f, angle = 180))
        list.add(FlashAttrsBean(mode = "surface", colors = colorsArray, gradientRatio = 0.5f, angle = 135))
        list.add(FlashAttrsBean(mode = "surface", colors = colorsArray, gradientRatio = 0.5f, angle = 315))

        val paralleColorsArray = intArrayOf(
            Color.TRANSPARENT,
            Color.parseColor("#39FF0000"),
            Color.parseColor("#BF00FF00"),
            Color.parseColor("#390000FF"),
            Color.TRANSPARENT
        )
        list.add(FlashAttrsBean(mode = "edge_race", colors = paralleColorsArray, gradientRatio = 0.6f, angle = 225))
        list.add(FlashAttrsBean(mode = "edge_race", colors = paralleColorsArray, gradientRatio = 0.6f, angle = 45))

        val greedyColorsArray = intArrayOf(
            Color.parseColor("#190000FF"),
            Color.parseColor("#330000FF"),
            Color.parseColor("#6600FF00"),
            Color.parseColor("#FF00FF00"),
        )
        list.add(FlashAttrsBean(mode = "edge_greedy", colors = greedyColorsArray, gradientRatio = 0.6f, angle = 0))
        list.add(FlashAttrsBean(mode = "edge_greedy", colors = greedyColorsArray, gradientRatio = 0.6f, angle = 45))
        list.add(FlashAttrsBean(mode = "edge_greedy", colors = greedyColorsArray, gradientRatio = 0.6f, angle = 90))
        list.add(FlashAttrsBean(mode = "edge_greedy", colors = greedyColorsArray, gradientRatio = 0.6f, angle = 125))
        list.add(FlashAttrsBean(mode = "edge_greedy", colors = greedyColorsArray, gradientRatio = 0.6f, angle = 180))
        return list
    }
}