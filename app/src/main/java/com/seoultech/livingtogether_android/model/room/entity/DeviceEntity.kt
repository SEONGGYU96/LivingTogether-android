package com.seoultech.livingtogether_android.model.room.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey


@Entity(
    tableName = "device_entity",
    inheritSuperIndices = false
)
data class DeviceEntity(
    @PrimaryKey(autoGenerate = true) var deviceId: Int = 0,

    @ForeignKey(
        entity = UserEntity::class,
        parentColumns = ["user_id"],
        childColumns = ["device_foreign_key"],
        onDelete = ForeignKey.CASCADE

    ) val deviceForeignKey: Int = 0,

    @ColumnInfo(name = "device_type") var deviceType: String = "",

    @ColumnInfo(name = "location") var location: String = "",

    //TODO: Date에 관련한 Model과 Util 클래스 생성하여 문자열을 대체하기
    @ColumnInfo(name = "init_date") var initDate: String = "",

    @ColumnInfo(name = "last_detection_type_one") var lastDetectionTypeOne: String = "",

    @ColumnInfo(name = "last_detection_type_two") var lastDetectionTypeTwo: String = "",

    @ColumnInfo(name = "is_available") var isAvailable: Boolean = false
)