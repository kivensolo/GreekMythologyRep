package com.kingz.mvvm.demo

import com.kingz.mvvm.demo.entity.LoginEntity
import com.zeke.kangaroo.utils.ZLog
import kotlinx.coroutines.delay


class MvvmRepository{

    suspend fun fetchMockLoginData(): MutableList<LoginEntity> {
        ZLog.d("MVVM","DATA ---> fetchMockLoginData Start ...")
        delay(5000)
        ZLog.d("MVVM","DATA <--- fetchMockLoginData End !!")
        return mutableListOf<LoginEntity>().apply {
            LoginEntity(0,"admin","success")
        }
    }
}