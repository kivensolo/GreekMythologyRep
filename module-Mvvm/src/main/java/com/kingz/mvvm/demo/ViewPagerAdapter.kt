package com.kingz.mvvm.demo

import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import com.kingz.mvvm.R
import com.kingz.mvvm.adapter.BaseRecyclerAdapter
import com.xgimi.sample.mvvm.mvvm.ViewPagerNormalHolder
import kotlinx.coroutines.CoroutineScope

class ViewPagerAdapter<T>(private val scope: CoroutineScope, private val owner: LifecycleOwner) :
    BaseRecyclerAdapter<T, ViewPagerNormalHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewPagerNormalHolder {
        return ViewPagerNormalHolder(scope, owner, initView(R.layout.item_ui_frame, parent))
    }

    override fun onBindViewHolder(holder: ViewPagerNormalHolder, position: Int) {

    }

}