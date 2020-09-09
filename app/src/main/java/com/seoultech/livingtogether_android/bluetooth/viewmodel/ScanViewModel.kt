package com.seoultech.livingtogether_android.bluetooth.viewmodel

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
import androidx.lifecycle.ViewModel
import com.seoultech.livingtogether_android.ApplicationImpl
import com.seoultech.livingtogether_android.bluetooth.util.BleCreater
import com.seoultech.livingtogether_android.device.data.Device
import com.seoultech.livingtogether_android.signal.SignalHistoryEntity
import com.seoultech.livingtogether_android.device.data.source.DeviceRepository
import com.seoultech.livingtogether_android.signal.SignalHistoryRepository
import com.seoultech.livingtogether_android.bluetooth.service.ScanService
import com.seoultech.livingtogether_android.bluetooth.util.AlarmUtil
import com.seoultech.livingtogether_android.device.data.source.DeviceDataSource
import com.seoultech.livingtogether_android.util.BluetoothUtil
import com.seoultech.livingtogether_android.util.ServiceUtil
import java.util.*


class ScanViewModel(deviceRepository: DeviceRepository, signalHistoryRepository: SignalHistoryRepository) : ViewModel() {

    companion object{
        private const val LIVING_TOGETHER_UUID = "01122334-4556-6778-899a-abbccddeeff0"
        private const val MIN_RSSI = -85
    }

    private val finishHandler = MutableLiveData<Boolean>()

    private val TAG = javaClass.simpleName

    private val application = ApplicationImpl.getInstance()

    private var isScanning = false

    val timeoutHandler = MutableLiveData<Boolean>()

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
        timeoutHandler.value = true
        Log.d(TAG, "Scan Timeout")
    }

    init {
        isBluetoothAvailable()
    }

    private fun isBluetoothAvailable() {
        if (BluetoothUtil.isBluetoothAvailable(application)) {
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

        startService()
    }

    fun startService() {
        application.startService(Intent(application, ScanService::class.java))
    }

    fun stopService() {
        // To service Stop.
        if (ServiceUtil.isServiceRunning(application, ScanService::class.java)) {
            val intent = Intent(application, ScanService::class.java)
            intent.putExtra(ScanService.FLAG_STOP_SERVICE, true)
            application.startService(intent)

            Log.d(TAG, "Request to terminate Service ")
        } else {
            Log.d(TAG, "Service is not running.")
        }
    }

    @ExperimentalUnsignedTypes
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

                Log.d(TAG, "uuid : ${bleDevice.uuid}, major : ${bleDevice.major}, minor : ${bleDevice.minor}, ${bleDevice.rssi}")

                if (bleDevice.uuid == LIVING_TOGETHER_UUID) {
                    Log.d(TAG, "Living Together H/W has been found")

                    stopScan()

                    deviceRepository.getDevice(bleDevice.address, object : DeviceDataSource.GetDeviceCallback {
                        override fun onDeviceLoaded(device: Device) {
                            Toast.makeText(application, "이미 등록된 버튼입니다.", Toast.LENGTH_SHORT).show()
                            Log.d(TAG, "This device is already registered. return.")
                            finishHandler.value = true
                            return
                        }

                        override fun onDataNotAvailable() {
                            val calendar = GregorianCalendar()

                            val newDevice = Device("발판", bleDevice.address, null,
                                calendar.timeInMillis, calendar.timeInMillis, calendar.timeInMillis)

                            deviceRepository.saveDevice(newDevice)

                            signalHistoryRepository.insert(SignalHistoryEntity(bleDevice.address, 3, calendar.timeInMillis))

                            AlarmUtil.setAlarm(application)

                            isFound.value = true

                            return
                        }
                    })
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