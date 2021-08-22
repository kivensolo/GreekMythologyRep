package com.zeke.demo.jetpack.paging

import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kingz.base.BaseVMActivity
import com.zeke.demo.R
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

/**
 * author: King.Z <br>
 * date:  2021/8/22 21:13 <br>
 * description:  <br>
 */
class PagingDemoActivity : BaseVMActivity() {

    lateinit var pagingAdapter: PagingDemoAdapter

    override val viewModel: UserInfoViewModel by lazy {
        UserInfoViewModel(ReqresApiService.getApiService())
    }

    override fun getContentLayout(): Int = R.layout.activity_paging_demo

    override fun initView(savedInstanceState: Bundle?) {
        super.initView(savedInstanceState)
        pagingAdapter = PagingDemoAdapter().apply {
            // 列表加载时回调
            addLoadStateListener {
                if (it.refresh == LoadState.Loading) {
                    // show progress view
                } else {
                    //hide progress view
                }
            }
            //withLoadStateHeaderAndFooter{ }
//            .withLoadStateFooter
        }
        findViewById<RecyclerView>(R.id.content_recycler).apply {
            layoutManager = LinearLayoutManager(this@PagingDemoActivity)
            adapter = pagingAdapter
        }
    }

    override fun initData(savedInstanceState: Bundle?) {
        lifecycleScope.launch {
            try {
                viewModel.listData.collect {
                    pagingAdapter.submitData(it)
                }
            } catch (e: Throwable) {
                println("Exception from the flow: $e")
            }
        }
    }

}