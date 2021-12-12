package com.kingz.pickerview

import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.SeekBar
import androidx.appcompat.app.AppCompatActivity
import com.bigkoo.pickerview.adapter.ArrayWheelAdapter
import com.contrarywind.view.WheelView
import kotlinx.android.synthetic.main.activity_wheel_view.*

/**
 * author：ZekeWang
 * date：2021/12/7
 * description：WheelView展示Demo
 *
 * 【数据联动】
 * 数据联动由 WheelOptions 实现， WheelView本身不支持数据联动
 */
class WheelViewDemoActivity: AppCompatActivity()  {


    private val gravityModes = arrayOf(
        "Center",
        "Left",
        "Right"
    )

    /**
     * 附加单位的绘制位置模式
     * 目前只在附加单位在中间显示时有效
     */
    private val labelAlignMode = arrayOf(
        "吸附于文字的右侧",
        "位于滚轮控件最右侧"
    )

    /**
     * 分隔线类型
     */
    private val dividerTypeArray = arrayOf(
        "FILL",
        "WRAP",
        "CIRCLE"
    )
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_wheel_view)
        initWheelViewData()
        initSpinners()
        initOptions()
    }

    private fun initSpinners() {
        initGravitySpinner()
        initLabelModeSpinner()
        initDividerTypeSpinner()
    }

    /**
     * 初始化属性控制UI
     */
    private fun initOptions() {
        showSecondWheelBox.setOnCheckedChangeListener { buttonView, isChecked ->
            wheelRenderView2.visibility = if (isChecked) {
                View.VISIBLE
            } else {
                View.GONE
            }
        }
        scrollLoopCheckBox.setOnCheckedChangeListener { _, isChecked ->
            wheelRenderView.isLoop = isChecked
            wheelRenderView.postInvalidate()
        }
        prefixUnitCheckObx.setOnCheckedChangeListener { _, isChecked ->
            val text = if (isChecked) {
                "地区"
            } else {
                ""
            }
            wheelRenderView.setPrefixLabel(text)
            wheelRenderView.postInvalidate()
        }
        suffixUnitCheckObx.setOnCheckedChangeListener { _, isChecked ->
            val text = if (isChecked) {
                "市"
            } else {
                ""
            }
            wheelRenderView.setLabel(text)
            wheelRenderView.postInvalidate()
        }

        prefixWeightBar.apply {
            progress = 4
            setOnSeekBarChangeListener(object :SeekBar.OnSeekBarChangeListener{
                override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                    this@WheelViewDemoActivity.prefixSplitRatio?.text = "PrefixSplitRatio(${(progress.toFloat()) / 10}):"
                    this@WheelViewDemoActivity.wheelRenderView.apply {
                        setPrefixLabelWeight((progress.toFloat()) / 10)
                        invalidate()
                    }
                }

                override fun onStartTrackingTouch(seekBar: SeekBar?) {}

                override fun onStopTrackingTouch(seekBar: SeekBar?) {}

            })
        }
    }

    private fun initWheelViewData() {
        wheelRenderView.apply {
            adapter = ArrayWheelAdapter(
                mutableListOf(
                    "成都", "德阳", "眉山", "资阳", "绵阳", "乐山","广元"
                )
            )
            currentItem = 0
            isLoop = false
            setIsOptions(true)
        }
        wheelRenderView2.apply {
            adapter = ArrayWheelAdapter(
                mutableListOf(
                    "A区", "B区", "C区", "D区", "E区", "F区"
                )
            )
            currentItem = 0
            isLoop = false
            setIsOptions(true)
        }
    }

    private fun initGravitySpinner() {
        val adapter: ArrayAdapter<String> = ArrayAdapter<String>(
            this,
            android.R.layout.simple_spinner_item,
            gravityModes
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        gravityMode.adapter = adapter
        gravityMode.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                when(position){ //FIXME 优化
                    0 ->{
                        wheelRenderView.setGravity(Gravity.CENTER)
                    }
                    1 ->{
                        wheelRenderView.setGravity(Gravity.LEFT)
                    }
                    2 ->{
                        wheelRenderView.setGravity(Gravity.RIGHT)
                    }
                }
                wheelRenderView.postInvalidate()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }
    }
    private fun initLabelModeSpinner() {
        val adapter: ArrayAdapter<String> = ArrayAdapter<String>(
            this,
            android.R.layout.simple_spinner_item,
            labelAlignMode
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        labelAlignModeSpinner.adapter = adapter
        labelAlignModeSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?,view: View?,position: Int,id: Long) {
                when(position){
                    0 -> {
                        wheelRenderView.setLabelAlignMode(WheelView.LabelAlignMode.RIGHT_OF_CONTENT_TEXT)
                    }
                    1 -> {
                        wheelRenderView.setLabelAlignMode(WheelView.LabelAlignMode.RIGHT_OF_VIEW)
                    }
                }
                wheelRenderView.postInvalidate()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }
    }
    private fun initDividerTypeSpinner() {
        val adapter: ArrayAdapter<String> = ArrayAdapter<String>(
            this,
            android.R.layout.simple_spinner_item,
            dividerTypeArray
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        dividerTypeSpinner.adapter = adapter
        dividerTypeSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?,view: View?,position: Int,id: Long) {
                when(position){
                    0 -> {
                        wheelRenderView.setDividerType(WheelView.DividerType.FILL)
                    }
                    1 -> {
                        wheelRenderView.setDividerType(WheelView.DividerType.WRAP)
                    }
                    2 -> {
                        wheelRenderView.setDividerType(WheelView.DividerType.CIRCLE)
                    }
                }
                wheelRenderView.postInvalidate()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }
    }
}