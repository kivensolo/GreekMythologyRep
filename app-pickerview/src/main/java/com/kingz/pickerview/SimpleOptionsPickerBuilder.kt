package com.kingz.pickerview

import android.content.Context
import android.graphics.Color
import com.bigkoo.pickerview.builder.OptionsPickerBuilder
import com.contrarywind.view.WheelView

/**
 * author: King.Z <br>
 * date:  2021/7/3 17:28 <br>
 * description: 简单的数据选择构建器 <br>
 */
class SimpleOptionsPickerBuilder(context: Context)
    : OptionsPickerBuilder(context) {

    //初始化公共属性
    init {
        setTitleSize(17)
        setTitleColor(Color.WHITE)
        setCancelColor(Color.RED)
        setSubmitColor(Color.GREEN)
        setTitleBgColor(Color.GRAY)
        setBgColor(Color.BLACK)
        setDividerColor(Color.GRAY)
        setTextColorCenter(Color.WHITE)
        setTextColorOut(Color.GRAY)
        setSelectOptions(0)
        //只显示选中项的label
        isCenterLabel(true)
    }

    /**
     * 设置后缀单位
     */
    fun setUnitLabels(label1:String="",label2:String="",label13:String="",
                      mode: WheelView.LabelAlignMode = WheelView.LabelAlignMode.RIGHT_OF_CONTENT_TEXT)
            :OptionsPickerBuilder{
        setLabelAlignMode(mode)
        return setLabels(label1, label2, label13)
    }
}