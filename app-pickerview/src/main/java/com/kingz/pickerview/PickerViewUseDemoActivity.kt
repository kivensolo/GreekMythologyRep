package com.kingz.pickerview

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bigkoo.pickerview.builder.TimePickerBuilder
import com.bigkoo.pickerview.listener.OnTimeSelectListener
import com.bigkoo.pickerview.view.TimePickerView
import kotlinx.android.synthetic.main.activity_main.*
import java.text.SimpleDateFormat
import java.util.*

class PickerViewUseDemoActivity : AppCompatActivity() {
    private var tpViewAsDialog: TimePickerView? = null
    private var tpViewNormal: TimePickerView? = null

    companion object{
        const val TAG = "TimePickerDemo"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initTimePicker()
        show_from_buttom_as_dialog.setOnClickListener {
            tpViewAsDialog?.show()
        }

        show_not_as_dialog.setOnClickListener {
            tpViewNormal?.show()
        }
    }

    private fun initTimePicker() {
        //Dialog 模式下，在底部弹出
        val startDate = Calendar.getInstance()
        val years = startDate.get(Calendar.YEAR)    //年
        val months = startDate.get(Calendar.MONTH)  //月份
        val days = startDate[Calendar.DAY_OF_MONTH] //日
        val hour = startDate[Calendar.HOUR_OF_DAY]  //时
        val minute = startDate[Calendar.MINUTE]     //分
        val second = startDate[Calendar.SECOND]     //秒
        //startDate.set(2010,1,23);
        //设置开始时间
        startDate.set(years, months, days, hour, minute, second)

        val endDate = Calendar.getInstance()
        // endDate.set(2069,2,28);//设置结束时间
        endDate.set(years + 5, months, days, hour, minute, second)
        tpViewAsDialog = TimePickerBuilder(this,
            OnTimeSelectListener { date, _ ->
                Toast.makeText(this, getTime(date), Toast.LENGTH_SHORT).show()
                //Log.i("pvTime", "onTimeSelect");
            })
            .setRangDate(startDate, endDate)    //设置开始时间和结束时间，不设置就是全部时间
            .setType(booleanArrayOf(true, true, true, true, true, false)) //控制年月日时分秒的显示和隐藏
            .isDialog(true)     //默认设置false ，内部实现将DecorView 作为它的父控件。
            .setItemVisibleCount(9)     //若设置偶数，实际值会加1（比如设置6，则最大可见条目为7）
            .setLineSpacingMultiplier(2.0f)
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
            .addOnCancelClickListener {
                Log.i(TAG, "onCancelClickListener")
            }
            .setTimeSelectChangeListener {
                Log.i(TAG, "onTimeSelectChanged")
            }
            .build()
    }

    private fun getTime(date: Date): String? {
        Log.d("getTime()", "choice date millis: " + date.time)
        val format = SimpleDateFormat("yyyy-MM-dd HH:mm")
        return format.format(date)
    }

}
