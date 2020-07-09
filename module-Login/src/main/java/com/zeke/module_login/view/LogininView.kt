package com.zeke.module_login.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.EditText
import android.widget.LinearLayout

import com.zeke.module_login.R

/**
 * 启动登录页面的用户账号登录视图
 */
class LogininView : LinearLayout {

    private var edit1: EditText? = null
    private var edit2: EditText? = null

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
        orientation = LinearLayout.VERTICAL
        LayoutInflater.from(context)
                .inflate(R.layout.form_view, this)
        edit1 = findViewById<EditText>(R.id.login_name)
        edit2 = findViewById<EditText>(R.id.login_pwd)
    }

    override fun setFocusable(focusable: Boolean) {
        super.setFocusable(focusable)
        edit1!!.isFocusable = focusable
        edit2!!.isFocusable = focusable
    }
}
