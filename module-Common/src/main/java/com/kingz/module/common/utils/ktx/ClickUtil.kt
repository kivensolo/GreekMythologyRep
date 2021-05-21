package com.kingz.module.common.utils.ktx

import android.view.View

var lastTime = 0L

/**
 * 控制控件 500ms内相应一次的扩展函数
 */
inline fun View.setSingleClick(crossinline onclick: (v: View?) -> Unit) {
    this.setOnClickListener {
        val currentTime = System.currentTimeMillis()
        if (currentTime - lastTime > 500) {
            onclick.invoke(it)
        }
        lastTime = currentTime
    }
}