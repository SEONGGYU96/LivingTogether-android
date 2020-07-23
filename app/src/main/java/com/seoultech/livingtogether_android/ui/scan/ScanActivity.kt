package com.seoultech.livingtogether_android.ui.scan

import android.app.AlertDialog
import android.bluetooth.BluetoothAdapter
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.lifecycle.Observer
import com.seoultech.livingtogether_android.R
import com.seoultech.livingtogether_android.base.BaseActivity
import com.seoultech.livingtogether_android.bluetooth.viewmodel.ScanViewModel
import com.seoultech.livingtogether_android.databinding.ActivityScanBinding

class ScanActivity : BaseActivity<ActivityScanBinding>(R.layout.activity_scan) {
    private lateinit var vm: ScanViewModel

    private companion object {
        const val POPPOP_UUID = "53454f55-4c54-4543-4850-6f70506f7030"
        const val SCAN_PERIOD = 10000L
        const val REQUEST_ENABLE_BT = 1000
        const val TAG = "ScanActivity"
    }

    //Todo: 위치 권한 요청하는 부분 삽입하기
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        vm = viewModelProvider.get(ScanViewModel::class.java)

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

        vm.finishHandler.observe(this, finishObserver)

        vm.timeoutHandler.observe(this, Observer {
            if (it) {
                AlertDialog.Builder(this)
                    .setTitle("스캔 실패")
                    .setMessage("센서를 찾지 못하였습니다.")
                    .setPositiveButton("재시도") { dialog, _ ->
                        vm.startScan(SCAN_PERIOD)
                        dialog.dismiss()
                    }
                    .setNegativeButton("종료") { dialog, _ ->
                        dialog.dismiss()
                        finish()
                    }
                    .show()
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

    override fun onBackPressed() {
        AlertDialog.Builder(this)
            .setTitle("스캔 종료")
            .setMessage("아직 센서를 찾지 못했습니다.\n스캔을 종료하시겠습니까?")
            .setPositiveButton("종료") { _: DialogInterface, _: Int ->
                vm.stopScan()
                finish()
            }
            .setNegativeButton("취소") { dialog: DialogInterface, _: Int ->
                dialog.dismiss()
            }
            .show()
    }
}
