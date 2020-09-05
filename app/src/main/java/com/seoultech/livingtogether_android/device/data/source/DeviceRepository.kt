package com.seoultech.livingtogether_android.device.data.source

import com.seoultech.livingtogether_android.device.data.Device

class DeviceRepository(private val deviceLocalDataSource: DeviceDataSource) : DeviceDataSource {

    private val cachedDevices = LinkedHashMap<String, Device>()

    private var cacheIsDirty = false

    override fun getDevices(callback: DeviceDataSource.LoadDevicesCallback) {
        if (cachedDevices.isNotEmpty() && !cacheIsDirty) {
            callback.onDevicesLoaded(ArrayList(cachedDevices.values))
            return
        }

        deviceLocalDataSource.getDevices(object : DeviceDataSource.LoadDevicesCallback {
            override fun onDevicesLoaded(devices: List<Device>) {
                refreshCache(devices)
                callback.onDevicesLoaded(devices)
            }

            override fun onDataNotAvailable() {
                callback.onDataNotAvailable()
            }
        })
    }

    override fun getDevice(deviceAddress: String, callback: DeviceDataSource.GetDeviceCallback) {
        val deviceInCache = getDeviceByAddress(deviceAddress)

        if (deviceInCache != null) {
            callback.onDeviceLoaded(deviceInCache)
            return
        }

        deviceLocalDataSource.getDevice(deviceAddress, object : DeviceDataSource.GetDeviceCallback {
            override fun onDeviceLoaded(device: Device) {
                cacheAndPerform(device) {
                    callback.onDeviceLoaded(it)
                }
            }

            override fun onDataNotAvailable() {
                callback.onDataNotAvailable()
            }
        })
    }

    override fun saveDevice(device: Device) {
        cacheAndPerform(device) {
            deviceLocalDataSource.saveDevice(it)
        }
    }

    override fun deleteDevice(deviceAddress: String) {
        deviceLocalDataSource.deleteDevice(deviceAddress)
        cachedDevices.remove(deviceAddress)
    }

    override fun updateDevice(device: Device) {
        cacheAndPerform(device) {
            deviceLocalDataSource.updateDevice(device)
        }
    }

    private fun getDeviceByAddress(deviceAddress: String) = cachedDevices[deviceAddress]

    private fun refreshCache(devices: List<Device>) {
        cachedDevices.clear()
        devices.forEach {
            cacheAndPerform(it) {}
        }
        cacheIsDirty = false
    }

    private inline fun cacheAndPerform(device: Device, perform: (Device) -> Unit) {
        val cacheDevice = Device(device.deviceType, device.deviceAddress, device.location,
            device.initDate, device.lastDetectionOfActionSignal, device.lastDetectionOfPreserveSignal)
        cachedDevices[cacheDevice.deviceAddress] = cacheDevice
        perform(cacheDevice)
    }

    companion object {

        private var INSTANCE: DeviceRepository? = null

        @JvmStatic fun getInstance(deviceLocalDataSource: DeviceDataSource) =
            INSTANCE ?: synchronized(DeviceRepository::class.java) {
                INSTANCE ?: DeviceRepository(deviceLocalDataSource)
                    .also { INSTANCE = it }
            }
    }
}