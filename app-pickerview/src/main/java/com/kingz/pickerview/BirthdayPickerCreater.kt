package com.kingz.pickerview

import android.content.Context
import android.graphics.Color
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.annotation.FloatRange
import com.bigkoo.pickerview.builder.TimePickerBuilder
import com.bigkoo.pickerview.view.TimePickerView
import java.util.*

/**
 * author: King.Z <br>
 * date:  2021/7/3 15:45 <br>
 * description: 生日选择控件的控制器 <br>
 */
class BirthdayPickerCreater(context: Context) {
    /**
     * 开始时间
     */
    private val startDate: Calendar = Calendar.getInstance()
    /**
     * 结束时间  默认为当前时间
     */
    private val endCalendar: Calendar = Calendar.getInstance()

    /**
     * Dialog 模式下，背景变暗程度
     */
    private var backgroundDimAmount = 0f

    var builder :TimePickerBuilder
        private set

    private lateinit var pickerView: TimePickerView

    init {
        startDate.set(1900, 1, 1) //从1900年开始
        builder = TimePickerBuilder(context)
        with(builder) {
            setType(booleanArrayOf(true, true, true, false, false, false)) //控制年月日时分秒的显示和隐藏
            setRangDate(startDate, endCalendar) //设置开始时间和结束时间

            isDialog(false)             //是否是对话框模式,默认为false
            setItemVisibleCount(9)              //设置最大可见数目
            setLineSpacingMultiplier(2f)      //设置间距倍数
            setTitleText("生日")                 //设置标题文字
            setTitleSize(17)                    //设置标题文字大小
            setTitleColor(Color.WHITE)          //设置标题颜色
            setCancelColor(Color.RED)           //设置取消按钮颜色
            setSubmitColor(Color.GREEN)         //设置确定按钮颜色
            setTitleBgColor(Color.GRAY)         //设置标题背景颜色

            setBgColor(Color.BLACK)             //设置滚轮背景颜色
            setDividerColor(Color.GRAY)         //设置分割线的颜色
            setTextColorCenter(Color.WHITE)     //设置分割线之间的文字的颜色
            setTextColorOut(Color.GRAY)         //设置分割线以外文字的颜色
            isAlphaGradient(true) //透明度是否渐变
        }
    }

    /**
     * 设置是否是对话框模式
     * false：内部实现将DecorView 作为它的父控件, 默认从最底部弹出
     */
    fun setDialog(isDialog: Boolean,
                  @FloatRange(from = 0.0, to = 1.0) dimAccount:Float = 0.3f)
        : BirthdayPickerCreater {
        builder.isDialog(isDialog)
        backgroundDimAmount = dimAccount
        return this
    }

    inline fun setCancelClickListener(
        crossinline onCancle: (cancleView: View) -> Unit
    ):BirthdayPickerCreater {
        builder.addOnCancelClickListener { onCancle(it) }
        return this
    }

    inline fun setTimeSelectChangeListener(
        crossinline onSelect: (date: Date) -> Unit
    ):BirthdayPickerCreater {
        builder.setTimeSelectChangeListener { date, _ ->
            onSelect(date)
        }
        return this
    }

    inline fun setTimeSelectListener(
        crossinline onConfirm: (date: Date, callerView: View?) -> Unit
    ):BirthdayPickerCreater {
        builder.setTimeSelectListener { date, _, v -> onConfirm(date, v) }
        return this
    }

    fun buildPickerView(): TimePickerView {
        pickerView = builder.build()
        pickerView.dialog?.apply {
            val params = FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                Gravity.BOTTOM
            )
            params.leftMargin = 0
            params.rightMargin = 0
            pickerView.dialogContainerLayout.layoutParams = params
            val dialogWindow = this.window
            dialogWindow?.apply {
                //设置动画样式为底部滑出
                setWindowAnimations(R.style.picker_view_slide_anim)
                //改成Bottom,底部显示
                setGravity(Gravity.BOTTOM)
                setDimAmount(backgroundDimAmount)
            }
        }
        return pickerView
    }
}