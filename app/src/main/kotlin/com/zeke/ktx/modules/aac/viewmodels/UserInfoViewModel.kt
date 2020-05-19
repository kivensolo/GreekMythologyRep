package com.zeke.ktx.modules.aac.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

/**
 * date：2020/1/17
 * description：ViewModel学习使用
 * 注意：
 *     确保将用于更新界面的 LiveData 对象存储在 ViewModel 对象中，
 *
 * 而不是将其存储在 Activity 或 Fragment 中，原因如下：
 * 1. 避免 Activity 和 Fragment 过于庞大。
 * 现在，这些UI控制器负责显示数据，而不负责保持数据状态。。
 *
 * 2. 将 LiveData 实例与特定的 Activity 或 Fragment 实例解耦，
 * 并使 LiveData 对象在配置更改后继续存在。
 *
 * 问题： ViewModel如何获得view的context之类？？
 *   Don't store Contexts in a ViewModel !!!!
 */
class UserInfoViewModel : ViewModel() {
    // 通常在 ViewModel 类中完成创建 LiveData 实例的操作，来存储某种类型的数据
    // Create a LiveData with a String
    val testStringLiveData = MutableLiveData<User>()

}

data class User(
        var name:String? = "",
        var age:String? = "",
        var sex:String? = ""
)