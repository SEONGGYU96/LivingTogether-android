package com.seoultech.livingtogether_android.device.viewmodel

import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.seoultech.livingtogether_android.ApplicationImpl
import com.seoultech.livingtogether_android.device.data.Device
import com.seoultech.livingtogether_android.device.data.source.DeviceDataSource
import com.seoultech.livingtogether_android.device.data.source.DeviceRepository

class LocationViewModel(private val deviceRepository: DeviceRepository) : ViewModel() {

    var location: String? = null

    private var device : Device? = null

    private val _updateLocationEvent = MutableLiveData<Boolean>()
    val updateLocationEvent: LiveData<Boolean>
        get() = _updateLocationEvent

    fun start() {
        getMostRecentDevice()
    }

    private fun getMostRecentDevice() {
        deviceRepository.getLatestDevice(object : DeviceDataSource.GetDeviceCallback {
            override fun onDeviceLoaded(device: Device) {
                this@LocationViewModel.device = device
            }

            override fun onDataNotAvailable() {
                Toast.makeText(ApplicationImpl.getInstance(), "저장된 디바이스가 없습니다.", Toast.LENGTH_SHORT).show()
            }
        })
    }

    fun updateLocation() {
        device?.let {
            it.location = this.location
            deviceRepository.updateDevice(it)
        }
        _updateLocationEvent.value = true
    }
}