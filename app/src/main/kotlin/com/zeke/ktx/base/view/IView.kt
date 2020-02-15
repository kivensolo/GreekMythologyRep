package com.zeke.ktx.base.view

import android.view.View

/**
 * View顶层抽象层
 */
interface IView {

    /**
     * 是否可见
     */
    val isShown: Boolean

    /**
     * 显示加载
     */
    fun showLoading()

    /**
     * 隐藏加载
     */
    fun hideLoading()

    /**
     * 错误显示
     */
    fun showError(listener: View.OnClickListener)

    /**
     * 空数据
     */
    fun showEmpty(listener: View.OnClickListener)

    /**
     * 弹出吐司
     */
    fun showMessage(tips: String)
}
