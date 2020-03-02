package com.zeke.ktx.activity.practice_draw

import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.view.ViewPager
import android.view.Menu
import android.view.View
import com.kingz.customdemo.R
import com.zeke.ktx.base.BaseActivity
import com.zeke.ktx.fragments.PracticePageFragment
import java.util.*

class PracticeDrawOneActivity : BaseActivity() {
    var tabLayout: TabLayout? = null
    var pager: ViewPager? = null
    private var pageModels: MutableList<PageModel> = ArrayList()

    private fun initPageModels() {
        pageModels.add(PageModel(R.string.title_1))
        pageModels.add(PageModel(R.string.title_2))
        pageModels.add(PageModel(R.string.title_3))
        pageModels.add(PageModel(R.string.title_4))
        pageModels.add(PageModel(R.string.title_5))
        pageModels.add(PageModel(R.string.title_6))
        pageModels.add(PageModel(R.string.title_7))
        pageModels.add(PageModel(R.string.title_8))
        pageModels.add(PageModel(R.string.title_9))
        pageModels.add(PageModel(R.string.title_10))
        pageModels.add(PageModel(R.string.title_11))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_tab)
        initPageModels()
        pager = findViewById<View>(R.id.viewpager) as ViewPager
        pager?.adapter = object : FragmentPagerAdapter(supportFragmentManager) {

            override fun getItem(position: Int): Fragment {
                val pageModel = pageModels[position]
                return PracticePageFragment.newInstance(pageModel.titleRes)
            }

            override fun getCount(): Int {
                return pageModels.size
            }

            override fun getPageTitle(position: Int): CharSequence? {
                return getString(pageModels[position].titleRes)
            }
        }

        tabLayout = findViewById<View>(R.id.tab_layout) as TabLayout
        val normalColor = resources.getColor(android.R.color.white)
        val selectedColor = resources.getColor(R.color.hub_yellow)
        tabLayout?.setPadding(0, 0, 0, 0)
        tabLayout?.setTabTextColors(normalColor, selectedColor)
        tabLayout?.setupWithViewPager(pager)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        return super.onCreateOptionsMenu(menu)
    }

    private inner class PageModel internal constructor(
            internal var titleRes: Int)
}
