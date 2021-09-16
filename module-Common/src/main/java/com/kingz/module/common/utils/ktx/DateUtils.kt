package com.kingz.module.common.utils.ktx

import java.text.SimpleDateFormat
import java.util.*

private val weekDays = arrayListOf(
    "星期天", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"
)

// <editor-fold defaultstate="collapsed" desc="简单日期格式化定义">
/** 年月日 */
val sdfDate: SimpleDateFormat
    get() = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

/** 年-月-日 时:分:秒 */
val sdfDateWithTime: SimpleDateFormat
    get() = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())

/** 年月日时分秒 */
val sdfCurrentTimeValue: SimpleDateFormat
    get() = SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault())

// </editor-fold>

// <editor-fold defaultstate="collapsed" desc="Date扩展属性&函数">

val Date.formatDate: String
    get() = sdfDate.format(time)

val Date.formatDateWithTime: String
    get() = sdfDateWithTime.format(time)

val Date.getCurrentTimeValue: String
    get() = sdfCurrentTimeValue.format(time)

/**
 * 获取当前时间对应指定格式的字符串
 */
fun Date.getTimeFormatString(pattern: String): String {
    val sdfDate = SimpleDateFormat(pattern, Locale.getDefault())
    return sdfDate.format(time)
}
// </editor-fold>

/**
 * N天前
 */
val Int.daysAgo: String
    get() = Calendar.getInstance().run {
        this.add(Calendar.DAY_OF_YEAR, -this@daysAgo)
        sdfDate.format(this.timeInMillis)
    }

/**
 * N天后
 */
val Int.daysLater: String
    get() = Calendar.getInstance().run {
        this.add(Calendar.DAY_OF_YEAR, +this@daysLater)
        sdfDate.format(this.timeInMillis)
    }

/**
 * N周前
 */
val Int.weekAgo: String
    get() = Calendar.getInstance().run {
        this.add(Calendar.WEEK_OF_YEAR, -this@weekAgo)
        sdfDate.format(this.timeInMillis)
    }

/**
 * N周后
 */
val Int.weekLater: String
    get() = Calendar.getInstance().run {
        this.add(Calendar.WEEK_OF_YEAR, +this@weekLater)
        sdfDate.format(this.timeInMillis)
    }

/**
 * N月前
 */
val Int.monthAgo: String
    get() = Calendar.getInstance().run {
        this.add(Calendar.MONTH, -this@monthAgo)
        sdfDate.format(this.timeInMillis)
    }

/**
 * N月后
 */
val Int.monthLater: String
    get() = Calendar.getInstance().run {
        this.add(Calendar.MONTH, +this@monthLater)
        sdfDate.format(this.timeInMillis)
    }

/**
 * N年前
 */
val Int.yearAgo: String
    get() = Calendar.getInstance().run {
        add(Calendar.YEAR, -this@yearAgo)
        sdfDate.format(timeInMillis)
    }

/**
 * N年后
 */
val Int.yearLater: String
    get() = Calendar.getInstance().run {
        this.add(Calendar.YEAR, +this@yearLater)
        sdfDate.format(this.timeInMillis)
    }

/**
 * 日期工具类
 */
object DateUtils {
    /**
     * 通过出生日期计算年龄
     * @param year  出生年份
     * @param month 出生月份
     * @param day   出生日期
     * @return Age  年龄
     */
    fun caculateAge(year: Int, month: Int, day: Int): Int {
        val date = sdfDate.format(Date())
        val splitDate = date.split("-").toTypedArray()
        val currentYear = splitDate[0].toInt()
        val currentMonth = splitDate[1].toInt()
        val currentDay = splitDate[2].toInt()
        return if (month < currentMonth) {
            currentYear - year
        } else if (month > currentMonth) {
            currentYear - year - 1
        } else {
            if (day < currentDay) currentYear - year else currentYear - year - 1
        }
    }

