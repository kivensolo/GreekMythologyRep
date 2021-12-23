package com.zeke.demo.widget

import android.content.Context
import android.view.ActionProvider
import android.view.View

/**
 * author：ZekeWang
 * date：2021/12/22
 * description： app:actionProviderClass 使用
 */
class ColorActionProvider(context: Context) : ActionProvider(context) {
    private val onMenuClickListener: OnMenuClickListener? = null

    interface OnMenuClickListener {
        fun onClick()
    }

    override fun onCreateActionView(): View {
        TODO("Not yet implemented")
    }
}