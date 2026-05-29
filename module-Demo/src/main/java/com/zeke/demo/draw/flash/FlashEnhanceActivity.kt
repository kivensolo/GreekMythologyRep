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
        //冰蓝光晕：深天蓝 → 浅钢蓝 → 浅蓝白 → 纯白 → 浅蓝白 → ...，呈现冷色调的玻璃高光扫过效果
        val surfaceColors = intArrayOf(
            Color.TRANSPARENT,
            Color.parseColor("#1A00BFFF"),
            Color.parseColor("#5587CEEB"),
            Color.parseColor("#CCE0FFFF"),
            Color.parseColor("#FFFFFF"),
            Color.parseColor("#CCE0FFFF"),
            Color.parseColor("#5587CEEB"),
            Color.parseColor("#1A00BFFF"),
            Color.TRANSPARENT
        )
        val list: MutableList<FlashAttrsBean> = ArrayList()
        list.add(FlashAttrsBean(FlashEnhanceView.MODE_SURFACE, surfaceColors,
            gradientRatio = 0.5f, angle = 0)
        )
        list.add(FlashAttrsBean(FlashEnhanceView.MODE_SURFACE, surfaceColors,
            gradientRatio = 0.5f, angle = 135))

        //金色流光：金橙 → 纯金 → 亮白 → 纯金 → 金橙，呈现暖色调的金属光泽扫边效果
        val edgeRaceColors = intArrayOf(
            Color.TRANSPARENT,
            Color.parseColor("#10FFD700"),
            Color.parseColor("#55FFA500"),
            Color.parseColor("#BBFFD700"),
            Color.parseColor("#FFFFFF"),
            Color.parseColor("#BBFFD700"),
            Color.parseColor("#55FFA500"),
            Color.parseColor("#10FFD700"),
            Color.TRANSPARENT
        )
        list.add(FlashAttrsBean(
            mode = FlashEnhanceView.MODE_EDGE_RACE,
            colors = edgeRaceColors,
            gradientRatio = 0.6f,
            angle = 0)
        )
        list.add(FlashAttrsBean(
            mode = FlashEnhanceView.MODE_EDGE_RACE,
            colors = edgeRaceColors,
            gradientRatio = 0.6f,
            angle = 45)
        )

        list.add(FlashAttrsBean(
            mode = FlashEnhanceView.MODE_EDGE_GREEDY,
            interval = 0,
            gradientRatio = 0.6f,
            angle = 0)
        )
        list.add(FlashAttrsBean(
            mode = FlashEnhanceView.MODE_EDGE_GREEDY,
            interval = 1000,
            gradientRatio = 0.6f,
            angle = 180)
        )
        list.add(FlashAttrsBean(
            mode = FlashEnhanceView.MODE_EDGE_GREEDY,
            autoRun = false)
        )
        return list
    }
}