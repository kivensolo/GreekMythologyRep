package com.zeke.home.fragments

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kingz.module.common.IView
import com.kingz.module.common.base.BaseFragment
import com.kingz.module.common.bean.MediaParams
import com.kingz.module.common.router.RPath
import com.kingz.module.common.router.Router
import com.kingz.module.home.R
import com.zeke.home.entity.Live
import com.zeke.kangaroo.adapter.CommonRecyclerAdapter

/**
 * author：KingZ
 * date：2019/12/29
 * description：首页/点播/每个tab页
 */
class SimplePageContentFragment : BaseFragment(), IView, View.OnClickListener {

    private var mRecycleView: RecyclerView? =null
    var mRV: RVAdapter = RVAdapter()

    override val isShown: Boolean
        get() = false

    override fun getLayoutId(): Int {
        return R.layout.single_recyclerview
    }

    override fun onViewCreated() {
        //TODO 为啥这个recycleView有(174,48,60,99)的padding????
        mRecycleView = rootView?.findViewById(R.id.content_recycler)
        mRecycleView?.apply {
            setHasFixedSize(true)
            layoutManager = GridLayoutManager(activity!!, 2)
            adapter = mRV
        }
    }

    override fun onDestroyView() {
        mRecycleView = null
        super.onDestroyView()
    }

    override fun showLoading() {
    }

    override fun hideLoading() {
    }

    override fun showError() {
    }

    override fun showEmpty() {

    }

    override fun showMessage(tips: String) {

    }

    override fun onClick(v: View) {

    }

    inner class RVAdapter : CommonRecyclerAdapter<Live>(){
        init {
            // Initializer Block
            //为Primary Constructor服务
        }

        override fun getItemLayout(type: Int): Int {
            return R.layout.live_channel_item
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            super.onBindViewHolder(holder, position)
            val data = getItem(position)
            val textView = holder.getView<TextView>(R.id.channel_id)
            textView.text = (data as Live).name

            if((position / 2) % 2 != 0){ // 偶数行
                textView.setBackgroundResource(R.color.text_gary_bkg)
            }

            holder.setOnClickListener {
                Router.startActivity(RPath.PAGE_PLAYER, Bundle().apply {
                    putParcelable(MediaParams.PARAMS_KEY ,MediaParams().apply{
                        videoName = data.name
                        videoUrl = data.live
                        videoType = "live"
                    })
                })
            }
        }
    }
}
