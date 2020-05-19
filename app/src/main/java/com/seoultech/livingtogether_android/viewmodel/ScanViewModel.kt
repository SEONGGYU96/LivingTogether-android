package com.seoultech.livingtogether_android.viewmodel

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanResult
import android.content.Context
import android.os.Handler
import android.util.Log
import com.seoultech.livingtogether_android.tools.BleCreater


class ScanViewModel(context: Context) {

    companion object{
        private const val TAG = "ScanViewModel"
        private const val LIVING_TOGETHER_UUID = "01122334-4556-6778-899a-abbccddeeff0"
        private const val MIN_RSSI = -85
    }

    private var isScanning = false

    private val bluetoothAdapter: BluetoothAdapter? by lazy(LazyThreadSafetyMode.NONE) {
        val bluetoothManager = context.
            getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        bluetoothManager.adapter
    }

    private val BluetoothAdapter.isDisabled: Boolean
        get() = !isEnabled

    private val handler = Handler()

    private var runnable = Runnable {
        isScanning = false
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
        bluetoothAdapter!!.bluetoothLeScanner.stopScan(scanCallback)
        Log.d(TAG, "Scan is terminated")
    }

    private val scanCallback = object : ScanCallback() {
        override fun onScanResult(callbackType: Int, result: ScanResult?) {
            super.onScanResult(callbackType, result)

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