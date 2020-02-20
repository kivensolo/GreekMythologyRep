package com.zeke.ktx.activities.indicator

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.support.v4.view.ViewPager
import android.view.View
import android.view.animation.AccelerateInterpolator
import android.view.animation.DecelerateInterpolator
import com.kingz.customdemo.R
import com.kingz.utils.ColorCompatUtils
import com.zeke.kangaroo.magicindicator.MagicIndicator
import com.zeke.kangaroo.magicindicator.ViewPagerHelper
import com.zeke.kangaroo.magicindicator.buildins.UIUtil
import com.zeke.kangaroo.magicindicator.buildins.commonnavigator.CommonNavigator
import com.zeke.kangaroo.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter
import com.zeke.kangaroo.magicindicator.buildins.commonnavigator.abs.IPagerIndicator
import com.zeke.kangaroo.magicindicator.buildins.commonnavigator.abs.IPagerTitleView
import com.zeke.kangaroo.magicindicator.buildins.commonnavigator.indicators.BezierPagerIndicator
import com.zeke.kangaroo.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator
import com.zeke.kangaroo.magicindicator.buildins.commonnavigator.indicators.TriangularPagerIndicator
import com.zeke.kangaroo.magicindicator.buildins.commonnavigator.indicators.WrapPagerIndicator
import com.zeke.kangaroo.magicindicator.buildins.commonnavigator.titles.ClipPagerTitleView
import com.zeke.kangaroo.magicindicator.buildins.commonnavigator.titles.ColorTransitionPagerTitleView
import com.zeke.kangaroo.magicindicator.buildins.commonnavigator.titles.SimplePagerTitleView
import com.zeke.ktx.adapter.ExamplePagerAdapter
import com.zeke.ktx.base.BaseActivity
import com.zeke.ktx.view.ColorFlipPagerTitleView
import com.zeke.ktx.view.ScaleTransitionPagerTitleView
import java.util.*


/**
 * author：KingZ
 * date：2020/2/16
 * description：可滑动的MagicIndicator Demo页面
 */
