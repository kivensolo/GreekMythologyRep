package com.zeke.network.domin

import okhttp3.OkHttpClient
import retrofit2.CallAdapter
import retrofit2.Converter

/**
 * @author zeke.wang
 * @date 2020/7/7
 * @maintainer zeke.wang
 * @desc:
 */
open interface IBuilder<ApiClass> : Set<IBuilder<ApiClass?>?> {
    fun setBuilder(var1: OkHttpClient.Builder?): IBuilder<ApiClass>?
    fun setConverterFactory(var1: Converter.Factory?): IBuilder<ApiClass>?
    fun setAdapterFactory(var1: CallAdapter.Factory?): IBuilder<ApiClass>?
}