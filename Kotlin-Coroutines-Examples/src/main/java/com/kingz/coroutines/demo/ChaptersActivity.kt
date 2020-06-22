package com.kingz.coroutines.demo

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Observer
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.kingz.coroutines.data.api.RetrofitBuilder
import com.kingz.coroutines.data.api.WAndroidApiImpl
import com.kingz.coroutines.demo.adapter.ChaptersAdapter
import com.kingz.coroutines.demo.base.BaseVMActivity
import com.kingz.coroutines.demo.entity.WAZChaptersEntity
import com.kingz.coroutines.demo.vm.ChaptersViewModel
import com.kingz.coroutines.utils.Status
import com.kingz.coroutines.utils.ViewModelFactory
import com.zeke.example.coroutines.R
import com.zeke.kangaroo.utils.ZLog
import kotlinx.android.synthetic.main.activity_recycler_view.*

class ChaptersActivity : BaseVMActivity<ChaptersViewModel>() {

    companion object {
        private const val TAG = "ChaptersActivity"
    }

    override val layoutRes: Int = R.layout.activity_recycler_view

    // 使用 'by viewModels()' 的Kotlin属性代理的方式
    override val viewModel: ChaptersViewModel by viewModels {
        ViewModelFactory.build {
            ChaptersViewModel(WAndroidApiImpl(RetrofitBuilder.wAndroidApi))
        }
    }

    private lateinit var adapter: ChaptersAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupUI()
        setupViewModel()
    }

    private fun setupUI() {
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = ChaptersAdapter(arrayListOf())
        recyclerView.addItemDecoration(
                DividerItemDecoration(
                        recyclerView.context,
                        (recyclerView.layoutManager as LinearLayoutManager).orientation
                )
        )
        recyclerView.adapter = adapter
    }

    private fun setupViewModel() {
        viewModel.viewModelScope.apply {

        }
        // 设置viewModel的监听
        viewModel.apply {
            getChapters().observe(this@ChaptersActivity, Observer {
                ZLog.d(TAG, "AdapterData onChanged, result=${it.status}")
                when (it.status) {
                    Status.SUCCESS -> {
                        progressBar.visibility = View.GONE
                        it.data?.let { users -> renderList(users) }
                        recyclerView.visibility = View.VISIBLE
                    }
                    Status.LOADING -> {
                        progressBar.visibility = View.VISIBLE
                        recyclerView.visibility = View.GONE
                    }
                    Status.ERROR -> {
                        //Handle Error
                        progressBar.visibility = View.GONE
                        Toast.makeText(this@ChaptersActivity,
                                it.message, Toast.LENGTH_LONG)
                                .show()
                    }
                }
            })
        }
    }

    private fun renderList(data: WAZChaptersEntity) {
        adapter.addData(data.data)
        adapter.notifyDataSetChanged()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

    override val binding: ViewDataBinding? = null


}