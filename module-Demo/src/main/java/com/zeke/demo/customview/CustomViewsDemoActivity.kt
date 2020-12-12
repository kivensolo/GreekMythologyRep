package com.zeke.demo.customview

import com.zeke.demo.base.AbsCardDemoActivity
import com.zeke.demo.customview.views.ChartMusicView
import com.zeke.demo.customview.views.ChartTextView
import com.zeke.demo.model.CardItemModel
import com.zeke.demo.model.DemoContentModel

/**
 * author: King.Z <br>
 * date:  2020/4/19 11:32 <br>
 * description: 自定义view Demo展示的页面 <br>
 */
class CustomViewsDemoActivity : AbsCardDemoActivity() {
    override fun initCardListData() {
        super.initCardListData()
          // 创建卡片数据
        cardList.add(CardItemModel("ChartMusicView", ChartMusicView(this)))
        cardList.add(CardItemModel("带音乐跳动效果的TextView", ChartTextView(this)))
    }
    override fun inflatePageData() {
        super.inflatePageData()
        pageModels.add(DemoContentModel("自定义views", cardList))
    }
}