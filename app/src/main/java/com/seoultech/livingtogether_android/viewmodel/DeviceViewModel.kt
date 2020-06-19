package com.seoultech.livingtogether_android.viewmodel

import android.app.Application
import androidx.lifecycle.LiveData
import com.seoultech.livingtogether_android.base.BaseViewModel
import com.seoultech.livingtogether_android.model.room.entity.DeviceEntity

class DeviceViewModel(application: Application) : BaseViewModel(application) {
    var devices = getAllObservable()

    fun getAllObservable() : LiveData<List<DeviceEntity>> {
        return db.deviceDao().getAllObservable()
    }
}