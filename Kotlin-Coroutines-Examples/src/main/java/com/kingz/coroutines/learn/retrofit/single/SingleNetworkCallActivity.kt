package com.kingz.coroutines.learn.retrofit.single

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.kingz.coroutines.data.api.ApiHelperImpl
import com.kingz.coroutines.data.api.RetrofitBuilder
import com.kingz.coroutines.data.local.DatabaseHelperImpl
import com.kingz.coroutines.data.model.ApiUser
import com.kingz.coroutines.learn.base.ApiUserAdapter
import com.kingz.coroutines.utils.Status
import com.kingz.coroutines.utils.ViewModelFactory
import com.kingz.database.DatabaseBuilder
import com.zeke.example.coroutines.R
import kotlinx.android.synthetic.main.activity_recycler_view.*

class SingleNetworkCallActivity : AppCompatActivity() {

    // Ktx的写法 使用了代理和懒加载模式
//    private val viewModel: SingleNetworkCallViewModel by viewModels {
//        ViewModelFactory.build {
//            SingleNetworkCallViewModel(
//                    ApiHelperImpl(RetrofitBuilder.apiService),
//                    DatabaseHelperImpl(DatabaseBuilder.getInstance(applicationContext)))
//        }
//    }

    private lateinit var viewModel: SingleNetworkCallViewModel
    private lateinit var adapter: ApiUserAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recycler_view)
        setupUI()
        setupViewModel()
        setupObserver()
    }

    private fun setupUI() {
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = ApiUserAdapter(
                arrayListOf()
            )
        recyclerView.addItemDecoration(
            DividerItemDecoration(
                recyclerView.context,
                (recyclerView.layoutManager as LinearLayoutManager).orientation
            )
        )
        recyclerView.adapter = adapter
    }

    private fun setupObserver() {
        viewModel.getUsers().observe(this, Observer {
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
                    Toast.makeText(this, it.message, Toast.LENGTH_LONG).show()
                }
            }
        })
    }

    private fun renderList(users: List<ApiUser>) {
        adapter.addData(users)
        adapter.notifyDataSetChanged()
    }

    private fun setupViewModel() {
        /**
         * 源码实现的最开始最基础的api为 ViewModelProviders.of(ViewModelStoreOwner, Factory)，
         * 后续被标记废弃，推荐使用 'by viewModels()' 的Kotlin属性代理的方式或
         * ViewModelProvider(ViewModelStoreOwner, Factory) 传递activity和factory的方法
         */
        // @Deprecated
        // viewModel = ViewModelProviders.of(
        viewModel = ViewModelProvider(
            this,
            ViewModelFactory.build {
                SingleNetworkCallViewModel(
                        ApiHelperImpl(RetrofitBuilder.apiService),
                        DatabaseHelperImpl(DatabaseBuilder.getInstance(applicationContext))
                        )
            }
        ).get(SingleNetworkCallViewModel::class.java)
    }
}
