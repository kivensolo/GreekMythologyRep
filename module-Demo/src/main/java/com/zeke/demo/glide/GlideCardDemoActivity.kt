package com.zeke.demo.glide

import com.zeke.demo.R
import com.zeke.demo.base.AbsMultiCardTabsDemoActivity
import com.zeke.demo.draw.base_api.Practice1ColorView
import com.zeke.demo.draw.base_api.Practice2CircleView
import com.zeke.demo.draw.base_api.Practice3RectView
import com.zeke.demo.model.CardItemModel

class GlideCardDemoActivity : AbsMultiCardTabsDemoActivity() {

    override fun inflatePageData() {
        tabDataMap = mapOf(
            getString(R.string.glide_circle) to mutableListOf(
                CardItemModel("Circle", Practice1ColorView(this)),
                CardItemModel("Round", Practice2CircleView(this)),
                CardItemModel("Round_with_border", Practice3RectView(this))
            )
        )
        super.inflatePageData()
    }
}
