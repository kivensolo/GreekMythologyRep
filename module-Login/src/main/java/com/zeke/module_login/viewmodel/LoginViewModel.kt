package com.zeke.module_login.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.switchMap
import com.kingz.base.BaseViewModel
import com.kingz.module.common.api.ApiServiceUtil
import com.zeke.kangaroo.utils.ZLog
import com.zeke.module_login.entity.UserInfoBean
import com.zeke.module_login.repository.LoginRepository
import retrofit2.Response

class LoginViewModel : BaseViewModel<LoginRepository>() {
    override fun createRepository() : LoginRepository{
        return LoginRepository(ApiServiceUtil.getApiService())
    }

    // 用户登录数据 FIXME 为什么不接受 ResponseResult<UserInfoBean> 对象
    //    val loginInfoData: MutableLiveData<ResponseResult<UserInfoBean>> by lazy {
    // 这种写法就是把LiveData暴露在ViewModel中，而不是放在repository中
    val loginInfoData: MutableLiveData<UserInfoBean> by lazy {
        MutableLiveData<UserInfoBean>()
    }
    // 用户注册数据
    val registerData: MutableLiveData<Response<UserInfoBean>> by lazy {
        MutableLiveData<Response<UserInfoBean>>()
    }

    // 使用ktx的方法 创建一个CoroutineLiveData
    private val testLiveData: LiveData<MutableList<String>> = liveData {
        // 作用域为 LiveDataScope<List<String>>
    }
    private val testFilterLiveData: LiveData<List<String>> = testLiveData.switchMap {
        liveData { emit(listOf("aaa","ccc")) }
    }

    /**
     * FIXME 数据请求行为由 ViewModel ---> Repository ---> Servers 请求么？
     */
    fun login(name:CharSequence, pwd:CharSequence){
        launchDefault {
            try {
                val result = repository.userLogin(name.toString(),pwd.toString())
                loginInfoData.postValue(result)
            }catch (e:Exception){
                ZLog.e("dologin on exception: ${e.printStackTrace()}")
                loginInfoData.postValue(null)
            }
        }
    }

    fun singUp(name:CharSequence,
               pwd:CharSequence,
               re_pwd:CharSequence){
        launchDefault {
            try {
                val result = repository.userRegister(
                    name.toString(),
                    pwd.toString(),
                    re_pwd.toString())
                registerData.postValue(result)
            }catch (e:Exception){
                ZLog.e("dologin on exception: ${e.printStackTrace()}")
                registerData.postValue(null)
            }
        }
    }




}