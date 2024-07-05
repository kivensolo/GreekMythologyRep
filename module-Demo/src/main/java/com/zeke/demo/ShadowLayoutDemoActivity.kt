package com.zeke.demo

import android.graphics.Color
import android.os.Bundle
import android.widget.SeekBar
import androidx.appcompat.app.AppCompatActivity
import com.zeke.demo.databinding.PageShadowlayoutDemoBinding

/**
 * 原生UI，使用自定义阴影控件
 * ShadowLayout
 */
class ShadowLayoutDemoActivity : AppCompatActivity() {
    private lateinit var viewBinding:PageShadowlayoutDemoBinding
    private var shadowAlpha :Int = 0
    private var red :Int = 0
    private var green:Int = 0
    private var blue:Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = PageShadowlayoutDemoBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        red = viewBinding.shadowColorRedSeekbar.progress
        green = viewBinding.shadowColorGreenSeekbar.progress
        blue = viewBinding.shadowColorBlueSeekbar.progress


        viewBinding.radioLeft.setOnCheckedChangeListener { _, isChecked ->
            viewBinding.shadowLayout.setShadowHiddenLeft(isChecked)
        }
        viewBinding.radioTop.setOnCheckedChangeListener { _, isChecked ->
            viewBinding.shadowLayout.setShadowHiddenTop(isChecked)
        }
        viewBinding.radioRight.setOnCheckedChangeListener { _, isChecked ->
            viewBinding.shadowLayout.setShadowHiddenRight(isChecked)
        }
        viewBinding.radioButtom.setOnCheckedChangeListener { _, isChecked ->
            viewBinding.shadowLayout.setShadowHiddenBottom(isChecked)
        }

        //-----------> 阴影颜色
        viewBinding.shadowColorRedSeekbar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                red = progress
                viewBinding.shadowLayout.setShadowColor(Color.argb(shadowAlpha, red, green, blue))
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }
        })

        viewBinding.shadowColorGreenSeekbar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                green = progress
                viewBinding.shadowLayout.setShadowColor(Color.argb(shadowAlpha, red, green, blue))
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }
        })

        viewBinding.shadowColorBlueSeekbar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                blue = progress
                viewBinding.shadowLayout.setShadowColor(Color.argb(shadowAlpha, red, green, blue))
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }
        })
        //<----------- 阴影颜色


        //阴影扩散程度
        viewBinding.shadowLimitSeekbar.apply {
            progress = viewBinding.shadowLayout.shadowLimit.toInt()
            viewBinding.shadowLimitTip.text = String.format("阴影扩撒程度:%s", progress)
            setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                    viewBinding.shadowLimitTip.text = String.format("阴影扩撒程度:%s", progress)
                    viewBinding.shadowLayout.setShadowLimit(progress)
                }

                override fun onStartTrackingTouch(seekBar: SeekBar?) {
                }

                override fun onStopTrackingTouch(seekBar: SeekBar?) {
                }
            })
        }

        //阴影X|Y偏移量
        viewBinding.shadowColorOffsetxSeekbar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                viewBinding.shadowOffsetxTip.text = String.format("阴影X偏移量:%s",progress)
                viewBinding.shadowLayout.setShadowOffsetX(progress.toFloat())
            }
            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })
        viewBinding.shadowColorOffsetySeekbar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                viewBinding.shadowOffsetyTip.text = String.format("阴影Y偏移量:%s",progress)
                viewBinding.shadowLayout.setShadowOffsetY(progress.toFloat())
            }
            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })

        //圆角效果
        viewBinding.shadowRoundRectSeekbar.apply {
            progress = viewBinding.shadowLayout.cornerRadius.toInt()
            viewBinding.shadowRoundRectTip.text = String.format("圆角大小:%s",progress)
            setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                    viewBinding.shadowRoundRectTip.text = String.format("圆角大小:%s",progress)
                    viewBinding.shadowLayout.setCornerRadius(progress)
                }

                override fun onStartTrackingTouch(seekBar: SeekBar?) {
                }

                override fun onStopTrackingTouch(seekBar: SeekBar?) {}
            })
        }

        //透明度设置
        viewBinding.shadowAlphaSeekbar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                shadowAlpha = progress
                viewBinding.shadowAlphaTip.text = String.format("透明度:%.0f %%",progress.toFloat() * 100 / 255)
                viewBinding.shadowLayout.setShadowColor(Color.argb(shadowAlpha, red, green, blue))
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }
        })
    }

}
