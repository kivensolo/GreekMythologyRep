package com.kingz.mvvm.demo

import android.os.Bundle
import android.util.Log
import androidx.lifecycle.Observer
import androidx.lifecycle.viewModelScope
import androidx.viewpager2.widget.ViewPager2
import com.kingz.mvvm.R
import com.kingz.mvvm.base.BaseVMActivity
import com.kingz.mvvm.demo.vm.ChaptersViewModel
import kotlinx.android.synthetic.main.activity_ui_frame.*

/**
 * 文章列表页
 * 使用了Paging和ViewPager2
 */
class ChaptersActivity : BaseVMActivity<ChaptersViewModel>() {

    companion object {
        private const val TAG = "SampleActivity"
    }

    override val layoutRes: Int = R.layout.activity_ui_frame

    override lateinit var viewModel: ChaptersViewModel //TODo 初始化ViewModel

    protected lateinit var adapter: ViewPagerAdapter<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewPager.orientation = ViewPager2.ORIENTATION_HORIZONTAL
        // viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
        //
        //     override fun onPageSelected(position: Int) {
        //         super.onPageSelected(position)
        //     }
        // })
        initViewModel()
    }

    private fun initViewModel() {
        viewModel.viewModelScope.apply {

        }
        // 设置viewModel的监听
        viewModel.apply {
            adapter = ViewPagerAdapter(viewModelScope, this@ChaptersActivity)
            viewPager.adapter = adapter

            // sampleData.observe(this@ChaptersActivity, Observer {
            //     it?.also {
            //         adapter.data = it
            //         adapter.notifyDataSetChanged()
            //     }
            // })

            // 观察mock数据
            mockData.observe(this@ChaptersActivity, Observer {
                it?.also {
                    adapter.data = it
                    adapter.notifyDataSetChanged()
                }
            })
        }

        btFetchData.setOnClickListener { viewModel.fetchMockData() }
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("内存泄漏", "onDestroy")
    }

    override fun onBackPressed() {
        super.onBackPressed()
        viewPager.removeAllViews()
        viewPager.adapter = null
        finish()
    }


}