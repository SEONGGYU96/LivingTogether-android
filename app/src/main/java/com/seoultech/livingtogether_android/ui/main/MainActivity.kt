package com.seoultech.livingtogether_android.ui.main

import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.seoultech.livingtogether_android.R
import com.seoultech.livingtogether_android.device.adapter.DeviceAdapter
import com.seoultech.livingtogether_android.nextofkin.adapter.NOKAdapter
import com.seoultech.livingtogether_android.base.BaseActivity
import com.seoultech.livingtogether_android.databinding.ActivityMainBinding
import com.seoultech.livingtogether_android.debug.viewmodel.DebugViewModel
import com.seoultech.livingtogether_android.device.viewmodel.DeviceViewModel
import com.seoultech.livingtogether_android.nextofkin.viewmodel.NextOfKinViewModel
import com.seoultech.livingtogether_android.util.MarginDecoration
import com.seoultech.livingtogether_android.viewmodel.MainViewModel


class MainActivity : BaseActivity<ActivityMainBinding>(R.layout.activity_main) {
    companion object {
        private const val BACK_KEY_TIME = 1500L
    }

    private var backKeyPressedTime = 0L
    private val deviceAdapter: DeviceAdapter by lazy { DeviceAdapter() }
    private val nextOfKinAdapter: NOKAdapter by lazy { NOKAdapter() }
    private lateinit var debugVm: DebugViewModel

    private lateinit var deviceViewModel: DeviceViewModel
    private lateinit var nextOfKinViewModel: NextOfKinViewModel
    private lateinit var mainViewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        deviceViewModel = obtainDeviceViewModel()
        nextOfKinViewModel = obtainNextOfKinViewModel()
        mainViewModel = obtainMainViewModel()

        binding.run {
            this.mainViewModel = this@MainActivity.mainViewModel
            this.deviceViewModel = this@MainActivity.deviceViewModel
            this.nextOfKinViewModel = this@MainActivity.nextOfKinViewModel

            recyclerviewMainSensors.run {
                adapter = deviceAdapter
                addItemDecoration(MarginDecoration(baseContext, 16, RecyclerView.HORIZONTAL))
            }

            recyclerviewMainNextofkin.run {
                adapter = nextOfKinAdapter
                addItemDecoration(MarginDecoration(baseContext, 16, RecyclerView.VERTICAL))
            }

            this@MainActivity.deviceViewModel.items.observe(this@MainActivity, Observer {
                textviewMainStatusboxtitle.text = if (it.isEmpty()) {
                    getText(R.string.sensor_no_registered)
                } else {
                    getText(R.string.status_box_on_going)
                }
            })
        }
    }

    override fun onResume() {
        super.onResume()
        mainViewModel.onResume()
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

    private fun obtainDeviceViewModel(): DeviceViewModel = obtainViewModel(DeviceViewModel::class.java)
    private fun obtainNextOfKinViewModel(): NextOfKinViewModel = obtainViewModel(NextOfKinViewModel::class.java)
    private fun obtainMainViewModel(): MainViewModel = obtainViewModel(MainViewModel::class.java)
}