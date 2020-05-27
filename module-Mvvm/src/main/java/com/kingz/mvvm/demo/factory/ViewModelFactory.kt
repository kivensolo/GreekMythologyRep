package com.kingz.mvvm.demo.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

/**
 * 自定义的ViewModel创建工厂
 */
object ViewModelFactory {

    inline fun <reified VM: ViewModel> build(crossinline method: () -> VM): ViewModelProvider.Factory {
        return object : ViewModelProvider.Factory {
            override fun <VM : ViewModel?> create(modelClass: Class<VM>): VM {
                @Suppress("UNCHECKED_CAST")
                return method() as VM
            }
        }
    }

}