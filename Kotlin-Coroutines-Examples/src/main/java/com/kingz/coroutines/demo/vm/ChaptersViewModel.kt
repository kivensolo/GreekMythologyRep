package com.kingz.coroutines.demo.vm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.kingz.coroutines.base.BaseViewModel
import com.kingz.coroutines.demo.MvvmRepository
import com.kingz.coroutines.demo.entity.WAZChaptersEntity
import com.zeke.kangaroo.utils.ZLog
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class ChaptersViewModel(private val mockRepository: MvvmRepository) : BaseViewModel() {

    // 获取到的数据
    val sampleData: LiveData<WAZChaptersEntity> = liveData {
        when (val result = mockRepository.fetchMockLoginData()) {
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

    val mockData: MutableLiveData<MutableList<String>> = MutableLiveData()

    fun fetchMockData() {
        viewModelScope.launch(Dispatchers.IO) {
            //模拟假数据返回
            ZLog.d("MvvmViewModel","异步执行  获取数据....Start")
            delay(5000)
            ZLog.d("MvvmViewModel","异步执行  获取数据....End")
            mockData.postValue(mutableListOf<String>().apply {
                for(i in 1..5){
                    add("数据_$i")
                }
            })

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