package com.seoultech.livingtogether_android.device.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.seoultech.livingtogether_android.ApplicationImpl
import com.seoultech.livingtogether_android.device.data.Device
import com.seoultech.livingtogether_android.device.data.DeviceStateChangedLiveData
import com.seoultech.livingtogether_android.device.data.source.DeviceDataSource
import com.seoultech.livingtogether_android.device.data.source.DeviceRepository
import com.seoultech.livingtogether_android.util.ServiceUtil

class DeviceViewModel(private val deviceRepository: DeviceRepository) : ViewModel() {

    private val _items = MutableLiveData<List<Device>>().apply { value = emptyList() }
    val items: LiveData<List<Device>>
        get() = _items

    private val _newSensorEvent = MutableLiveData<Boolean>()
    val newSensorEvent: LiveData<Boolean>
        get() = _newSensorEvent

    private val _emptyListEvent = MutableLiveData<Boolean>()
    val emptyListEvent: LiveData<Boolean>
        get() = _emptyListEvent

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
                ServiceUtil.startService(ApplicationImpl.getInstance())
                DeviceStateChangedLiveData.value = false
            }

            override fun onDataNotAvailable() {
                _items.value = emptyList()
                _emptyListEvent.value = true
                ServiceUtil.stopService(ApplicationImpl.getInstance())
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

    fun registerNewSensor() {
        _newSensorEvent.value = true
    }
}