package com.zeke.module_login.viewmodel

import androidx.lifecycle.MutableLiveData
import com.kingz.base.BaseViewModel
import com.kingz.base.response.ResponseResult
import com.zeke.module_login.repository.LoginRepository

/**
 *
 */
class LoginViewModel : BaseViewModel<LoginRepository>() {
    override fun createRepository() = LoginRepository()

    val loginInfoData: MutableLiveData<ResponseResult<String>> by lazy {
        MutableLiveData<ResponseResult<String>>()
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