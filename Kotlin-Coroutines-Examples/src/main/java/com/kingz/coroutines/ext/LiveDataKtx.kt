package com.kingz.coroutines.ext

import androidx.annotation.MainThread
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer

/**
 *  内联函数如何使用？？？
 *  ?????????????
 *  ?????????????
 *  TODO ?????????
 */
@MainThread
inline fun <T> LiveData<T>.observe(owner: LifecycleOwner, crossinline onChanged: (T) -> Unit): Observer<T> {
    val wrappedObserver = Observer<T> { t -> onChanged.invoke(t) }
    observe(owner, wrappedObserver)
    return wrappedObserver
}

/**
 * 观察一次  onChanged后即毁
 */
fun <T> LiveData<T>.observeOnce(observer: Observer<T>) {
    observeForever(object : Observer<T> {
        override fun onChanged(t: T?) {
            observer.onChanged(t)
            removeObserver(this)
        }
    })
}

/**
 * 观察一次  满足执行条件再removeObserver
 */
fun <T> LiveData<T>.observeWith(observer: Observer<T>, func: (it: T?) -> Boolean) {
    observeForever(object : Observer<T> {
        override fun onChanged(t: T?) {
            observer.onChanged(t)
            if (func(t)) removeObserver(this)
        }
    })
}