package com.kingz.coroutines.utils

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

/**
 * 自定义的ViewModel创建工厂
 */
object ViewModelFactory {

    /**
     * 最原始的写法是实现ViewModelProvider.Factory 接口, 实现create()方法，承担ViewModel的创建工厂角色。
     * 然后使用 ：
     * ViewModelProviders.of(this,CustomViewModelFactory对象).get(modelClass) 来实例化对应的ViewModel。
     */
    inline fun <reified T: ViewModel> build(crossinline createViewModel: () -> T): ViewModelProvider.Factory {
        return object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                @Suppress("UNCHECKED_CAST")
                return createViewModel() as T
            }
        }
    }

}