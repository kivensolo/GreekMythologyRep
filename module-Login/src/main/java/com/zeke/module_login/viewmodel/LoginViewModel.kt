package com.zeke.module_login.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.switchMap
import com.kingz.base.BaseViewModel
import com.kingz.base.response.ResponseResult
import com.zeke.module_login.repository.LoginRepository

class LoginViewModel : BaseViewModel<LoginRepository>() {
    override fun createRepository() = LoginRepository()

    val loginInfoData: MutableLiveData<ResponseResult<String>> by lazy {
        MutableLiveData<ResponseResult<String>>()
    }

    // 使用ktx的方法 创建一个CoroutineLiveData
    private val testLiveData: LiveData<MutableList<String>> = liveData {
        // 作用域为 LiveDataScope<List<String>>

    }

    private val testFilterLiveData: LiveData<List<String>> = testLiveData.switchMap {
        liveData { emit(listOf("aaa","ccc")) }
    }

    fun doLogin(){
        launchDefault {
            loginInfoData.postValue(ResponseResult.loading(""))
            try {
                val result = repository.userLogin()
                loginInfoData.postValue(result)
            }catch (e:Exception){
                loginInfoData.postValue(ResponseResult.error(e.toString(), null))
            }
        }
    }
}