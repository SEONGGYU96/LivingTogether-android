package com.seoultech.livingtogether_android.model.room.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.seoultech.livingtogether_android.service.Signal
import java.util.*

@Entity(tableName = "signal_history_entity")
data class SignalHistoryEntity(
    @ColumnInfo(name = "device_major") var deviceMajor: String,
    @ColumnInfo(name = "signal_type") var type: Signal,
    @ColumnInfo(name = "detective_time") var detectiveTime: Long
) {
    @PrimaryKey(autoGenerate = true) var id:Int = 0

    fun getDetectedTimeToString() : String {
        val calendar = GregorianCalendar()
        calendar.timeInMillis = detectiveTime

        return "${calendar.get(Calendar.YEAR)}/${calendar.get(Calendar.MONTH)}/${calendar.get(
            Calendar.DAY_OF_MONTH)}  ${calendar.get(Calendar.HOUR_OF_DAY)} : ${calendar.get(Calendar.MINUTE)}"
    }
}