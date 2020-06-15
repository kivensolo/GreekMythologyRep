package com.kingz.coroutines.learn.room

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
import com.kingz.coroutines.data.local.DatabaseBuilder
import com.kingz.coroutines.data.local.DatabaseHelperImpl
import com.kingz.coroutines.data.local.entity.User
import com.kingz.coroutines.learn.base.UserAdapter
import com.kingz.coroutines.utils.Status
import com.kingz.coroutines.utils.ViewModelFactory
import com.zeke.example.coroutines.R
import kotlinx.android.synthetic.main.activity_recycler_view.*

/**
 * Room Database Operation:
 * Learn how to fetch or insert entity in database using Kotlin Coroutines.
 * This is useful when you are using Room Database in your Android Application.
 */
class RoomDBActivity : AppCompatActivity() {

    private lateinit var viewModel: RoomDBViewModel
    private lateinit var adapter: UserAdapter

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
            UserAdapter(
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

    private fun renderList(users: List<User>) {
        adapter.addData(users)
        adapter.notifyDataSetChanged()
    }

    private fun setupViewModel() {
        viewModel = ViewModelProvider(
            this,
            ViewModelFactory.build {
                RoomDBViewModel(
                    ApiHelperImpl(RetrofitBuilder.apiService),
                    DatabaseHelperImpl(DatabaseBuilder.getInstance(applicationContext))
                )
            }
        ).get(RoomDBViewModel::class.java)
    }
}
