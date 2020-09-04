package com.seoultech.livingtogether_android.nok.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "next_of_kin")
data class NextOfKin (
    @ColumnInfo(name = "name")
    var name: String,

    @PrimaryKey
    @ColumnInfo(name = "phone_number")
    var phoneNumber: String
)