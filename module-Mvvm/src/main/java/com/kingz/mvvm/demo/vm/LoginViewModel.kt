package com.kingz.mvvm.demo.vm

import androidx.lifecycle.MutableLiveData
import com.kingz.mvvm.base.BaseViewModel
import com.kingz.mvvm.demo.MvvmRepository
import com.kingz.mvvm.demo.entity.LoginEntity
import com.zeke.kangaroo.utils.ZLog

class LoginViewModel(private val mockRepository: MvvmRepository) : BaseViewModel() {

    val loginData: MutableLiveData<MutableList<LoginEntity>> = MutableLiveData()

    fun fetchMockData() {
        ZLog.d("MVVM","ViewModel ---> fetchMockData")
        launchIO {
            when (val result = mockRepository.fetchMockLoginData()) {
                else -> {
                    ZLog.d("MVVM","ViewModel <--- Get MockData. PostValue by LiveData.")
                    loginData.postValue(result)
                }
            }
        }
    }

    override fun onCleared() {
        ZLog.d("MVVM","ViewModel ---> onCleared")
        super.onCleared()
    }

}