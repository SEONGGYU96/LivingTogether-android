package com.seoultech.livingtogether_android.model.room.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.seoultech.livingtogether_android.model.room.entity.DeviceEntity

@Dao
interface DeviceDao {
    @Query("SELECT * FROM device_entity")
    fun getAllObservable(): LiveData<List<DeviceEntity>>

    @Insert
    fun insert(deviceEntity: DeviceEntity)

    @Update
    fun update(deviceEntity: DeviceEntity)

    @Delete
    fun delete(deviceEntity: DeviceEntity)
}