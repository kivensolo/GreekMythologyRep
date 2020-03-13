package com.zeke.ktx.demo.glide

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentPagerAdapter
import android.view.Menu
import com.kingz.customdemo.R
import com.zeke.ktx.activity.practice_draw.Practice1DrawColorView
import com.zeke.ktx.activity.practice_draw.Practice2DrawCircleView
import com.zeke.ktx.activity.practice_draw.Practice3DrawRectView
import com.zeke.ktx.demo.BaseDemoActivity
import com.zeke.ktx.demo.CardItemData
import com.zeke.ktx.demo.CardVerticalDemoFragment
import com.zeke.ktx.demo.modle.DemoContentModel

class GlideCardDemoActivity : BaseDemoActivity() {

    private var cardDatas: MutableList<CardItemData>? = null

    override fun initPageModels() {
        // 创建卡片数据
        cardDatas = ArrayList()
        cardDatas?.add(CardItemData("Circle", Practice1DrawColorView(this)))
        cardDatas?.add(CardItemData("Round", Practice2DrawCircleView(this)))
        cardDatas?.add(CardItemData("Round_with_border", Practice3DrawRectView(this)))

        // 初始化Page数据
        pageModels.add(DemoContentModel(getString(R.string.glide_circle), cardDatas))
        // pageModels.add(DemoContentModel(getString(R.string.glide_round), cardDatas))
    }

    override fun initPagerAdapter() {
        super.initPagerAdapter()
        pager?.adapter = object : FragmentPagerAdapter(supportFragmentManager) {
            override fun getItem(position: Int): Fragment {
                return CardVerticalDemoFragment.newInstance(pageModels[position])
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
