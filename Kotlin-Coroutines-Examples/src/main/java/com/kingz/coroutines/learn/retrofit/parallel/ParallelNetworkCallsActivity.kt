package com.kingz.coroutines.learn.retrofit.parallel

import androidx.lifecycle.ViewModelProvider
import com.kingz.coroutines.data.api.ApiHelperImpl
import com.kingz.coroutines.data.api.RetrofitBuilder
import com.kingz.coroutines.data.local.DatabaseBuilder
import com.kingz.coroutines.data.local.DatabaseHelperImpl
import com.kingz.coroutines.learn.base.ApiUserAdapter
import com.kingz.coroutines.learn.retrofit.BaseNetworkCallsActivity
import com.kingz.coroutines.utils.ViewModelFactory

class ParallelNetworkCallsActivity : BaseNetworkCallsActivity<ParallelNetworkCallsViewModel>() {

    override lateinit var viewModel: ParallelNetworkCallsViewModel
    override lateinit var adapter: ApiUserAdapter
    override fun getUser() = viewModel.getUsers()

    override fun setupViewModel() {
           viewModel = ViewModelProvider(
            this,
            ViewModelFactory.build {
                ParallelNetworkCallsViewModel(
                        ApiHelperImpl(RetrofitBuilder.apiService),
                        DatabaseHelperImpl(DatabaseBuilder.getInstance(applicationContext))
                        )
            }
        ).get(ParallelNetworkCallsViewModel::class.java)
    }
}
