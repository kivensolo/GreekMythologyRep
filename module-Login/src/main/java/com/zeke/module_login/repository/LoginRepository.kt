package com.zeke.module_login.repository

import com.kingz.module.wanandroid.repository.WanAndroidRepository
import com.zeke.module_login.api.LoginApiService
import com.zeke.module_login.entity.UserInfoBean
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response

class LoginRepository(private val apiServer: LoginApiService) : WanAndroidRepository(apiServer) {

    /**
     * 进行用户登录操作
     */
    suspend fun userLogin(name: String = "", password: String = ""): UserInfoBean {
        return withContext(Dispatchers.IO) {
            apiServer.userLogin(name, password)
        }
    }

    /**
     * 进行用户注册操作
     */
    suspend fun userRegister(name: String = "",
                             password: String = "",
                             rePassword: String = ""): Response<UserInfoBean> {
        return withContext(Dispatchers.IO) {
            apiServer.userRegister(name, password, rePassword)
        }
    }
}