package com.zeke.demo.meterial

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Bundle
import androidx.palette.graphics.Palette
import com.kingz.base.BaseVMActivity
import com.zeke.demo.R
import com.zeke.reactivehttp.base.BaseReactiveViewModel
import kotlinx.android.synthetic.main.activity_palette_demo.*

/**
 * author：ZekeWang
 * date：2021/9/20
 * description：Palette Demo展示
 * Palette是一个类似调色板的工具类，根据传入的bitmap，提取出主体颜色，
 * 使得图片和颜色更加搭配，界面更协调
 *   Palette 提供了四个静态方法用来生成对象:
 *   ● Palette generate(Bitmap bitmap)
 *   ● Palette generate(Bitmap bitmap, int numColors)
 *   ● generateAsync(Bitmap bitmap, PaletteAsyncListener listener)
 *   ● generateAsync(Bitmap bitmap, int numColors, final PaletteAsyncListener listener)

 *   得到Palette对象后，还可以拿到提取到的颜色值的Swatch对象，getXxxxxSwatch()
 *   ● getPopulation(): 样本中的像素数量
 *   ● getRgb(): 颜色的RBG值
 *   ● getHsl(): 颜色的HSL值
 *   ● getBodyTextColor(): 主体文字的颜色值
 *   ● getTitleTextColor(): 标题文字的颜色值
 *  通过 getRgb() 可以得到最终的颜色值并应用到UI中。
 *  getBodyTextColor() 和 getTitleTextColor() 可以得到此颜色下文字适合的颜色，
 *  这样很方便我们设置文字的颜色，使文字看起来更加舒服。
 *
 *  【高级玩法：自定义Palette】
 *  Palette.Builder允许自定义Palette, 通过从结果Palette中选择多少种颜色,
 *  Builder使用图片的什么区域生成Palette, Palette中允许什么颜色等.
 *  比如, 你能够过滤掉黑色, 或者, Builder只能够使用图片的上半部分生成Palette.
 *
 *  通过Palette.Builder中的下列方法, 可以微调Palette尺寸和颜色:
 *  addFilter():
 *  该方法添加了过滤器, 用以表明结果Palette中什么颜色是允许的.
 *  传入自己的Palette.Filter, 修改isAllowed()方法来决定Palette过滤哪些方法.
 *
 *  maximumColorCount():
 *  该方法设置了Palette中最大的颜色数目. 默认值是16,
 *  最优值依赖于源图. 对于风景图, 最优值处于8~16, 而肖像图通常拥有16~24个值.
 *  颜色越多, Palette.Builder生成Palette就需要花费超久的时间.
 *
 *  setRegion():
 *  该方法指出了生成Palette时, Builder使用Bitmap的什么区域.
 *  你只能使用这个方法从Bitmap中生成Palette, 而不会影响源图.
 *
 *  addTarget():
 *  该方法允许你通过向Builder添加Target色彩配置文件来执行自己的颜色匹配.
 *  如果默认的Target并不有效的话, 高级开发人员能够使用Target.Builder创建自己的Target.
 *
 *  API:
 *   https://developer.android.google.cn/reference/androidx/palette/graphics/Palette
 */
class PaletteDemoActivity : BaseVMActivity() {
    override val viewModel: BaseReactiveViewModel
        get() = TODO("Not yet implemented")

    override fun getContentLayout(): Int = R.layout.activity_palette_demo

    override fun initData(savedInstanceState: Bundle?) {
    }

    override fun initView(savedInstanceState: Bundle?) {
        super.initView(savedInstanceState)
        createPaletteAsync()
    }

    private fun createPaletteAsync() {
        val bitmap: Bitmap = BitmapFactory.decodeResource(resources, R.drawable.palette)
        //初级玩法
        Palette.from(bitmap).generate { palette ->
            palette?.apply {
                //Vibrant （鲜艳的）
                vibrant.setBackgroundColor(getVibrantColor(Color.BLUE))
                vibrant.setTextColor(darkVibrantSwatch?.bodyTextColor?:Color.RED)
                //Vibrant light（鲜艳的 亮色）
                light_vibrant.setBackgroundColor(getLightVibrantColor(Color.BLUE))
                light_vibrant.setTextColor(lightVibrantSwatch?.bodyTextColor?:Color.RED)
                //Vibrant dark（鲜艳的 暗色）
                dark_vibrant.setBackgroundColor(getDarkVibrantColor(Color.BLUE))
                dark_vibrant.setTextColor(darkVibrantSwatch?.bodyTextColor?:Color.RED)

                //Muted （柔和的）
                muted.setBackgroundColor(getMutedColor(Color.BLUE))
                muted.setTextColor(mutedSwatch?.bodyTextColor?:Color.RED)
                //Muted light（柔和的 亮色）
                light_muted.setBackgroundColor(getLightMutedColor(Color.BLUE))
                light_muted.setTextColor(lightMutedSwatch?.bodyTextColor?:Color.RED)
                //Muted dark（柔和的 暗色）
                dark_muted.setBackgroundColor(getDarkMutedColor(Color.BLUE))
                dark_muted.setTextColor(darkMutedSwatch?.bodyTextColor?:Color.RED)
            }
        }
    }
}