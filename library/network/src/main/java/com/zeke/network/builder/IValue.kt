package com.zeke.network.builder

import com.zeke.network.domin.time.ITime
import okhttp3.OkHttpClient

/**
 * @author zeke.wang
 * @date 2020/7/7
 * @maintainer zeke.wang
 * @desc:
 */
interface IValue : ITime.Get {
    fun builder(): OkHttpClient.Builder
    fun factory(): retrofit2.Converter.Factory
    fun adapterFactory(): retrofit2.CallAdapter.Factory
}