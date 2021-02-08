package com.kingz.utils.ktx

import android.os.Handler
import android.os.Looper
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

/**
 * @author cx
 */
val handler = Handler(Looper.getMainLooper())
private val coreSize = Runtime.getRuntime().availableProcessors() + 1

private val fixed: ExecutorService = Executors.newFixedThreadPool(coreSize)
private val cached: ExecutorService = Executors.newCachedThreadPool()
private val single: ExecutorService = Executors.newSingleThreadExecutor()
private val scheduled: ExecutorService = Executors.newScheduledThreadPool(coreSize)

/**
 * 切换到主线程
 * inline: 函数内联
 * crossinline 禁用非局部返回(non-local return)
 */
inline fun <T> T.RunOnUi(crossinline block: T.() -> Unit) {
    handler.post {
        block()
    }
}

/**
 * 延迟delayMillis后切换到主线程
 */
fun <T> T.RunOnUiDelay(delayMillis: Long, block: T.() -> Unit) {
    handler.postDelayed({
        block()
    }, delayMillis)
}

/**
 * 子线程执行 SingleThreadPool
 */
fun <T> T.RunOnSingle(block: T.() -> Unit) {
    single.execute {
        block()
    }
}

/**
 * 子线程执行 FixedThreadPool
 */
fun <T> T.RunOnFixed(block: T.() -> Unit) {
    fixed.execute {
        block()
    }
}

/**
 * 子线程执行 CachedThreadPool
 */
fun <T> T.RunOnCached(block: T.() -> Unit) {
    cached.execute {
        block()
    }
}