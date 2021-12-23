package com.zeke.demo.color

import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.widget.ImageView
import android.widget.SeekBar
import android.widget.TextView
import androidx.annotation.FloatRange
import androidx.fragment.app.viewModels
import com.kingz.base.factory.ViewModelFactory
import com.kingz.module.wanandroid.fragemnts.CommonFragment
import com.zeke.demo.R

/**
 * author：ZekeWang
 * date：2021/12/5
 * description：
 * HSV颜色色彩模型的使用
 */
class ColorMatrixFragment : CommonFragment<ColorViewModel>() {

    var mRedFilter = 1f
    var mGreenFilter = 1f
    var mBlueFilter = 1f
    var mAlphaFilter = 1f
    private var imageView: ImageView? = null
    private var matrixTextView: TextView? = null
    private val charArray = arrayOf("R", "G", "B", "A")

    override fun getLayoutResID(): Int = R.layout.activity_colormatrix
    override val viewModel: ColorViewModel by viewModels {
        ViewModelFactory.build { ColorViewModel() }
    }

    override fun onViewCreated() {
        super.onViewCreated()
        setHasOptionsMenu(true)
        activity?.apply {
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
        }
        setArgb(mAlphaFilter, mRedFilter, mGreenFilter, mBlueFilter)
    }

    private val seekBarChangeListener: SeekBar.OnSeekBarChangeListener =
        object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                val filter = progress.toFloat() / 100
                when (seekBar.tag) {
                    charArray[0] -> {
                        mRedFilter = filter
                    }
                    charArray[1] -> {
                        mGreenFilter = filter
                    }
                    charArray[2] -> {
                        mBlueFilter = filter
                    }
                    charArray[3] -> {
                        mAlphaFilter = filter
                    }
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
                mRedFilter, 0f, 0f, 0f, 0f,
                0f, mGreenFilter, 0f, 0f, 0f,
                0f, 0f, mBlueFilter, 0f, 0f,
                0f, 0f, 0f, mAlphaFilter, 0f
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