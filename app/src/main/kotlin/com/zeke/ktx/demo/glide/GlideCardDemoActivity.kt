package com.zeke.ktx.demo.glide

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentPagerAdapter
import android.view.Menu
import com.kingz.customdemo.R
import com.zeke.ktx.demo.BaseDemoActivity
import com.zeke.ktx.demo.CardVerticalDemoFragment
import com.zeke.ktx.demo.draw.base_api.Practice1ColorView
import com.zeke.ktx.demo.draw.base_api.Practice2CircleView
import com.zeke.ktx.demo.draw.base_api.Practice3RectView
import com.zeke.ktx.demo.modle.CardItemModle
import com.zeke.ktx.demo.modle.DemoContentModel

class GlideCardDemoActivity : BaseDemoActivity() {

    private var cardDatas: MutableList<CardItemModle>? = null

    override fun initPageModels() {
        // 创建卡片数据
        cardDatas = ArrayList()
        cardDatas?.add(CardItemModle("Circle", Practice1ColorView(this)))
        cardDatas?.add(CardItemModle("Round", Practice2CircleView(this)))
        cardDatas?.add(CardItemModle("Round_with_border", Practice3RectView(this)))

        // 初始化Page数据
        pageModels.add(DemoContentModel(getString(R.string.glide_circle), cardDatas))
        // pageModels.add(DemoContentModel(getString(R.string.glide_round), cardDatas))
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
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        return super.onCreateOptionsMenu(menu)
    }
}
