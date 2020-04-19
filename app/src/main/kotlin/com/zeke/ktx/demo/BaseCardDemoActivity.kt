package com.zeke.ktx.demo

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentPagerAdapter
import com.zeke.ktx.demo.modle.CardItemModel

/**
 * author: King.Z <br>
 * date:  2020/4/19 11:32 <br>
 * description: 卡片式Demo页面基类
 * 实现类只需要进行cardList和pageModels的数据装载即可。
 * <br>
 */
abstract class BaseCardDemoActivity : BaseDemoActivity() {
    val cardList: MutableList<CardItemModel> by lazy { ArrayList<CardItemModel>() }

    override fun initPageModels() {}

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
        }
    }
}