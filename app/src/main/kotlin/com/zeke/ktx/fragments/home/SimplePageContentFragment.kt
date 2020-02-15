package com.zeke.ktx.fragments.home

import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.TextView
import com.kingz.customdemo.R
import com.zeke.kangaroo.adapter.CommonRecyclerAdapter
import com.zeke.ktx.base.BaseFragment
import com.zeke.ktx.base.view.IView
import com.zeke.ktx.player.entity.Live

/**
 * author：KingZ
 * date：2019/12/29
 * description：首页/点播/每个tab页
 * //可以 将Recycle替换为DragRecyclerView
 */
class SimplePageContentFragment : BaseFragment(), IView, View.OnClickListener {

    private lateinit var mRecycleView: RecyclerView
    var mRV: RVAdapter = RVAdapter()

    override val isShown: Boolean
        get() = false

    override fun getLayoutId(): Int {
        return R.layout.fragment_vod_item
    }

    override fun initView() {
        mRecycleView = rootView.findViewById<RecyclerView>(R.id.vod_content_recycler)
        mRecycleView.setHasFixedSize(true)
        mRecycleView.layoutManager = GridLayoutManager(activity!!,2)
        mRecycleView.adapter = mRV
    }

    override fun initData() {

    }

    override fun showLoading() {
    }

    override fun hideLoading() {
    }

    override fun showError(listener: View.OnClickListener) {
    }

    override fun showEmpty(listener: View.OnClickListener) {

    }

    override fun showMessage(tips: String) {

    }

    override fun onClick(v: View) {
    }

    inner class RVAdapter : CommonRecyclerAdapter<Live>(){
        init { // Initializer Block
            //为Primary Constructor服务
        }

        override fun getItemLayout(type: Int): Int {
            return R.layout.live_channel_item
        }

        override fun onBindViewHolder(holder: CommonRecyclerAdapter.ViewHolder, position: Int) {
            super.onBindViewHolder(holder, position)
            val data = getItem(position)
            holder.getView<TextView>(R.id.channel_id).text = (data as Live).name
        }
    }
}
