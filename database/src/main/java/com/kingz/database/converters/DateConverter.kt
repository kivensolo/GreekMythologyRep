package com.kingz.database.converters

import androidx.room.TypeConverter
import java.text.SimpleDateFormat
import java.util.*

class DateConverter {
    private val spf = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH)

    @TypeConverter
    fun stringToObject(value: String): Date {
        return spf.parse(value)
    }

    @TypeConverter
    fun objectToString(date: Date?): String {
        return spf.format(date)
    }
}