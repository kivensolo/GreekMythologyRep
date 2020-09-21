package com.zeke.home.viewmodel

import androidx.lifecycle.LiveData
import com.kingz.base.BaseViewModel
import com.kingz.base.response.ResponseResult
import com.kingz.module.common.bean.BannerData
import com.zeke.home.repository.MainRepository

/**
 * author: King.Z <br>
 * date:  2020/9/21 22:26 <br>
 * description:  <br>
 *     首页的ViewModel
 */
@Deprecated(message = "使用了WanAndroidViewModel代替")
class HomeViewModel: BaseViewModel<MainRepository>() {
    override fun createRepository() = MainRepository()

    suspend fun getBanner(): LiveData<ResponseResult<BannerData>>{
         return repository.getBanner()
    }

}