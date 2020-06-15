package com.seoultech.livingtogether_android.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.seoultech.livingtogether_android.model.room.DataBaseManager
import com.seoultech.livingtogether_android.model.room.entity.DeviceEntity

class DeviceViewModel(application: Application) : AndroidViewModel(application) {

    private val db = DataBaseManager.getInstance(application)

    var devices = getAllObservable()

    fun getAllObservable() : LiveData<List<DeviceEntity>> {
        return db.deviceDao().getAllObservable()
    }
}