package com.zeke.demo.base

import com.zeke.demo.fragments.CardVerticalDemoFragment
import com.zeke.demo.model.CardItemModel

/**
 * author: King.Z <br>
 * date:  2020/4/19 11:32 <br>
 * description: Demo templete base Activity of Card style.
 * 实现类只需要进行cardList和pageModels的数据装载即可。
 * <br>
 */
abstract class AbsCardDemoActivity : AbsDemoActivity() {
    val cardList: MutableList<CardItemModel> by lazy { ArrayList<CardItemModel>() }
    /**
     *  初始化卡片模板数据
     */
    open fun initCardListData(){}

    override fun inflatePageData() {
        super.inflatePageData()
        initCardListData()
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
        }
    }
}