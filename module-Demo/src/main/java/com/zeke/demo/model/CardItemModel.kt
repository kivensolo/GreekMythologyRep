package com.zeke.demo.model

import android.view.View

/**
 * CardView的数据Modle
 * @param title CardView 标题
 * @param content CardView 内容View对象
 */
data class CardItemModel(
    var title: String,
    var content: View?
): BaseDemoData()