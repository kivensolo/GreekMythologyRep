package com.kingz.pickerview

import android.app.Dialog
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bigkoo.pickerview.builder.OptionsPickerBuilder
import com.bigkoo.pickerview.builder.TimePickerBuilder
import com.bigkoo.pickerview.listener.OnOptionsSelectListener
import com.bigkoo.pickerview.listener.OnTimeSelectListener
import com.bigkoo.pickerview.view.OptionsPickerView
import com.bigkoo.pickerview.view.TimePickerView
import kotlinx.android.synthetic.main.activity_main.*
import java.text.SimpleDateFormat
import java.util.*


/**
 * Android-PickerView系列之介绍与使用篇（一）
 * https://blog.csdn.net/qq_22393017/article/details/58099486
 * Android-PickerView系列之源码解析篇（二）
 * https://blog.csdn.net/qq_22393017/article/details/59488906
 *
 * PickerView的使用Demo页面
 * 1. 展示TimePicker和OptionsPicker的用法
 */
class PickerViewUseDemoActivity : AppCompatActivity() {
    private var tpViewAsDialog: TimePickerView? = null
    private var tpViewNormal: TimePickerView? = null

    private var areaOptionsPickerView: OptionsPickerView<String>? = null
    private val options1Items: MutableList<String> = ArrayList()
    private val options2Items: MutableList<MutableList<String>> = ArrayList()

    companion object {
        const val TAG = "PickerViewDemo"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initTimePicker()

        initOptionsPicker()
    }

    /**
     * 初始化自定义数据选择器
     */
    private fun initOptionsPicker() {
        getOptionData()
        areaOptionsPickerView = OptionsPickerBuilder(this,
            OnOptionsSelectListener { options1: Int, options2: Int, options3: Int, view: View ->
                //返回的分别是三个级别的选中位置
                Log.i(TAG, "options1=${options1}, options2=${options2}, options3=${options3}")

            }).setTitleText("城市选择")
            .setContentTextSize(20)//设置滚轮文字大小
            .setDividerColor(Color.LTGRAY)//设置分割线的颜色
            .setSelectOptions(0, 1)//默认选中项
            .setBgColor(Color.BLACK)
            .setTitleBgColor(Color.DKGRAY)
            .setTitleColor(Color.LTGRAY)
            .setCancelColor(Color.YELLOW)
            .setSubmitColor(Color.YELLOW)
            .setTextColorCenter(Color.LTGRAY)
            .isRestoreItem(true)//切换时是否还原，设置默认选中第一项。
            .isCenterLabel(false) //是否只显示中间选中项的label文字，false则每项item全部都带有label。
            .setLabels("" +
                    "", "市", "区")
            .setOutSideColor(0x00000000) //设置外部遮罩颜色
            .setOptionsSelectChangeListener { options1: Int, options2: Int, options3: Int ->
                val str = "options1: $options1\noptions2: $options2\noptions3: $options3"
                Toast.makeText(this@PickerViewUseDemoActivity, str, Toast.LENGTH_SHORT).show()
            }
            .build()
        areaOptionsPickerView?.setPicker(options1Items, options2Items)

        show_area_options.setOnClickListener {
            areaOptionsPickerView?.show()
        }

        val simpleOptionsPickerView = SimpleOptionsPickerBuilder(this)
            .setUnitLabels("cm")
            .setTitleText("省份选择")
            .setTitleColor(Color.WHITE)
            .build<String>()
        // ----- 演示单个数值选择的效果
        simpleOptionsPickerView.setPicker(options1Items)
        simpleOptionsPickerView.setSelectOptions(options1Items.size - 1)
        show_custom_single_picker.setOnClickListener {
            simpleOptionsPickerView.show()
        }
    }

