package com.kingz.coroutines.demo

import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import com.kingz.coroutines.adapter.BaseRecyclerAdapter
import com.zeke.example.coroutines.R
import kotlinx.coroutines.CoroutineScope

class ViewPagerAdapter<T>(private val scope: CoroutineScope, private val owner: LifecycleOwner) :
    BaseRecyclerAdapter<T, ViewPagerNormalHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewPagerNormalHolder {
        return ViewPagerNormalHolder(scope, owner, initView(R.layout.item_ui_frame, parent))
    }

    override fun onBindViewHolder(holder: ViewPagerNormalHolder, position: Int) {

    }

}