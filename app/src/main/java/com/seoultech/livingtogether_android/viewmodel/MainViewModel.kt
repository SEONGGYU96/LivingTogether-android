package com.seoultech.livingtogether_android.viewmodel

import android.app.Application
import android.bluetooth.BluetoothAdapter
import android.content.Intent
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.seoultech.livingtogether_android.bluetooth.model.BluetoothLiveData
import com.seoultech.livingtogether_android.bluetooth.service.ScanService
import com.seoultech.livingtogether_android.util.ServiceUtil

class MainViewModel(private val bluetoothAdapter: BluetoothAdapter, val application: Application) : ViewModel() {

    companion object {
        private const val TAG = "MainViewModel"
    }

    private var hasDevice = false

    var isBluetoothOn = BluetoothLiveData

    private val _seeMoreNextOfKinEvent = MutableLiveData<Boolean>()
    val seeMoreNextOfKinEvent: LiveData<Boolean>
        get() = _seeMoreNextOfKinEvent

    private val _seeMoreSensorsEvent = MutableLiveData<Boolean>()
    val seeMoreSensorsEvent: LiveData<Boolean>
        get() = _seeMoreSensorsEvent

    private val _bluetoothOnEvent = MutableLiveData<Boolean>()
    val bluetoothOnEvent: LiveData<Boolean>
        get() = _bluetoothOnEvent

    fun onResume() {
        startService()
        if (hasDevice) {
            bluetoothStateCheck()
        }
    }

    fun seeMoreNextOfKin() {
        _seeMoreNextOfKinEvent.value = true
    }

    fun seeMoreSensors() {
        _seeMoreSensorsEvent.value = true
    }

    fun setBluetoothOn() {
        _bluetoothOnEvent.value = true
    }

    private fun startService() {
        if (!isServiceRunning()) {
            // 등록된 버튼 있는데 스캔 Service 미 동작중이면 서비스 실행 시킴
            application.startService(Intent(application, ScanService::class.java))

            Log.d(TAG, "Devices registered are exist and service is not running. start service.")
        } else {
            Log.d(TAG, "No device registered or service is already running")
        }
    }

//    fun stopService() {
//        // To service Stop.
//        if (ServiceUtil.isServiceRunning(getApplication(), ScanService::class.java)) {
//            val intent = Intent(getApplication(), ScanService::class.java)
//            intent.putExtra(ScanService.FLAG_STOP_SERVICE, true)
//            getApplication<Application>().startService(intent)
//
//            Log.d(TAG, "Request to terminate Service ")
//        } else {
//            Log.d(TAG, "Service is not running.")
//        }
//    }

    fun isServiceRunning(): Boolean {
        return ServiceUtil.isServiceRunning(application, ScanService::class.java)
    }

//    fun isScanning() : Boolean {
//        return bluetoothAdapter?.isDiscovering ?: false
//    }

    fun bluetoothStateCheck() {
        isBluetoothOn.value = bluetoothAdapter.isEnabled
    }

    fun setHasDevice(hasDevice: Boolean) {
        this.hasDevice = hasDevice;
    }
}