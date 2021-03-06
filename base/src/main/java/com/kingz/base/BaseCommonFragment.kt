package com.kingz.base

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment

/**
 * author：ZekeWang
 * date：2021/2/24
 * description：公共层的Fragment
 */
abstract class BaseCommonFragment : Fragment() {

    var rootView: View?= null
    var mActivity: Activity? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mActivity = context as Activity
    }

    override fun onDetach() {
        super.onDetach()
        mActivity = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onViewCreated()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        rootView = inflater.inflate(getLayoutResID(), container, false)
        onCreateViewReady()
        return rootView
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initViewModel()
        initView()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        rootView = null
        onViewDestory()
    }

    open fun onCreateViewReady() {}

    open fun onViewCreated(){}

    open fun onViewDestory(){}

    /** VIewModel中持有的UI数据监听 */
    open fun initViewModel() {}

    /** 子类重写 获取layoutId **/
    abstract fun getLayoutResID(): Int

    abstract fun initView()
}