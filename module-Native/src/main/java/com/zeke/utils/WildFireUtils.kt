package com.zeke.utils

import java.nio.Buffer

/**
 * author：ZekeWang
 * date：2022/7/17
 * description：native方法的工具类
 */
object WildFireUtils {
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
    /**
     * 进行图片模糊
     * @param buf 图片数据buffer, 只能接受直接缓冲区的类型
     * @param width 图片像素宽度
     * @param height 图片像素高度
     * @param pass 模糊次数
     */
    external fun doBlur(buf: Buffer,width:Int, height:Int, pass:Int):Boolean

    external fun doFastBlur(buf: Buffer,width:Int, height:Int, radius:Int):Boolean
}
