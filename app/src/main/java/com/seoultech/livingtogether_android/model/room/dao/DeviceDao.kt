package com.seoultech.livingtogether_android.model.room.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.seoultech.livingtogether_android.model.room.entity.DeviceEntity

@Dao
interface DeviceDao {
    @Query("SELECT * FROM device_entity")
    fun getAllObservable(): LiveData<List<DeviceEntity>>

    @Query("SELECT * FROM device_entity")
    fun getAll(): List<DeviceEntity>

    @Query("SELECT * FROM device_entity WHERE device_major = :major")
    fun getAll(major: String): DeviceEntity

    @Query("SELECT DISTINCT device_address FROM device_entity")
    fun getAllDeviceAddress(): List<String>

    @Query(" SELECT * FROM device_entity ORDER BY last_detection_type_one DESC LIMIT 1")
    fun getMostRecentDevice(): DeviceEntity?

    @Insert
    fun insert(deviceEntity: DeviceEntity)

    @Update
    fun update(deviceEntity: DeviceEntity)

    @Delete
    fun delete(deviceEntity: DeviceEntity)

    @Query("DELETE FROM device_entity")
    fun deleteAll()
}