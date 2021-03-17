package com.zeke.utils

/**
 * author：ZekeWang
 * date：2021/3/17
 * description：调用native方法的图片工具类
 */
object BitmapUtils {
    init {
         System.loadLibrary("ZekeBitMap")
    }

    //------- 测试方法
    external fun native_get_string(): String

    @Suppress("FunctionName")
    external fun _native_intFromJNI(): Int
}
