package com.zeke.ktx

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.OnLifecycleEvent

/**
 * author：KingZ
 * date：2020/1/27
 * description：应用的 LifeCycle
 *  也可使用自带的：GenericLifecycleObserver
 *
 * // getLifecycle().addObserver(new GenericLifecycleObserver() {
 * //     @Override
 * //     public void onStateChanged(LifecycleOwner source, Lifecycle.Event event) {
 * //         ZLog.d(TAG, "onStateChanged: event ="+event);
 * //     }
 * // });
 */
class AppLifeCycle(var TAG:String) : LifecycleObserver {
    companion object { var TAG = "Lifecycle" }

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun onCreate(){
        // Log.d(TAG, "onCreate: ")
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun onStart() {
        //  Log.d(TAG, "onStart: ")
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun onResume() {
        // Log.d(TAG, "onResume: ")
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    fun onPause() {
        // Log.d(TAG, "onPause: ")
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun onStop() {
        // Log.d(TAG, "onStop: ")
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onDestroy() {
        // Log.d(TAG, "onDestroy: ")
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_ANY)
    fun onAny() {
        // Log.d(TAG, "onAny: ")
    }
}
