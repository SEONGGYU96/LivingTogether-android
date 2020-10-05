package com.seoultech.livingtogether_android.user.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "profile")
data class Profile constructor(
    @ColumnInfo(name = "name")
    var name: String = "",

    @ColumnInfo(name = "province")
    var province: String = "",

    @ColumnInfo(name = "city")
    var city: String = "",

    @ColumnInfo(name = "middle_address")
    var middleAddress: String = "",

    @ColumnInfo(name = "extra_address")
    var extraAddress: String = "",

    @ColumnInfo(name = "phone_num")
    var phoneNumber: String = "",

    @PrimaryKey
    @ColumnInfo(name = "profile_id")
    var id: String = UUID.randomUUID().toString()
)