package com.zeke.module_login.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import com.zeke.module_login.R
import kotlinx.android.synthetic.main.form_view.view.*

/**
 * 启动登录页面的用户账号登录视图
 */
class LogininView : LinearLayout {

    constructor(context: Context) : super(context) {
        loadView()
    }

    constructor(context: Context,
                attrs: AttributeSet) : super(context, attrs) {
        loadView()
    }

    constructor(context: Context,
                attrs: AttributeSet,
                defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        loadView()
    }

    private fun loadView() {
        orientation = VERTICAL
        LayoutInflater.from(context).inflate(R.layout.form_view, this)
    }

    override fun setFocusable(focusable: Boolean) {
        super.setFocusable(focusable)
        login_name?.isFocusable = focusable
        login_pwd?.isFocusable = focusable
    }
}
