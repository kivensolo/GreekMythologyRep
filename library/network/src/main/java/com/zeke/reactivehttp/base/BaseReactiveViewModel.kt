package com.zeke.reactivehttp.base

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zeke.reactivehttp.viewmodel.*
import kotlinx.coroutines.CoroutineScope

/**
 * @Author: leavesC
 * @Date: 2020/7/24 0:43
 * @Desc:
 * Hold UI Data(内部持有LiveData).
 * ViewModel中持有 数据源对象(Repository的概念)，并通过它执行具体逻辑.
 *
 * 用户数据的承载体和处理者，包含了多个和网络请求事件相关的 LiveData 用于驱动界面层的 UI 变化，
 * 和 BaseReactiveActivity 之间依靠 IViewModelActionEvent 接口来联系。
 */
open class BaseReactiveViewModel : ViewModel(), IViewModelActionEvent {

    val TAG: String = javaClass.simpleName

    override val lifecycleSupportedScope: CoroutineScope
        get() = viewModelScope

    override val showLoadingEventLD = MutableLiveData<ShowLoadingEvent>()

    override val dismissLoadingEventLD = MutableLiveData<DismissLoadingEvent>()

    override val showToastEventLD = MutableLiveData<ShowToastEvent>()

    override val finishViewEventLD = MutableLiveData<FinishViewEvent>()

}

open class BaseReactiveAndroidViewModel(application: Application)
    : AndroidViewModel(application), IViewModelActionEvent {

    override val lifecycleSupportedScope: CoroutineScope
        get() = viewModelScope

    override val showLoadingEventLD = MutableLiveData<ShowLoadingEvent>()

    override val dismissLoadingEventLD = MutableLiveData<DismissLoadingEvent>()

    override val showToastEventLD = MutableLiveData<ShowToastEvent>()

    override val finishViewEventLD = MutableLiveData<FinishViewEvent>()

}