package com.zeke.demo.meterial

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kingz.module.common.adapter.SimpleLabelAdapter
import com.zeke.demo.R
import java.util.*

/**
 * author：ZekeWang
 * date：2021/2/28
 * description：常规的上下隐藏效果的Demo
 */
class AppBarNormalActivity: AppCompatActivity() {
    private var mAdapter: SimpleLabelAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_appbarlayout_normal)
        val datas = ArrayList<String>()
        for (i in 0..29) {
            datas.add(String.format("我是第%s个Item", i + 1))
        }
        mAdapter = SimpleLabelAdapter(datas)
        val recyclerView = findViewById<RecyclerView>(R.id.content_recycler)
        recyclerView?.apply {
            addItemDecoration(
                DividerItemDecoration(
                    this@AppBarNormalActivity,
                    DividerItemDecoration.VERTICAL
                )
            )
            layoutManager = LinearLayoutManager(this@AppBarNormalActivity)
            adapter = mAdapter
        }
    }
}