package com.zeke.module_login.repository

import com.kingz.base.BaseRepository
import com.zeke.module_login.ApiServiceUtil
import com.zeke.module_login.api.LoginApiService
import com.zeke.module_login.entity.UserInfoBean
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response

class LoginRepository : BaseRepository() {

    /**
     * 进行用户登录操作
     */
    suspend fun userLogin(name: String = "", password: String = ""): Response<UserInfoBean> {
        val service = ApiServiceUtil.getApiService<LoginApiService>()
        return withContext(Dispatchers.IO) {
            service.userLogin(name, password)
        }
    }

    /**
     * 进行用户注册操作
     */
    suspend fun userRegister(name: String = "",
                             password: String = "",
                             rePassword: String = ""): Response<UserInfoBean> {
        val service = ApiServiceUtil.getApiService<LoginApiService>()
        return withContext(Dispatchers.IO) {
            service.userRegister(name, password, rePassword)
        }
    }
}