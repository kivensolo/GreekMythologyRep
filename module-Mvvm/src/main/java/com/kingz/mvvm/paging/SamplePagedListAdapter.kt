package com.kingz.mvvm.paging

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncDifferConfig
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.kingz.mvvm.R
import com.kingz.mvvm.adapter.BasePagedListAdapter
import com.kingz.mvvm.demo.Data
import kotlinx.android.synthetic.main.item_page_mock.view.*

/**
 * @author chongyang.zhang
 * @date 2019/11/20
 * @maintainer chongyang.zhang
 * @copyright 2019 www.xgimi.com Inc. All rights reserved.
 * @desc:
 */
class SamplePagedListAdapter :
        BasePagedListAdapter<Data, SamplePagedListAdapter.ViewHolder> {

    constructor(diffCallback: DiffUtil.ItemCallback<Data>) : super(diffCallback)

    constructor (config: AsyncDifferConfig<Data>) : super(config)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(initView(R.layout.item_page_mock, parent))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        getItem(position)?.also { holder.bindTo(it) }
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bindTo(data: Data) {
            itemView.tvTitle.text = data.name
        }
    }
}