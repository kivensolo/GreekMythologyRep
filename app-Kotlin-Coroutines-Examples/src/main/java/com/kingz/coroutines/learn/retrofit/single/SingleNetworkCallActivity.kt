package com.kingz.coroutines.learn.retrofit.single

import androidx.lifecycle.ViewModelProvider
import com.kingz.coroutines.learn.base.ApiUserAdapter
import com.kingz.coroutines.learn.retrofit.BaseNetworkCallsActivity
import com.kingz.coroutines.utils.ViewModelFactory

class SingleNetworkCallActivity : BaseNetworkCallsActivity<SingleNetworkCallViewModelV2>() {

    // Ktx的写法 使用了代理和懒加载模式
//    private val viewModel: SingleNetworkCallViewModel by viewModels {
//        ViewModelFactory.build {
//            SingleNetworkCallViewModel(
//                    ApiHelperImpl(RetrofitBuilder.apiService),
//                    DatabaseHelperImpl(DatabaseBuilder.getInstance(applicationContext)))
//        }
//    }

    override lateinit var viewModel: SingleNetworkCallViewModelV2
    override lateinit var adapter: ApiUserAdapter
    override fun getUser() = viewModel.getUsers()

    override fun setupViewModel() {
        /**
         * 源码实现的最开始最基础的api为 ViewModelProviders.of(ViewModelStoreOwner, Factory)，
         * 后续被标记废弃，推荐使用
         * 'by viewModels()' 的Kotlin属性代理的方式
         * 或
         * ViewModelProvider(ViewModelStoreOwner, Factory).get() 传递activity和factory的方法
         */
        // @Deprecated
        // viewModel = ViewModelProviders.of(

        viewModel = ViewModelProvider(
            this,
            ViewModelFactory.build {
//                SingleNetworkCallViewModel(
//                        ApiHelperImpl(RetrofitBuilder.USER_SERVICE_API),
//                        DatabaseHelperImpl(DatabaseBuilder.getInstance(applicationContext))
//                        )
                SingleNetworkCallViewModelV2()
            }
        ).get(SingleNetworkCallViewModelV2::class.java)
    }
}
