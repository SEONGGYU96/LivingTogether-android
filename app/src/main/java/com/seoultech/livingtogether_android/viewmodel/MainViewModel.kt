package com.seoultech.livingtogether_android.viewmodel

import android.app.Application
import android.content.Intent
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.seoultech.livingtogether_android.model.room.DataBaseManager
import com.seoultech.livingtogether_android.model.room.entity.DeviceEntity
import com.seoultech.livingtogether_android.model.room.entity.NOKEntity
import com.seoultech.livingtogether_android.service.ScanService
import com.seoultech.livingtogether_android.util.ServiceUtil

class MainViewModel(application: Application) : AndroidViewModel(application) {

    companion object {
        private const val TAG = "MainViewModel"
    }

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

    fun startService() {
        if (db.deviceDao().getAll().isNotEmpty()
            && !ServiceUtil.isServiceRunning(getApplication(), ScanService::class.java)) {
            // 등록된 버튼 있는데 스캔 Service 미 동작중이면 서비스 실행 시킴
            getApplication<Application>().startService(Intent(getApplication(), ScanService::class.java))

            Log.d(TAG, "Devices registered are exist and service is not running. start service.")
        } else {
            Log.d(TAG, "No device registered or service is already running")
        }
    }
}