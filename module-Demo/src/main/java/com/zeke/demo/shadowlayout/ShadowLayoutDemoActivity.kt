package com.zeke.demo.shadowlayout

import android.graphics.Color
import android.os.Bundle
import android.widget.SeekBar
import androidx.appcompat.app.AppCompatActivity
import com.zeke.demo.R
import kotlinx.android.synthetic.main.page_shadowlayout_demo.*


class ShadowLayoutDemoActivity : AppCompatActivity() {

    private var shadowAlpha :Int = 0
    private var red :Int = 0
    private var green:Int = 0
    private var blue:Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.page_shadowlayout_demo)

        red = shadow_color_red_seekbar.progress
        green = shadow_color_green_seekbar.progress
        blue = shadow_color_blue_seekbar.progress


        radio_left.setOnCheckedChangeListener { _, isChecked ->
            shadowLayout.setShadowHiddenLeft(isChecked)
        }
        radio_top.setOnCheckedChangeListener { _, isChecked ->
            shadowLayout.setShadowHiddenTop(isChecked)
        }
        radio_right.setOnCheckedChangeListener { _, isChecked ->
            shadowLayout.setShadowHiddenRight(isChecked)
        }
        radio_buttom.setOnCheckedChangeListener { _, isChecked ->
            shadowLayout.setShadowHiddenBottom(isChecked)
        }

        //-----------> 阴影颜色
        shadow_color_red_seekbar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                red = progress
                shadowLayout.setShadowColor(Color.argb(shadowAlpha, red, green, blue))
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }
        })

        shadow_color_green_seekbar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                green = progress
                shadowLayout.setShadowColor(Color.argb(shadowAlpha, red, green, blue))
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }
        })

        shadow_color_blue_seekbar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                blue = progress
                shadowLayout.setShadowColor(Color.argb(shadowAlpha, red, green, blue))
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }
        })
        //<----------- 阴影颜色


        //阴影扩散程度
        shadow_limit_seekbar.apply {
            progress = shadowLayout.shadowLimit.toInt()
            shadow_limit_tip.text = String.format("阴影扩撒程度:%s", progress)
            setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                    shadow_limit_tip.text = String.format("阴影扩撒程度:%s", progress)
                    shadowLayout.setShadowLimit(progress)
                }

                override fun onStartTrackingTouch(seekBar: SeekBar?) {
                }

                override fun onStopTrackingTouch(seekBar: SeekBar?) {
                }
            })
        }

        //阴影X|Y偏移量
        shadow_color_offsetx_seekbar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                shadow_offsetx_tip.text = String.format("阴影X偏移量:%s",progress)
                shadowLayout.setShadowOffsetX(progress.toFloat())
            }
            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })
        shadow_color_offsety_seekbar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                shadow_offsety_tip.text = String.format("阴影Y偏移量:%s",progress)
                shadowLayout.setShadowOffsetY(progress.toFloat())
            }
            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })

        //圆角效果
        shadow_round_rect_seekbar.apply {
            progress = shadowLayout.cornerRadius.toInt()
            shadow_round_rect_tip.text = String.format("圆角大小:%s",progress)
            setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                    shadow_round_rect_tip.text = String.format("圆角大小:%s",progress)
                    shadowLayout.setCornerRadius(progress)
                }

                override fun onStartTrackingTouch(seekBar: SeekBar?) {
                }

                override fun onStopTrackingTouch(seekBar: SeekBar?) {}
            })
        }

        //透明度设置
        shadow_alpha_seekbar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                shadowAlpha = progress
                shadow_alpha_tip.text = String.format("透明度:%.0f %%",progress.toFloat() * 100 / 255)
                shadowLayout.setShadowColor(Color.argb(shadowAlpha, red, green, blue))
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }
        })
    }

}
