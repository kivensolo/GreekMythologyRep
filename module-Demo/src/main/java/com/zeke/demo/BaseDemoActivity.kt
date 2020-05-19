package com.zeke.demo

import android.os.Bundle
import android.view.Menu
import android.view.View
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.kingz.module.common.BaseActivity
import com.zeke.demo.model.DemoContentModel

/**
 * Demo练习页面的抽象基类
 * 由TabLayout和ViewPager组成的页面
 */
abstract class BaseDemoActivity : BaseActivity() {
    open var tabLayout: TabLayout? = null
    open var pager: ViewPager? = null
    open val pageModels: MutableList<DemoContentModel> by lazy { ArrayList<DemoContentModel>() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_tab)
        initViews()
        initPageModels()
        initPagerAdapter()
    }

    open fun initViews(){
        val normalColor = resources.getColor(android.R.color.white)
        val selectedColor = resources.getColor(R.color.hub_yellow)
        tabLayout?.tabMode = TabLayout.MODE_SCROLLABLE
        tabLayout?.setTabTextColors(normalColor, selectedColor)
        tabLayout?.setupWithViewPager(pager)

        pager = findViewById<View>(R.id.viewpager) as androidx.viewpager.widget.ViewPager
    }

    /**
     * 初始化页面数据模型
     */
    open fun initPageModels(){}

    /**
     * 初始化Pager的Adapter
     */
    open fun initPagerAdapter(){}

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        return super.onCreateOptionsMenu(menu)
    }

}
