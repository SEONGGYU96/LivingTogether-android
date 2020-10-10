package com.seoultech.livingtogether_android.device.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.google.firebase.database.Exclude
import com.google.firebase.database.IgnoreExtraProperties
import com.seoultech.livingtogether_android.util.StringUtil
import java.lang.StringBuilder
import java.util.*


@Entity(
    tableName = "device",
    inheritSuperIndices = false
)
@IgnoreExtraProperties
data class Device(
    @Exclude
    @ColumnInfo(name = "device_type")
    var deviceType: String = "",

    @Exclude
    @PrimaryKey
    @ColumnInfo(name = "device_address")
    var deviceAddress: String = "",

    @Exclude
    @ColumnInfo(name = "location")
    var location: String? = "",

    @Exclude
    @ColumnInfo(name = "init_date")
    var initDate: Long = 0,

    @Exclude
    @ColumnInfo(name = "last_detection_type_one")
    var lastDetectionOfActionSignal: Long = 0,

    @Exclude
    @ColumnInfo(name = "last_detection_type_two")
    var lastDetectionOfPreserveSignal: Long = 0,

    @Exclude
    @ColumnInfo(name = "is_available")
    var _isAvailable: Int = 1
) {

    var isAvailable: Boolean
        get() = _isAvailable == 1
        set(value) {
            _isAvailable = if (value) {
                1
            } else {
                0
            }
        }

    fun getLastDetectedTimeToMinuet(): String {
        val timeGap = GregorianCalendar().timeInMillis - lastDetectionOfActionSignal
        val string = StringBuilder()

        when {
            timeGap < 3600000 -> {
                string.append(timeGap / (1000 * 60))
                string.append("분 전")
            }
            timeGap < 86400000 -> {
                string.append(timeGap / (1000 * 60 * 60))
                string.append("시간 전")
            }
            else -> {
                string.append(timeGap / (1000 * 60 * 60 * 24))
                string.append("일 전")
            }
        }
        return string.toString()
    }

    fun getLastDetectedActiveTimeToString(): String {
        return getTimeToString(lastDetectionOfActionSignal)
    }

    fun getLastDetectedPreserveTimeToString(): String {
        return getTimeToString(lastDetectionOfPreserveSignal)
    }

    private fun getTimeToString(timeInMillis: Long): String {
        return StringUtil.longToDate(timeInMillis, year = false, time = true)
    }

    fun getInitDateToString(): String {
        return StringUtil.longToDate(initDate, year = true, time = false)
    }

    fun getDeviceAvailable(): String {
        return if (updateIsAvailable()) {
            "양호"
        } else {
            "불량"
        }
    }

    fun updateIsAvailable(): Boolean {
        val calendar = GregorianCalendar()
        val gap = calendar.timeInMillis - lastDetectionOfPreserveSignal
        if (isAvailable) {
            if (gap > PRESERVE_GAP) {
                isAvailable = false
            }
        } else {
            if (gap <= PRESERVE_GAP) {
                isAvailable = true
            }
        }
        return isAvailable
    }

    companion object {
        //preserve 갭 6시간
        //private const val PRESERVE_GAP = 21600000
        private const val PRESERVE_GAP = 130000 //2분10초
    }
}