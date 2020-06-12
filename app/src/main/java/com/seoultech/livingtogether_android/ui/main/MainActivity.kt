package com.seoultech.livingtogether_android.ui.main

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.seoultech.livingtogether_android.R
import com.seoultech.livingtogether_android.adapter.DeviceAdapter
import com.seoultech.livingtogether_android.adapter.NOKAdapter
import com.seoultech.livingtogether_android.base.BaseActivity
import com.seoultech.livingtogether_android.databinding.ActivityMainBinding
import com.seoultech.livingtogether_android.databinding.DebugLayoutBinding
import com.seoultech.livingtogether_android.debug.scan.ScanServiceTest
import com.seoultech.livingtogether_android.debug.viewmodel.DebugViewModel
import com.seoultech.livingtogether_android.service.ScanService
import com.seoultech.livingtogether_android.ui.main.decoration.MarginDecoration
import com.seoultech.livingtogether_android.ui.nok.NOKListActivity
import com.seoultech.livingtogether_android.ui.profile.EditProfileActivity
import com.seoultech.livingtogether_android.ui.scan.ScanActivity
import com.seoultech.livingtogether_android.util.ServiceUtil
import com.seoultech.livingtogether_android.viewmodel.MainViewModel


class MainActivity : BaseActivity<ActivityMainBinding>(R.layout.activity_main) {
    private val deviceAdapter: DeviceAdapter by lazy { DeviceAdapter() }
    private val nokAdapter: NOKAdapter by lazy { NOKAdapter() }
    private lateinit var vm: MainViewModel
    private lateinit var debugVm: DebugViewModel
    private lateinit var viewModelFactory: ViewModelProvider.AndroidViewModelFactory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModelFactory = ViewModelProvider.AndroidViewModelFactory(application)
        vm = ViewModelProvider(this, viewModelFactory).get(MainViewModel::class.java)
        debugVm = ViewModelProvider(this, viewModelFactory).get(DebugViewModel::class.java)

        binding.run {
            lifecycleOwner = this@MainActivity

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
        }
    }

    override fun onResume() {
        super.onResume()

        vm.startService()
    }
}