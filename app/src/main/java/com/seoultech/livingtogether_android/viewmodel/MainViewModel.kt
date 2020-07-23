package com.seoultech.livingtogether_android.viewmodel

import android.app.Application
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.seoultech.livingtogether_android.base.BaseViewModel
import com.seoultech.livingtogether_android.device.model.DeviceEntity
import com.seoultech.livingtogether_android.nok.model.NOKEntity
import com.seoultech.livingtogether_android.device.repository.DeviceRepository
import com.seoultech.livingtogether_android.nok.repository.NOKRepository
import com.seoultech.livingtogether_android.bluetooth.service.ScanService
import com.seoultech.livingtogether_android.util.ServiceUtil

class MainViewModel(application: Application) : BaseViewModel(application) {

    private val bluetoothAdapter: BluetoothAdapter? by lazy(LazyThreadSafetyMode.NONE) {
        val bluetoothManager = application.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        bluetoothManager.adapter
    }

    private val deviceRepository: DeviceRepository by lazy { DeviceRepository() }
    private val nokRepository: NOKRepository by lazy { NOKRepository() }

    //Todo: 사용가능한 센서들도 따로 가져오기. 메인 화면에 정상 작동 개수 나타내기 위함
    var sensors = getSensorAll()

    var noks = getNOKAll()

    private val sensorObserver = Observer<List<DeviceEntity>> {
        if (it.isNotEmpty()) {
            startService()
        } else {
            stopService()
        }
    }

    init {
        sensors.observeForever(sensorObserver)
    }

    fun getNOKAll(): LiveData<List<NOKEntity>> {
        return nokRepository.getAllObservable()
    }

    fun getSensorAll(): LiveData<List<DeviceEntity>> {
        return deviceRepository.getAllObservable()
    }

    fun startService() {
        if (!isServiceRunning()) {
            // 등록된 버튼 있는데 스캔 Service 미 동작중이면 서비스 실행 시킴
            getApplication<Application>().startService(Intent(getApplication(), ScanService::class.java))

            Log.d(TAG, "Devices registered are exist and service is not running. start service.")
        } else {
            Log.d(TAG, "No device registered or service is already running")
        }
    }

    //Todo: ScanViewModel과 중복된 코드. 중복 제거
    fun stopService() {
        // To service Stop.
        if (ServiceUtil.isServiceRunning(getApplication(), ScanService::class.java)) {
            val intent = Intent(getApplication(), ScanService::class.java)
            intent.putExtra(ScanService.FLAG_STOP_SERVICE, true)
            getApplication<Application>().startService(intent)

            Log.d(TAG, "Request to terminate Service ")
        } else {
            Log.d(TAG, "Service is not running.")
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

    override fun onCleared() {
        super.onCleared()
        sensors.removeObserver(sensorObserver)
    }
}