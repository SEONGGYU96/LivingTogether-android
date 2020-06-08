package com.seoultech.livingtogether_android.model.room.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "signal_history_entity")
data class SignalHistoryEntity(
    @ColumnInfo(name = "device_major") var deviceMajor: String,
    @ColumnInfo(name = "signal_type") var type: Int,
    @ColumnInfo(name = "detective_time") var detectiveTime: Long
) {
    @PrimaryKey(autoGenerate = true) var id:Int = 0
}