package com.seoultech.livingtogether_android.ui.scan

import android.bluetooth.BluetoothAdapter
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.seoultech.livingtogether_android.R
import com.seoultech.livingtogether_android.base.BaseActivity
import com.seoultech.livingtogether_android.databinding.ActivityScanBinding
import com.seoultech.livingtogether_android.service.ScanService
import com.seoultech.livingtogether_android.util.BluetoothUtil
import com.seoultech.livingtogether_android.viewmodel.ScanViewModel

class ScanActivity : BaseActivity<ActivityScanBinding>(R.layout.activity_scan) {
    private lateinit var vm: ScanViewModel
    private lateinit var viewModelFactory: ViewModelProvider.AndroidViewModelFactory

    private companion object {
        const val POPPOP_UUID = "53454f55-4c54-4543-4850-6f70506f7030"
        const val SCAN_PERIOD = 10000L
        const val REQUEST_ENABLE_BT = 1000
        const val TAG = "ScanActivity"

    }

    //Todo: 위치 권한 요청하는 부분 삽입하기
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModelFactory = ViewModelProvider.AndroidViewModelFactory(application)
        vm = ViewModelProvider(this, viewModelFactory).get(ScanViewModel::class.java)

        //TODO: ViewModel 안에 넣기
        if (BluetoothUtil.isBluetoothAvailable(this)) {
            Log.d(TAG, "This device does not support Bluetooth.")
            finish()
        } else {
            Log.d(TAG, "This device supports Bluetooth.")
        }

        setSupportActionBar(binding.toolbarScan)

        supportActionBar?.let {
            it.setDisplayShowTitleEnabled(false)
            it.setDisplayHomeAsUpEnabled(true)
            it.setHomeAsUpIndicator(R.drawable.ic_keyboard_arrow_left_white_32dp)
        }

        vm.isFound.observe(this, Observer {
            if (it) {
                startActivity(Intent(this, InsertLocationActivity::class.java))
                finish()
            }
        })

        vm.hasAlready.observe(this, Observer {
            if (it) {
                finish()
            }
        })
    }

    override fun onResume() {
        super.onResume()
        
        //블루투스가 활성화 되어있는지 확인. 비활성화 되어 있으면 블루투스를 켜는 화면으로 이동
        if (vm.isBluetoothOn()) {
            val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT)

        } else {
            vm.stopService()

            //블루투스가 켜져있다면 스캔 시작
            vm.startScan(SCAN_PERIOD)
        }
    }

    //TODO: 뒤로 가기 버튼 클릭 시 스캔을 종료하겠냐는 다이얼로그 띄우기
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            vm.stopScan()
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}
