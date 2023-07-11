package com.zeke.utils

import java.nio.Buffer

/**
 * author：ZekeWang
 * date：2021/3/17
 * description：调用native方法的图片工具类
 */
object MyNativeUtils {
    init {
         System.loadLibrary("WildFireLib")
    }

    //------- 测试方法
    external fun native_get_Hello(): String

    @Suppress("FunctionName")
    external fun _native_intFromJNI(): Int

//     public native int[] modifyArrayValue(int[] array);
    external fun modifyArrayValue(array: IntArray?): IntArray?

//    external fun modifyArrayValue(array:Int):Int[]
    external fun doBlur(buf: Buffer,width:Int, height:Int, pass:Int):Boolean
}
