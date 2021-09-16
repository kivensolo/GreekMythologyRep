package com.kingz.module.common

import com.kingz.module.common.utils.ktx.DateUtils
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
}
