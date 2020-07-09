package com.zeke.module_login.viewmodel

import androidx.lifecycle.MutableLiveData
import com.kingz.base.BaseViewModel
import com.zeke.module_login.repository.LoginRepository

/**
 * @author cx
 */
class LoginViewModel : BaseViewModel<LoginRepository>() {
    override fun createRepository() = LoginRepository()

    val contentLiveData: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }

    fun getChapters() {
//        launch(netBlock = { repository.getChapters() }) {
//            contentLiveData.value = Gson().toJson(it.data)
//        }
    }
}