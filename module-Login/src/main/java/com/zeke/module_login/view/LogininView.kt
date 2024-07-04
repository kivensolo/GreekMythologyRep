package com.zeke.module_login.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import com.zeke.module_login.databinding.FormViewBinding

/**
 * 启动登录页面的用户账号登录视图
 */
class LogininView : LinearLayout {
    private lateinit var formViewbinding: FormViewBinding

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
        formViewbinding = FormViewBinding.inflate(LayoutInflater.from(context), this);
    }

    fun getBinding():FormViewBinding = formViewbinding

    override fun setFocusable(focusable: Boolean) {
        super.setFocusable(focusable)
        formViewbinding.loginName.isFocusable = focusable
        formViewbinding.loginPwd.isFocusable = focusable
    }
}
