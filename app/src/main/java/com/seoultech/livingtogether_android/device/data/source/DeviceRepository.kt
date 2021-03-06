package com.seoultech.livingtogether_android.device.data.source

import android.util.Log
import com.seoultech.livingtogether_android.device.data.Device
import com.seoultech.livingtogether_android.device.data.DeviceStateChangedLiveData

class DeviceRepository(
    private val deviceLocalDataSource: DeviceDataSource,
    private val deviceRemoteDataSource: DeviceDataSource
) : DeviceDataSource {

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
                getDevicesFromRemoteDataSource(callback)
            }
        })
    }

    override fun getDevice(deviceAddress: String, callback: DeviceDataSource.GetDeviceCallback) {
        val deviceInCache = getDeviceByAddress(deviceAddress)

        if (deviceInCache != null) {
            val d = Log.d(TAG, "cache has the device : ${deviceAddress}")
            callback.onDeviceLoaded(deviceInCache)
            return
        }

        deviceLocalDataSource.getDevice(deviceAddress, object : DeviceDataSource.GetDeviceCallback {
            override fun onDeviceLoaded(device: Device) {
                cacheAndPerform(device) {
                    Log.d(TAG, "local has the data : $deviceAddress")
                    callback.onDeviceLoaded(it)
                }
            }

            override fun onDataNotAvailable() {
                Log.d(TAG, "local does not has data : $deviceAddress")
                getDeviceFromRemoteDataSource(deviceAddress, callback)
            }
        })
    }

    override fun getDeviceAddresses(callback: DeviceDataSource.LoadDeviceAddressesCallback) {
        if (cachedDevices.isNotEmpty() && !cacheIsDirty) {
            val result = mutableListOf<String>()
            val iterator = cachedDevices.iterator()
            while (iterator.hasNext()) {
                result.add(iterator.next().value.deviceAddress)
            }
            callback.onDeviceAddressesLoaded(result)
            return
        }

        if (cacheIsDirty) {
            getDeviceAddressFromRemoteDataResource(callback)
            return
        }

        deviceLocalDataSource.getDeviceAddresses(object: DeviceDataSource.LoadDeviceAddressesCallback {
            override fun onDeviceAddressesLoaded(addresses: List<String>) {
                callback.onDeviceAddressesLoaded(addresses)
            }

            override fun onDataNotAvailable() {
                getDeviceAddressFromRemoteDataResource(callback)
            }
        })
    }

    override fun getLatestDevice(callback: DeviceDataSource.GetDeviceCallback) {
        deviceLocalDataSource.getLatestDevice(callback)
    }

    override fun saveDevice(device: Device) {
        cacheAndPerform(device) {
            deviceLocalDataSource.saveDevice(it)
            deviceRemoteDataSource.saveDevice(it)
        }
    }

    override fun deleteDevice(deviceAddress: String) {
        deviceLocalDataSource.deleteDevice(deviceAddress)
        deviceRemoteDataSource.deleteDevice(deviceAddress)
        cachedDevices.remove(deviceAddress)
    }

    override fun deleteAllDevices() {
        deviceLocalDataSource.deleteAllDevices()
        deviceRemoteDataSource.deleteAllDevices()
        cachedDevices.clear()
    }

    override fun updateDevice(device: Device) {
        cacheAndPerform(device) {
            deviceLocalDataSource.updateDevice(device)
            deviceRemoteDataSource.updateDevice(device)
        }
    }

    private fun getDeviceAddressFromRemoteDataResource(callback: DeviceDataSource.LoadDeviceAddressesCallback) {
        deviceRemoteDataSource.getDeviceAddresses(object : DeviceDataSource.LoadDeviceAddressesCallback {
            override fun onDeviceAddressesLoaded(addresses: List<String>) {
                callback.onDeviceAddressesLoaded(addresses)
            }

            override fun onDataNotAvailable() {
                callback.onDataNotAvailable()
            }
        })
    }
    
    private fun getDevicesFromRemoteDataSource(callback: DeviceDataSource.LoadDevicesCallback) {
        deviceRemoteDataSource.getDevices(object: DeviceDataSource.LoadDevicesCallback {
            override fun onDevicesLoaded(devices: List<Device>) {
                refreshCache(devices)
                refreshLocalDataSource(devices)
                callback.onDevicesLoaded(devices)
            }

            override fun onDataNotAvailable() {
                callback.onDataNotAvailable()
            }
        })
    }
    
    private fun getDeviceFromRemoteDataSource(deviceAddress: String, callback: DeviceDataSource.GetDeviceCallback) {
        deviceRemoteDataSource.getDevice(deviceAddress, object: DeviceDataSource.GetDeviceCallback {
            override fun onDeviceLoaded(device: Device) {
                Log.d(TAG, "remote have the data : $deviceAddress")
                cacheAndPerform(device) { deviceLocalDataSource.saveDevice(device) }
                callback.onDeviceLoaded(device)
            }

            override fun onDataNotAvailable() {
                callback.onDataNotAvailable()
            }
        })
    }

    private fun getDeviceByAddress(deviceAddress: String) = cachedDevices[deviceAddress]

    private fun refreshLocalDataSource(devices: List<Device>) {
        deviceLocalDataSource.run {
            deleteAllDevices()
            for (device in devices) {
                saveDevice(device)
            }
        }
    }
    
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
        private const val TAG = "DeviceRepository"

        @JvmStatic fun getInstance(deviceLocalDataSource: DeviceDataSource, deviceRemoteDataSource: DeviceDataSource) =
            INSTANCE ?: synchronized(DeviceRepository::class.java) {
                INSTANCE ?: DeviceRepository(deviceLocalDataSource, deviceRemoteDataSource)
                    .also { INSTANCE = it }
            }
    }
}