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

        internal const val FLAG_BT_CHANGED_VALUE_ON = "ON"
        internal const val FLAG_BT_CHANGED_VALUE_OFF = "OFF"

        const val FLAG_STOP_SERVICE = "FLAG_STOP_SERVICE"
        internal const val FLAG_BT_CHANGED = "FLAG_BT_CHANGED"

        private const val NOTIFICATION_ID = 100

        private const val ACTION_SIGNAL = "100"
        private const val PRESERVE_SIGNAL = "200"

        private const val LOC_CODE1 = 27
        private const val LOC_CODE2 = 28
    }

    private var lastDetectedCode2: Byte? = null
    private var lastDetectedCode1: Byte? = null
    
    private val foregroundNotification: ForegroundNotification by lazy { ForegroundNotification(application) }

    private val bluetoothAdapter: BluetoothAdapter? by lazy(LazyThreadSafetyMode.NONE) {
        val bluetoothManager = application.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        bluetoothManager.adapter
    }

    private val btStateReceiver: BluetoothStateReceiver by lazy { BluetoothStateReceiver() }

    private val db : DataBaseManager by lazy{ DataBaseManager.getInstance(application) }

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
            if (isNewSignal(result.scanRecord!!.bytes)) {
                onScanResult(result)
            } else {
                Log.d(TAG, "This Device has just been registered.")
            }
        }

        override fun onBatchScanResults(results: List<ScanResult>) {
            for (result in results) {
                if (isNewSignal(result.scanRecord!!.bytes)) {
                    onScanResult(result)
                } else {
                    Log.d(TAG, "This Device has just been registered.")
                }
            }
        }
    }

    private fun onScanResult(result: ScanResult) {
        if (result.scanRecord != null) {
            Log.d(TAG, "Device has been found.")

            updateDB(result)
        }
    }

    @ExperimentalUnsignedTypes
    private fun updateDB(result: ScanResult) {
        val bleDevice = BleCreater.create(result.device, result.rssi, result.scanRecord!!.bytes)

        val targetDevice = db.deviceDao().getAll(result.device.address)

        if (targetDevice == null) {
            Log.d(TAG, "Unresolved address : ${result.device.address}")
            return
        }

        val currentTime = GregorianCalendar().timeInMillis

        //감지된 신호의 타입을 분석
        when (bleDevice.major.toString()) {
            ACTION_SIGNAL -> {
                targetDevice.lastDetectionOfActionSignal = currentTime
                targetDevice.isAvailable = true

                db.deviceDao().update(targetDevice)
                db.signalHistoryDao().insert(SignalHistoryEntity(targetDevice.deviceAddress, Signal.ACTION, currentTime))
            }

            PRESERVE_SIGNAL -> {
                targetDevice.lastDetectionOfPreserveSignal = currentTime
                targetDevice.isAvailable = true

                db.deviceDao().update(targetDevice)
                db.signalHistoryDao().insert(SignalHistoryEntity(targetDevice.deviceAddress, Signal.PRESERVE, currentTime))
            }

            //그 외에는 이상한 major
            else -> {
                Log.d(TAG, "Unresolved major : ${bleDevice.major}")
                return
            }
        }
    }

    // 새로운 신호인지 확인
    // 신호마다 byte arr를 보내주는데 27, 28번째 byte가 달라짐
    private fun isNewSignal(codes: ByteArray): Boolean {
        val decisionCode1 = codes[LOC_CODE1]
        val decisionCode2 = codes[LOC_CODE2]
        return if (lastDetectedCode1 != decisionCode1 || lastDetectedCode2 != decisionCode2) {
            lastDetectedCode1 = decisionCode1
            lastDetectedCode2 = decisionCode2
            true
        } else {
            false
        }
    }
}