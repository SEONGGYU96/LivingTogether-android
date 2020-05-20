package com.seoultech.livingtogether_android.model.room.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "nok_entity")
data class NOKEntity (

    @ColumnInfo(name = "nok_name")
    var name: String = "",

    @ColumnInfo(name = "nok_phone_num")
    var phoneNum: String = ""

    ) {
    @PrimaryKey(autoGenerate = true)
    var nokId: Int = 0
}