package com.zeke.demo.draw.layout

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import android.widget.RelativeLayout
import android.widget.SeekBar
import androidx.appcompat.widget.AppCompatSeekBar
import com.zeke.demo.R
import com.zeke.kangaroo.utils.UIUtils
import com.zeke.kangaroo.utils.ZLog


/**
 * author：ZekeWang
 * date：2021/6/30
 * description： layout demo练习
 * 用来做 ImageView 外部宽高可调节的ViewGourp
 * FIXME 接入到Card_item后的UI显示问题
 */
class Common01AdjustablePanel @JvmOverloads constructor(context: Context?,
                       attrs: AttributeSet? = null,
                       defStyleAttr: Int = 0)
    : RelativeLayout(context, attrs, defStyleAttr) {

    var parentLayout: FrameLayout? = null
    var heightBar: AppCompatSeekBar? = null
    var widthBar: AppCompatSeekBar? = null
    var bottomMargin: Float = UIUtils.dip2px(context,48f).toFloat()
    var containerMinWidth: Float =  UIUtils.dip2px(context,80f).toFloat()
    var containerMinHeight: Float =  UIUtils.dip2px(context,100f).toFloat()


    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        parentLayout = findViewById(R.id.parentLayout)
        widthBar = findViewById(R.id.widthBar)
        heightBar = findViewById(R.id.heightBar)

        val listener: SeekBar.OnSeekBarChangeListener = object : SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, percent: Int, b: Boolean) {
                val layoutParams: LayoutParams = parentLayout?.layoutParams as LayoutParams
                layoutParams.width = (containerMinWidth + (width - containerMinWidth) * widthBar!!.progress / 100).toInt()
                layoutParams.height = (containerMinHeight + (height - bottomMargin - containerMinHeight) * heightBar!!.progress / 100).toInt()
                ZLog.d("onProgressChanged  height=$height")
                parentLayout?.layoutParams = layoutParams
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        }
        widthBar?.setOnSeekBarChangeListener(listener)
        heightBar?.setOnSeekBarChangeListener(listener)
    }

//    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
//        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    //FIXME 为什么写死高度，内容就不绘制了？
//        setMeasuredDimension(width, UIUtils.dip2px(context,600f))
//    }
}