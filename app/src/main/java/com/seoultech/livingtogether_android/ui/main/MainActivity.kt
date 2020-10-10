package com.seoultech.livingtogether_android.ui.main

import android.bluetooth.BluetoothAdapter
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.seoultech.livingtogether_android.R
import com.seoultech.livingtogether_android.device.adapter.DeviceAdapter
import com.seoultech.livingtogether_android.nextofkin.adapter.NextOfKinMainAdapter
import com.seoultech.livingtogether_android.base.BaseActivity
import com.seoultech.livingtogether_android.databinding.ActivityMainBinding
import com.seoultech.livingtogether_android.device.data.DeviceStateChangedLiveData
import com.seoultech.livingtogether_android.device.viewmodel.DeviceViewModel
import com.seoultech.livingtogether_android.nextofkin.viewmodel.NextOfKinViewModel
import com.seoultech.livingtogether_android.ui.contacts.ContactListActivity
import com.seoultech.livingtogether_android.ui.nok.AddNextOfKinActivity
import com.seoultech.livingtogether_android.ui.nok.NextOfKinListActivity
import com.seoultech.livingtogether_android.ui.scan.ScanActivity
import com.seoultech.livingtogether_android.ui.sensor.SensorListActivity
import com.seoultech.livingtogether_android.util.MarginDecoration
import com.seoultech.livingtogether_android.viewmodel.MainViewModel
import kotlinx.android.synthetic.main.activity_main.*
import com.seoultech.livingtogether_android.library.LTDialogBuilder as LTDialogBuilder


class MainActivity : BaseActivity<ActivityMainBinding>(R.layout.activity_main) {
    companion object {
        private const val BACK_KEY_TIME = 1500L
        private const val REQUEST_ENABLE_BT = 1000
    }

    private var backKeyPressedTime = 0L
    private val deviceAdapter: DeviceAdapter by lazy { DeviceAdapter() }

    private lateinit var deviceViewModel: DeviceViewModel
    private lateinit var nextOfKinViewModel: NextOfKinViewModel
    private lateinit var mainViewModel: MainViewModel

    private lateinit var statusBoxTitle: CharSequence

    private lateinit var bluetoothStateObserver: Observer<Boolean>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        deviceViewModel = obtainDeviceViewModel().apply {
            newSensorEvent.observe(this@MainActivity, Observer {
                this@MainActivity.registerNewSensor()
            })
        }

        nextOfKinViewModel = obtainNextOfKinViewModel().apply {
            newNextOfKinEvent.observe(this@MainActivity, Observer {
                this@MainActivity.addNewNextOfKin(null)
            })
        }

        mainViewModel = obtainMainViewModel().apply {
            seeMoreNextOfKinEvent.observe(this@MainActivity, Observer {
                this@MainActivity.seeMoreNextOfKin()
            })

            seeMoreSensorsEvent.observe(this@MainActivity, Observer {
                this@MainActivity.seeMoreSensors()
            })

            bluetoothOnEvent.observe(this@MainActivity, Observer {
                this@MainActivity.setBluetoothOn()
            })
        }

        DeviceStateChangedLiveData.observe(this, Observer {
            if (it) {
                deviceViewModel.start()
            }
        })

        binding.run {
            this.mainViewModel = this@MainActivity.mainViewModel
            this.deviceViewModel = this@MainActivity.deviceViewModel
            this.nextOfKinViewModel = this@MainActivity.nextOfKinViewModel

            toolbarMain.setMyPageButton()

            recyclerviewMainSensors.run {
                adapter = deviceAdapter
                addItemDecoration(MarginDecoration(baseContext, 16, RecyclerView.HORIZONTAL))
            }

            recyclerviewMainNextofkin.run {
                adapter = NextOfKinMainAdapter()
                addItemDecoration(MarginDecoration(baseContext, 16, RecyclerView.VERTICAL))
            }
        }

        bluetoothStateObserver = Observer {
            binding.textviewMainStatusboxtitle.text = if (it) {
                statusBoxTitle
            } else {
                getText(R.string.status_box_bluetooth_off)
            }

            binding.groupMainBluetoothoff.visibility = if (it) {
                View.GONE
            } else {
                View.VISIBLE
            }

            binding.groupMainNumberofsensors.visibility = if (it) {
                View.VISIBLE
            } else {
                View.INVISIBLE
            }

            binding.imageviewMainStatus.visibility = if (it) {
                View.VISIBLE
            } else {
                View.GONE
            }
        }

        deviceViewModel.empty.observe(this, Observer {
            statusBoxTitle = if (it) {
                getText(R.string.sensor_no_registered)
            } else {
                getText(R.string.status_box_on_going)
            }
            binding.textviewMainStatusboxtitle.text = statusBoxTitle

            group_main_bluetoothoff.visibility = if (it) {
                View.INVISIBLE
            } else {
                View.VISIBLE
            }

            if (it) {
                group_main_numberofsensors.visibility = View.INVISIBLE
                mainViewModel.run {
                    isBluetoothOn.removeObserver(bluetoothStateObserver)
                }
            } else {
                group_main_numberofsensors.visibility = View.VISIBLE
                mainViewModel.run {
                    isBluetoothOn.observe(this@MainActivity, bluetoothStateObserver)
                    bluetoothStateCheck()
                }
            }
            mainViewModel.setHasDevice(!it)
        })
    }

    override fun onResume() {
        super.onResume()
        nextOfKinViewModel.start()
        deviceViewModel.start()
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

    private fun <B: ViewDataBinding>addNewNextOfKin(binding: B?) {
        LTDialogBuilder<B, Any>()
            .addVerticalButton(getString(R.string.dialog_button_add_next_of_kin_directly)) { dialog, _ ->
                val intent = Intent(this, AddNextOfKinActivity::class.java)
                intent.putExtra("isFirstAdd", true)
                startActivity(intent)
                dialog.dismiss()
            }
            .addVerticalButton(getString(R.string.dialog_button_add_next_of_kin_contact)) {dialog, _ ->
                val intent = Intent(this, ContactListActivity::class.java)
                intent.putExtra("isFirstAdd", true)
                startActivity(intent)
                dialog.dismiss()
            }
            .build()
            .show(supportFragmentManager, "add_new_next_of_kin")
    }

    private fun seeMoreSensors() {
        startActivity(Intent(this, SensorListActivity::class.java))
    }

    private fun seeMoreNextOfKin() {
        startActivity(Intent(this, NextOfKinListActivity::class.java))
    }

    private fun registerNewSensor() {
        startActivity(Intent(this, ScanActivity::class.java))
    }

    private fun setBluetoothOn() {
        startActivityForResult(Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE), REQUEST_ENABLE_BT)
    }

    private fun obtainDeviceViewModel(): DeviceViewModel = obtainViewModel(DeviceViewModel::class.java)
    private fun obtainNextOfKinViewModel(): NextOfKinViewModel = obtainViewModel(NextOfKinViewModel::class.java)
    private fun obtainMainViewModel(): MainViewModel = obtainViewModel(MainViewModel::class.java)
}