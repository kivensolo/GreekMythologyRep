package com.kingz.base

import com.kingz.base.response.BaseResponse
import kotlinx.coroutines.CoroutineScope

@Deprecated(message = "don't use Block replace this lamda")
internal typealias Block = suspend CoroutineScope.() -> Unit

internal typealias Success<T> = (BaseResponse<T>) -> Unit

internal typealias NetBlock<T> = suspend CoroutineScope.() -> BaseResponse<T>

internal typealias Error = (Exception) -> Unit