package com.kingz.coroutines.learn.retrofit.single

import androidx.lifecycle.ViewModelProvider
import com.kingz.coroutines.data.api.ApiHelperImpl
import com.kingz.coroutines.data.api.RetrofitBuilder
import com.kingz.coroutines.data.local.DatabaseBuilder
import com.kingz.coroutines.data.local.DatabaseHelperImpl
import com.kingz.coroutines.learn.base.ApiUserAdapter
import com.kingz.coroutines.learn.retrofit.BaseNetworkCallsActivity
import com.kingz.coroutines.utils.ViewModelFactory

class SingleNetworkCallActivity : BaseNetworkCallsActivity<SingleNetworkCallViewModel>() {

    // Ktx的写法 使用了代理和懒加载模式
//    private val viewModel: SingleNetworkCallViewModel by viewModels {
//        ViewModelFactory.build {
//            SingleNetworkCallViewModel(
//                    ApiHelperImpl(RetrofitBuilder.apiService),
//                    DatabaseHelperImpl(DatabaseBuilder.getInstance(applicationContext)))
//        }
//    }

    override lateinit var viewModel: SingleNetworkCallViewModel
    override lateinit var adapter: ApiUserAdapter
    override fun getUser() = viewModel.getUsers()

    override fun setupViewModel() {
        /**
         * 源码实现的最开始最基础的api为 ViewModelProviders.of(ViewModelStoreOwner, Factory)，
         * 后续被标记废弃，推荐使用 'by viewModels()' 的Kotlin属性代理的方式或
         * ViewModelProvider(ViewModelStoreOwner, Factory) 传递activity和factory的方法
         */
        // @Deprecated
        // viewModel = ViewModelProviders.of(

        viewModel = ViewModelProvider(
            this,
            ViewModelFactory.build {
                SingleNetworkCallViewModel(
                        ApiHelperImpl(RetrofitBuilder.apiService),
                        DatabaseHelperImpl(DatabaseBuilder.getInstance(applicationContext))
                        )
            }
        ).get(SingleNetworkCallViewModel::class.java)
    }
}
