@file:JvmName("ExceptionWrapper")
// 使用@JvmName注解来改变生成的类的名称，需放在包名前。

package com.kingz.module.common.ext

/**
 * @author zeke.wang
 * @date 2020/5/26
 * @maintainer zeke.wang
 * @desc: 顶层函数 && 扩展函数 工具类，
 */
fun Any.tryCatch(func: () -> Unit) {
    try {
        func()
    } catch (t: Throwable) {
        t.printStackTrace()
    }
}

// 函数类型变量error可空，需要将整 函数类型的定义包含在括号类，并在括号后添加一个问号
fun Any.tryCatch(func: () -> Unit, error: ((t: Throwable) -> Unit)? = null) {
    try {
        func()
    } catch (t: Throwable) {
        t.printStackTrace()
        error?.invoke(t)
    }
}

fun <T> Any.tryCatchWithDefault(func: () -> T, defaultValue: T): T {
    return try {
        func() ?: defaultValue
    } catch (t: Throwable) {
        t.printStackTrace()
        defaultValue
    }
}

suspend fun Any.trySuspend(func: suspend () -> Unit) {
    try {
        func()
    } catch (t: Throwable) {
        t.printStackTrace()
    }
}