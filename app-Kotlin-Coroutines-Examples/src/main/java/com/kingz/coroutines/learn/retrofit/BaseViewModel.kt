package com.kingz.coroutines.learn.retrofit

import androidx.lifecycle.ViewModel
import com.kingz.coroutines.data.UserInfoRemoteDataSource

/**
 * author：ZekeWang
 * date：2021/6/18
 * description：
 */
open class BaseViewModel: ViewModel(){
    protected val remoteDataSource: UserInfoRemoteDataSource by lazy {
        UserInfoRemoteDataSource()
    }
}