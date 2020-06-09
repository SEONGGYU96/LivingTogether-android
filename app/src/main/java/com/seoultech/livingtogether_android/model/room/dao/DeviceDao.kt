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

    @Insert
    fun insert(deviceEntity: DeviceEntity)

    @Update
    fun update(deviceEntity: DeviceEntity)

    @Delete
    fun delete(deviceEntity: DeviceEntity)
}