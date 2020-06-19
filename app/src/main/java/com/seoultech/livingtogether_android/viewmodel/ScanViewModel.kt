package com.seoultech.livingtogether_android.viewmodel

import android.app.Application
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanResult
import android.content.Context
import android.content.Intent
import android.os.Handler
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.seoultech.livingtogether_android.base.BaseViewModel
import com.seoultech.livingtogether_android.model.room.entity.DeviceEntity
import com.seoultech.livingtogether_android.model.room.entity.SignalHistoryEntity
import com.seoultech.livingtogether_android.service.ScanService
import com.seoultech.livingtogether_android.service.Signal
import com.seoultech.livingtogether_android.tools.BleCreater
import com.seoultech.livingtogether_android.util.BluetoothUtil
import com.seoultech.livingtogether_android.util.ServiceUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*


class ScanViewModel(application: Application) : BaseViewModel(application) {

    companion object{
        private const val LIVING_TOGETHER_UUID = "01122334-4556-6778-899a-abbccddeeff0"
        private const val MIN_RSSI = -85
    }

    private var isScanning = false

    var isFound = MutableLiveData<Boolean>()

    private val bluetoothAdapter: BluetoothAdapter? by lazy(LazyThreadSafetyMode.NONE) {
        val bluetoothManager = application.
            getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        bluetoothManager.adapter
    }

    private val BluetoothAdapter.isDisabled: Boolean
        get() = !isEnabled

    private val handler = Handler()

    private var runnable = Runnable {
        bluetoothAdapter!!.bluetoothLeScanner.stopScan(scanCallback)
        Log.d(TAG, "Scan Timeout")
    }

    init {
        isBluetoothAvailable()
    }

    private fun isBluetoothAvailable() {
        if (BluetoothUtil.isBluetoothAvailable(getApplication())) {
            Log.d(TAG, "This device does not support Bluetooth.")
            finishHandler.value = true
        } else {
            Log.d(TAG, "This device supports Bluetooth.")
        }
    }

    fun isBluetoothOn(): Boolean {
        return bluetoothAdapter!!.isDisabled
    }

    fun startScan(time: Long) {
        handler.postDelayed(runnable, time)
        isScanning = true
        bluetoothAdapter!!.bluetoothLeScanner.startScan(scanCallback)
        Log.d(TAG, "Scan is started")
    }

    fun stopScan() {
        isScanning = false
        handler.removeCallbacks(runnable)
        bluetoothAdapter!!.bluetoothLeScanner.stopScan(scanCallback)
        Log.d(TAG, "Scan is terminated")
    }

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

    private val scanCallback = object : ScanCallback() {
        override fun onScanResult(callbackType: Int, result: ScanResult?) {
            super.onScanResult(callbackType, result)

            if (!isScanning) {
                Log.d(TAG, "isScanning is false. return.")
                return
            }

            result?.let { it ->
                Log.d(TAG, "find : ${result.device}")

                if (it.rssi < MIN_RSSI) {
                    Log.d(TAG, "rssi (${it.rssi}) is too week. return")
                    return
                }

                if (it.scanRecord == null) {
                    Log.d(TAG, "This device does not have scanRecord. return")
                    return
                }

                val bleDevice = BleCreater.create(it.device, it.rssi, it.scanRecord!!.bytes)

                Log.d(TAG, "uuid : ${bleDevice!!.uuid}, major : ${bleDevice.major}, minor : ${bleDevice.minor}, ${bleDevice.rssi}")

                if (bleDevice.uuid == LIVING_TOGETHER_UUID) {
                    Log.d(TAG, "Living Together H/W has been found")

                    stopScan()

                    if (db.deviceDao().getAll().isNotEmpty()) {
                        Toast.makeText(getApplication(), "이미 등록된 버튼입니다.", Toast.LENGTH_SHORT).show()
                        Log.d(TAG, "This device is already registered. return.")
                        finishHandler.value = true
                        return
                    }

                    val calendar = GregorianCalendar()

                    //Todo: null 처리한 정보들 받아올 수 있도록 하기
                    viewModelScope.launch(Dispatchers.IO) {
                        db.deviceDao().insert(DeviceEntity("발판", bleDevice.major.toString(), bleDevice.minor.toString(), bleDevice.address, null, null, calendar.timeInMillis, calendar.timeInMillis, true))
                        db.signalHistoryDao().insert(SignalHistoryEntity(bleDevice.major.toString(), Signal.RESIST, calendar.timeInMillis))
                    }

                    isFound.value = true

                    return
                }
            }
        }

        override fun onBatchScanResults(results: MutableList<ScanResult>?) {
            super.onBatchScanResults(results)
        }

        override fun onScanFailed(errorCode: Int) {
            super.onScanFailed(errorCode)
        }
    }
}