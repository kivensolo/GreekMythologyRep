package com.kingz.module.common.utils.ktx

import android.os.Build
import java.io.BufferedReader
import java.io.FileReader

/**
 * @author zeke.wang
 * @date 2020/7/15
 * @maintainer zeke.wang
 * @desc: 系统版本工具类
 */
class VersionUtils {
    fun isLowVersion(): Boolean {
        return Build.VERSION.SDK_INT < 21
    }

    fun isLowAndroid_6_0(): Boolean {
        return Build.VERSION.SDK_INT < 23
    }

    fun isMoreAndroid_6_0(): Boolean {
        return Build.VERSION.SDK_INT >= 23
    }

    fun isLowVersionOrRam(): Boolean {
        return isLowVersion() || getTotalRam() < 2.5f
    }

    fun isLowRaw(): Boolean {
        return getTotalRam() < 1.2f
    }

    private fun getTotalRam(): Float {
        val path = "/proc/meminfo"
        var firstLine: String? = null
        var totalRam = 0.0f
        try {
            val fileReader = FileReader(path)
            val br = BufferedReader(fileReader, 8 * 1024)
            firstLine = br.readLine().split("\\s+").toTypedArray()[1]
            br.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        if (firstLine != null) {
            totalRam = java.lang.Float.valueOf(firstLine) / 1048576.0f
        }
        return totalRam
    }
}