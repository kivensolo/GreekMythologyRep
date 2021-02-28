package com.zeke.demo.meterial

import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.kingz.module.common.adapter.SimpleLabelAdapter
import com.kingz.module.common.base.BaseActivity
import com.zeke.demo.R
import kotlinx.android.synthetic.main.activity_collapse_toolbar.*
import java.util.*

/**
 * 折叠式AppbarLayout
 */
class CollapseToolbarNoramalActivity : BaseActivity() {
    private var mAdapter: SimpleLabelAdapter? = null
    private var fab: FloatingActionButton? = null
    private var toolbar: Toolbar? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_collapse_toolbar)
        val mainContent = findViewById<View>(R.id.main_content)
        toolbar = findViewById(R.id.toolbar)
        toolbar?.title = "CollapseToolbar"
        setSupportActionBar(toolbar)

        mAdapter = SimpleLabelAdapter(null)
        initData()
        val recyclerView = findViewById<RecyclerView>(R.id.content_recycler)
        recyclerView.addItemDecoration(
            DividerItemDecoration(
                this,
                DividerItemDecoration.VERTICAL
            )
        )
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = mAdapter
        fab = findViewById(R.id.src_layout_fab)
        fab?.setOnClickListener {
            Snackbar.make(mainContent, "Show Snackbar", Snackbar.LENGTH_SHORT)
                .setAction("action", null)
                .setActionTextColor(resources.getColor(R.color.colorAccent))
                .show()
        }

        backdrop?.setOnClickListener {
            finish()
        }
    }

    private fun initData() {
        val datas = ArrayList<String>()
        for (i in 0..29) {
            datas.add(String.format("我是第%s个Item", i + 1))
        }
        mAdapter?.addData(datas)
    }
}