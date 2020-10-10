package com.seoultech.livingtogether_android.bluetooth.viewmodel

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanResult
import android.content.Context
import android.content.Intent
import android.os.Handler
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.seoultech.livingtogether_android.ApplicationImpl
import com.seoultech.livingtogether_android.bluetooth.model.BleDevice
import com.seoultech.livingtogether_android.bluetooth.util.BleCreater
import com.seoultech.livingtogether_android.device.data.Device
import com.seoultech.livingtogether_android.device.data.source.DeviceRepository
import com.seoultech.livingtogether_android.signal.data.source.SignalRepository
import com.seoultech.livingtogether_android.bluetooth.service.ScanService
import com.seoultech.livingtogether_android.bluetooth.service.ScanService.Companion.ACTION_SIGNAL
import com.seoultech.livingtogether_android.bluetooth.util.AlarmUtil
import com.seoultech.livingtogether_android.device.data.source.DeviceDataSource
import com.seoultech.livingtogether_android.signal.data.Signal
import com.seoultech.livingtogether_android.util.BluetoothUtil
import com.seoultech.livingtogether_android.util.ServiceUtil
import java.util.*


class ScanViewModel(
    private val deviceRepository: DeviceRepository,
    private val signalRepository: SignalRepository
) : ViewModel() {

    companion object {
        //POPPOP
        //private const val LIVING_TOGETHER_UUID = "53454f55-4c54-4543-4850-6f70506f7030"
        //LIVINGTOGHETER
        private const val LIVING_TOGETHER_UUID = "01122334-4556-6778-899a-abbccddeeff0"
        private const val MIN_RSSI = -85
        private const val TAG = "ScanViewModel"
    }

    private val application = ApplicationImpl.getInstance()

    private var isScanning = false

    private val _backKeyEvent = MutableLiveData<Boolean>()
    val backKeyEvent: LiveData<Boolean>
        get() = _backKeyEvent

    private val _duplicateEvent = MutableLiveData<Boolean>()
    val duplicateEvent: LiveData<Boolean>
        get() = _duplicateEvent

    private val _timeOutEvent = MutableLiveData<Boolean>()
    val timeOutEvent: LiveData<Boolean>
        get() = _timeOutEvent

    private val _foundSensorEvent = MutableLiveData<Boolean>()
    val foundSensorEvent: LiveData<Boolean>
        get() = _foundSensorEvent

    private val _bluetoothIsNotAvailableEvent = MutableLiveData<Boolean>()
    val bluetoothIsNotAvailableEvent: LiveData<Boolean>
        get() = _bluetoothIsNotAvailableEvent

    private val bluetoothAdapter: BluetoothAdapter? by lazy(LazyThreadSafetyMode.NONE) {
        val bluetoothManager = application.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        bluetoothManager.adapter
    }

    private val BluetoothAdapter.isDisabled: Boolean
        get() = !isEnabled

    private val handler = Handler()

    private var runnable = Runnable {
        bluetoothAdapter!!.bluetoothLeScanner.stopScan(scanCallback)
        _timeOutEvent.value = true
        Log.d(TAG, "Scan Timeout")
    }

    fun checkBluetoothAvailable() {
        if (BluetoothUtil.isBluetoothAvailable(application)) {
            Log.d(TAG, "This device does not support Bluetooth.")
            _bluetoothIsNotAvailableEvent.value = true
        } else {
            Log.d(TAG, "This device supports Bluetooth.")
        }
    }

    fun isBluetoothOn(): Boolean {
        return bluetoothAdapter!!.isDisabled
    }

    fun startScan(time: Long) {
        stopService()
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

    fun setBackKeyEvent() {
        stopScan()
        _backKeyEvent.value = true
    }

    private fun stopService() {
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

            if (isScanning && result != null) {
                validateResult(result)
            }
        }
    }

    private fun validateResult(result: ScanResult) {
        Log.d(TAG, "find : ${result.device}")

        if (result.rssi < MIN_RSSI) {
            Log.d(TAG, "rssi (${result.rssi}) is too week. return")
            return
        }

        if (result.scanRecord == null) {
            Log.d(TAG, "This device does not have scanRecord. return")
            return
        }

        val bleDevice = BleCreater.create(result.device, result.rssi, result.scanRecord!!.bytes)

        Log.d(
            TAG,
            "uuid : ${bleDevice.uuid}, major : ${bleDevice.major}, minor : ${bleDevice.minor}, ${bleDevice.rssi}"
        )

        if (bleDevice.uuid != LIVING_TOGETHER_UUID) {
            return
        }

        Log.d(TAG, "Living Together H/W has been found")

        if (bleDevice.major.toString() != ACTION_SIGNAL) {
            Log.d(TAG, "But it is preserved signal")
            return
        }

        stopScan()

        deviceRepository.getDevice(bleDevice.address, object : DeviceDataSource.GetDeviceCallback {
            override fun onDeviceLoaded(device: Device) {
                Log.d(TAG, "This device is already registered. return.")
                _duplicateEvent.value = true
                return
            }

            override fun onDataNotAvailable() {
                registerSensor(bleDevice)
            }
        })
    }
    
    private fun registerSensor(bleDevice: BleDevice) {
        val calendar = GregorianCalendar()

        val newDevice = Device(
            "발판", bleDevice.address, null,
            calendar.timeInMillis, calendar.timeInMillis, calendar.timeInMillis
        )

        deviceRepository.saveDevice(newDevice)

        signalRepository.saveSignal(Signal(bleDevice.address, 3, calendar.timeInMillis))

        AlarmUtil.setActiveAlarm(application)

        _foundSensorEvent.value = true
    }
}