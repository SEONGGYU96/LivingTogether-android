package com.seoultech.livingtogether_android.manager;

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.bluetooth.le.ScanCallback
import android.content.Context
import android.content.Intent
import android.os.Handler
import android.util.Log
import com.seoultech.livingtogether_android.ui.scan.ScanActivity

/**
 * 블루투스 스캔을 이용하는 매니저 클래스
 * TODO: 싱글턴으로 변경
 */
class SimpleBluetoothManager(val context: Context, var callback: ScanCallback?) {
    companion object {
        private const val TAG = "BluetoothManager"
        private var instance: SimpleBluetoothManager? = null

        fun getInstance(context: Context): SimpleBluetoothManager {
            return instance ?: synchronized(this) {
                instance ?: SimpleBluetoothManager(context, null)
            }
        }

        fun getInstance(callback: ScanCallback): SimpleBluetoothManager {
            instance!!.scanCallback = callback
            return instance as SimpleBluetoothManager
        }
    }

    private var scanCallback = callback

    private var isScanning = false

    private val bluetoothAdapter: BluetoothAdapter? by lazy(LazyThreadSafetyMode.NONE) {
        val bluetoothManager =
            context.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
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

    fun startScan(time: Long) {
        handler.postDelayed(runnable, time)
        isScanning = true
        bluetoothAdapter!!.bluetoothLeScanner.startScan(scanCallback)
    }

    fun stopScan() {
        isScanning = false
        bluetoothAdapter!!.bluetoothLeScanner.stopScan(scanCallback)
        Log.d(TAG, "Scan is terminated")
    }
}
