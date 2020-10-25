package com.seoultech.livingtogether_android

import org.junit.Test

import org.junit.Assert.*

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
    fun representLastDetectedTimeTest() {
        assertEquals(8, getLastDetectedTimeToMinuet(31415213L))
        assertEquals(40, getLastDetectedTimeToMinuet(2453265L))
        assertEquals(15, getLastDetectedTimeToMinuet(54757757L))
        assertEquals(1, getLastDetectedTimeToMinuet(86400000L))
        assertEquals(2, getLastDetectedTimeToMinuet(172800000L))
        assertEquals(8, getLastDetectedTimeToMinuet(691200000L))
        assertEquals(9, getLastDetectedTimeToMinuet(777600000L))
        assertEquals(8, getLastDetectedTimeToMinuet(777457757L))
        assertEquals(89, getLastDetectedTimeToMinuet(7774577574L))

    }

    private fun getLastDetectedTimeToMinuet(timeGap: Long) : Long {
        return when {
            timeGap < 3600000 -> {
                ((timeGap / (1000 * 60)))
            }
            timeGap < 86400000 -> {
                ((timeGap / (1000 * 60 * 60 )))
            }
            else -> {
                ((timeGap / (1000 * 60 * 60 * 24)))
            }
        }
    }
}
