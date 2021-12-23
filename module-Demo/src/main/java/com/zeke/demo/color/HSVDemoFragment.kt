package com.zeke.demo.color

import android.graphics.Color
import android.widget.SeekBar
import androidx.fragment.app.viewModels
import com.kingz.base.factory.ViewModelFactory
import com.kingz.module.wanandroid.fragemnts.CommonFragment
import com.zeke.demo.R
import kotlinx.android.synthetic.main.activity_color_light.*

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
    private var mHue = 0f //色调范围0-360
    private var mSat = 0f //饱和度范围0-1
    private var mVal = 0f //亮度范围0-1

    override fun getLayoutResID(): Int = R.layout.activity_color_light

    override val viewModel: ColorViewModel by viewModels {
        ViewModelFactory.build { ColorViewModel() }
    }

    override fun onViewCreated() {
        super.onViewCreated()
        color_picker.apply {
            //颜色初始化
            mColor = color
            mask_view?.setColor(color)
            setOnColorSelectedListener { mColor = it}
            setOnColorChangedListener {
                mColor = it
                mask_view?.setColor(it)
            }

            addOpacityBar(opacityBar)

            addSVBar(svBar)
            addSaturationBar(saturationBar)
            addValueBar(valueBar)
        }
        initSeeks()
    }


    //旧版本原生seekBar控制。
    private fun initSeeks() {
        //色调
        hueSeek.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, b: Boolean) {
                val hue = (3.6 * progress).toFloat()
                tv_hue_seek?.text = "$hue"
                val hsv = FloatArray(3)
                Color.colorToHSV(mColor, hsv)
                mHue = hue
                mSat = hsv[1]
                mVal = hsv[2]
                val hsvToColor = Color.HSVToColor(floatArrayOf(mHue, mSat, mVal))
//                //更新maskView
//                mask_view.setColor(hsvToColor)
                //更新颜色选择器
                color_picker.color = hsvToColor
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {}
            override fun onStopTrackingTouch(seekBar: SeekBar) {}
        })
       /* //饱和度
        satSeek.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                val value = progress.toFloat() / seekBar.max
                tv_sat_seek?.text = "$value"
                val hsv = FloatArray(3)
                Color.colorToHSV(mColor, hsv)
                mHue = hsv[0]
                mSat = value
                mVal = hsv[2]
                mask_view?.setColor(Color.HSVToColor(floatArrayOf(mHue, mSat, mVal)))
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {}

            override fun onStopTrackingTouch(seekBar: SeekBar) {}
        })
        //亮度
        valueSeek.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, b: Boolean) {
                val value = progress.toFloat() / seekBar.max
                tv_value_seek?.text = value.toString()
                val hsv = FloatArray(3)
                Color.colorToHSV(mColor, hsv)
                mHue = hsv[0]
                mSat = hsv[1]
                mVal = value
                if (mVal < 0.35) {
                    mVal = 0.35f
                }
                mask_view?.setColor(Color.HSVToColor(floatArrayOf(mHue, mSat, mVal)))
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {}
            override fun onStopTrackingTouch(seekBar: SeekBar) {}
        })*/

    }

}