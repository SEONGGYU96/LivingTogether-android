package com.seoultech.livingtogether_android.user.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "profile")
data class Profile(
    @ColumnInfo(name = "name")
    var name: String,

    @ColumnInfo(name = "address")
    var address: String,

    @PrimaryKey
    @ColumnInfo(name = "phone_num")
    var phoneNum: String
)