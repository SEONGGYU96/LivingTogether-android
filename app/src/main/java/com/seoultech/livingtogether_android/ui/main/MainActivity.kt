package com.seoultech.livingtogether_android.ui.main

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.seoultech.livingtogether_android.R
import com.seoultech.livingtogether_android.adapter.DeviceAdapter
import com.seoultech.livingtogether_android.adapter.NOKAdapter
import com.seoultech.livingtogether_android.base.BaseActivity
import com.seoultech.livingtogether_android.databinding.ActivityMainBinding
import com.seoultech.livingtogether_android.debug.scan.ScanServiceTest
import com.seoultech.livingtogether_android.debug.viewmodel.DebugViewModel
import com.seoultech.livingtogether_android.ui.main.decoration.MarginDecoration
import com.seoultech.livingtogether_android.ui.nok.NOKListActivity
import com.seoultech.livingtogether_android.ui.profile.EditProfileActivity
import com.seoultech.livingtogether_android.ui.scan.InsertLocationActivity
import com.seoultech.livingtogether_android.ui.scan.ScanActivity
import com.seoultech.livingtogether_android.ui.sensor.SensorListActivity
import com.seoultech.livingtogether_android.viewmodel.MainViewModel
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : BaseActivity<ActivityMainBinding>(R.layout.activity_main) {
    private val deviceAdapter: DeviceAdapter by lazy { DeviceAdapter() }
    private val nokAdapter: NOKAdapter by lazy { NOKAdapter() }
    private lateinit var vm: MainViewModel
    private lateinit var debugVm: DebugViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        vm = viewModelProvider.get(MainViewModel::class.java)
        debugVm = viewModelProvider.get(DebugViewModel::class.java)

        binding.run {
            viewModel = vm

            recyclerDeviceListMain.layoutManager = LinearLayoutManager(baseContext, LinearLayoutManager.HORIZONTAL, false)
            recyclerDeviceListMain.adapter = deviceAdapter
            recyclerDeviceListMain.addItemDecoration(MarginDecoration(baseContext, 15, RecyclerView.HORIZONTAL))

            recyclerNokListMain.layoutManager = LinearLayoutManager(baseContext)
            recyclerNokListMain.adapter = nokAdapter
            recyclerNokListMain.addItemDecoration(MarginDecoration(baseContext, 15, RecyclerView.VERTICAL))

            buttonShowMoreNokMain.setOnClickListener {
                startActivity(Intent(this@MainActivity, NOKListActivity::class.java))
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

        //Todo: BidingAdapter로 변경
        vm.sensors.observe(this@MainActivity, Observer {
            text_scan_state_main.run {
                if (it.isEmpty()) {
                    changeStatusBox(this, false, context.getString(R.string.status_box_no_sensor))

                } else {
                    if (!vm.isBluetoothOn()) {
                        changeStatusBox(this, false,  context.getString(R.string.status_box_bluetooth_off))
                    } else if (!vm.isServiceRunning()) {
                        changeStatusBox(this, false, context.getString(R.string.status_box_fail))
                    } else {
                         changeStatusBox(this, true, context.getString(R.string.status_box_on_going))
                    }
                }
            }
        })
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
}