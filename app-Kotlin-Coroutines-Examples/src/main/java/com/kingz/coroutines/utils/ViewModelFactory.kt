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

    /**
     * 这是按照kotlin的写法
     */
    // inline 内联函数
    // crossinline: 修饰lambda表达式，因为如果将lambda表达式作为参数的函数是内联函数，那么return 等局部返回的
    // 操作也是内联的，且是被允许的。
    //
    // 但是⼀些内联函数可能调⽤传给它们的不是直接来⾃函数体、⽽是来⾃另⼀个执⾏上下⽂的lambda 表达式参数，例如来⾃局部对象或嵌套函数。
    // 在这种情况下，该 lambda 表达式中也不允许⾮局部控制流。
    // 为了标识这种情况，该 lambda 表达式参数需要⽤ crossinline 修饰符标记:
    inline fun <reified T: ViewModel> build(crossinline createViewModel: () -> T): ViewModelProvider.Factory {
        return object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
//                @Suppress("UNCHECKED_CAST")
                return createViewModel() as T
            }
        }
    }

}