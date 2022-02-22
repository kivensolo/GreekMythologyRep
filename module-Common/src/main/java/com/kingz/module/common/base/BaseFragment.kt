package com.kingz.module.common.base

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import com.kingz.module.common.AppLifeCycle
import com.kingz.module.common.CommonApp
import com.kingz.module.common.MultipleStatusView


/**
 * description: MVP模式基类的Fragment
 */
abstract class BaseFragment : Fragment() {
    protected val TAG: String = javaClass.simpleName
    protected var mActivity: Activity? = null
    var rootView: View? = null
    // 加载状态View
    protected var loadStatusView: MultipleStatusView? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mActivity = activity
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycle.addObserver(AppLifeCycle(TAG))
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        if(rootView == null){
            rootView = inflater.inflate(getLayoutId(), null)
        }
        initViews()
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }

    override fun onDetach() {
        super.onDetach()
        rootView = null
        mActivity = null
    }

    override fun getContext(): Context? {
        return if (mActivity == null) CommonApp.getInstance().baseContext else mActivity
    }

    open fun onBackPressed(): Boolean {
        return false
    }

    /** 设置布局id*/
    @LayoutRes
    abstract fun getLayoutId(): Int

    /** 进行UI初始化操作，在onCreateView中调用*/
    open fun initViews() {}
}
