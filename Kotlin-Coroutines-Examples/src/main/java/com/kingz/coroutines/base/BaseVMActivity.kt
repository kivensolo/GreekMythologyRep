package com.kingz.coroutines.base

import kotlinx.coroutines.CoroutineScope

/**
 * @author zeke.wang
 * @date 2020/5/26
 * @maintainer zeke.wang
 * @copyright 2020 www.xgimi.com Inc. All rights reserved.
 * @desc:
 */
abstract class BaseVMActivity<VM : BaseViewModel> : BaseActivity() {

    protected abstract val viewModel: VM

    fun launchMain(block: suspend CoroutineScope.() -> Unit) {
        viewModel.launchMain { block() }
    }

    fun launchIO(block: suspend CoroutineScope.() -> Unit) {
        viewModel.launchIO { block() }
    }

    fun launchDefault(block: suspend CoroutineScope.() -> Unit) {
        viewModel.launchDefault { block() }
    }

}