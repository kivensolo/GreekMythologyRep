package com.kingz.mvvm.paging

import androidx.paging.ItemKeyedDataSource
import com.kingz.mvvm.demo.entity.Data
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class SamplePagedSource(private val scope: CoroutineScope) :
        ItemKeyedDataSource<Int, Data>() {


    override fun loadInitial(params: LoadInitialParams<Int>, callback: LoadInitialCallback<Data>) {
        loadData(callback)
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Data>) {
        loadData(callback)
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Data>) {
        // loadData(callback)
    }

    override fun getKey(item: Data): Int {
        return item.id
    }

    private fun loadData(callback: LoadCallback<Data>) {
        scope.launch {
            // TODO 异步获取数据
            // when (val result = NetworkManager.request { apiService.fetchModeData() }) {
            //     is XResult.Success -> {
            //         callback.onResult(result.data.data)
            //     }
            // }
        }
    }

    override fun invalidate() {
        super.invalidate()
        // scope.cancel()
    }

}