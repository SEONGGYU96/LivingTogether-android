package com.seoultech.livingtogether_android.viewmodel

import android.app.Application
import androidx.lifecycle.LiveData
import com.seoultech.livingtogether_android.base.BaseViewModel
import com.seoultech.livingtogether_android.model.room.entity.DeviceEntity
import com.seoultech.livingtogether_android.model.room.repository.DeviceRepository

class DeviceViewModel(application: Application) : BaseViewModel(application) {

    private val deviceRepository: DeviceRepository by lazy { DeviceRepository() }

    var devices = getAllObservable()

    fun getAllObservable() : LiveData<List<DeviceEntity>> {
        return deviceRepository.getAllObservable()
    }
}