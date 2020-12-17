package com.zeke.ktx.modules.aac.sharedvm

import android.util.Log

fun Any?.log() {
    Log.d("KingZ", this?.toString() ?: "null")
}