package com.zeke.demo.customview

import android.graphics.Color
import android.view.View
import android.view.ViewGroup
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import com.zeke.demo.R
import com.zeke.demo.base.AbsMultiCardTabsDemoActivity
import com.zeke.demo.customview.views.ChartMusicView
import com.zeke.demo.customview.views.ChartTextView
import com.zeke.demo.model.CardItemModel


/**
 * author: King.Z <br>
 * date:  2020/4/19 11:32 <br>
 * description: 自定义view Demo展示的页面 <br>
 */
class CustomViewsDemoMultiCardActivity : AbsMultiCardTabsDemoActivity() {

    /**
     * 对于图表关键需要知道并理解的是图、数据、数据集以及 Entry，
     * 这是定义并显示图表的关键概念，
     * 它们的关系是Entry 添加到数据集中，数据集被添加到数据中，数据被添加到图表中。
     *
     * 如果要对 X 轴和 Y 轴进行设置可分别通过 XAxis 和 YAxis 进行设置
     * 如果要对数据进行设置，则通过 DataSet 进行设置
     * 如果要设置手势等，可通过图表 Chart 进行设置
     */
    private fun createSimpleChartView(): View {
        val chartView = LineChart(baseContext)

        chartView.apply {
            //设置边框颜色
            setDrawBorders(false)
//            setBorderColor(Color.TRANSPARENT)
//            setBorderWidth(2f)

            //Description object of the chart
            description.isEnabled = false

            //Sets the Legend
            legend.apply {
                isEnabled = false
                // other setXXX methods
            }

            // 自定义 MarkView，当数据被选择时会展示
            // val mv = MyMarkerView(this, R.layout.custom_marker_view)
            // mv.setChartView(chart)
            // setMarker(mv)

            // 背景色
            setBackgroundColor(Color.WHITE)
            setDrawGridBackground(false)

            // 添加监听器
            setOnChartValueSelectedListener(object : OnChartValueSelectedListener {
                override fun onNothingSelected() {}
                override fun onValueSelected(e: Entry?, h: Highlight?) {}
            })

            // <editor-fold defaultstate="collapsed" desc="滑动&缩放设置">
            // enable touch gestures
//            setTouchEnabled(true)
//            dragDecelerationFrictionCoef = 0.9f
            // enable scaling and dragging
//            isDragEnabled = true
//            isScaleXEnabled = true  // Enable scale x
//            isScaleYEnabled = true  // Enable scale y
            setScaleEnabled(true)     // Enable scale x & y
            setPinchZoom(true)        // 双指缩放
            // </editor-fold>

      // <editor-fold defaultstate="collapsed" desc="坐标轴设置">
            // 允许显示 X 轴的垂直网格线
//            xAxis.enableGridDashedLine(10f, 10f, 0f)
            xAxis.position = XAxis.XAxisPosition.BOTTOM
            xAxis.setDrawGridLines(false)
            //左/右侧间距 value值 不是px
            xAxis.axisMinimum = 0.5f
            xAxis.axisMaximum = 0.5f
//            xAxis.axisLineColor = Color.TRANSPARENT
            // 禁止右轴
            axisRight.isEnabled = false

            // Y 轴为起点的水平网格线()
            axisLeft.enableGridDashedLine(10f, 10f, 0f);
            // 设置 Y 轴的数值范围
            axisLeft.axisMaximum = 200f
            axisLeft.axisMinimum = 0f
//            axisLeft.axisLineColor = Color.TRANSPARENT

      // </editor-fold>

            layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 800)

            // 手势设置
            setTouchEnabled(true)
        }

        // 1. 每个数据是一个 Entry   创建假数据集合
        val valuesList: MutableList<Entry> = ArrayList()
        for (i in 0 until 30) {
            val value: Float = (Math.random() * 150f + 30f).toFloat()
            valuesList.add(
                Entry(i.toFloat(),value)
            )
        }

        // 2. 创建一个数据集 DataSet ，用来添加 Entry。一个图中可以包含多个数据集
//        val dataSet = LineDataSet(valuesList, "DataSet 1")
        val dataSet = LineDataSet(valuesList, null) // 不显示数据集描述
        // 3. 通过数据集设置数据的样式，如字体颜色、线型、线型颜色、填充色、线宽等属性
        with(dataSet){
            // draw/nodraw dashed line
//            enableDashedLine(10f, 5f, 0f)
            disableDashedLine()

            // Set lines and points color
            color = Color.RED
            setCircleColor(Color.RED)
            lineWidth = 1f     //line thickness of data set
            circleRadius = 1f  //point size of data point
            // draw points as solid circles
            setDrawCircleHole(false)
            setDrawCircles(false) // 不绘制数据集的数据点
            setDrawValues(false)  // 不绘制数据集的值

            //set gradient color  无效
//            setGradientColor(Color.RED,Color.WHITE)
            setDrawFilled(true)
            fillDrawable = resources.getDrawable(R.drawable.chart_fill)

            // 曲线风格
//            mode = LineDataSet.Mode.HORIZONTAL_BEZIER  FIXME
//            cubicIntensity = 0.05f  //设置曲线的平滑度
        }

        // 4.将数据集添加到数据 ChartData 中
        val data = LineData(dataSet)
        // 5. 将数据添加到图表中
        chartView.data = data
        return chartView
    }

    override fun inflatePageData() {
        tabDataMap = mapOf(
            "自定义views" to mutableListOf(
                CardItemModel("ChartMusicView", ChartMusicView(this)),
                CardItemModel("带音乐跳动效果的TextView", ChartTextView(this)),
                CardItemModel("SimpleChartView", createSimpleChartView())
            ),
        )
        super.inflatePageData()
    }



}