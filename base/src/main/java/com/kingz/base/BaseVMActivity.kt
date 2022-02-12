package com.kingz.base

import android.content.Context
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import com.zeke.reactivehttp.base.BaseReactiveViewModel
import com.zeke.reactivehttp.viewmodel.IUIActionEventObserver
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job

/**
 * MVVM模式的Activity Base层，
 * 通过IUIActionEventObserver接口与ViewModel层通信
 */
abstract class BaseVMActivity : BaseSimpleActivity(), IUIActionEventObserver {

    val TAG: String = javaClass.simpleName

    protected abstract val viewModel: BaseReactiveViewModel

    override val lifecycleSupportedScope: CoroutineScope
        get() = lifecycleScope

    override val lContext: Context?
        get() = this

    override val lLifecycleOwner: LifecycleOwner
        get() = this

    override fun initViewModel() {
//        initViewModelStatusActions()
    }

    //    private fun initViewModelStatusActions() {
//        viewModel.statusLiveData.observe(this, Observer { status ->
//            status?.run {
//                when (this) {
//                    CoroutineState.START -> {//协程开始
//                        Log.d("Coroutine-status:", "START")
//                    }
//                    CoroutineState.REFRESH -> {//协程开始&&进度菊花圈
//                        Log.d("Coroutine-status:", "REFRESH")
//                        showLoading()
//                    }
//                    CoroutineState.FINISH -> {//协程结束
//                        Log.d("Coroutine-status:", "FINISHED")
//                        dismissLoading()
//                    }
//                    CoroutineState.ERROR -> {//协程异常
//                        Log.e("Coroutine-status:", "ERROR")
//                        dismissLoading()
//                    }
//                }
//            }
//        })
//    }
// <editor-fold defaultstate="collapsed" desc="UIAction From IUIActionEvent">

    override fun showLoading(job: Job?) {
        progress?.let {
            if (!it.isShown) {
                it.visibility = View.VISIBLE
            }
        }
    }

    override fun  dismissLoading() {
        progress?.let {
            if (it.isShown) {
                it.visibility = View.GONE
            }
        }
    }

    override fun showToast(msg: String) {
        Log.d(TAG,"showToast: $msg")
        if (msg.isNotBlank()) {
            Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
        }
    }

    override fun finishView() {
        finish()
    }
// </editor-fold>

    override fun onDestroy() {
        dismissLoading()
        super.onDestroy()
    }
}