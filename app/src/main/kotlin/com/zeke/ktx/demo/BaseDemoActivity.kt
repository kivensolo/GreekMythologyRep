package com.zeke.ktx.demo

import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.view.ViewPager
import android.view.Menu
import android.view.View
import com.kingz.customdemo.R
import com.zeke.ktx.base.BaseActivity
import com.zeke.ktx.demo.modle.DemoContentModel
import java.util.*

/**
 * Demo练习页面的抽象基类
 */
abstract class BaseDemoActivity : BaseActivity() {
    var tabLayout: TabLayout? = null
    var pager: ViewPager? = null
    protected var pageModels: MutableList<DemoContentModel> = ArrayList()

    /**
     * 初始化页面数据模型
     */
    open fun initPageModels(){}

    /**
     * 初始化Pager的Adapter
     */
    open fun initPagerAdapter(){}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_tab)
        initPageModels()
        pager = findViewById<View>(R.id.viewpager) as ViewPager
        initPagerAdapter()
        initTabLayout()
    }

    /**
     * 初始化TabLayout
     */
    private fun initTabLayout() {
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

}
