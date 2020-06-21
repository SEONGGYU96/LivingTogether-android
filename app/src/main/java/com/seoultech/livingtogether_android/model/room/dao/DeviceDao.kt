package com.seoultech.livingtogether_android.model.room.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.seoultech.livingtogether_android.base.BaseDao
import com.seoultech.livingtogether_android.model.room.entity.DeviceEntity

@Dao
abstract class DeviceDao : BaseDao<DeviceEntity> {
    @Query("SELECT * FROM device_entity")
    abstract fun getAllObservable(): LiveData<List<DeviceEntity>>

    @Query("SELECT * FROM device_entity")
    abstract fun getAll(): List<DeviceEntity>

    @Query("SELECT * FROM device_entity WHERE device_major = :major")
    abstract fun getAll(major: String): DeviceEntity

    @Query("SELECT DISTINCT device_address FROM device_entity")
    abstract fun getAllDeviceAddress(): List<String>

    @Query(" SELECT * FROM device_entity ORDER BY last_detection_type_one DESC LIMIT 1")
    abstract fun getMostRecentDevice(): DeviceEntity?

    @Query("DELETE FROM device_entity")
    abstract fun deleteAll()
}