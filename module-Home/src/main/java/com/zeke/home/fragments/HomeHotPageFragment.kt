package com.zeke.home.fragments

import androidx.recyclerview.widget.LinearLayoutManager
import com.kingz.module.common.fragments.CommonPageFragment
import com.zeke.home.adapter.HomeRecomAdapter
import com.zeke.home.adapter.OnePicDelegateAdapter
import com.zeke.home.adapter.ThreePicDelegateAdapter
import com.zeke.home.entity.HomeRecomData
import com.zeke.kangaroo.utils.ZLog

/**
 * author: King.Z <br>
 * date:  2020/5/24 13:49 <br>
 * description:  <br>
 */
class HomeHotPageFragment: CommonPageFragment() {

    override fun onViewCreated() {
        //doNothing
        ZLog.d("HomeHotPageFragment onViewCreated")
    }

    override fun initRecyclerView() {
        ZLog.d("HomeHotPageFragment initRecyclerView")
        super.initRecyclerView()
        val layoutManager = LinearLayoutManager(context)
        mRecyclerView.layoutManager = layoutManager
        val mAdapter = HomeRecomAdapter()
        // 添加委托Adapter
        mAdapter.addDelegate(OnePicDelegateAdapter())
        mAdapter.addDelegate(ThreePicDelegateAdapter())

        // 模拟假数据
        mAdapter.addItem(HomeRecomData("123","新闻视频","one_pic",null))
        mAdapter.addItem(HomeRecomData("123","这是一条新闻，这是一条新闻，这是一条新闻","three_pic",null))
        mRecyclerView.adapter = mAdapter

    }

}