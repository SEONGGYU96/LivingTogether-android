package com.seoultech.livingtogether_android.service

import android.app.Service
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanFilter
import android.bluetooth.le.ScanResult
import android.bluetooth.le.ScanSettings
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.os.IBinder
import android.text.TextUtils
import android.util.Log
import com.seoultech.livingtogether_android.model.BleDevice
import com.seoultech.livingtogether_android.model.room.DataBaseManager
import com.seoultech.livingtogether_android.model.room.entity.SignalHistoryEntity
import com.seoultech.livingtogether_android.receiver.BluetoothStateReceiver
import com.seoultech.livingtogether_android.tools.BleCreater
import java.util.*

class ScanService : Service() {
    companion object {
        private const val TAG = "ScanService"

        private const val LIVING_TOGETHER_UUID = "01122334-4556-6778-899a-abbccddeeff0"

        internal const val FLAG_BT_CHANGED_VALUE_ON = "ON"
        internal const val FLAG_BT_CHANGED_VALUE_OFF = "OFF"

        private const val SIGNAL_TRANSMIT_TIME = 10000

        const val FLAG_STOP_SERVICE = "FLAG_STOP_SERVICE"
        internal const val FLAG_BT_CHANGED = "FLAG_BT_CHANGED"

        private const val NOTIFICATION_ID = 100

        private const val TYPE_ONE = "100"
        private const val TYPE_TWO = "200"

        private const val LOC_CODE1 = 27
        private const val LOC_CODE2 = 28
    }

    private var lastDetectedCode1: Byte = 0
    private var lastDetectedCode2: Byte = 0

    private val foregroundNotification: ForegroundNotification by lazy { ForegroundNotification(application) }

    private val bluetoothAdapter: BluetoothAdapter? by lazy(LazyThreadSafetyMode.NONE) {
        val bluetoothManager = application.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        bluetoothManager.adapter
    }

    private val btStateReceiver: BluetoothStateReceiver by lazy { BluetoothStateReceiver() }

    private val db : DataBaseManager by lazy{ DataBaseManager.getInstance(application) }

    private val deviceMajorArray = mutableListOf<String>()

    //service 가 처음 생성되었을 때 최초 1회 호출되는 부분
    override fun onCreate() {
        super.onCreate()
        Log.d(TAG, "Service is created")
    }

    //startService()가 호출되면 실행되는 부분. service 의 시작점
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d(TAG, "onStartCommand is invoked")

        if (intent != null) {
            if (intent.getBooleanExtra(FLAG_STOP_SERVICE, false)) {
                // 서비스 중단 요청
                Log.d(TAG, "onStartCommand :stopScan() is called because FLAG_STOP_SERVICE")
                stopService()
                return START_NOT_STICKY
            }

            if (intent.hasExtra(FLAG_BT_CHANGED)) {
                //블루투스가 켜져서 호출됐을 때
                val btChanged = intent.getStringExtra(FLAG_BT_CHANGED)

                if (TextUtils.equals(btChanged, FLAG_BT_CHANGED_VALUE_ON)) {
                    startScanService()
                    Log.d(TAG, "onStartCommand : startScanService() is called because BT is turned on")

                } else if (TextUtils.equals(btChanged, FLAG_BT_CHANGED_VALUE_OFF)) {
                    //블루투스가 꺼졌을 때
                    stopScan()

                    //Notification 상태 변경
                    startForeground(NOTIFICATION_ID, foregroundNotification.getNotification(false))

                    Log.d(TAG, "onStartCommand : stopScan() is called because BT is turned off")
                }

                //START_STICKY
                return super.onStartCommand(intent, flags, startId)
            }
        }

        // 서비스 실행 요청됨
        if (!BluetoothAdapter.getDefaultAdapter().isEnabled) {
            //BT adapter 꺼진상태인지 체크
            Log.d(TAG, "onStartCommend : Bluetooth is enabled")

            startForeground(NOTIFICATION_ID, foregroundNotification.getNotification(false))

            //START_STICKY
            return super.onStartCommand(intent, flags, startId)
        }

        val deviceList = db.deviceDao().getAll()

        if (deviceList.isEmpty()) {
            Log.d(TAG, "No device in DB")
            stopService()
            return START_NOT_STICKY
        }

        for (device in deviceList) {
            deviceMajorArray.add(device.deviceMajor)
        }

        startScanService()

