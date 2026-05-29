package com.kingz.coroutines.learn.flows

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.kingz.coroutines.data.model.ApiUser
import com.zeke.example.coroutines.R
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

/**
 *  Flow的使用demo
 */
class FlowsActivity : AppCompatActivity() {

    fun foo(): Flow<Int> = flow {
        for (i in 1..3) {
            delay(100)
            println("Emitting $i")
            emit(i)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recycler_view)
        setupUI()
        setupViewModel()
        setupObserver()
    }

    private fun setupUI() {
    }

    private fun setupObserver() {
    }

    private fun renderList(users: List<ApiUser>) {
    }

    private fun setupViewModel() {
    }
}
