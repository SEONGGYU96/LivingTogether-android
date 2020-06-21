package com.seoultech.livingtogether_android.viewmodel

import android.app.Application
import android.util.Log
import android.widget.Toast
import com.seoultech.livingtogether_android.base.BaseViewModel
import com.seoultech.livingtogether_android.model.room.entity.DeviceEntity
import com.seoultech.livingtogether_android.model.room.repository.DeviceRepository

class LocationViewModel(application: Application) : BaseViewModel(application) {

    var location: String? = null

    private val deviceRepository: DeviceRepository by lazy { DeviceRepository() }

    private val device = getMostRecentDevice()

    private fun getMostRecentDevice() : DeviceEntity? {
        val mostResentDevice = deviceRepository.getMostRecentDevice()

        if (mostResentDevice == null) {
            Log.d(TAG, "empty device")
            Toast.makeText(getApplication(), "저장된 디바이스가 없습니다.", Toast.LENGTH_SHORT).show()

            finishHandler.value = true
            return null
        }
        return mostResentDevice
    }

    fun updateLocation() {
        device!!.location = location

        deviceRepository.update(device)
        finishHandler.value = true
    }
}