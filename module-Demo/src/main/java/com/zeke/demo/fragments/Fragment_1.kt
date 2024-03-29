package com.zeke.demo.fragments

import android.content.Context
import android.os.Bundle
import android.view.View
import com.kingz.module.common.base.BaseFragment
import com.zeke.demo.R
import com.zeke.kangaroo.zlog.ZLog

/**
 * author: King.Z <br>
 * date:  2021/2/24 19:32 <br>
 * description:  <br>
 */
class Fragment_1 : BaseFragment() {

    override fun onAttach(context: Context) {
        super.onAttach(context)
        ZLog.d("onAttach")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ZLog.d("onCreate")
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        ZLog.d("onViewCreated")
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        ZLog.d("onActivityCreated")
        super.onActivityCreated(savedInstanceState)
    }

    override fun onDetach() {
        super.onDetach()
        ZLog.d("onDetach")

    }

    override fun onDestroyView() {
        super.onDestroyView()
        ZLog.d("onDestroyView")

    }

    override fun onPause() {
        super.onPause()
        ZLog.d("onPause")
    }

    override fun onDestroy() {
        super.onDestroy()
        ZLog.d("onDestroy")
    }

    override fun onStart() {
        super.onStart()
        ZLog.d("onStart")
    }

    override fun onResume() {
        super.onResume()
        ZLog.d("onResume")
    }

    override fun getLayoutId() = R.layout.item_image
}