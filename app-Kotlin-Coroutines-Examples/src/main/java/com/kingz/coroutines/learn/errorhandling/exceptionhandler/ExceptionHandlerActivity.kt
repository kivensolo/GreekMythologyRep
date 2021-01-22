package com.kingz.coroutines.learn.errorhandling.exceptionhandler

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.kingz.base.response.Status
import com.kingz.coroutines.data.api.ApiHelperImpl
import com.kingz.coroutines.data.api.RetrofitBuilder
import com.kingz.coroutines.data.local.DatabaseBuilder
import com.kingz.coroutines.data.local.DatabaseHelperImpl
import com.kingz.coroutines.data.model.ApiUser
import com.kingz.coroutines.learn.base.ApiUserAdapter
import com.kingz.coroutines.utils.ViewModelFactory
import com.zeke.example.coroutines.R
import kotlinx.android.synthetic.main.activity_recycler_view.*

/**
 * Learn how to handle error in Kotlin Coroutines using CoroutineExceptionHandler.
 */
class ExceptionHandlerActivity : AppCompatActivity() {

    private lateinit var viewModel: ExceptionHandlerViewModel
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
        viewModel = ViewModelProvider(
            this,
            ViewModelFactory.build {
                ExceptionHandlerViewModel(
                    ApiHelperImpl(RetrofitBuilder.USER_SERVICE_API),
                    DatabaseHelperImpl(DatabaseBuilder.getInstance(applicationContext))
                )
            }
        ).get(ExceptionHandlerViewModel::class.java)
    }
}
