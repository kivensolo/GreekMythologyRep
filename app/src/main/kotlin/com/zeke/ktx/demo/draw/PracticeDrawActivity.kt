package com.zeke.ktx.demo.draw

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentPagerAdapter
import android.view.Menu
import android.view.ViewGroup
import com.kingz.customdemo.R
import com.zeke.ktx.demo.BaseDemoActivity
import com.zeke.ktx.demo.CardVerticalDemoFragment
import com.zeke.ktx.demo.draw.base_api.*
import com.zeke.ktx.demo.draw.paint.*
import com.zeke.ktx.demo.modle.CardItemModle
import com.zeke.ktx.demo.modle.DemoContentModel
import java.util.*

/**
 * 关于绘制练习的页面
 */
class PracticeDrawActivity : BaseDemoActivity() {

    override fun initPageModels() {
        // 1-1 基础api展示数据
        val cardData1: MutableList<CardItemModle> = ArrayList()
        cardData1.add(CardItemModle("canvas.drawColor(Color.YELLOW)",Practice1ColorView(this)))
        cardData1.add(CardItemModle("drawCircle", Practice2CircleView(this)))
        cardData1.add(CardItemModle("drawRect", Practice3RectView(this)))
        cardData1.add(CardItemModle("drawPoint: ROUND、BUTT、SQUARE",Practice4PointView(this)))
        cardData1.add(CardItemModle("drawOval", Practice5OvalView(this)))
        cardData1.add(CardItemModle("drawLine", Practice6LineView(this)))
        cardData1.add(CardItemModle("drawArc", Practice7ArcView(this)))
        cardData1.add(CardItemModle("drawPath", Practice8PathView(this)))

        val cardData2: MutableList<CardItemModle> = ArrayList()
        cardData2.add(CardItemModle("线性着色器展示", Paint1ShaderView(this)))
        cardData2.add(CardItemModle("BitmapShader", Paint2BitmapShaderView(this)))
        cardData2.add(CardItemModle("ComposeShader(未生效)", Paint3ComposeShaderView(this)))
        cardData2.add(CardItemModle("ColorFilter", Paint4ColorFilterView(this)))
        cardData2.add(CardItemModle("Xfermode(离屏缓冲 未生效)", Paint5XfermodeView(this)))

        val cardData3: MutableList<CardItemModle> = ArrayList()
        cardData3.add(CardItemModle("简单效果使用", Paint6NormalEffectView(this)))
        cardData3.add(CardItemModle("PathEffect", Paint7PathEffectView(this)))

        // 初始化Page数据
        pageModels.add(DemoContentModel(getString(R.string.draw_1_1), cardData1))
        pageModels.add(DemoContentModel(getString(R.string.draw_paint_color), cardData2))
        pageModels.add(DemoContentModel(getString(R.string.draw_paint_effect), cardData3))
    }

    override fun initPagerAdapter() {
        super.initPagerAdapter()
        pager?.adapter = object : FragmentPagerAdapter(supportFragmentManager) {
            override fun getItem(position: Int): Fragment {
                val fragment = CardVerticalDemoFragment()
                fragment.initData(pageModels[position])
                return fragment
            }

            override fun getCount(): Int {
                return pageModels.size
            }

            override fun getPageTitle(position: Int): CharSequence? {
                return pageModels[position].title
            }

            override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
                // 将所有Fragment的视图保存下来
                // super.destroyItem(container, position, `object`)
            }
        }
        // 设置缓存的试图数据是当前页的左边2+右边2
        pager?.offscreenPageLimit = 4
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        return super.onCreateOptionsMenu(menu)
    }
}
