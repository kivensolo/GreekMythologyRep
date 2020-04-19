package com.zeke.ktx.demo.glide

import com.kingz.customdemo.R
import com.zeke.ktx.demo.BaseCardDemoActivity
import com.zeke.ktx.demo.draw.base_api.Practice1ColorView
import com.zeke.ktx.demo.draw.base_api.Practice2CircleView
import com.zeke.ktx.demo.draw.base_api.Practice3RectView
import com.zeke.ktx.demo.modle.CardItemModel
import com.zeke.ktx.demo.modle.DemoContentModel

class GlideCardDemoActivity : BaseCardDemoActivity() {

    override fun initPageModels() {
        // 创建卡片数据
        cardList.add(CardItemModel("Circle", Practice1ColorView(this)))
        cardList.add(CardItemModel("Round", Practice2CircleView(this)))
        cardList.add(CardItemModel("Round_with_border", Practice3RectView(this)))
        // 初始化Page数据
        pageModels.add(DemoContentModel(getString(R.string.glide_circle), cardList))
        // pageModels.add(DemoContentModel(getString(R.string.glide_round), cardDatas))
    }
}
