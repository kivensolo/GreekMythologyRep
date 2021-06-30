package com.zeke.demo.base

import android.os.Bundle
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.kingz.module.common.BaseActivity
import com.zeke.demo.R
import com.zeke.demo.fragments.CardVerticalDemoFragment
import com.zeke.demo.model.DemoContentModel

/**
 * Demo练习页面的抽象基类
 * 由TabLayout和ViewPager组成的页面
 */
abstract class AbsDemoActivity : BaseActivity() {
    open var tabLayout: TabLayout? = null
    open var pager: ViewPager? = null
    open val pageModels: MutableList<DemoContentModel> by lazy { ArrayList<DemoContentModel>() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_tab)
        initViews()
        inflatePageData()
        initPagerAdapter()
    }

    open fun initViews(){
        val normalColor = resources.getColor(android.R.color.black)
        val selectedColor = resources.getColor(R.color.hub_yellow)
        pager = findViewById<View>(R.id.viewpager) as ViewPager
        tabLayout = findViewById<View>(R.id.tab_layout) as TabLayout
        tabLayout?.apply {
            tabMode = TabLayout.MODE_SCROLLABLE
            setTabTextColors(normalColor, selectedColor)
            setupWithViewPager(pager)
        }
        findViewById<View>(R.id.iv_tab_menu).visibility = View.GONE
        findViewById<View>(R.id.ivSearch).visibility = View.GONE
    }

    /**
     * 填充页面数据Page数据
     */
    open fun inflatePageData(){}

    /**
     * 初始化Pager的Adapter
     */
    open fun initPagerAdapter(){
         pager?.adapter = object : FragmentPagerAdapter(supportFragmentManager,
             BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
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
                val rootRecycleView = (`object` as androidx.fragment.app.Fragment).view
                val contentView = rootRecycleView?.findViewById<LinearLayout>(R.id.card_content_layout)
                if (contentView != null && contentView.childCount > 0) {
                    contentView.removeAllViews()
                }
                // 将所有Fragment的视图保存下来
                // super.destroyItem(container, position, `object`)
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        return super.onCreateOptionsMenu(menu)
    }

}
