package com.zeke.module_login.repository

import com.kingz.base.BaseRepository
import com.kingz.base.response.ResponseResult
import com.zeke.module_login.ApiServiceUtil
import com.zeke.module_login.api.LoginApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class LoginRepository : BaseRepository() {

    /**
     * 进行用户登录操作
     */
    suspend fun userLogin(name: String = "", password: String = ""): ResponseResult<String> {
        val service = ApiServiceUtil.getApiService<LoginApiService>()
        return withContext(Dispatchers.IO) {
            service.userLogin(name, password)
        }
    }
}