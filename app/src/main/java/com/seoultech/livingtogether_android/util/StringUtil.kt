package com.seoultech.livingtogether_android.util

import java.lang.StringBuilder
import java.util.*

object StringUtil {

    fun removeDash(str: String): String {
        val result = StringBuilder()

        if (str.contains("-")) {
            for (num in str.split("-")) {
                result.append(num)
            }
        } else {
            result.append(str)
        }

        return result.toString()
    }

    fun longToDate(timeInMillis: Long, year: Boolean, time: Boolean): String {
        val calendar = GregorianCalendar()
        val str = StringBuilder()

        calendar.timeInMillis = timeInMillis

        if (year) {
            str.makeFullDate(calendar.get(Calendar.YEAR).toString().substring(2))
                .append("-")
        }

        str.makeFullDate(calendar.get(Calendar.MONTH) + 1)
            .append("-")
            .makeFullDate(calendar.get(Calendar.DAY_OF_MONTH))

        if (time) {
            str.append(" ")
                .makeFullDate(calendar.get(Calendar.HOUR_OF_DAY))
                .append(":")
                .makeFullDate(calendar.get(Calendar.MINUTE))
        }

        return str.toString()
    }

    private fun StringBuilder.makeFullDate(date: Int): StringBuilder {
        if (date < 10) {
            this.append("0")
        }
        this.append(date)
        return this
    }

    private fun StringBuilder.makeFullDate(date: String): StringBuilder {
        return this.makeFullDate(Integer.parseInt(date))
    }
}