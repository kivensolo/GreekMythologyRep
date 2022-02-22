package com.kingz.base

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.CallSuper
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import com.zeke.reactivehttp.base.BaseReactiveViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job

/**
 * 与业务无关的Base层Fragment
 */
abstract class BaseVMFragment< T : BaseReactiveViewModel>
    : BaseLazyFragment() {

    override val lContext: Context?
        get() = activity

    override val lifecycleSupportedScope: CoroutineScope
        get() = activity!!.lifecycleScope

    override val lLifecycleOwner: LifecycleOwner
        get() = this

    protected abstract val viewModel: T

    var rootView: View?= null
    var mActivity: Activity? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mActivity = context as Activity
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        rootView = inflater.inflate(getLayoutResID(), container, false)
        return rootView
    }

    /**
     *  fragement 的initView逻辑建议在onViewCreated中处理.
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onViewCreated()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        rootView = null
    }

    override fun onDetach() {
        super.onDetach()
        mActivity = null
    }

    /**
     * 当View已经加载的回调
     */
    open fun onViewCreated(){}

    /** VIewModel中持有的UI数据监听 */
    @CallSuper
    open fun initViewModel() {}

    /** 子类重写 获取layoutId **/
    abstract fun getLayoutResID(): Int

    abstract fun initView()

//    private fun initViewModelActions() {
//        viewModel.statusLiveData.observe(this, Observer { status ->
//            status?.run {
//                when (this) {
//                    CoroutineState.START -> {//协程开始
//                        Log.d("coroutine-status:", "---> START")
//                    }
//                    CoroutineState.REFRESH -> {//协程开始&&进度菊花圈
//                        Log.d("coroutine-status:", "---> REFRESH")
//                        activity?.run {}
//                    }
//                    CoroutineState.FINISH -> {//协程结束
//                        Log.d("coroutine-status:", "<--- FINISH")
//                    }
//                    CoroutineState.ERROR -> {//协程异常
//                        Log.d("coroutine-status:", "<--- ERROR")
//                    }
//                }
//            }
//        })
//    }

    override fun showLoading(job: Job?) {}
    override fun showToast(msg: String) {
        if (msg.isNotBlank()) {
            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
        }
    }
    override fun dismissLoading() {}
    override fun showNoNetworkView() {}
    override fun finishView() {}
}