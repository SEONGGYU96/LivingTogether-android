package com.seoultech.livingtogether_android.device.data.source

import androidx.room.*
import com.seoultech.livingtogether_android.base.BaseDao
import com.seoultech.livingtogether_android.device.data.Device

@Dao
abstract class DeviceDao : BaseDao<Device> {

    @Query("SELECT * FROM device")
    abstract fun getDevices(): List<Device>

    @Query("SELECT * FROM device WHERE device_address = :deviceAddress")
    abstract fun getDevice(deviceAddress: String): Device?

    @Query("SELECT DISTINCT device_address FROM device")
    abstract fun getAddressesOfDevices(): List<String>

    @Query(" SELECT * FROM device ORDER BY last_detection_type_one DESC LIMIT 1")
    abstract fun getLatestDevice(): Device?

    @Query("DELETE FROM device")
    abstract fun deleteAll()

    @Query("DELETE FROM device WHERE device_address = :deviceAddress")
    abstract fun deleteDeviceByAddress(deviceAddress: String)
}