    /**
     * 两个日期是否相同
     * @param time1 被比较日期1
     * @param time2 被比较日期2
     */
    fun isSameDay(time1: Date, time2: Date): Boolean {
        val tmp1 = Calendar.getInstance()
        tmp1.time = time1
        val tmp2 = Calendar.getInstance()
        tmp2.time = time2
        return tmp1[Calendar.YEAR] == tmp2[Calendar.YEAR] &&
                tmp1[Calendar.MONTH] == tmp2[Calendar.MONTH] &&
                tmp1[Calendar.DATE] == tmp2[Calendar.DATE]
    }

    /**
     * 两个日期月份是否相同
     * @param time1 被比较日期1的时间戳
     * @param time2 被比较日期2的时间戳
     */
    fun isSameMonth(time1: Long, time2: Long): Boolean {
        val tmp1 = Calendar.getInstance()
        tmp1.timeInMillis = time1
        val tmp2 = Calendar.getInstance()
        tmp2.timeInMillis = time2
        return (tmp1[Calendar.YEAR] == tmp2[Calendar.YEAR] &&
                tmp1[Calendar.MONTH] == tmp2[Calendar.MONTH])
    }


    /**
     * 判断目标日期是否是周末，即周六、日
     *
     * @param date 目标日期
     * @return true|false
     */
    fun isWeekend(date: Date): Boolean {
        val week: Int = getWeekFormCalendar(date)
        return week == 0 || week == 6
    }

    /**
     * 获取某个日期是星期几
     * 测试通过
     *
     * @param date 目标日期
     * @return 返回某个日期是星期几
     *      MONDAY~SATURDAY : 1~6
     *      SUNDAY: 0
     */
    fun getWeekFormCalendar(date: Date): Int {
        val calendar = Calendar.getInstance()
        calendar.time = date
        return calendar[Calendar.DAY_OF_WEEK] - 1
    }

    /**
     * 是否是闰年
     *
     * @param year 年份
     * @return True|Flase
     */
    fun isLeapYear(year: Int): Boolean {
        return year % 4 == 0 && year % 100 != 0 || year % 400 == 0
    }

    /**
     * 获取某年某月的天数
     *
     * @param year  年
     * @param month 月
     * @return 天数
     */
    fun getMonthDaysCount(year: Int, month: Int): Int {
        var count = 0
        when (month) {
            //判断大月份
            1, 3, 5, 7, 8, 10, 12 -> {
                count = 31
            }
            //判断小月
            4, 6, 9, 11 -> {
                count = 30
            }
            //判断平年与闰年
            2 -> {
                count = if (isLeapYear(year)) {
                    29
                } else {
                    28
                }
            }
        }
        return count
    }


    /**
     * 获取某天在该月的第几周, 根据周起始动态获取
     * @param date 目标日期
     * @param weekStart 周起始，可选：
     *  [Calendar.SUNDAY] and [Calendar.MONDAY] and [Calendar.SATURDAY] .....
     *
     * @return Number of week
     */
    fun getWeekFromDayInMonth(date: Date, weekStart: Int): Int {
        val calendar = Calendar.getInstance()
        calendar.time = date
        val diff: Int = getDayOffsetInMonth(calendar, weekStart)
        return (calendar.get(Calendar.DAY_OF_MONTH) + diff - 1) / 7 + 1
    }

    /**
     * 获取日期所在月份，第1天对应的起始偏移量
     * （也就是月示例图，前面会显示xx天上个月的日期）
     *
     * @param cal 日期数据
     * @param weekStart 周起始标准(周日为1，周六为7)
     * @return 月视图，起始偏移量
     */
    fun getDayOffsetInMonth(cal: Calendar, weekStart: Int): Int {
        //日期所在的星期index  1~7(周日~周六)
        val calendar = Calendar.getInstance()
        calendar.set(cal.get(Calendar.YEAR),cal.get(Calendar.MONTH),1,12,0,0)
        val weekIndex: Int = calendar.get(Calendar.DAY_OF_WEEK)
        return when (weekStart) {
            Calendar.SUNDAY -> weekIndex - 1
            Calendar.MONDAY -> {
                if (weekIndex == Calendar.SUNDAY) { 6  //偏移6天(前6天是上个月的)
                } else {
                    weekIndex - Calendar.MONDAY        //偏移0~5
                }
            }
            Calendar.SATURDAY -> 0
            else -> weekIndex
        }
    }
}

