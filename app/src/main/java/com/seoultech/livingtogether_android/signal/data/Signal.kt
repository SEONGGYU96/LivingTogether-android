package com.seoultech.livingtogether_android.signal.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.seoultech.livingtogether_android.util.StringUtil

@Entity(tableName = "signal")
data class Signal(
    @ColumnInfo(name = "device_address") var deviceAddress: String = "",
    @ColumnInfo(name = "signal_type") var type: Int = -1,
    @PrimaryKey
    @ColumnInfo(name = "detective_time") var detectiveTime: Long = 0
) {
    fun getDetectedTimeToString() : String {
        return StringUtil.longToDate(detectiveTime, year = true, time = true)
    }

    fun getSignalType(): Signal {
        return when (type) {
            1 -> Signal.ACTION
            2 -> Signal.PRESERVE
            3 -> Signal.RESIST
            else -> Signal.RESIST
        }
    }

    enum class Signal {
        ACTION, PRESERVE, RESIST
    }
}