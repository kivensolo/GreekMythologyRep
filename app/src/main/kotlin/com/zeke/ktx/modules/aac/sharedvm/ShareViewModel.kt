package com.zeke.ktx.modules.aac.sharedvm

import androidx.activity.ComponentActivity
import androidx.lifecycle.*

/**
 * author: King.Z <br>
 * date:  2020/12/17 22:03 <br>
 * description:
 *   Activity之间共享ViewModel的扩展实现方案 <br>
 */

/**
 * 作用域对应的VMStoresMap
 * <scopeName, 作用域对应的VMStore>
 */
private val vMStoreMap = HashMap<String, CustomVMStore>()

/**
 * 自定义ViewStore
 */
class CustomVMStore : ViewModelStoreOwner {
    //LifecycleOwner宿主列表
    private val _bindHostsList = ArrayList<LifecycleOwner>()
    private var _vmStore: ViewModelStore? = null

    override fun getViewModelStore(): ViewModelStore {
        if (_vmStore == null)
            _vmStore = ViewModelStore()
        //此处进行显示强转
        return _vmStore as ViewModelStore
    }

    /**
     * 监听host的生命周期
     */
    fun bindHost(host: LifecycleOwner) {
        if (!_bindHostsList.contains(host)) {
            _bindHostsList.add(host)
            host.lifecycle.addObserver(object : LifecycleEventObserver {
                override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
                    if (event == Lifecycle.Event.ON_DESTROY) {
                        _bindHostsList.remove(host)
                        //如果当前CustomVMStore没有任何关联对象时，释放内存
                        if (_bindHostsList.isEmpty()) {
                            vMStoreMap.entries.find {
                                it.value == this@CustomVMStore
                            }?.also {
                                _vmStore?.clear()
                                vMStoreMap.remove(it.key)
                                "clear size={${vMStoreMap.size}}".log()
                            }
                        }
                    }
                }
            })
        }
    }
}

/**
 * 子Activity在onCreate时进行VMScope注入
 * 将子Activity的Viewmodel进行赋值操作
 */
fun ComponentActivity.injectVM() {
    this::class.java.declaredFields.forEach { field ->
        field.getAnnotation(VMScope::class.java)?.also { scope ->
            val scopeName = scope.name
            val store: CustomVMStore
            if (vMStoreMap.keys.contains(scopeName)) {
                store = vMStoreMap[scopeName]!!
            } else {
                store = CustomVMStore()
                vMStoreMap[scopeName] = store
            }
            store.bindHost(this)
            @Suppress("UNCHECKED_CAST")
            val clazz = field.type as Class<ViewModel>
            val vm = ViewModelProvider(store,CustomVMFactory()).get(clazz)
            field.set(this, vm)
        }
    }
}

/**
 * 自定义ViewModel创建工厂
 */
class CustomVMFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return modelClass.newInstance()
    }
}