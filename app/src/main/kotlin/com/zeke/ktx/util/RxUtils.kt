package com.zeke.ktx.util

import android.util.Log
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit

/**
 * RxJava封装工具类
 */
object RxUtils {

    /**
     * 在ui线程中执行任务
     */
    fun ui(onSuccess: () -> Unit, onError: ((t: Throwable) -> Unit)? = null): Disposable {
        return Observable.just(0)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ onSuccess() }, {
                    it.printStackTrace()
                    if (onError != null) {
                        onError(it)
                    }
                })
    }

    /***
     * 在IO线程中执行任务
     */
    fun io(onSuccess: () -> Unit, onError: ((t: Throwable) -> Unit)? = null): Disposable {
        return Observable.just(0)
                .observeOn(Schedulers.io())
                .subscribe({ onSuccess() }, {
                    it.printStackTrace()
                    if (onError != null) {
                        onError(it)
                    }
                })
    }

    /**
     * 延迟指定时间后，在UI线程执行任务
     * @param delay 延迟的毫秒数
     * @onSuccess 订阅正常后需执行的操作
     * @onError  订阅异常后需执行的操作
     */
    fun postDelay(delay: Long,
                  onSuccess: () -> Unit,
                  onError: ((t: Throwable) -> Unit)? = null): Disposable {

        return Observable.timer(delay, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe ({onSuccess()},{
                    it.printStackTrace()
                    if(onError != null){
                        onError(it)
                    }
                })
    }


    fun postDelay(time: Long, action: Consumer<Long?>?): Disposable? {
        return Observable.timer(time, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(action, Consumer { throwable ->
                    Log.e("RxUtils", "RxUtils err = $throwable")
                })
    }

    /**
     * 在UI线程定时执行任务
     * 适用于播放器UI刷新检测等场景
     */
    fun intervalOnUiThread(initialDelay:Long,period:Long,
                           onSuccess: () -> Unit,
                           onError: ((t: Throwable) -> Unit)? = null): Disposable{
        return Observable.interval(initialDelay,period, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({onSuccess()},{
                    it.printStackTrace()
                    if(onError != null){
                        onError(it)
                    }
                })

    }


}