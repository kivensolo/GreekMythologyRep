package com.kingz.module.common.utils.ktx

import android.os.Build
import android.view.View
import kotlinx.coroutines.Job

/**
 * author: King.Z <br>
 * date:  2020/8/6 20:55 <br>
 * description: 协程版本的AutoDispose <br>
 *     原本kotlin提供了MainScope作用域来关联协程与UI，
 * 但是在每个View层中都手动创建MainScope并不是个好办法。
 *
 * AutoDisposableJob 实际上创建一个新的Job,和Uber的AutoDispose做法类似。
 * 所以AutoDisposableJob就是绑定UI的实现类。
 *
 * KTX中提供了LifecycleCoroutineScope类及其获取的方式，
 * 例如可以在activity中直接使用lifecycleScope来获取这个实例：
 * lifecycleScope.launch{
 *      ... //执行协程体
 * }
 * 原理是你的Activity的父类实现了LifecycleOwner这个接口，而lifecycleScope正是他的扩展成员。
 */

fun Job.asAutoDisposable(view:View) = AutoDisposableJob(view,this)

class AutoDisposableJob(private val view: View,
                        private val wrapped:Job
):Job by wrapped, View.OnAttachStateChangeListener {

    init {
        if(isViewAttached()){
            view.addOnAttachStateChangeListener(this)
        }else{
            cancel()
        }

        invokeOnCompletion {
            view.post{
                view.removeOnAttachStateChangeListener(this)
            }
        }
    }

    override fun onViewDetachedFromWindow(v: View?) = Unit
    override fun onViewAttachedToWindow(v: View?) {
        cancel()
        view.removeOnAttachStateChangeListener(this)
    }

    private fun isViewAttached(): Boolean {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT &&
            view.isAttachedToWindow || view.windowToken != null
    }
}