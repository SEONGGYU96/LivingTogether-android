package com.seoultech.livingtogether_android.util

import java.lang.StringBuilder

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
}