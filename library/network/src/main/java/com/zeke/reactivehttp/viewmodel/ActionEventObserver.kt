package com.zeke.reactivehttp.viewmodel

import android.content.Context
import androidx.lifecycle.*
import com.zeke.reactivehttp.coroutine.ICoroutineEvent
import kotlinx.coroutines.Job

/**
 * @Author: leavesC
 * @Date: 2020/4/30 15:23
 * @Desc: 用于定义 View 和  ViewModel 均需要实现的一些 UI 层行为
 * @GitHub：https://github.com/leavesC
 *
 *                   |----------------------|
 *                   | BaseReactiveActivity |
 *                   |----------------------|
 *                               |
 *                               |
 *                               ↓
 *      |-----------------------------------------------------|
 *      |                       /————>  ShowLoadingLiveData   |
 *      | BaseReactiveViewMOdel |————> DismissLoadingLiveData |
 *      |                       \————>  ShowToastLiveData     |
 *      |-----------------------------------------------------|
 *                               |
 *                               |
 *                               ↓
 *                   |----------------------|
 *                   | BaseRemoteDataSource |
 *                   |----------------------|
 *
 * [#BaseRemoteDataSource]: 作为数据提供者处于最下层，只用于向上层提供数据，提供了多个同步请求和异步请求方法，
 * 和 BaseReactiveViewModel 之间依靠 IUIActionEvent 接口来联系
 *
 * [#BaseReactiveViewModel]：作为用户数据的承载体和处理者，包含了多个和网络请求事件相关的
 *  LiveData 用于驱动界面层的 UI 变化，和 BaseReactiveActivity 之间依靠 IViewModelActionEvent 接口来联系
 *
 * [#BaseReactiveActivity]: 包含与系统和用户交互的逻辑，其负责响应 BaseReactiveViewModel
 * 中的数据变化，提供了和 BaseReactiveViewModel 进行绑定的方法
 */

/**
 * 模块通信层，用于业务上常用的UI事件分发
 * DataSource <=====> ViewModel <=====> UI
 * 扩展至协程作用域顶层，以便更方便的使用协程。
 */
interface IUIActionEvent : ICoroutineEvent {
    fun showLoading(job: Job? = null)

    fun dismissLoading()

    fun showToast(msg: String)

    fun showNoNetworkView()

    fun finishView()

}

/**
 * 连接ViewModel和View层
 */
interface IViewModelActionEvent : IUIActionEvent {

    val showLoadingEventLD: MutableLiveData<ShowLoadingEvent>

    val dismissLoadingEventLD: MutableLiveData<DismissLoadingEvent>

    val showToastEventLD: MutableLiveData<ShowToastEvent>

    val finishViewEventLD: MutableLiveData<FinishViewEvent>

    val showNoNetwork: MutableLiveData<ShowNoNetworkEvent>

    override fun showLoading(job: Job?) {
        showLoadingEventLD.value = ShowLoadingEvent(job)
    }

    override fun dismissLoading() {
        dismissLoadingEventLD.value = DismissLoadingEvent
    }

    override fun showToast(msg: String) {
        showToastEventLD.value = ShowToastEvent(msg)
    }

    override fun showNoNetworkView() {
        showNoNetwork.value = ShowNoNetworkEvent
    }

    override fun finishView() {
        finishViewEventLD.value = FinishViewEvent
    }

}

interface IUIActionEventObserver : IUIActionEvent {

    val lContext: Context?

    val lLifecycleOwner: LifecycleOwner

    /**
     * 创建ViewModel的代理方法,可用于View层获取ViewModel对象
     * @param clazz java class of viewmodel
     * @param factory ViewModelProvider Factory
     */
    fun <VM> getViewModel(
        clazz: Class<VM>,
        factory: ViewModelProvider.Factory? = null,
        initializer: (VM.(lifecycleOwner: LifecycleOwner) -> Unit)? = null
    ): Lazy<VM> where VM : ViewModel, VM : IViewModelActionEvent {
        return lazy {
            getViewModelFast(clazz, factory, initializer)
        }
    }

    fun <VM> getViewModelFast(clazz: Class<VM>,
                              factory: ViewModelProvider.Factory? = null,
                              initializer: (VM.(lifecycleOwner: LifecycleOwner) -> Unit)? = null
    ): VM where VM : ViewModel, VM : IViewModelActionEvent {
        return when (val localValue = lLifecycleOwner) {
            is ViewModelStoreOwner -> {
                if (factory == null) {
                    ViewModelProvider(localValue).get(clazz)
                } else {
                    ViewModelProvider(localValue, factory).get(clazz)
                }
            }
            else -> {
                factory?.create(clazz) ?: clazz.newInstance()
            }
        }.apply {
            generateActionEvent(this)
            initializer?.invoke(this, lLifecycleOwner)
        }
    }

    fun <VM> generateActionEvent(viewModel: VM) where VM : ViewModel, VM : IViewModelActionEvent {
        viewModel.showLoadingEventLD.observe(lLifecycleOwner, Observer {
            this@IUIActionEventObserver.showLoading(it.job)
        })
        viewModel.dismissLoadingEventLD.observe(lLifecycleOwner, Observer {
            this@IUIActionEventObserver.dismissLoading()
        })
        viewModel.showToastEventLD.observe(lLifecycleOwner, Observer {
            if (it.message.isNotBlank()) {
                this@IUIActionEventObserver.showToast(it.message)
            }
        })
        viewModel.showNoNetwork.observe(lLifecycleOwner, Observer {
            this@IUIActionEventObserver.showNoNetworkView()
        })
        viewModel.finishViewEventLD.observe(lLifecycleOwner, Observer {
            this@IUIActionEventObserver.finishView()
        })
    }

}