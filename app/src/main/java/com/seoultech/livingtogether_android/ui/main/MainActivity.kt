package com.seoultech.livingtogether_android.ui.main

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.seoultech.livingtogether_android.ApplicationImpl
import com.seoultech.livingtogether_android.R
import com.seoultech.livingtogether_android.device.adapter.DeviceAdapter
import com.seoultech.livingtogether_android.nok.adapter.NOKAdapter
import com.seoultech.livingtogether_android.base.BaseActivity
import com.seoultech.livingtogether_android.bluetooth.model.BluetoothLiveData
import com.seoultech.livingtogether_android.bluetooth.service.ServiceLiveData
import com.seoultech.livingtogether_android.databinding.ActivityMainBinding
import com.seoultech.livingtogether_android.debug.ScanServiceTest
import com.seoultech.livingtogether_android.debug.viewmodel.DebugViewModel
import com.seoultech.livingtogether_android.device.model.DeviceEntity
import com.seoultech.livingtogether_android.util.MarginDecoration
import com.seoultech.livingtogether_android.ui.nok.NextOfKinListActivity
import com.seoultech.livingtogether_android.ui.profile.EditProfileActivity
import com.seoultech.livingtogether_android.ui.scan.InsertLocationActivity
import com.seoultech.livingtogether_android.ui.scan.ScanActivity
import com.seoultech.livingtogether_android.ui.sensor.SensorListActivity
import com.seoultech.livingtogether_android.viewmodel.MainViewModel
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : BaseActivity<ActivityMainBinding>(R.layout.activity_main) {
    companion object {
        private const val BACK_KEY_TIME = 1500L
    }

    private var backKeyPressedTime = 0L
    private val deviceAdapter: DeviceAdapter by lazy { DeviceAdapter() }
    private val nokAdapter: NOKAdapter by lazy { NOKAdapter() }
    private lateinit var vm: MainViewModel
    private lateinit var debugVm: DebugViewModel

    private lateinit var serviceObserver: Observer<Boolean>
    private lateinit var stateObserver: Observer<List<DeviceEntity>>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        vm = viewModelProvider.get(MainViewModel::class.java)
        debugVm = viewModelProvider.get(DebugViewModel::class.java)

        binding.run {
            viewModel = vm

            if (!vm.isInitialized) {
                AlertDialog.Builder(this@MainActivity)
                    .setTitle("환영합니다!")
                    .setMessage("사용하기 전에 사용자 정보를 먼저 등록해주세요")
                    .setPositiveButton("확인") { dialog, _ ->
                        startActivity(Intent(this@MainActivity, EditProfileActivity::class.java))
                        dialog.dismiss()
                    }
                    .show()
            }

            recyclerDeviceListMain.layoutManager = LinearLayoutManager(baseContext, LinearLayoutManager.HORIZONTAL, false)
            recyclerDeviceListMain.adapter = deviceAdapter
            recyclerDeviceListMain.addItemDecoration(
                MarginDecoration(
                    baseContext,
                    15,
                    RecyclerView.HORIZONTAL
                )
            )

            recyclerNokListMain.layoutManager = LinearLayoutManager(baseContext)
            recyclerNokListMain.adapter = nokAdapter
            recyclerNokListMain.addItemDecoration(
                MarginDecoration(
                    baseContext,
                    15,
                    RecyclerView.VERTICAL
                )
            )

            buttonShowMoreNokMain.setOnClickListener {
                startActivity(Intent(this@MainActivity, NextOfKinListActivity::class.java))
            }

            buttonEditProfile.setOnClickListener{
                startActivity(Intent(this@MainActivity, EditProfileActivity::class.java))
            }

            buttonSensorMoreMain.setOnClickListener {
                startActivity(Intent(this@MainActivity, SensorListActivity::class.java))
            }

            layoutDebug.buttonScanTestActivity.setOnClickListener {
                startActivity(Intent(this@MainActivity, ScanServiceTest::class.java))
            }

            layoutDebug.buttonScanTest.setOnClickListener {
                startActivity(Intent(this@MainActivity, ScanActivity::class.java))
            }

            layoutDebug.buttonAddDeviceTest.setOnClickListener {
                debugVm.addDevice()
            }

            layoutDebug.buttonDeleteAllDeviceTest.setOnClickListener {
                debugVm.deleteAll()
            }

            layoutDebug.buttonInsertLocationTest.setOnClickListener {
                startActivity(Intent(this@MainActivity, InsertLocationActivity::class.java))
            }
        }

        serviceObserver = Observer {
            if (!it) {
                changeStatusBox(text_scan_state_main, false, getString(R.string.status_box_fail))
            } else {
                changeStatusBox(text_scan_state_main, true, getString(R.string.status_box_on_going))
            }
        }

        stateObserver = Observer {
            text_scan_state_main.run {
                if (it.isEmpty()) {
                    ServiceLiveData.removeObserver(serviceObserver)
                    changeStatusBox(this, false, context.getString(R.string.status_box_no_sensor))
                } else {
                    ServiceLiveData.observe(this@MainActivity, serviceObserver)
                    changeStatusBox(this, true, context.getString(R.string.status_box_on_going))
                }
            }
        }

        BluetoothLiveData.observe(this, Observer {
            if (!it) {
                changeStatusBox(text_scan_state_main, false,  this.getString(R.string.status_box_bluetooth_off))
                vm.sensors.removeObserver(stateObserver)
                ServiceLiveData.removeObserver(serviceObserver)
            } else {
                vm.sensors.observe(this@MainActivity, stateObserver)
            }
        })

        Log.d(TAG, ApplicationImpl.db.signalHistoryDao().getAll().toString())
    }

    private fun changeStatusBox(view: TextView, on: Boolean, text: String) {
        view.text = text
        if (!on) {
            view.setTextAppearance(R.style.GrayStateText)
            group_num_of_sensor_main.visibility = View.INVISIBLE
        } else {
            view.setTextAppearance(R.style.NormalStateText)
            group_num_of_sensor_main.visibility = View.VISIBLE
        }
    }

    override fun onBackPressed() {
        if (System.currentTimeMillis() > backKeyPressedTime + BACK_KEY_TIME) {
            backKeyPressedTime = System.currentTimeMillis()
            Toast.makeText(this, getString(R.string.toast_back_key_exit), Toast.LENGTH_SHORT).show()
            return
        } else {
            finish()
        }
    }
}