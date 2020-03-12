package com.zeke.ktx.activity.glide

import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.view.ViewPager
import android.view.Menu
import android.view.View
import com.kingz.customdemo.R
import com.zeke.ktx.base.BaseActivity
import java.util.*

//TODO 抽离
class GlideDemoActivity : BaseActivity() {
    var tabLayout: TabLayout? = null
    var pager: ViewPager? = null
    private var pageModels: MutableList<PageModel> = ArrayList()

    private fun initPageModels() {
        pageModels.add(PageModel(getString(R.string.glide_circle)))
        pageModels.add(PageModel(getString(R.string.glide_round)))
        pageModels.add(PageModel(getString(R.string.glide_round_with_border)))
        pageModels.add(PageModel(getString(R.string.glide_blur)))
        pageModels.add(PageModel(getString(R.string.glide_load_fade)))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_tab)
        initPageModels()
        pager = findViewById<View>(R.id.viewpager) as ViewPager
        pager?.adapter = object : FragmentPagerAdapter(supportFragmentManager) {

            override fun getItem(position: Int): Fragment {
                val pageModel = pageModels[position]
                return GlideDemoFragment.newInstance(pageModel.title)
            }

            override fun getCount(): Int {
                return pageModels.size
            }

            override fun getPageTitle(position: Int): CharSequence? {
                return pageModels[position].title
            }
        }

        tabLayout = findViewById<View>(R.id.tab_layout) as TabLayout
        val normalColor = resources.getColor(android.R.color.white)
        val selectedColor = resources.getColor(R.color.hub_yellow)
        tabLayout?.tabMode = TabLayout.MODE_SCROLLABLE
        tabLayout?.setTabTextColors(normalColor, selectedColor)
        tabLayout?.setupWithViewPager(pager)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        return super.onCreateOptionsMenu(menu)
    }

    private inner class PageModel internal constructor(
            internal var title: String)
}
