package com.seoultech.livingtogether_android.ui

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.bluetooth.le.BluetoothLeScanner
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanResult
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.Toast
import com.seoultech.livingtogether_android.R
import com.seoultech.livingtogether_android.base.BaseActivity
import com.seoultech.livingtogether_android.databinding.ActivityScanBinding
import com.seoultech.livingtogether_android.tools.BleCreater
import java.util.*
import kotlin.collections.ArrayList

class ScanActivity : BaseActivity<ActivityScanBinding>(R.layout.activity_scan) {

    private companion object {
        const val LIVING_TOGETHER_UUID = "01122334-4556-6778-899a-abbccddeeff0"
        const val LIVING_TOGETHER2_UUID = "01122334-4556-6778-899a-abbccddeeff1"
        const val POPPOP_UUID = "53454f55-4c54-4543-4850-6f70506f7030"
        const val SCAN_PERIOD = 60000
        const val REQUEST_ENABLE_BT = 1
        const val TAG = "ScanActivity"

    }


    private val mBluetoothAdapter: BluetoothAdapter? by lazy(LazyThreadSafetyMode.NONE) {
        val bluetoothManager = getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        bluetoothManager.adapter
    }

    private val BluetoothAdapter.isDisabled: Boolean
        get() = !isEnabled

    private fun PackageManager.missingSystemFeature(name: String): Boolean = !hasSystemFeature(name)

    private var mScanning = false

    private val handler = Handler()

    private var arrayDevices = ArrayList<BluetoothDevice>()

    lateinit var runnable: Runnable

    private var yCount = 0;
    private var jCount = 0;

    private val mScanCallback = object : ScanCallback() {

        override fun onScanResult(callbackType: Int, result: ScanResult?) {
            super.onScanResult(callbackType, result)
            result?.let {
                Log.d(TAG, "find : ${result.device}")

                //신호 강도가 너무 약하면 리턴
                if (it.rssi < -85) {
                    Log.d(
                        TAG,
                        "But rssi (${it.rssi}) is too week. return"
                    )
                    return
                }
            }
            val scanRecord =
                Objects.requireNonNull(result!!.scanRecord)!!.bytes
            var bleDevice = BleCreater.create(result.device, result.rssi, scanRecord)

            Log.d(
                TAG,
                "uuid : ${bleDevice!!.uuid}, major : ${bleDevice!!.major}, minor : ${bleDevice.minor}, ${bleDevice.rssi}"
            )

            if (bleDevice.uuid == LIVING_TOGETHER_UUID || bleDevice.uuid == LIVING_TOGETHER2_UUID) {
                Log.d(TAG, "Living Together H/W has been found! ");
                Log.d(
                    TAG,
                    "major : ${bleDevice!!.major}, minor : ${bleDevice.minor}, ${bleDevice.rssi}"
                )
                //scanBLEevice(false)
                if (bleDevice.uuid == LIVING_TOGETHER_UUID) {
                    yCount++
                    binding.yCount.text = yCount.toString()
                } else {
                    jCount++
                    binding.jCount.text = jCount.toString()
                }
                return
            }

            if (bleDevice.uuid == POPPOP_UUID) {
                Log.d(TAG, "POPPOP H/W has been found! ");
                Log.d(
                    TAG,
                    "major : ${bleDevice!!.major}, minor : ${bleDevice.minor}, rssi : ${bleDevice.rssi}"
                )
                handler.removeCallbacks(runnable)
                scanBLEevice(false)

                return
            }
        }

        override fun onBatchScanResults(results: MutableList<ScanResult>?) {
            super.onBatchScanResults(results)
            results?.let {
                for (result in it) {
                    if (!arrayDevices.contains(result.device))
                        arrayDevices.add(result.device)
                }
            }
        }

        override fun onScanFailed(errorCode: Int) {
            super.onScanFailed(errorCode)
        }
    }

    lateinit var mBLEScanner: BluetoothLeScanner

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        runnable = Runnable {
            mScanning = false
            mBluetoothAdapter!!.bluetoothLeScanner.stopScan(mScanCallback)
            Log.d(TAG, "Catch Count(YJ) : $yCount per 60 second")
            Log.d(TAG, "Catch Count(JB) : $jCount per 60 second")
            Log.d(TAG, "Scan Timeout")
        }
    }

    override fun onResume() {
        super.onResume()

        //BLE 지원하는지 확인
        packageManager.takeIf { it.missingSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE) }
            ?.also {
                Toast.makeText(this, "BLE를 지원하지 않습니다.", Toast.LENGTH_SHORT).show()
                finish()
            }

        //블루투스가 활성화 되어있는지 확인. 비활성화 되어 있으면 블루투스를 켜는 화면으로 이동
        mBluetoothAdapter?.takeIf { it.isDisabled }?.apply {
            val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT)
        }
    }

    override fun onStart() {
        super.onStart()
        scanBLEevice(true)
    }

    private fun scanBLEevice(enable: Boolean) {
        when (enable) {
            true -> {
                //SCAN_PERIOD 시간 이후에 스캔을 중지하도록 타이머 설정
                handler.postDelayed(runnable, SCAN_PERIOD.toLong())
                mScanning = true
                arrayDevices.clear()
                mBluetoothAdapter!!.bluetoothLeScanner.startScan(mScanCallback)
            }
            else -> {
                mScanning = false
                mBluetoothAdapter!!.bluetoothLeScanner.stopScan(mScanCallback)
                Log.d(TAG, "Scan is terminated")
            }
        }
    }
}
