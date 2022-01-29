package com.kingz.base

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.CallSuper
import androidx.lifecycle.lifecycleScope
import com.zeke.reactivehttp.base.BaseReactiveViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

/**
 * 生命周期顺序为：
 *  onCreateView()
 *  |--> onCreateView (自定义: onCreateViewReady)
 *  \--->onViewCreated
 */
abstract class BaseVMFragment< T : BaseReactiveViewModel>
    : BaseLazyFragment() {

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // fragement 的initView逻辑实际应该在onViewCreated中处理.
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

    fun launch(blockCode: suspend CoroutineScope.() -> Unit) {
        lifecycleScope.launch {
            try {
                blockCode()
            } catch (e: Exception) {
                //异常处理
            }
        }
    }

    fun launchMain(block: suspend CoroutineScope.() -> Unit) {
        viewModel.launchMain { block() }
    }

    fun launchIO(block: suspend CoroutineScope.() -> Unit) {
        viewModel.launchIO { block() }
    }

    fun launchDefault(block: suspend CoroutineScope.() -> Unit) {
        viewModel.launchCPU { block() }
    }
}