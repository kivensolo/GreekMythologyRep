package com.kingz.module.common.exception

/**
 * @author zeke.wang
 * @date 2020/5/26
 * @maintainer zeke.wang
 * @desc: 扩展函数工具类
 */
fun Any.tryCatch(func: () -> Unit) {
    try {
        func()
    } catch (t: Throwable) {
        t.printStackTrace()
    }
}


fun Any.tryCatch(func: () -> Unit, error: ((t: Throwable) -> Unit)? = null) {
    try {
        func()
    } catch (t: Throwable) {
        t.printStackTrace()
        error?.invoke(t)
    }
}

fun <T> Any.tryCatchForResult(errorParams: T, func: () -> T): T {
    return try {
        func()
    } catch (t: Throwable) {
        t.printStackTrace()
        errorParams
    }
}

suspend fun Any.trySuspend(func: suspend () -> Unit) {
    try {
        func()
    } catch (t: Throwable) {
        t.printStackTrace()
    }
}