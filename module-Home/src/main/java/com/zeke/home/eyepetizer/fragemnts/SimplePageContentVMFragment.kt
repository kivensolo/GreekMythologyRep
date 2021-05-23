package com.zeke.home.eyepetizer.fragemnts

import android.os.Bundle
import android.widget.TextView
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kingz.base.BaseVMFragment
import com.kingz.base.factory.ViewModelFactory
import com.kingz.module.common.bean.MediaParams
import com.kingz.module.common.router.RPath
import com.kingz.module.common.router.Router
import com.kingz.module.home.R
import com.kingz.module.wanandroid.viewmodel.EyepetizerViewModel
import com.zeke.home.entity.Live
import com.zeke.kangaroo.adapter.CommonRecyclerAdapter

/**
 * author：KingZ
 * date：2019/12/29
 * description：开眼视频的简单内容Fragment
 */
class SimplePageContentVMFragment : BaseVMFragment<EyepetizerViewModel>() {

    private var mRecycleView: RecyclerView? = null
    var mRV: RVAdapter = RVAdapter()

    override val viewModel: EyepetizerViewModel by viewModels {
        ViewModelFactory.build { EyepetizerViewModel() }
    }
    override fun getLayoutResID(): Int = R.layout.single_recyclerview

    override fun initView() {
        initRecyclerView()
    }

    override fun lazyInit() {
        initView()
        initViewModel()
    }

    override fun initViewModel() {
        super.initViewModel()
            //TODO 初始化VIewmodel
//        viewModel.xxxx
    }

    private fun initRecyclerView() {
        mRecycleView = rootView?.findViewById(R.id.content_recycler) as RecyclerView
        mRecycleView?.apply {
            isVerticalScrollBarEnabled = true
            layoutManager = LinearLayoutManager(context)
        }
    }

    override fun onDestroyView() {
        mRecycleView = null
        super.onDestroyView()
    }

    @Deprecated("被BaseQuickAdapter代替")
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
