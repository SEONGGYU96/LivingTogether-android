package com.seoultech.livingtogether_android.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.seoultech.livingtogether_android.model.room.DataBaseManager
import com.seoultech.livingtogether_android.model.room.entity.DeviceEntity
import com.seoultech.livingtogether_android.model.room.entity.NOKEntity

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val db = DataBaseManager.getInstance(application)

    var sensors: LiveData<List<DeviceEntity>>

    var noks: LiveData<List<NOKEntity>>

    init {
        sensors = getSensorAll()

        noks = getNOKAll()
    }

    fun getNOKAll(): LiveData<List<NOKEntity>> {
        return db.nokDao().getAllObservable()
    }

    fun getSensorAll(): LiveData<List<DeviceEntity>> {
        return db.deviceDao().getAllObservable()
    }
}