package com.kingz.coroutines.demo.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView

/**
 * @author zeke.wang
 * @date 2020/5/26
 * @maintainer zeke.wang
 * @copyright 2020 www.xgimi.com Inc. All rights reserved.
 * @desc:
 */
abstract class BaseRecyclerAdapter<DATA, Holder : RecyclerView.ViewHolder> :
        RecyclerView.Adapter<Holder>() {

    var data: List<DATA> = mutableListOf()

    protected fun initView(@LayoutRes layoutRes: Int, parent: ViewGroup): View {
        return LayoutInflater.from(parent.context).inflate(layoutRes, parent, false)
    }

    override fun getItemCount(): Int {
        return if (data.isEmpty()) 0 else data.size
    }

}