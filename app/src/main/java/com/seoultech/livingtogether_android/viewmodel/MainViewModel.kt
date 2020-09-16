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

    private val _newNextOfKinEvent = MutableLiveData<Boolean>()
    val newNextOfKinEvent: LiveData<Boolean>
        get() = _newNextOfKinEvent

    fun onResume() {
        startService()
        if (hasDevice) {
            bluetoothStateCheck()
        }
    }

    fun addNewNextOfKin() {
        _newNextOfKinEvent.value = true
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
        isBluetoothOn.value = bluetoothAdapter.isEnabled ?: false
    }

    fun setHasDevice(hasDevice: Boolean) {
        this.hasDevice = hasDevice;
    }
}