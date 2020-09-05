package com.seoultech.livingtogether_android.device.data.source

import com.seoultech.livingtogether_android.device.data.Device
import com.seoultech.livingtogether_android.util.AppExecutors

class DeviceLocalDataSource private constructor(
    private val appExecutors: AppExecutors, private val deviceDao: DeviceDao) : DeviceDataSource {

    override fun getDevices(callback: DeviceDataSource.LoadDevicesCallback) {
        appExecutors.diskIO.execute {
            val devices = deviceDao.getDevices()
            appExecutors.mainThread.execute {
                if (devices.isEmpty()) {
                    callback.onDataNotAvailable()
                } else {
                    callback.onDevicesLoaded(devices)
                }
            }
        }
    }

    override fun getDevice(deviceAddress: String, callback: DeviceDataSource.GetDeviceCallback) {
        appExecutors.diskIO.execute {
            val device = deviceDao.getDevice(deviceAddress)
            appExecutors.mainThread.execute {
                if (device == null) {
                    callback.onDataNotAvailable()
                } else {
                    callback.onDeviceLoaded(device)
                }
            }
        }
    }

    override fun saveDevice(device: Device) {
        appExecutors.diskIO.execute {
            deviceDao.insert(device)
        }
    }

    override fun deleteDevice(deviceAddress: String) {
        appExecutors.diskIO.execute {
            deviceDao.deleteDeviceByAddress(deviceAddress)
        }
    }

    override fun updateDevice(device: Device) {
        appExecutors.diskIO.execute {
            deviceDao.update(device)
        }
    }

    companion object {
        private var INSTANCE: DeviceLocalDataSource? = null

        @JvmStatic
        fun getInstance(appExecutors: AppExecutors, deviceDao: DeviceDao): DeviceLocalDataSource {
            if (INSTANCE == null) {
                synchronized(DeviceLocalDataSource::javaClass) {
                    INSTANCE = DeviceLocalDataSource(appExecutors, deviceDao)
                }
            }
            return INSTANCE!!
        }
    }
}

