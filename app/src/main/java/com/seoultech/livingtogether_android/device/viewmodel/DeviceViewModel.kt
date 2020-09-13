package com.seoultech.livingtogether_android.device.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.seoultech.livingtogether_android.device.data.Device
import com.seoultech.livingtogether_android.device.data.source.DeviceDataSource
import com.seoultech.livingtogether_android.device.data.source.DeviceRepository

class DeviceViewModel(private val deviceRepository: DeviceRepository) : ViewModel(){

    private val _items = MutableLiveData<List<Device>>().apply { value = emptyList() }
    val items: LiveData<List<Device>>
        get() = _items

    val empty: LiveData<Boolean> = Transformations.map(_items) {
        it.isEmpty()
    }

    fun start() {
        loadDevice()
    }

    val itemSizeString: LiveData<String> = Transformations.map(_items) {
        it.size.toString()
    }

    private fun loadDevice() {
        deviceRepository.getDevices(object : DeviceDataSource.LoadDevicesCallback {
            override fun onDevicesLoaded(devices: List<Device>) {
                _items.value = devices
            }

            override fun onDataNotAvailable() {
                _items.value = emptyList()
            }
        })
    }

    fun deleteDevice(deviceAddress: String) {
        deviceRepository.deleteDevice(deviceAddress)
        loadDevice()
    }

    fun updateDevice(device: Device) {
        deviceRepository.updateDevice(device)
        loadDevice()
    }
}