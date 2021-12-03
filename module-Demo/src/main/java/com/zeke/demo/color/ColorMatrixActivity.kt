package com.zeke.demo.color

import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.os.Bundle
import android.widget.ImageView
import android.widget.SeekBar
import android.widget.TextView
import androidx.annotation.FloatRange
import com.alibaba.android.arouter.facade.annotation.Route
import com.kingz.module.common.BaseActivity
import com.kingz.module.common.router.RouterConfig
import com.zeke.demo.R

/**
 * author: King.Z <br></br>
 * date:  2017/5/26 22:01 <br></br>
 *
 * ARGB 色值采用16进制，取值在 0 － 255 之间 ，0（0x00） 即 完全没有 ，255(0xff) 代表满值;
 *
 * setColorFilter(ColorFilter filter):设置颜色过滤器,可以通过颜色过滤器过滤掉对应的色值，
 * 比如去掉照片颜色，生成老照片效果；ColorFilter有以下几个子类可用:
 *  ColorMatrixColorFilter
 *  LightingColorFilter
 *  PorterDuffColorFilter
 *  修改图片 RGBA 的值需要ColorMatrix类的支持，它定义了一个 4*5 的float[]类型的矩阵
 *  颜色矩阵M是以一维数组的方式进行存储的
 *  m=[a,b,c,d,e,  ----- 表示三原色中的红色
 *    f,g,h,i,j,  ----- 表示三原色中的绿色
 *    k,l,m,n,o,  ----- 表示三原色中的蓝色
 *    p,q,r,s,t]  ----- 表示颜色的透明度
 *  第五列用于表示颜色的偏移量
 *
 *  参考文章:http://www.tuicool.com/articles/yYvEn2q
 */
@Route(path = RouterConfig.PAGE_COLOR_MATRIX)
class ColorMatrixActivity : BaseActivity() {
    var mRedFilter = 1f
    var mGreenFilter = 1f
    var mBlueFilter = 1f
    var mAlphaFilter = 1f
    private var imageView: ImageView? = null
    private var matrixTextView: TextView? = null
    private val charArray = arrayOf("R", "G", "B", "A")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_colormatrix)
        imageView = findViewById(R.id.picImageView)
        matrixTextView = findViewById(R.id.matrixTextView)
        findViewById<SeekBar>(R.id.matrixRSeekBar)?.apply {
            setOnSeekBarChangeListener(seekBarChangeListener)
            tag = charArray[0]
        }
        findViewById<SeekBar>(R.id.matrixGSeekBar)?.apply {
            setOnSeekBarChangeListener(seekBarChangeListener)
            tag = charArray[1]
        }
        findViewById<SeekBar>(R.id.matrixBSeekBar)?.apply {
            setOnSeekBarChangeListener(seekBarChangeListener)
            tag = charArray[2]
        }
        findViewById<SeekBar>(R.id.matrixASeekBar)?.apply {
            setOnSeekBarChangeListener(seekBarChangeListener)
            tag = charArray[3]
        }
        setArgb(mAlphaFilter, mRedFilter, mGreenFilter, mBlueFilter)
    }

    private val seekBarChangeListener: SeekBar.OnSeekBarChangeListener =
        object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                val filter = progress.toFloat() / 100
                when (seekBar.tag) {
                    charArray[0] -> { mRedFilter = filter }
                    charArray[1] -> { mGreenFilter = filter }
                    charArray[2] -> { mBlueFilter = filter }
                    charArray[3] -> { mAlphaFilter = filter }
                }
                setArgb(mAlphaFilter, mRedFilter, mGreenFilter, mBlueFilter)

            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {}
            override fun onStopTrackingTouch(seekBar: SeekBar) {}
        }

    /**
     * 设置ColoMatrix的值
     * ┌ 1 0 0 0 0 ┐ - red vector
     * | 0 1 0 0 0 | - green vector
     * | 0 0 1 0 0 | - blue vector
     * └ 0 0 0 1 0 ┘ - alpha vector
     * 第五列是偏移量
     */
    fun setArgb(
        @FloatRange(from = 0.0, to = 1.0) alpha: Float,
        @FloatRange(from = 0.0, to = 1.0) red: Float,
        @FloatRange(from = 0.0, to = 1.0) green: Float,
        @FloatRange(from = 0.0, to = 1.0) blue: Float
    ) {
        mRedFilter = red
        mGreenFilter = green
        mBlueFilter = blue
        mAlphaFilter = alpha
        val mColorMatrix = ColorMatrix(
            floatArrayOf(
                mRedFilter,0f,0f,0f,0f,
                0f,mGreenFilter,0f,0f,0f,
                0f,0f,mBlueFilter,0f,0f,
                0f,0f,0f,mAlphaFilter,0f
            )
        )
        imageView?.colorFilter = ColorMatrixColorFilter(mColorMatrix)
        matrixTextView?.text = matrixArrayToString(mColorMatrix.array)
    }

    private fun matrixArrayToString(array: FloatArray): String {
        val sb = StringBuilder(128)
        sb.append("Matrix(RGBA):\n")
        toShortString(sb, array)
        return sb.toString()
    }

    private fun toShortString(sb: StringBuilder, values: FloatArray) {
        for (i in values.indices) {
            if (i % 5 == 0) { //Start
                sb.append("   [")
            }
            sb.append(values[i])
            if ((i + 1) % 5 == 0) { //End
                sb.append("]\n")
            } else {
                sb.append(", ")
            }
        }
    }
}