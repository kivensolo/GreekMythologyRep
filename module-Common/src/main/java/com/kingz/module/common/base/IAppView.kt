package com.kingz.module.common.base

import android.view.View

/**
 * author：KingZ
 * date：2019/7/30
 * description：app顶层view接口
 */
interface IAppView {
    fun setPresenter(presenter: IPresenter)
    /**
     * 显示加载
     */
    fun showLoading()

    /**
     * 隐藏加载
     */
    fun hideLoading()

    fun showError(listener: View.OnClickListener?)
    fun showEmpty(listener: View.OnClickListener?)
    /**
     * 是否可见
     */
    val isShown: Boolean

    /**
     * 弹出吐司
     */
    fun showMessage(tips: String?)
}