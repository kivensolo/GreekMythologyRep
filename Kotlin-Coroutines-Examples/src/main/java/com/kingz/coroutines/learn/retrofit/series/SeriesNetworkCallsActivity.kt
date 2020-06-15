package com.kingz.coroutines.learn.retrofit.series

import androidx.lifecycle.ViewModelProvider
import com.kingz.coroutines.data.api.ApiHelperImpl
import com.kingz.coroutines.data.api.RetrofitBuilder
import com.kingz.coroutines.data.local.DatabaseBuilder
import com.kingz.coroutines.data.local.DatabaseHelperImpl
import com.kingz.coroutines.learn.base.ApiUserAdapter
import com.kingz.coroutines.learn.retrofit.BaseNetworkCallsActivity
import com.kingz.coroutines.utils.ViewModelFactory

class SeriesNetworkCallsActivity : BaseNetworkCallsActivity<SeriesNetworkCallsViewModel>() {
    override lateinit var viewModel: SeriesNetworkCallsViewModel
    override lateinit var adapter: ApiUserAdapter
    override fun getUser() = viewModel.getUsers()

    override fun setupViewModel() {
        viewModel = ViewModelProvider(this, ViewModelFactory.build {
            SeriesNetworkCallsViewModel(
                    ApiHelperImpl(RetrofitBuilder.apiService),
                    DatabaseHelperImpl(DatabaseBuilder.getInstance(applicationContext))
            )
        }).get(SeriesNetworkCallsViewModel::class.java)
    }
}