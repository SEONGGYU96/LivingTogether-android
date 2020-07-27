package com.seoultech.livingtogether_android.nok.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "nok_entity")
data class NOKEntity (

    @ColumnInfo(name = "nok_name")
    var name: String = "",

    @PrimaryKey
    @ColumnInfo(name = "nok_phone_num")
    var phoneNum: String = ""

)