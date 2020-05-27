package com.kingz.mvvm.demo

import android.view.View
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.kingz.mvvm.demo.entity.Data
import com.kingz.mvvm.paging.SamplePagedListAdapter
import kotlinx.android.synthetic.main.item_ui_frame.view.*
import kotlinx.coroutines.CoroutineScope

/**
 * @author chongyang.zhang
 * @date 2019/11/25
 * @maintainer chongyang.zhang
 * @copyright 2019 www.xgimi.com Inc. All rights reserved.
 * @desc:
 */
class ViewPagerNormalHolder(scope: CoroutineScope, owner: LifecycleOwner, itemView: View) :
    RecyclerView.ViewHolder(itemView) {

    private val adapter: SamplePagedListAdapter
    // private val factory: SamplePagedFactory

    private val diffCallback = object : DiffUtil.ItemCallback<Data>() {
        override fun areItemsTheSame(oldItem: Data, newItem: Data): Boolean =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Data, newItem: Data): Boolean =
            oldItem == newItem
    }

    init {
        adapter = SamplePagedListAdapter(diffCallback)
        itemView.rvViewPager.adapter = adapter

        // factory = SamplePagedFactory(scope)
        // //TODO toLiveData 啥意思？
        // val data = factory.toLiveData(10)
        // data.observe(owner, Observer { adapter.submitList(it) })
    }
}