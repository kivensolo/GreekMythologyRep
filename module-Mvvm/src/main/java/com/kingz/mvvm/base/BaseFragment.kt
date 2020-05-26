package com.kingz.mvvm.base

import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import com.kingz.mvvm.ext.observe
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * @author zeke.wang
 * @date 2020/5/26
 * @maintainer zeke.wang
 * @copyright 2020 www.xgimi.com Inc. All rights reserved.
 * @desc:
 */
open class BaseFragment : Fragment() {

    companion object {
        private const val TAG = "BaseFragment"
    }

    /**
     * 组合挂起函数
     * 在开始观察前已主动切换至主线程，避免线程错误
     */
    suspend fun <T> LiveData<T>.observe(onChanged: (T) -> Unit) = withContext(Dispatchers.Main) {
        observe(viewLifecycleOwner) { onChanged(it) }
    }
}