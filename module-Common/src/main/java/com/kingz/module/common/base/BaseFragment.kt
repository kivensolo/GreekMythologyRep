package com.kingz.module.common.base

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.kingz.module.common.AppLifeCycle
import com.kingz.module.common.CommonApp
import com.kingz.module.common.LoadStatusView


/**
 * description: 基类的Fragment
 */
abstract class BaseFragment : Fragment() {
    companion object {
        val TAG: String = BaseFragment::class.java.simpleName
    }
    protected var mActivity: Activity? = null
    protected lateinit var rootView: View
    // 加载状态View
    protected var loadStatusView: LoadStatusView? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mActivity = activity
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycle.addObserver(AppLifeCycle(TAG))
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        rootView = inflater.inflate(getLayoutId(), null)
        onCreateViewReady()
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onViewCreated()
        setListener()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }

    override fun onDetach() {
        super.onDetach()
        mActivity = null
    }

    override fun getContext(): Context? {
        return if (mActivity == null) CommonApp.getInstance() else mActivity
    }

    open fun onBackPressed(): Boolean {
        return false
    }

    /** 设置布局id*/
    abstract fun getLayoutId(): Int

    /** 进行初始化操作，在onCreateView中调用*/
    open fun onCreateViewReady() {}

    abstract fun onViewCreated()

    /** 设置监听*/
    open fun setListener() {}

}
