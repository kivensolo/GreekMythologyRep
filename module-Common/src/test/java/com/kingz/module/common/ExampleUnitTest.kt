package com.kingz.module.common

import com.kingz.module.common.utils.DateUtils
import com.kingz.module.common.utils.HexUtil
import org.junit.Assert.assertEquals
import org.junit.Test
import java.util.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }


    @Test
    fun getWeekFromDay(){
        //9月12号  星期天
        val date = Date(2021 - 1900,8,12)
        val weekFromDayInMonth = DateUtils.getWeekFromDayInMonth(date, Calendar.MONDAY)
        println("指定日期在当月的第几周=$weekFromDayInMonth")
        assertEquals(2, weekFromDayInMonth)

        val value = DateUtils.getWeekFromDayInMonth(date, Calendar.SUNDAY)
        assertEquals(3, value)

        val date2 = Date(2021 - 1900,8,13)
        val value2 = DateUtils.getWeekFromDayInMonth(date2, Calendar.MONDAY)
        assertEquals(3, value2)

        val date3 = Date(2021 - 1900,8,19)
        val value3 = DateUtils.getWeekFromDayInMonth(date3, Calendar.SUNDAY)
        assertEquals(4,value3)

    }

    /**
     * 字节数值转为十六进制
     * byte-bits: 8 最高位符号位
     * range: [-128,127]  可以表示256个数
     *
     * [基 础 复 习]
     * 原码 ：二进制定点表示法，即最高位为符号位，“0”表示正，“1”表示负，其余位表示数值的大小。
     * 反码 ：正数的反码与其原码相同；负数的反码是对其原码逐位取反，但符号位除外。
     * 补码 ：正数的补码与其原码相同；负数的补码是在其反码的末位加1(负数的补码 = 反码 + 1)。
     *
     * 补码表示形式：
     * 10000000(0x80) : -0 == -128  最小负数
     *
     *            1000 0001(补 0x81) <---> 1000 0000(反) <---> 1111 1111(原)  -127
     *            -127 + (-1) = -128 = 1000 0000(补)  == -0
     *
     * 在内存中都是使用补码来进行计算的（整数类型）！
     *
     * 11111111(0xff) ：-1  最大负数  11111111(补) <--- 1111 1110(反) <--- 1000 0001 (原)
     * 00000000(0x00) : 0
     * 01111111(0x7f) : 127 最大正数值
     */
    @Test
    fun bytesToHex_test(){
        val encodeHex = HexUtil.encodeHex(byteArrayOf(-128,-127, -2, -1, 0, 1, 127, 128.toByte(), 254.toByte(), 255.toByte(), 256.toByte()))
        println(String(encodeHex!!))
        System.out.println(encodeHex)
    }
}
