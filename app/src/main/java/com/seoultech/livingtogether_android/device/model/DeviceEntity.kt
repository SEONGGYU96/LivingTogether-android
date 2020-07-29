package com.seoultech.livingtogether_android.device.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.seoultech.livingtogether_android.user.model.UserEntity
import java.lang.StringBuilder
import java.util.*


@Entity(
    tableName = "device_entity",
    inheritSuperIndices = false
)
data class DeviceEntity(

    @ColumnInfo(name = "device_type") var deviceType: String = "",

    @ColumnInfo(name = "device_address") var deviceAddress: String,

    @ColumnInfo(name = "location") var location: String?,

    @ColumnInfo(name = "init_date") var initDate: Long,

    @ColumnInfo(name = "last_detection_type_one") var lastDetectionOfActionSignal: Long,

    @ColumnInfo(name = "last_detection_type_two") var lastDetectionOfPreserveSignal: Long,

    @ColumnInfo(name = "is_available") var isAvailable: Boolean = false
) {
    @PrimaryKey(autoGenerate = true) var deviceId: Int = 0

    @ColumnInfo(name = "user_id")
    @ForeignKey(
        entity = UserEntity::class,
        parentColumns = ["Id"],
        childColumns = ["user_id"],
        onDelete = ForeignKey.CASCADE

    ) var userId: Int = 0

    companion object {
        //preserve 갭 6시간
        private const val PRESERVE_GAP = 21600000
    }

    fun getLastDetectedTimeToMinuet() : String {
        val timeGap = GregorianCalendar().timeInMillis - lastDetectionOfActionSignal
        val string = StringBuilder()

        when {
            timeGap < 3600000 -> {
                string.append(timeGap / (1000 * 60))
                string.append("분 전")
            }
            timeGap < 86400000 -> {
                string.append(timeGap / (1000 * 60 * 60 ))
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

    private fun getTimeToString(timeInMillis: Long) : String {
        val calendar = GregorianCalendar()
        val str = StringBuilder()

        calendar.timeInMillis = timeInMillis

        str.append(calendar.get(Calendar.MONTH) + 1)
            .append("-")
            .append(calendar.get(Calendar.DAY_OF_MONTH))
            .append(" ")
            .append(calendar.get(Calendar.HOUR_OF_DAY))
            .append(":")
            .append(calendar.get(Calendar.MINUTE))

        return str.toString()
    }

    fun getInitDateToString(): String {
        val calendar = GregorianCalendar()
        val str = StringBuilder()

        calendar.timeInMillis = initDate

        str.append(calendar.get(Calendar.YEAR).toString().substring(2))
            .append("-")
            .append(calendar.get(Calendar.MONTH) + 1)
            .append("-")
            .append(calendar.get(Calendar.DAY_OF_MONTH))

        return str.toString()
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
}