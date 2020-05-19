package com.zeke.demo.draw

import android.os.Bundle
import android.view.Menu
import android.view.ViewGroup
import android.widget.LinearLayout
import com.zeke.demo.BaseDemoActivity
import com.zeke.demo.R
import com.zeke.demo.draw.base_api.*
import com.zeke.demo.draw.paint.*
import com.zeke.demo.draw.text.*
import com.zeke.demo.fragments.CardVerticalDemoFragment
import com.zeke.demo.model.CardItemModel
import com.zeke.demo.model.DemoContentModel
import java.util.*

/**
 * 关于绘制练习的页面
 */
class PracticeDrawActivity : BaseDemoActivity() {

    override fun initPageModels() {
        // 1-1 基础api展示数据
        val cardData1: MutableList<CardItemModel> = ArrayList()
        cardData1.add(CardItemModel("canvas.drawColor(Color.YELLOW)", Practice1ColorView(this)))
        cardData1.add(CardItemModel("drawCircle", Practice2CircleView(this)))
        cardData1.add(CardItemModel("drawRect", Practice3RectView(this)))
        cardData1.add(CardItemModel("drawPoint: ROUND、BUTT、SQUARE", Practice4PointView(this)))
        cardData1.add(CardItemModel("drawOval", Practice5OvalView(this)))
        cardData1.add(CardItemModel("drawLine", Practice6LineView(this)))
        cardData1.add(CardItemModel("drawArc", Practice7ArcView(this)))
        cardData1.add(CardItemModel("drawPath", Practice8PathView(this)))

        val cardData2: MutableList<CardItemModel> = ArrayList()
        cardData2.add(CardItemModel("线性着色器展示", Paint1ShaderView(this)))
        cardData2.add(CardItemModel("BitmapShader", Paint2BitmapShaderView(this)))
        cardData2.add(CardItemModel("ComposeShader(未生效)", Paint3ComposeShaderView(this)))
        cardData2.add(CardItemModel("ColorFilter", Paint4ColorFilterView(this)))
        cardData2.add(CardItemModel("Xfermode(离屏缓冲 未生效)", Paint5XfermodeView(this)))

        val cardData3: MutableList<CardItemModel> = ArrayList()
        cardData3.add(CardItemModel("简单效果使用", Paint6NormalEffectView(this)))
        cardData3.add(CardItemModel("PathEffect", Paint7PathEffectView(this)))
        cardData3.add(CardItemModel("ShadowLayer(在下面附加效果)", Paint8ShadowLayerView(this)))
        cardData3.add(CardItemModel("MaskFilter(在上面附加效果)", Paint9MaskFilterView(this)))
        cardData3.add(CardItemModel("getPath", Paint10GetPathView(this)))

        val cardData4: MutableList<CardItemModel> = ArrayList()
        cardData4.add(CardItemModel("文字效果绘制API", Paint11TextEffecsView(this)))
        cardData4.add(CardItemModel("文字尺寸绘制API", Paint12TextDimensionView(this)))
        cardData4.add(CardItemModel("FontMetric", Paint13FontMetricView(this)))
        cardData4.add(CardItemModel("drawTextOnPath()", Paint14TextOnPathView(this)))
        cardData4.add(CardItemModel("文字换行绘制 StaticLayout", Paint15TextStaticLayoutView(this)))

        // 初始化Page数据
        pageModels.add(DemoContentModel(getString(R.string.draw_1_1), cardData1))
        pageModels.add(DemoContentModel(getString(R.string.draw_paint_color), cardData2))
        pageModels.add(DemoContentModel(getString(R.string.draw_paint_effect), cardData3))
        pageModels.add(DemoContentModel(getString(R.string.draw_text), cardData4))
    }

    override fun initPagerAdapter() {
        super.initPagerAdapter()
        pager?.adapter = object : androidx.fragment.app.FragmentPagerAdapter(supportFragmentManager) {
            override fun getItem(position: Int): androidx.fragment.app.Fragment {
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
                val rootRecycleView = (`object` as androidx.fragment.app.Fragment).view
                val contentView = rootRecycleView?.findViewById<LinearLayout>(R.id.card_content_layout)
                if (contentView != null && contentView.childCount > 0) {
                    contentView.removeAllViews()
                }

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
