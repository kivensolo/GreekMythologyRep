package com.zeke.demo.color

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.kingz.base.factory.ViewModelFactory
import com.kingz.module.wanandroid.fragemnts.CommonFragment
import com.zeke.demo.databinding.ActivityHvsDemoBinding

/**
 * author：ZekeWang
 * date：2021/12/5
 * description：
 *
 * HSV( Hue-Saturation-Value )颜色色彩模型的使用，
 *   Hue: 色调，取值范围0-360
 *   Saturation: 饱和度，取值范围0-1
 *   Value： 亮度，取值范围0-1
 *
 * RGB转HVS，会把透明度alpha忽略。
 *
 *
 * https://blog.csdn.net/yangdashi888/article/details/53782481
 *
 * https://blog.csdn.net/u010134293/article/details/52813756
 */
class HSVDemoFragment : CommonFragment<ColorViewModel>() {
    private var mColor = 0
    private lateinit var viewBinding: ActivityHvsDemoBinding

    private lateinit var  colorLightBinding:ActivityHvsDemoBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        colorLightBinding = ActivityHvsDemoBinding.inflate(LayoutInflater.from(context))
        return colorLightBinding.root
    }

    override fun getLayoutResID(): Int {
        TODO("Not yet implemented")
    }
    override fun inflateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        viewBinding = ActivityHvsDemoBinding.inflate(inflater, container, false)
        return viewBinding.root
    }

    override val viewModel: ColorViewModel by viewModels {
        ViewModelFactory.build { ColorViewModel() }
    }

    override fun onViewCreated() {
        super.onViewCreated()
        viewBinding.colorPicker.apply {
            //颜色初始化
            mColor = color
            colorLightBinding.maskView.setColor(color)
            setOnColorSelectedListener { mColor = it}
            setOnColorChangedListener {
                mColor = it
                viewBinding.maskView.setColor(it)
            }

            addOpacityBar(viewBinding.opacityBar)

            addHueBar(viewBinding.hueBar)
            addSaturationBar(viewBinding.saturationBar)
            addValueBar(viewBinding.valueBar)
            addSVBar(viewBinding.svBar)
        }
    }
}