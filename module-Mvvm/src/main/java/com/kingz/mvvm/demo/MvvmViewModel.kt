package com.kingz.mvvm.demo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import com.kingz.mvvm.base.BaseViewModel
import com.zeke.kangaroo.utils.ZLog

class MvvmViewModel(private val mockRepository: MvvmRepository) : BaseViewModel() {

    // 获取到的数据
    private val sampleData: LiveData<WAZChaptersEntity> = liveData {
        when (val result = mockRepository.fetchMockData()) {
                // 获取成功  发射数据 emit(result.data)
                // 获取失败  exception.postValue(result.throwable)
        }
    }

    //TODO 学习SwitchMap
    //
    // val adapterData = sampleData.switchMap {
    //     liveData {
    //         emit(mutableListOf<String>().apply {
    //             for (i in it.data) {
    //                 this.add(i.id.toString())
    //             }
    //         })
    //     }
    // }

    val mockData2: MutableLiveData<MutableList<String>> = MutableLiveData()

    fun fetchMockData() {
        launchIO {
            ZLog.d("异步执行  获取数据")
            // when (val result = mockRepository.fetchMockData()) {
            //     is XResult.Success -> {
            //         mockData2.postValue(mutableListOf<String>().apply {
            //             for (i in result.data.data) {
            //                 this.add(i.name)
            //             }
            //         })
            //     }
            //     is XResult.Error -> {
            //         exception.postValue(result.throwable)
            //     }
            // }
        }
    }

}