package com.kingz.utils.ktx

import java.util.*

/**
 * 根据毫秒返回时分秒
 */
fun formatDuration(duration: Long): String {
    var time = duration / 1000
    if (time == 0L && duration > 0) {
        time = 1
    }

    val second = time % 60
    var min = time / 60
    var hour: Long = 0
    if (min > 60) {
        hour = (time / 3600)
        min = time / 60 % 60
    }
    return if (hour > 0)
        String.format("%02d:%02d:%02d", hour, min, second)
    else
        String.format("00:%02d:%02d", min, second)
}

fun getSystemTime(): String {
    val c = Calendar.getInstance()
    var time: String
    val h = c.get(Calendar.HOUR_OF_DAY)
    time = "$h:"
    val m = c.get(Calendar.MINUTE)
    time = if (m < 10) time + "0" + m + ":" else "$time$m:"
    val s = c.get(Calendar.SECOND)
    time = if (s < 10) time + "0" + s else time + s
    return time
}