        return super.onStartCommand(intent, flags, startId)
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    private fun startScanService() {
        Log.d(TAG, "ScanService() is invoked")

        startForeground(NOTIFICATION_ID, foregroundNotification.getNotification(true))

        startScan()

        //블루투스 상태가 바뀌면 해당 시스템 메시지를 받기로 함
        registerReceiver(btStateReceiver, IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED))
    }

    private fun startScan() {
        Log.d(TAG, "startScan() is invoke")

        val bluetoothLeScanner = bluetoothAdapter?.run {
            bluetoothLeScanner
        }

        if (bluetoothLeScanner == null) {
            Log.d(TAG, "BluetoothLeScanner is null")
            return
        }

        val filters = mutableListOf<ScanFilter>()
        val deviceAddresses = db.deviceDao().getAllDeviceAddress()

        for (address in deviceAddresses) {
            filters.add(ScanFilter.Builder().setDeviceAddress(address).build())
        }

        val settingsBuilder = ScanSettings.Builder()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            //정확한것은 아니지만 MATCH_MODE_AGGRESSIVE(적당히 매칭되면 OK)를 적용하니, 잘되는것 같다.
            settingsBuilder.setScanMode(ScanSettings.MATCH_MODE_AGGRESSIVE)
        } else {
            settingsBuilder.setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY)
        }

        bluetoothLeScanner.startScan(filters, settingsBuilder.build(), scanCallback)
    }

    private fun stopService() {
        Log.d(TAG, "stopService() is invoked")
        try {
            //리시버 할당 해제
            unregisterReceiver(btStateReceiver);
        } catch (e: Exception) {
            e.printStackTrace()
        }

        //블루투스 스캔 중지
        stopScan()
        //서비스 중단
        stopSelf()
    }

    private fun stopScan() {
        Log.d(TAG, "stopScan() is invoked")

        //Todo: startScan()과 중복하여 사용하는 것이 맞을까?
        val bluetoothLeScanner = bluetoothAdapter?.run {
            bluetoothLeScanner
        }

        if (bluetoothLeScanner == null) {
            Log.d(TAG, "BluetoothLeScanner is null")
            return
        }
            BluetoothAdapter.getDefaultAdapter().bluetoothLeScanner

       bluetoothLeScanner.stopScan(scanCallback)
    }

    private val scanCallback: ScanCallback = object : ScanCallback() {
        override fun onScanResult(callbackType: Int, result: ScanResult) {
            onScanResult(result)
        }

        override fun onBatchScanResults(results: List<ScanResult>) {
            for (result in results) {
                onScanResult(result)
            }
        }
    }

    private fun onScanResult(result: ScanResult) {
        if (result.scanRecord != null) {
            Log.d(TAG, "Device has been found.")

            updateDB(result)
        }
    }

    private fun isNewSignal(bleDevice: BleDevice) : Boolean {
        val device = db.deviceDao().getAll(bleDevice.major.toString())
            if (GregorianCalendar().timeInMillis - device.lastDetectionTypeOne < SIGNAL_TRANSMIT_TIME) {
                Log.d(TAG, "This Device has just been registered.")
                return true
            }
        return false
    }

    private fun updateDB(result: ScanResult) {
        val bleDevice = BleCreater.create(result.device, result.rssi, result.scanRecord!!.bytes)

        if (isNewSignal(bleDevice)) {
            return
        }

        //감지된 신호의 major 와 동일한 기기가 DB에 있다면
        if (deviceMajorArray.contains(bleDevice.major.toString())) {
            val targetDevice = db.deviceDao().getAll(bleDevice.major.toString())

            val currentTime = GregorianCalendar().timeInMillis
            Log.d(TAG, "The device just been found is the same as the one in the DB.")

            //감지된 신호의 타입을 분석
            when (bleDevice.minor.toString()) {
                TYPE_ONE -> { //Type-I 신호는 두 가지 신호 감지 시간을 모두 업데이트
                    targetDevice.lastDetectionTypeOne = currentTime
                    targetDevice.lastDetectionTypeTwo = currentTime

                    db.signalHistoryDao().insert(SignalHistoryEntity(targetDevice.deviceMajor, 0, currentTime))
                }
                //Type-II 신호는 해당 감지 시간만 업데이트
                TYPE_TWO -> {
                    targetDevice.lastDetectionTypeTwo = currentTime
                    db.signalHistoryDao().insert(SignalHistoryEntity(targetDevice.deviceMajor, 1, currentTime))
                }

                //그 외에는 이상한 minor
                else -> {
                    Log.d(TAG, "Unresolved minor : ${bleDevice.minor}")
                    return
                }
            }
            db.deviceDao().update(targetDevice)
        } else {
            Log.d(TAG, "There is no same data in DB")
        }
    }
}