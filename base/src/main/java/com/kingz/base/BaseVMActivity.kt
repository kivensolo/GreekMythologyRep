package com.kingz.base

import android.content.Context
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

    protected abstract val viewModel: BaseReactiveViewModel

    override val lifecycleSupportedScope: CoroutineScope
        get() = lifecycleScope

    override val lContext: Context?
        get() = this

    override val lLifecycleOwner: LifecycleOwner
        get() = this

    override fun initViewModel() {
        super.initViewModel()
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

    override fun showLoading(job: Job?) {
        showLoading()
    }

    override fun finishView() {
        finish()
    }
}