    /**
     * 时间(日历日期)选择器初始化
     * Dialog 模式下，在底部弹出
     */
    private fun initTimePicker() {
        val startDate = Calendar.getInstance()
        val years = startDate.get(Calendar.YEAR)    //年
        val months = startDate.get(Calendar.MONTH)  //月份
        val days = startDate[Calendar.DAY_OF_MONTH] //日
        val hour = startDate[Calendar.HOUR_OF_DAY]  //时
        val minute = startDate[Calendar.MINUTE]     //分
        val second = startDate[Calendar.SECOND]     //秒
        //startDate.set(2010,1,23);
        //设置开始时间
        startDate.set(1900, 1, 1) //从1900年开始
//        startDate.set(years, months, days, hour, minute, second)

        val endDate = Calendar.getInstance()
        // endDate.set(2069,2,28);//设置结束时间
//        endDate.set(years + 5, months, days, hour, minute, second)
        endDate.set(years, months, days, hour, minute, second)
        tpViewAsDialog = TimePickerBuilder(this,
            OnTimeSelectListener { date, _ ->
                Toast.makeText(this, getTime(date), Toast.LENGTH_SHORT).show()
                //Log.i("pvTime", "onTimeSelect");
            })
            .setRangDate(startDate, endDate)    //设置开始时间和结束时间，不设置就是全部时间
            .setType(booleanArrayOf(true, true, true, false, false, false)) //控制年月日时分秒的显示和隐藏
            .isDialog(true)     //默认设置false ，内部实现将DecorView 作为它的父控件。
            .setItemVisibleCount(7)     //若设置偶数，实际值会加1（比如设置6，则最大可见条目为7）
            .setLineSpacingMultiplier(2.0f)
            .setTitleText("生日")
            .setTitleSize(17)
            .setCancelColor(Color.RED)
            .setTitleBgColor(Color.BLACK)
            .isAlphaGradient(true)
            .addOnCancelClickListener {
                Log.i(TAG, "onCancelClickListener")
            }
            .setTimeSelectChangeListener {
                Log.i(TAG, "onTimeSelectChanged")
            }
            .build()

        val mDialog: Dialog? = tpViewAsDialog?.dialog
        if (mDialog != null) {
            val params = FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                Gravity.BOTTOM
            )
            params.leftMargin = 0
            params.rightMargin = 0
            tpViewAsDialog?.dialogContainerLayout?.layoutParams = params
            val dialogWindow = mDialog.window
            if (dialogWindow != null) {
                //修改动画样式
                dialogWindow.setWindowAnimations(R.style.picker_view_scale_anim)
                //改成Bottom,底部显示
                dialogWindow.setGravity(Gravity.BOTTOM)
                dialogWindow.setDimAmount(0.3f)
            }
        }


        tpViewNormal = TimePickerBuilder(this,
            OnTimeSelectListener { date, _ ->
                Toast.makeText(this, getTime(date), Toast.LENGTH_SHORT).show()
                //Log.i("pvTime", "onTimeSelect");
            })
            .setRangDate(startDate, endDate)    //设置开始时间和结束时间，不设置就是全部时间
            .setType(booleanArrayOf(true, true, true, true, true, false)) //控制年月日时分秒的显示和隐藏
            .isDialog(false)     //默认设置false ，内部实现将DecorView 作为它的父控件。
            .setItemVisibleCount(9)     //若设置偶数，实际值会加1（比如设置6，则最大可见条目为7）
            .setLineSpacingMultiplier(2.0f)
            .isAlphaGradient(true)
            .addOnCancelClickListener {Log.i(TAG, "onCancelClickListener")}
            .setTimeSelectChangeListener {Log.i(TAG, "onTimeSelectChanged")}
            .build()


        show_from_bottom_as_dialog.setOnClickListener {tpViewAsDialog?.show()}
        show_not_as_dialog.setOnClickListener {tpViewNormal?.show()}


        //Demo-4 自定义日期选择时间
        val birthdayPickerView = BirthdayPickerCreater(this)
            .setCancelClickListener { }
            .setTimeSelectChangeListener { }
            .setTimeSelectListener { date, v ->
                Toast.makeText(this, getTime(date), Toast.LENGTH_SHORT).show()
            }
            .setDialog(true)
            .buildPickerView()
        show_custom_calendar_options.setOnClickListener {
            birthdayPickerView.show()
        }


    }

    private fun getTime(date: Date): String? {
        Log.d("getTime()", "choice date millis: " + date.time)
        val format = SimpleDateFormat("yyyy-MM-dd HH:mm")
        return format.format(date)
    }


    @Suppress("LocalVariableName")
    private fun getOptionData() {
        //选项1
        options1Items.add("广东")
        options1Items.add("湖南")
        options1Items.add("广西")

        //选项2
        val options2Items_01: ArrayList<String> = ArrayList()
        options2Items_01.add("广州")
        options2Items_01.add("佛山")
        options2Items_01.add("东莞")
        options2Items_01.add("珠海")
        val options2Items_02: ArrayList<String> = ArrayList()
        options2Items_02.add("长沙")
        options2Items_02.add("岳阳")
        options2Items_02.add("株洲")
        options2Items_02.add("衡阳")
        val options2Items_03: ArrayList<String> = ArrayList()
        options2Items_03.add("桂林")
        options2Items_03.add("玉林")

        options2Items.add(options2Items_01)
        options2Items.add(options2Items_02)
        options2Items.add(options2Items_03)
    }


}
