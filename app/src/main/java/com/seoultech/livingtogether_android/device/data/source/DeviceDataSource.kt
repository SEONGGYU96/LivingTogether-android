package com.seoultech.livingtogether_android.device.data.source

import com.seoultech.livingtogether_android.device.data.Device

interface DeviceDataSource {

    interface LoadDevicesCallback {

        fun onDevicesLoaded(devices: List<Device>)

        fun onDataNotAvailable()
    }

    interface GetDeviceCallback {

        fun onDeviceLoaded(device: Device)

        fun onDataNotAvailable()
    }

    interface LoadDeviceAddressesCallback {

        fun onDeviceAddressesLoaded(addresses: List<String>)

        fun onDataNotAvailable()
    }

    fun getDevices(callback: LoadDevicesCallback)

    fun getDevice(deviceAddress: String, callback: GetDeviceCallback)

    fun getDeviceAddresses(callback: LoadDeviceAddressesCallback)

    fun getLatestDevice(callback: GetDeviceCallback)

    fun saveDevice(device: Device)

    fun deleteDevice(deviceAddress: String)
    
    fun deleteAllDevices()

    fun updateDevice(device: Device)
}