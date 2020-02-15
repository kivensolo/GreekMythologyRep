package com.zeke.ktx.base

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.zeke.ktx.App
import com.zeke.ktx.AppLifeCycle


/**
 * description: 基类的Fragment
 */
abstract class BaseFragment : Fragment() {
    companion object {
        val TAG = BaseFragment::class.java.simpleName
    }
    private var mActivity: Activity? = null
    protected lateinit var rootView: View

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        mActivity = activity
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycle.addObserver(AppLifeCycle(TAG))
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        rootView = inflater.inflate(getLayoutId(), null)
        initOperate()
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initData()
        setListener()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }

    override fun onDetach() {
        super.onDetach()
        mActivity = null
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    override fun getContext(): Context? {
        return if (mActivity == null) App.instance else mActivity
    }

    fun onBackPressed(): Boolean {
        return true
    }


    /** 设置布局id*/
    abstract fun getLayoutId(): Int

    /** 进行初始化操作，在onCreateView中调用*/
    open fun initOperate() {}

    /** 初始化视图*/
    abstract fun initView()

    /** 初始化数据*/
    abstract fun initData()

    /** 设置监听*/
    open fun setListener() {}
}
