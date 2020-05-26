package com.kingz.mvvm.paging

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.kingz.mvvm.demo.Data
import kotlinx.coroutines.CoroutineScope


class SamplePagedFactory(private val scope: CoroutineScope) : DataSource.Factory<Int, Data>() {

    val sourceLiveData: MutableLiveData<SamplePagedSource> = MutableLiveData()

    private val api by lazy {
        //NetworkManager.createApi<MvvmApi>(MvvmApi.BASE_URL)
        }

    override fun create(): DataSource<Int, Data> {
        val source = SamplePagedSource(scope, api)
        sourceLiveData.postValue(source)
        return source
    }

}