class ScrollableTabExampleActivity : BaseActivity() {
    companion object {
        private val VERSIONS = arrayOf("CUPCAKE", "DONUT", "ECLAIR",
                "GINGERBREAD", "HONEYCOMB", "ICE_CREAM_SANDWICH",
                "JELLY_BEAN", "KITKAT", "LOLLIPOP", "M", "NOUGAT")
        val mDataList: List<String> = VERSIONS.toList() as ArrayList<String>
    }
    private var pagerAdapter = ExamplePagerAdapter(mDataList)
    private var mViewPager: ViewPager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scrollable_indicator_example_layout)
        mViewPager = findViewById(R.id.view_pager)
        mViewPager!!.adapter = pagerAdapter

        initMagicIndicator1()
        initMagicIndicator2()
        initMagicIndicator3()
        initMagicIndicator4()
        initMagicIndicator5()
        initMagicIndicator6()
        initMagicIndicator7()
        initMagicIndicator8()
        initMagicIndicator9()
    }


    /**
     * 创建滚动中有Clip效果的指示器
     */
    private fun initMagicIndicator1() {
        val magicIndicator = findViewById<View>(R.id.magic_indicator1) as MagicIndicator
        magicIndicator.setBackgroundColor(ColorCompatUtils.getColor(resources,R.color.accent_A400))

        val commonNavigator = CommonNavigator(this)
        commonNavigator.isSkimOver = false

        val padding: Int = UIUtil.getScreenWidth(this) / 3
        commonNavigator.rightPadding = padding
        commonNavigator.leftPadding = padding
        commonNavigator.adapter = object : CommonNavigatorAdapter() {

            override fun getCount(): Int {
                return mDataList.size
            }

            override fun getTitleView(context: Context, index: Int): IPagerTitleView {
                val clipPagerTitleView = ClipPagerTitleView(context)
                clipPagerTitleView.text = mDataList[index]
                clipPagerTitleView.textColor = ColorCompatUtils.getColor(resources,R.color.pink)
                clipPagerTitleView.clipColor = Color.WHITE
                clipPagerTitleView.setOnClickListener {
                    mViewPager!!.currentItem = index
                }
                return clipPagerTitleView
            }

            override fun getIndicator(context: Context): IPagerIndicator? {
                return null
            }
        }
        magicIndicator.navigator = commonNavigator
        ViewPagerHelper.bind(magicIndicator, mViewPager)
    }

    private fun initMagicIndicator2() {
        val magicIndicator = findViewById<View>(R.id.magic_indicator2) as MagicIndicator
        magicIndicator.setBackgroundColor(Color.parseColor("#00c853"))
        val commonNavigator = CommonNavigator(this)
        commonNavigator.scrollPivotX = 0.25f
        commonNavigator.adapter = object : CommonNavigatorAdapter() {
            override fun getCount(): Int {
                return mDataList.size
            }

            override fun getTitleView(context: Context, index: Int): IPagerTitleView {
                val simplePagerTitleView = SimplePagerTitleView(context)
                simplePagerTitleView.text = mDataList[index]
                simplePagerTitleView.normalColor = Color.parseColor("#c8e6c9")
                simplePagerTitleView.selectedColor = Color.WHITE
                simplePagerTitleView.textSize = 12f
                simplePagerTitleView.setOnClickListener { mViewPager!!.currentItem = index }
                return simplePagerTitleView
            }

            override fun getIndicator(context: Context): IPagerIndicator {
                val indicator = LinePagerIndicator(context)
                indicator.mode = LinePagerIndicator.MODE_EXACTLY
                indicator.yOffset = UIUtil.dip2px(context, 3.0).toFloat()
                indicator.setColors(Color.parseColor("#ffffff"))
                return indicator
            }
        }
        magicIndicator.navigator = commonNavigator
        ViewPagerHelper.bind(magicIndicator, mViewPager)
    }

    private fun initMagicIndicator3() {
        val magicIndicator = findViewById<View>(R.id.magic_indicator3) as MagicIndicator
        magicIndicator.setBackgroundColor(Color.BLACK)
        val commonNavigator = CommonNavigator(this)
        commonNavigator.adapter = object : CommonNavigatorAdapter() {

            override fun getCount(): Int {
                return mDataList.size
            }

            override fun getTitleView(context: Context, index: Int): IPagerTitleView {
                val simplePagerTitleView = ColorTransitionPagerTitleView(context)
                simplePagerTitleView.normalColor = Color.GRAY
                simplePagerTitleView.selectedColor = Color.WHITE
                simplePagerTitleView.text = mDataList[index]
                simplePagerTitleView.setOnClickListener { mViewPager!!.currentItem = index }
                return simplePagerTitleView
            }

            override fun getIndicator(context: Context): IPagerIndicator {
                val linePagerIndicator = LinePagerIndicator(context)
                linePagerIndicator.mode = LinePagerIndicator.MODE_WRAP_CONTENT
                linePagerIndicator.setColors(Color.WHITE)
                return linePagerIndicator
            }
        }
        magicIndicator.navigator = commonNavigator
        ViewPagerHelper.bind(magicIndicator, mViewPager)
    }

    private fun initMagicIndicator4() {
        val magicIndicator = findViewById<View>(R.id.magic_indicator4) as MagicIndicator
        magicIndicator.setBackgroundColor(Color.parseColor("#455a64"))
        val commonNavigator = CommonNavigator(this)
        commonNavigator.adapter = object : CommonNavigatorAdapter() {
            override fun getCount(): Int {
                return mDataList.size
            }

            override fun getTitleView(context: Context, index: Int): IPagerTitleView {
                val simplePagerTitleView = ColorTransitionPagerTitleView(context)
                simplePagerTitleView.text = mDataList[index]
                simplePagerTitleView.normalColor = Color.parseColor("#88ffffff")
                simplePagerTitleView.selectedColor = Color.WHITE
                simplePagerTitleView.setOnClickListener { mViewPager!!.currentItem = index }
                return simplePagerTitleView
            }

            override fun getIndicator(context: Context): IPagerIndicator {
                val indicator = LinePagerIndicator(context)
                indicator.setColors(Color.parseColor("#40c4ff"))
                return indicator
            }
        }
        magicIndicator.navigator = commonNavigator
        ViewPagerHelper.bind(magicIndicator, mViewPager)
    }

    /**
     * 今日头条效果的Indicator: 大小缩放&颜色渐变
     */
    private fun initMagicIndicator5() {
        val magicIndicator = findViewById<View>(R.id.magic_indicator5) as MagicIndicator
        magicIndicator.setBackgroundColor(Color.WHITE)
        val commonNavigator = CommonNavigator(this)
        commonNavigator.scrollPivotX = 0.8f
        commonNavigator.adapter = object : CommonNavigatorAdapter() {
            override fun getCount(): Int {
                return mDataList.size
            }

            override fun getTitleView(context: Context, index: Int): IPagerTitleView {
                val simplePagerTitleView = ScaleTransitionPagerTitleView(context)
                simplePagerTitleView.text = mDataList[index]
                simplePagerTitleView.textSize = 18f
                simplePagerTitleView.normalColor = Color.parseColor("#616161")
                simplePagerTitleView.selectedColor = Color.parseColor("#f57c00")
                simplePagerTitleView.setOnClickListener { mViewPager!!.currentItem = index }
                return simplePagerTitleView
            }

            override fun getIndicator(context: Context): IPagerIndicator {
                val indicator = LinePagerIndicator(context)
                indicator.startInterpolator = AccelerateInterpolator()
                indicator.endInterpolator = DecelerateInterpolator(1.6f)
//                indicator.yOffset = UIUtil.dip2px(context, 39.0).toFloat()
                indicator.yOffset = UIUtil.dip2px(context, 0.0).toFloat()
                indicator.lineHeight = UIUtil.dip2px(context, 3.0).toFloat()
//                indicator.setColors(Color.parseColor("#f57c00"))
                indicator.setColors(resources.getColor(R.color.hub_yellow))
                return indicator
            }
        }
        magicIndicator.navigator = commonNavigator
        ViewPagerHelper.bind(magicIndicator, mViewPager)
    }

    /**
     * 贝塞尔曲线带颜色渐变的ViewPager指示器
     */
    private fun initMagicIndicator6() {
        val magicIndicator = findViewById<View>(R.id.magic_indicator6) as MagicIndicator
        magicIndicator.setBackgroundColor(Color.WHITE)
        val commonNavigator = CommonNavigator(this)
        commonNavigator.adapter = object : CommonNavigatorAdapter() {
            override fun getCount(): Int {
                return mDataList.size
            }

            override fun getTitleView(context: Context, index: Int): IPagerTitleView {
                val simplePagerTitleView = ScaleTransitionPagerTitleView(context)
                simplePagerTitleView.text = mDataList[index]
                simplePagerTitleView.textSize = 18f
                simplePagerTitleView.normalColor = Color.GRAY
                simplePagerTitleView.selectedColor = Color.BLACK
                simplePagerTitleView.setOnClickListener { mViewPager!!.currentItem = index }
                return simplePagerTitleView
            }

            override fun getIndicator(context: Context): IPagerIndicator {
                val indicator = BezierPagerIndicator(context)
                indicator.setColors(Color.parseColor("#ff4a42"), Color.parseColor("#fcde64"), Color.parseColor("#73e8f4"), Color.parseColor("#76b0ff"), Color.parseColor("#c683fe"))
                return indicator
            }
        }
        magicIndicator.navigator = commonNavigator
        ViewPagerHelper.bind(magicIndicator, mViewPager)
    }

    private fun initMagicIndicator7() {
        val magicIndicator = findViewById<View>(R.id.magic_indicator7) as MagicIndicator
        magicIndicator.setBackgroundColor(Color.parseColor("#fafafa"))
        val commonNavigator7 = CommonNavigator(this)
        commonNavigator7.scrollPivotX = 0.65f
        commonNavigator7.adapter = object : CommonNavigatorAdapter() {
            override fun getCount(): Int {
                return mDataList.size
            }

            override fun getTitleView(context: Context, index: Int): IPagerTitleView {
                val simplePagerTitleView = ColorFlipPagerTitleView(context)
                simplePagerTitleView.text = mDataList[index]
                simplePagerTitleView.normalColor = Color.parseColor("#9e9e9e")
                simplePagerTitleView.selectedColor = Color.parseColor("#00c853")
                simplePagerTitleView.setOnClickListener { mViewPager!!.currentItem = index }
                return simplePagerTitleView
            }

            override fun getIndicator(context: Context): IPagerIndicator {
                val indicator = LinePagerIndicator(context)
                indicator.mode = LinePagerIndicator.MODE_EXACTLY
                indicator.lineHeight = UIUtil.dip2px(context, 6.0).toFloat()
                indicator.lineWidth = UIUtil.dip2px(context, 10.0).toFloat()
                indicator.roundRadius = UIUtil.dip2px(context, 3.0).toFloat()
                indicator.startInterpolator = AccelerateInterpolator()
                indicator.endInterpolator = DecelerateInterpolator(2.0f)
                indicator.setColors(Color.parseColor("#00c853"))
                return indicator
            }
        }
        magicIndicator.navigator = commonNavigator7
        ViewPagerHelper.bind(magicIndicator, mViewPager)
    }

    private fun initMagicIndicator8() {
        val magicIndicator = findViewById<View>(R.id.magic_indicator8) as MagicIndicator
        magicIndicator.setBackgroundColor(Color.WHITE)
        val commonNavigator = CommonNavigator(this)
        commonNavigator.scrollPivotX = 0.35f
        commonNavigator.adapter = object : CommonNavigatorAdapter() {
            override fun getCount(): Int {
                return mDataList.size
            }

            override fun getTitleView(context: Context, index: Int): IPagerTitleView {
                val simplePagerTitleView = SimplePagerTitleView(context)
                simplePagerTitleView.text = mDataList[index]
                simplePagerTitleView.normalColor = Color.parseColor("#333333")
                simplePagerTitleView.selectedColor = Color.parseColor("#e94220")
                simplePagerTitleView.setOnClickListener { mViewPager!!.currentItem = index }
                return simplePagerTitleView
            }

            override fun getIndicator(context: Context): IPagerIndicator {
                val indicator = WrapPagerIndicator(context)
                indicator.fillColor = Color.parseColor("#ebe4e3")
                return indicator
            }
        }
        magicIndicator.navigator = commonNavigator
        ViewPagerHelper.bind(magicIndicator, mViewPager)
    }

    private fun initMagicIndicator9() {
        val magicIndicator = findViewById<View>(R.id.magic_indicator9) as MagicIndicator
        magicIndicator.setBackgroundColor(Color.WHITE)
        val commonNavigator = CommonNavigator(this)
        commonNavigator.scrollPivotX = 0.15f
        commonNavigator.adapter = object : CommonNavigatorAdapter() {
            override fun getCount(): Int {
                return mDataList.size
            }

            override fun getTitleView(context: Context, index: Int): IPagerTitleView {
                val simplePagerTitleView = SimplePagerTitleView(context)
                simplePagerTitleView.text = mDataList[index]
                simplePagerTitleView.normalColor = Color.parseColor("#333333")
                simplePagerTitleView.selectedColor = Color.parseColor("#e94220")
                simplePagerTitleView.setOnClickListener { mViewPager!!.currentItem = index }
                return simplePagerTitleView
            }

            override fun getIndicator(context: Context): IPagerIndicator {
                val indicator = TriangularPagerIndicator(context)
                indicator.lineColor = Color.parseColor("#e94220")
                return indicator
            }
        }
        magicIndicator.navigator = commonNavigator
        ViewPagerHelper.bind(magicIndicator, mViewPager)
    }
}