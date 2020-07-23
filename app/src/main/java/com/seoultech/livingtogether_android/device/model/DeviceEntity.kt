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

    @ColumnInfo(name = "init_date") var initDate: Long?,

    @ColumnInfo(name = "last_detection_type_one") var lastDetectionOfActionSignal: Long,

    @ColumnInfo(name = "last_detection_type_two") var lastDetectionOfPreserveSignal: Long?,

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

    fun getLastDetectedTypeTwoToString() : String {
        return if (lastDetectionOfPreserveSignal == null) {
            "null"
        } else {
            getLastDetectedSignalToString(2)
        }
    }

    fun getLastDetectedTypeOneToString() : String {
        return if (lastDetectionOfActionSignal == null) {
            "null"
        } else {
            getLastDetectedSignalToString(1)
        }
    }

    private fun getLastDetectedSignalToString(type: Int) : String {
        return if (type == 1) {
            getTimeToString(lastDetectionOfActionSignal!!)
        } else {
            getTimeToString(lastDetectionOfPreserveSignal!!)
        }
    }

    private fun getTimeToString(timeInMillis: Long) : String {
        val calendar = GregorianCalendar()
        calendar.timeInMillis = timeInMillis
        return "${calendar.get(Calendar.YEAR)}/${calendar.get(Calendar.MONTH)}/${calendar.get(Calendar.DAY_OF_MONTH)}" +
                " ${calendar.get(Calendar.HOUR_OF_DAY)} : ${calendar.get(Calendar.MINUTE)}"
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
}