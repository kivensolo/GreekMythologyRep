package com.zeke.demo.draw

import com.zeke.demo.R
import com.zeke.demo.base.AbsDemoActivity
import com.zeke.demo.model.CardItemModel
import com.zeke.demo.model.DemoContentModel
import java.util.*

/**
 * author：ZekeWang
 * date：2021/6/30
 * description：
 * 自定义View的Layout练习页面
 */
class PracticeLayoutActivity: AbsDemoActivity() {

    override fun inflatePageData() {
        val pageDataLayout: MutableList<CardItemModel> = ArrayList()
        with(pageDataLayout){
            val inflateView = layoutInflater.inflate(R.layout.practice_square_image_view, null, false)
            add(CardItemModel("onMeasure_First", inflateView))
        }

        with(pageModels) {
            add(DemoContentModel("自定义布局", pageDataLayout))
        }
    }
}