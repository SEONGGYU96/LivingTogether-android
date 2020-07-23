package com.seoultech.livingtogether_android.device.viewmodel

import android.app.Application
import androidx.lifecycle.LiveData
import com.seoultech.livingtogether_android.base.BaseViewModel
import com.seoultech.livingtogether_android.device.model.DeviceEntity
import com.seoultech.livingtogether_android.device.repository.DeviceRepository

class DeviceViewModel(application: Application) : BaseViewModel(application) {

    private val deviceRepository: DeviceRepository by lazy { DeviceRepository() }

    var devices = getAllObservable()

    fun getAllObservable() : LiveData<List<DeviceEntity>> {
        return deviceRepository.getAllObservable()
    }
}