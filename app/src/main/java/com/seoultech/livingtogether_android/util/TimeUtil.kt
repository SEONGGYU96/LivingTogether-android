package com.seoultech.livingtogether_android.util

import java.util.*

object TimeUtil {
    fun calculateTimeGapWithCurrent(timeInMillis: Long): Long {
        return GregorianCalendar().timeInMillis - timeInMillis
    }
}