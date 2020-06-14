package com.kingz.mvvm.demo

import android.os.Bundle
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Observer
import androidx.lifecycle.viewModelScope
import com.kingz.mvvm.R
import com.kingz.mvvm.base.BaseVMActivity
import com.kingz.mvvm.demo.factory.ViewModelFactory
import com.kingz.mvvm.demo.vm.LoginViewModel
import com.zeke.kangaroo.utils.ZLog
import kotlinx.android.synthetic.main.activity_login.*

/**
 * @author zeke.wang
 * @date 2020/5/27
 * @maintainer zeke.wang
 * @copyright 2020 www.xgimi.com Inc. All rights reserved.
 * @desc:
 */
class LoginActivity : BaseVMActivity<LoginViewModel>() {

    // Use the 'by viewModels()' Kotlin property delegate
    // override val viewModel: LoginViewModel  by viewModels()
    override val viewModel: LoginViewModel by viewModels {
        ViewModelFactory.build {
            LoginViewModel(MvvmRepository())
        }
    }

    override val layoutRes: Int = R.layout.activity_login
    override val binding: ViewDataBinding?
        get() = DataBindingUtil.setContentView(this, layoutRes)

    override fun onCreate(savedInstanceState: Bundle?) {
//      binding.viewModel = viewModel
        binding?.lifecycleOwner = this
        super.onCreate(savedInstanceState)

        viewModel.viewModelScope.apply {
        }

        // 不使用DataBinding时需要这样手动设置
        viewModel.apply {
            // Observe the LiveData, passing in this activity as the LifecycleOwner and the observer.
            loginData.observe(this@LoginActivity, Observer {
                it?.apply {
                    ZLog.d("MVVM", "View onChanged --->$this")
                    tips_view.text = "登录成功"
                }
            })
        }

        bt_login.setOnClickListener {
            ZLog.d("MVVM", "View ---> fetchMockData")
            viewModel.fetchMockData()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        ZLog.d("MVVM", "View ---> onDestroy")
    }
}