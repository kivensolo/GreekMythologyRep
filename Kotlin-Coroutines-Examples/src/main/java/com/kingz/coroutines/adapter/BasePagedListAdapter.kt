package com.kingz.coroutines.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.AsyncDifferConfig
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView

/**
 * @author zeke.wang
 * @date 2020/5/26
 * @maintainer zeke.wang
 * @copyright 2020 www.xgimi.com Inc. All rights reserved.
 * @desc:
 */
abstract class BasePagedListAdapter<DATA, Holder : RecyclerView.ViewHolder> :
        PagedListAdapter<DATA, Holder> {

    constructor(diffCallback: DiffUtil.ItemCallback<DATA>) : super(diffCallback)

    constructor (config: AsyncDifferConfig<DATA>) : super(config)

    protected fun initView(@LayoutRes layoutRes: Int, parent: ViewGroup): View {
        return LayoutInflater.from(parent.context).inflate(layoutRes, parent, false)
    }


}