package com.seoultech.livingtogether_android.ui.scan

import android.bluetooth.BluetoothAdapter
import android.content.Intent
import android.os.Bundle
import android.util.Log
import com.seoultech.livingtogether_android.R
import com.seoultech.livingtogether_android.base.BaseActivity
import com.seoultech.livingtogether_android.databinding.ActivityScanBinding
import com.seoultech.livingtogether_android.util.BluetoothUtil
import com.seoultech.livingtogether_android.viewmodel.ScanViewModel

class ScanActivity : BaseActivity<ActivityScanBinding>(R.layout.activity_scan) {

    private companion object {
        const val POPPOP_UUID = "53454f55-4c54-4543-4850-6f70506f7030"
        const val SCAN_PERIOD = 10000L
        const val REQUEST_ENABLE_BT = 1000
        const val TAG = "ScanActivity"

    }

    //TODO: Context 를 넘기지 않는 방법으로 수
    private val vm: ScanViewModel by lazy { ScanViewModel(this.applicationContext) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //TODO: ViewModel 안에 넣기
        if (BluetoothUtil.isBluetoothAvailable(this)) {
            Log.d(TAG, "This device does not support Bluetooth.")
            finish()
        } else {
            Log.d(TAG, "This device supports Bluetooth.")
        }
    }

    override fun onResume() {
        super.onResume()
        
        //블루투스가 활성화 되어있는지 확인. 비활성화 되어 있으면 블루투스를 켜는 화면으로 이동
        if (vm.isBluetoothOn()) {
            val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT)

        } else {
            //블루투스가 켜져있다면 스캔 시작
            vm.startScan(SCAN_PERIOD)
        }
    }
}
