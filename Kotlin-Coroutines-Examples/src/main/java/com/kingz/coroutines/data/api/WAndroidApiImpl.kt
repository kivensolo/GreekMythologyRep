package com.kingz.coroutines.data.api

import com.kingz.coroutines.data.api.wandroid.NewApiHelper
import com.kingz.coroutines.data.api.wandroid.WAndroidApi
import com.kingz.coroutines.demo.entity.LoginEntity
import com.zeke.kangaroo.utils.ZLog
import kotlinx.coroutines.delay

/**
 * @author zeke.wang
 * @date 2020/6/22
 * @maintainer zeke.wang
 * @desc: 玩Android Api接口实现
 */
class WAndroidApiImpl(private val apiService: WAndroidApi = RetrofitBuilder.wAndroidApi)
    : WAndroidApi {

//    override suspend fun fetchChapterData() = apiService.fetchChapterData()
    override suspend fun fetchChapterData() = NewApiHelper.wanAndroidService.fetchChapterData()

    override suspend fun fetchMockLoginData(): MutableList<LoginEntity> {
        ZLog.d("MVVM", "DATA ---> fetchMockLoginData Start ...")
        delay(5000)
        ZLog.d("MVVM", "DATA <--- fetchMockLoginData End !!")
        return mutableListOf<LoginEntity>().apply {
            add(LoginEntity(0, "admin", "success"))
        }
    }
}