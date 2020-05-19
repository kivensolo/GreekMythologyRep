package com.zeke.demo.glide

import com.zeke.demo.BaseCardDemoActivity
import com.zeke.demo.R
import com.zeke.demo.draw.base_api.Practice1ColorView
import com.zeke.demo.draw.base_api.Practice2CircleView
import com.zeke.demo.draw.base_api.Practice3RectView
import com.zeke.demo.model.CardItemModel
import com.zeke.demo.model.DemoContentModel

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
