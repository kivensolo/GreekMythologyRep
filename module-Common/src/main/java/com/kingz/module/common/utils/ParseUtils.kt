package com.kingz.module.common.utils

import android.text.TextUtils

/**
 * author：ZekeWang
 * date：2021/6/2
 * description：
 * 转换工具类
 */
object ParseUtils{
    // <editor-fold defaultstate="collapsed" desc="基础数据类型之间的转换">
    fun tryParseFloat(`val`: String, defVal: Float): Float {
        if (TextUtils.isEmpty(`val`)) {
            return defVal
        }
        try {
            return `val`.toFloat()
        } catch (e: Exception) {
        }
        return defVal
    }

    fun tryParseDouble(`val`: String, defVal: Double): Double {
        if (TextUtils.isEmpty(`val`)) {
            return defVal
        }
        try {
            return `val`.toDouble()
        } catch (e: java.lang.Exception) {
        }
        return defVal
    }

    fun tryParseHex(`val`: String, defVal: Long): Long {
        if (TextUtils.isEmpty(`val`)) {
            return defVal
        }
        try {
            return `val`.toLong(16)
        } catch (e: java.lang.Exception) {
        }
        return defVal
    }

    fun tryParseLong(`val`: String, defVal: Long): Long {
        if (TextUtils.isEmpty(`val`)) {
            return defVal
        }
        try {
            return `val`.toLong()
        } catch (e: java.lang.Exception) {
        }
        return defVal
    }
    fun tryParseInt(`val`: String, defVal: Int): Int {
        if (TextUtils.isEmpty(`val`)) {
            return defVal
        }
        try {
            return `val`.toInt()
        } catch (e: java.lang.Exception) {
        }
        return defVal
    }

    fun tryParseInt(`val`: String): Int {
        return tryParseInt(`val`, 0)
    }

    fun tryParseLong(`val`: String): Long {
        return tryParseLong(`val`, 0)
    }

    // </editor-fold>

// <editor-fold defaultstate="collapsed" desc="精度提升转换">

    fun roundToInt(`val`: Double): Int {
        return if (`val` >= 0) {
            (`val` + 0.5).toInt()
        } else {
            (`val` - 0.5).toInt()
        }
    }

    fun roundToInt(`val`: Float): Int {
        return if (`val` >= 0) {
            (`val` + 0.5f).toInt()
        } else {
            (`val` - 0.5f).toInt()
        }
    }
// </editor-fold>
}