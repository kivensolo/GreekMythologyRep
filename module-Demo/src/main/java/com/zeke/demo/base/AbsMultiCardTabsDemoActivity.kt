package com.zeke.demo.base

import com.zeke.demo.model.CardItemModel
import com.zeke.demo.model.DemoContentModel

/**
 * author: King.Z <br>
 * date:  2020/4/19 11:32 <br>
 * description:
 * 支持多个Tab页面，每个页面支持卡片样式的展示。
 * 实现类只需要进行cardList和pageModels的数据装载即可。
 * <br>
 */
abstract class AbsMultiCardTabsDemoActivity : AbsDemoActivity() {
    open lateinit var tabDataMap: Map<String, MutableList<CardItemModel>>

    override fun inflatePageData() {
        super.inflatePageData()
        tabDataMap.forEach {
            pageModels.add(DemoContentModel(it.key, it.value))
        }
    }
}