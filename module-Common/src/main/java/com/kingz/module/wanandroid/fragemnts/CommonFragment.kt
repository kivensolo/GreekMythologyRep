package com.kingz.module.wanandroid.fragemnts

import android.app.Service
import android.os.VibrationEffect
import android.os.Vibrator
import android.widget.Toast
import androidx.annotation.CallSuper
import androidx.annotation.StringRes
import androidx.recyclerview.widget.LinearLayoutManager
import com.alibaba.android.arouter.launcher.ARouter
import com.kingz.base.BaseVMFragment
import com.kingz.module.common.LoadStatusView
import com.kingz.module.common.router.RouterConfig
import com.kingz.module.common.utils.ktx.SDKVersion
import com.kingz.module.wanandroid.bean.Article
import com.zeke.kangaroo.zlog.ZLog
import com.zeke.reactivehttp.base.BaseReactiveViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * author：ZekeWang
 * date：2021/3/29
 * description： 业务Common层的Fragment
 *
 * -> initViewModel()
 * --> initView()
 * ---> initData()
 */
abstract class CommonFragment<T : BaseReactiveViewModel> : BaseVMFragment<T>() {

    /**
     * 多状态视图View 由子类具体实现
     */
    protected var loadStatusView: LoadStatusView? = null

    /**
     * LinearLayoutManager for RecyclerView
     */
    protected val linearLayoutManager: LinearLayoutManager by lazy {
        LinearLayoutManager(activity)
    }

    @CallSuper
    override fun lazyInit() {
        initViewModel()
        initView()
        initData()
    }

    /**
     * 初始化UI
     * 若子类使用到全局loading状态UI, 重写时，则需调用super
     */
    @CallSuper
    override fun initView() {
        ZLog.d("initView()")
        loadStatusView?.showLoading()
    }

    open fun initData() {}

    override fun onDestroyView() {
        super.onDestroyView()
        ZLog.d("onDestroyView.")
        loadStatusView = null
    }

    override fun onDestroy() {
        ZLog.d("onDestroy.")
        super.onDestroy()
    }

    override fun onDetach() {
        ZLog.d("Detach fragment.")
        super.onDetach()
    }

    @Deprecated("使用showXXXX")
    protected open fun dismissLoading(){
        ZLog.d("dismiss Loading View.")
        loadStatusView?.dismiss()
    }

    protected open fun showLoading(){
        loadStatusView?.showLoading()
    }

    protected open fun showErrorStatus(){
        loadStatusView?.showError()
    }

    protected open fun showContent(){
        loadStatusView?.showContent()
    }

    protected open fun showEmptyStatus(){
        loadStatusView?.showEmpty()
    }

    protected suspend fun showToast(@StringRes id:Int) {
        withContext(Dispatchers.Main) {
            Toast.makeText(
                context,
                resources.getString(id),
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    /**
     * 打开文章web页面
    */
    protected fun openWeb(data: Article?) {
        ARouter.getInstance()
            .build(RouterConfig.PAGE_WEB)
            .withParcelable(RouterConfig.PARAM_WEB_ARTICAL_INFO, data)
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