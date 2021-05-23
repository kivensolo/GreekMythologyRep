package com.kingz.module.wanandroid.fragemnts

import android.app.Service
import android.os.VibrationEffect
import android.os.Vibrator
import com.alibaba.android.arouter.launcher.ARouter
import com.kingz.base.BaseVMFragment
import com.kingz.module.common.LoadStatusView
import com.kingz.module.common.R
import com.kingz.module.common.router.RPath
import com.kingz.module.common.utils.ktx.SDKVersion
import com.kingz.module.wanandroid.WADConstants
import com.kingz.module.wanandroid.bean.Article
import com.zeke.kangaroo.utils.ZLog
import com.zeke.reactivehttp.base.BaseReactiveViewModel

/**
 * author：ZekeWang
 * date：2021/3/29
 * description： 业务Common层的Fragment
 *
 * -> initViewModel()
 * --> initView()
 *
 */
abstract class CommonFragment<T : BaseReactiveViewModel> : BaseVMFragment<T>() {

    //公共Loading控件
    protected var loadStatusView: LoadStatusView? = null

    override fun lazyInit() {
        initViewModel()
        initView()
    }

    override fun initView() {
        ZLog.d("initView()")
        loadStatusView = rootView?.findViewById(R.id.load_status)
        loadStatusView?.showProgress()
    }

    override fun onViewDestory() {
        ZLog.d("onViewDestory.")
        super.onViewDestory()
        loadStatusView = null
    }

    override fun onDestroy() {
        ZLog.d("release.")
        super.onDestroy()
    }

    override fun onDetach() {
        ZLog.d("Detach fragment.")
        super.onDetach()
    }

    protected open fun dismissLoading(){
        ZLog.d("dismiss Loading View.")
        loadStatusView?.dismiss()
    }

    protected open fun showErrorStatus(){
        loadStatusView?.showError()
    }

    protected open fun showEmptyStatus(){
        loadStatusView?.showEmpty()
    }

    /**
     * 打开文章web页面
    */
    protected fun openWeb(data: Article?) {
        ARouter.getInstance()
            .build(RPath.PAGE_WEB)
            .withString(WADConstants.KEY_URL, data?.link)
            .withString(WADConstants.KEY_TITLE, data?.title)
            .withString(WADConstants.KEY_AUTHOR, data?.author)
            .withBoolean(WADConstants.KEY_IS_COLLECT, data?.collect ?: false)
            .withInt(WADConstants.KEY_ID, data?.id ?: -1)
            .navigation(activity, 0x01)
    }

    protected fun openWeb(url: String?) {
        ARouter.getInstance()
            .build(RPath.PAGE_WEB)
            .withString(WADConstants.KEY_URL, url)
            .navigation(activity, 0x01)
    }

      /**
     * 触发震动效果
     */
    protected fun fireVibrate() {
        val vibrator = context?.getSystemService(Service.VIBRATOR_SERVICE) as Vibrator
        if (SDKVersion.afterOreo()) {
            vibrator.vibrate(
                VibrationEffect.createOneShot(40, VibrationEffect.DEFAULT_AMPLITUDE)
            )
        } else {
            @Suppress("DEPRECATION")
            vibrator.vibrate(40)
        }
    }
}