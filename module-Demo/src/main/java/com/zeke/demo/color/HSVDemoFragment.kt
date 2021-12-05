package com.zeke.demo.color

import android.os.Bundle
import androidx.fragment.app.viewModels
import com.kingz.base.factory.ViewModelFactory
import com.kingz.module.wanandroid.fragemnts.CommonFragment
import com.zeke.demo.R

/**
 * author：ZekeWang
 * date：2021/12/5
 * description：
 * HSV颜色色彩模型的使用
 */
class HSVDemoFragment : CommonFragment<ColorViewModel>() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun getLayoutResID(): Int = R.layout.pager_navigator_layout
    override val viewModel: ColorViewModel by viewModels {
        ViewModelFactory.build { ColorViewModel() }
    }
}