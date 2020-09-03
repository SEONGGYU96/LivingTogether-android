package com.seoultech.livingtogether_android.signal

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "signal_history_entity")
data class SignalHistoryEntity(
    @ColumnInfo(name = "device_address") var deviceAddress: String,
    @ColumnInfo(name = "signal_type") var type: Int,
    @ColumnInfo(name = "detective_time") var detectiveTime: Long
) {
    @PrimaryKey(autoGenerate = true) var id:Int = 0

    fun getDetectedTimeToString() : String {
        val calendar = GregorianCalendar()
        calendar.timeInMillis = detectiveTime

        return "${calendar.get(Calendar.YEAR)}/${calendar.get(Calendar.MONTH)}/${calendar.get(
            Calendar.DAY_OF_MONTH)}  ${calendar.get(Calendar.HOUR_OF_DAY)} : ${calendar.get(Calendar.MINUTE)}"
    }

    fun getSignalType(): Signal {
        return when (type) {
            1 -> Signal.ACTION
            2 -> Signal.PRESERVE
            3 -> Signal.RESIST
            else -> Signal.RESIST
        }
    }
}