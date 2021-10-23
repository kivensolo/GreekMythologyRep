package com.zeke.module_login.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.switchMap
import com.kingz.module.wanandroid.bean.UserInfoBean
import com.kingz.module.wanandroid.response.WanAndroidResponse
import com.kingz.module.wanandroid.viewmodel.WanAndroidViewModelV2
import com.zeke.kangaroo.zlog.ZLog

class LoginViewModel : WanAndroidViewModelV2() {

    // 这种写法就是把LiveData暴露在ViewModel中，而不是放在repository中
    val loginInfoData: MutableLiveData<UserInfoBean> by lazy {
        MutableLiveData<UserInfoBean>()
    }

    val registerLiveData: MutableLiveData<WanAndroidResponse<UserInfoBean>> by lazy {
        MutableLiveData<WanAndroidResponse<UserInfoBean>>()
    }

    // 使用ktx的方法 创建一个CoroutineLiveData
    private val testLiveData: LiveData<MutableList<String>> = liveData {
        // 作用域为 LiveDataScope<List<String>>
    }

    private val testFilterLiveData: LiveData<List<String>> = testLiveData.switchMap {
        liveData { emit(listOf("aaa", "ccc")) }
    }

    fun login(name: CharSequence, pwd: CharSequence) {
        launchCPU {
            try {
                val result = remoteDataSource.userLogin(name.toString(), pwd.toString())
                loginInfoData.postValue(result)
            } catch (e: Exception) {
                ZLog.e("dologin on exception: ${e.printStackTrace()}")
                loginInfoData.postValue(null)
            }
        }
    }

    fun singUp(name: CharSequence, pwd: CharSequence, re_pwd: CharSequence) {
        launchCPU {
            try {
                val result = remoteDataSource.userRegister(name.toString(),
                    pwd.toString(), re_pwd.toString())
                registerLiveData.postValue(result)
            } catch (e: Exception) {
                ZLog.e("singUp on exception: ${e.printStackTrace()}")
                registerLiveData.postValue(null)
            }
        }
    }


}