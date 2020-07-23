package com.seoultech.livingtogether_android.device.repository

import android.util.Log
import androidx.lifecycle.LiveData
import com.seoultech.livingtogether_android.ApplicationImpl
import com.seoultech.livingtogether_android.base.BaseRepository
import com.seoultech.livingtogether_android.device.model.DeviceEntity
import com.seoultech.livingtogether_android.device.dao.DeviceDao

class DeviceRepository : BaseRepository<DeviceEntity>() {

    private val dao: DeviceDao by lazy { ApplicationImpl.db.deviceDao() }

    private val observableDevice: LiveData<List<DeviceEntity>> by lazy { dao.getAllObservable() }

    private val devices: List<DeviceEntity> by lazy { dao.getAll() }

    fun getAllObservable(): LiveData<List<DeviceEntity>> {
        Log.d(TAG, "getAllObservable : ${observableDevice.value}")
        return observableDevice
    }

    fun getAll(): List<DeviceEntity> {
        Log.d(TAG, "getAll : $devices")
        return devices
    }

    fun getAll(deviceAddress: String): DeviceEntity? {
        val result = dao.getAll(deviceAddress)
        Log.d(TAG, "getAll : $result")
        return result
    }

    fun getAllDeviceAddress(): List<String> {
        val result = dao.getAllDeviceAddress()
        Log.d(TAG, "getAllDeviceAddress : $result")
        return result
    }

    fun getMostRecentDevice(): DeviceEntity? {
        val result = dao.getMostRecentDevice()
        Log.d(TAG, "getMostResentDevice : $result")
        return result
    }

    fun deleteAll() {
        Log.d(TAG, "deleteAll")
        dao.deleteAll()
    }

    public override fun insert(entity: DeviceEntity) {
        super.insert(entity)
        dao.insert(entity)
    }

    public override fun delete(entity: DeviceEntity) {
        super.delete(entity)
        dao.delete(entity)
    }

    public override fun update(entity: DeviceEntity) {
        super.update(entity)
        dao.update(entity)
    }
}