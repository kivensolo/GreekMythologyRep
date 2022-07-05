package com.zeke.demo.base

import com.zeke.demo.model.CardItemModel

/**
 * author: King.Z <br>
 * date:  2020/4/19 11:32 <br>
 * description: Demo templete base Activity of Card style.
 * 实现类只需要进行cardList和pageModels的数据装载即可。
 * <br>
 */
abstract class AbsCardDemoActivity : AbsDemoActivity() {
    val cardList: MutableList<CardItemModel> by lazy { ArrayList() }
    /**
     *  初始化卡片模板数据
     */
    open fun initCardListData(){}

    override fun inflatePageData() {
        super.inflatePageData()
        initCardListData()
    }
}