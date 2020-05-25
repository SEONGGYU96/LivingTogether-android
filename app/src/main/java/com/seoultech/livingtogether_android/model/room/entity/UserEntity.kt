package com.seoultech.livingtogether_android.model.room.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_entity")
data class UserEntity(
    @ColumnInfo(name = "name") var name: String = "",

    @ColumnInfo(name = "address") var address: String = "",

    @ColumnInfo(name = "phone_num") var phoneNum: String = "",

    //TODO: 지역구 Enum 클래스 만들기
    @ColumnInfo(name = "district") var district: String = "",

    @ColumnInfo(name = "last_location") var lastLocation: String = ""
) {
    @PrimaryKey(autoGenerate = true) var userId: Int = 0
}