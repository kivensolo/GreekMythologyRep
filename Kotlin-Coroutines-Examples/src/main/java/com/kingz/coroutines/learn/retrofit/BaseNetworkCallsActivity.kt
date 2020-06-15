package com.kingz.coroutines.learn.retrofit

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.kingz.coroutines.data.model.ApiUser
import com.kingz.coroutines.learn.base.ApiUserAdapter
import com.kingz.coroutines.utils.Resource
import com.kingz.coroutines.utils.Status
import com.zeke.example.coroutines.R
import kotlinx.android.synthetic.main.activity_recycler_view.*

abstract class BaseNetworkCallsActivity<T:ViewModel> : AppCompatActivity() {
    protected abstract var viewModel: T
    protected abstract var adapter: ApiUserAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recycler_view)
        setupUI()
        setupViewModel()
        setupObserver()
    }

    open fun setupUI() {
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter =
            ApiUserAdapter(
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

    open fun setupObserver(){
        getUser().observe(this, Observer {
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

    open fun renderList(users: List<ApiUser>) {
        adapter.addData(users)
        adapter.notifyDataSetChanged()
    }

    abstract fun setupViewModel()

    abstract fun getUser(): LiveData<Resource<List<ApiUser>>>
}