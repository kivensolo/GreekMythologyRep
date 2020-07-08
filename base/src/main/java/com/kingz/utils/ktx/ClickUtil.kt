package com.kingz.utils.ktx

import android.view.View

var lastTime = 0L

inline fun View.setSingleClick(crossinline onclick: (v: View?) -> Unit) {
    this.setOnClickListener {
        val currentTime = System.currentTimeMillis()
        if (currentTime - lastTime > 500) {
            onclick.invoke(it)
        }
        lastTime = currentTime
    }
}