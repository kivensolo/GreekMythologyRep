package com.zeke.demo.glide

import com.zeke.demo.R
import com.zeke.demo.base.AbsCardDemoActivity
import com.zeke.demo.draw.base_api.Practice1ColorView
import com.zeke.demo.draw.base_api.Practice2CircleView
import com.zeke.demo.draw.base_api.Practice3RectView
import com.zeke.demo.model.CardItemModel
import com.zeke.demo.model.DemoContentModel

class GlideCardDemoActivity : AbsCardDemoActivity() {

    // 创建卡片数据
    override fun initCardListData() {
        super.initCardListData()
        cardList.add(CardItemModel("Circle", Practice1ColorView(this)))
        cardList.add(CardItemModel("Round", Practice2CircleView(this)))
        cardList.add(CardItemModel("Round_with_border", Practice3RectView(this)))
    }
    override fun inflatePageData() {
        super.inflatePageData()
        pageModels.add(DemoContentModel(getString(R.string.glide_circle), cardList))
    }
}
