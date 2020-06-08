package com.seoultech.livingtogether_android.model.room.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.util.*


@Entity(
    tableName = "device_entity",
    inheritSuperIndices = false
)
data class DeviceEntity(

    @ColumnInfo(name = "device_type") var deviceType: String = "",

    @ColumnInfo(name = "device_major") var deviceMajor: String = "",

    @ColumnInfo(name = "location") var location: String?,

    //TODO: Date에 관련한 Model과 Util 클래스 생성하여 문자열을 대체하기
    @ColumnInfo(name = "init_date") var initDate: String?,

    @ColumnInfo(name = "last_detection_type_one") var lastDetectionTypeOne: Long?,

    @ColumnInfo(name = "last_detection_type_two") var lastDetectionTypeTwo: Long?,

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
        return if (lastDetectionTypeTwo == null) {
            "null"
        } else {
            getLastDetectedSignalToString(2)
        }
    }

    fun getLastDetectedTypeOneToString() : String {
        return if (lastDetectionTypeOne == null) {
            "null"
        } else {
            getLastDetectedSignalToString(1)
        }
    }

    private fun getLastDetectedSignalToString(type: Int) : String {
        return if (type == 1) {
            getTimeToString(lastDetectionTypeOne!!)
        } else {
            getTimeToString(lastDetectionTypeTwo!!)
        }
    }

    private fun getTimeToString(timeInMillis: Long) : String {
        val calendar = GregorianCalendar()
        calendar.timeInMillis = timeInMillis
        return "${calendar.get(Calendar.YEAR)}/${calendar.get(Calendar.MONTH)}/${calendar.get(Calendar.DAY_OF_MONTH)}" +
                " ${calendar.get(Calendar.HOUR_OF_DAY)} : ${calendar.get(Calendar.MINUTE)}"
    }
}