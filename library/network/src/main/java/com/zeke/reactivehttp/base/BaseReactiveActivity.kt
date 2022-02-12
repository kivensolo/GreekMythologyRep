package com.zeke.reactivehttp.base

import android.app.ProgressDialog
import android.content.Context
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import com.zeke.reactivehttp.viewmodel.IUIActionEventObserver
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job

/**
 * @Author: leavesC
 * @Date: 2020/10/22 10:27
 * @Desc:
 *     提供的一个默认 BaseActivity，其实现了 IUIActionEventObserver 接口，用于提供一些默认参数和默认行为，
 * 例如 CoroutineScope 和 showLoading。
 *
 *     当中包含与系统和用户交互的逻辑，其负责响应 BaseReactiveViewModel 中的数据变化，
 * 提供了和 BaseReactiveViewModel 进行绑定的方法。
 *
 * 外部也可以自己实现IUIActionEventObserver 接口
 */
open class BaseReactiveActivity : AppCompatActivity(), IUIActionEventObserver {

    override val lifecycleSupportedScope: CoroutineScope
        get() = lifecycleScope

    override val lContext: Context?
        get() = this

    override val lLifecycleOwner: LifecycleOwner
        get() = this

    private var loadDialog: ProgressDialog? = null

    override fun showLoading(job: Job?) {
        dismissLoading()
        loadDialog = ProgressDialog(lContext).apply {
            setCancelable(true)
            setCanceledOnTouchOutside(false)
            //用于实现当弹窗销毁的时候同时取消网络请求
//            setOnDismissListener {
//                job?.cancel()
//            }
            show()
        }
    }

    override fun dismissLoading() {
        loadDialog?.takeIf { it.isShowing }?.dismiss()
        loadDialog = null
    }

    override fun showToast(msg: String) {
        if (msg.isNotBlank()) {
            Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
        }
    }

    override fun showNoNetworkView() {
    }

    override fun finishView() {
        finish()
    }

    override fun onDestroy() {
        super.onDestroy()
        dismissLoading()
    }

}