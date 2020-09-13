package com.seoultech.livingtogether_android.device.viewmodel

import android.app.Application
import android.util.Log
import android.widget.Toast
import com.seoultech.livingtogether_android.Injection
import com.seoultech.livingtogether_android.base.BaseViewModel
import com.seoultech.livingtogether_android.device.data.Device
import com.seoultech.livingtogether_android.device.data.source.DeviceDataSource
import com.seoultech.livingtogether_android.device.data.source.DeviceRepository

class LocationViewModel(application: Application) : BaseViewModel(application) {

    var location: String? = null

    private val deviceRepository: DeviceRepository by lazy { Injection.provideDeviceRepository(application) }

    private var device : Device? = null

    fun getMostRecentDevice() {
        deviceRepository.getLatestDevice(object : DeviceDataSource.GetDeviceCallback {
            override fun onDeviceLoaded(device: Device) {
                this@LocationViewModel.device = device
            }

            override fun onDataNotAvailable() {
                Log.d(TAG, "empty device")
                Toast.makeText(getApplication(), "저장된 디바이스가 없습니다.", Toast.LENGTH_SHORT).show()

                finishHandler.value = true
            }
        })
    }

    fun updateLocation() {
        device?.let {
            it.location = this.location

            deviceRepository.updateDevice(it)
            finishHandler.value = true
        }
    }
}