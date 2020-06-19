package com.seoultech.livingtogether_android.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.seoultech.livingtogether_android.ApplicationImpl
import com.seoultech.livingtogether_android.model.room.entity.DeviceEntity

class DeviceViewModel(application: Application) : AndroidViewModel(application) {

    private val db = ApplicationImpl.db

    var devices = getAllObservable()

    fun getAllObservable() : LiveData<List<DeviceEntity>> {
        return db.deviceDao().getAllObservable()
    }
}