package com.seoultech.livingtogether_android.viewmodel

import android.app.Application
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.seoultech.livingtogether_android.ApplicationImpl
import com.seoultech.livingtogether_android.model.room.entity.DeviceEntity
import com.seoultech.livingtogether_android.model.room.entity.NOKEntity
import com.seoultech.livingtogether_android.service.ScanService
import com.seoultech.livingtogether_android.util.ServiceUtil

class MainViewModel(application: Application) : AndroidViewModel(application) {

    companion object {
        private const val TAG = "MainViewModel"
    }

    private val db = ApplicationImpl.db

    private val bluetoothAdapter: BluetoothAdapter? by lazy(LazyThreadSafetyMode.NONE) {
        val bluetoothManager = application.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        bluetoothManager.adapter
    }

    //Todo: 사용가능한 센서들도 따로 가져오기. 메인 화면에 정상 작동 개수 나타내기 위함
    var sensors = getSensorAll()

    var noks = getNOKAll()

    fun getNOKAll(): LiveData<List<NOKEntity>> {
        return db.nokDao().getAllObservable()
    }

    fun getSensorAll(): LiveData<List<DeviceEntity>> {
        return db.deviceDao().getAllObservable()
    }

    fun startService() {
        if (db.deviceDao().getAll().isNotEmpty()
            && !isServiceRunning()) {
            // 등록된 버튼 있는데 스캔 Service 미 동작중이면 서비스 실행 시킴
            getApplication<Application>().startService(Intent(getApplication(), ScanService::class.java))

            Log.d(TAG, "Devices registered are exist and service is not running. start service.")
        } else {
            Log.d(TAG, "No device registered or service is already running")
        }
    }

    fun isServiceRunning() : Boolean {
        return ServiceUtil.isServiceRunning(getApplication(), ScanService::class.java)
    }

    //Fixme: 스캔 중이어도 false를 return
    fun isScanning() : Boolean {
        return bluetoothAdapter?.isDiscovering ?: false
    }

    fun isBluetoothOn() : Boolean {
        return bluetoothAdapter?.isEnabled?: false
    }
}