package com.seoultech.livingtogether_android.viewmodel

import android.app.Application
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanResult
import android.content.Context
import android.os.Handler
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.seoultech.livingtogether_android.model.room.DataBaseManager
import com.seoultech.livingtogether_android.model.room.entity.DeviceEntity
import com.seoultech.livingtogether_android.tools.BleCreater
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*


class ScanViewModel(application: Application) : AndroidViewModel(application) {

    companion object{
        private const val TAG = "ScanViewModel"
        private const val LIVING_TOGETHER_UUID = "01122334-4556-6778-899a-abbccddeeff0"
        private const val MIN_RSSI = -85
    }

    private val db = DataBaseManager.getInstance(application)

    private var isScanning = false

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

                    val calendar = GregorianCalendar()

                    //Todo: null 처리한 정보들 받아올 수 있도록 하기
                    viewModelScope.launch(Dispatchers.IO) {
                        db.deviceDao().insert(DeviceEntity("발판", bleDevice.major.toString(), null, null, calendar.timeInMillis, calendar.timeInMillis, true))
                    }

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