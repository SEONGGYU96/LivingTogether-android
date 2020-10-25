package com.seoultech.livingtogether_android.ui.sensor

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.seoultech.livingtogether_android.R
import com.seoultech.livingtogether_android.device.adapter.DeviceAdapter
import com.seoultech.livingtogether_android.base.BaseActivity
import com.seoultech.livingtogether_android.bluetooth.service.ScanService
import com.seoultech.livingtogether_android.databinding.ActivitySensorListBinding
import com.seoultech.livingtogether_android.databinding.DialogDeviceDetailBinding
import com.seoultech.livingtogether_android.device.data.Device
import com.seoultech.livingtogether_android.device.data.DeviceStateChangedLiveData
import com.seoultech.livingtogether_android.util.MarginDecoration
import com.seoultech.livingtogether_android.ui.scan.ScanActivity
import com.seoultech.livingtogether_android.device.viewmodel.DeviceViewModel
import com.seoultech.livingtogether_android.library.LTDialog
import com.seoultech.livingtogether_android.library.LTDialogBuilder
import com.seoultech.livingtogether_android.util.ServiceUtil
import kotlinx.android.synthetic.main.dialog_device_detail.view.*

class SensorListActivity : BaseActivity<ActivitySensorListBinding>(R.layout.activity_sensor_list) {

    companion object {
        private const val NUM_OF_COLUMN = 2
    }

    private val deviceAdapter: DeviceAdapter by lazy { DeviceAdapter() }

    private lateinit var deviceViewModel: DeviceViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        deviceViewModel = obtainViewModel().apply {
            emptyListEvent.observe(this@SensorListActivity, Observer {
                if (it) {
                    finish()
                }
            })

            newSensorEvent.observe(this@SensorListActivity, Observer {
                if (it) {
                    startActivity(Intent(this@SensorListActivity, ScanActivity::class.java))
                }
            })
        }

        DeviceStateChangedLiveData.observe(this, Observer {
            if (it) {
                deviceViewModel.start()
            }
        })

        initDeviceAdapterListener()

        binding.run {
            viewModel = deviceViewModel

            lttoolbarSensorlist.setBackButton()

            recyclerviewSensorlist.run {
                adapter = deviceAdapter
                addItemDecoration(MarginDecoration(baseContext, NUM_OF_COLUMN, 20, 25))
            }
        }
    }

    override fun onResume() {
        super.onResume()
        deviceViewModel.start()
    }

    private fun initDeviceAdapterListener() {
        deviceAdapter.setOnDeviceClickListener(object: DeviceAdapter.OnDeviceClickListener {
            override fun onClick(data: Device) = LTDialogBuilder<DialogDeviceDetailBinding, Device>()
                .setTitle(getString(R.string.dialog_title_sensor_info))
                .setContentView(R.layout.dialog_device_detail)
                .injection { it.device = data }
                .addHorizontalButton(getString(R.string.dialog_button_delete)) { dialog, binding ->
                    showRecheckDialog(dialog, binding?.device!!)
                }
                .addHorizontalButton(getString(R.string.dialog_button_confirm)) { dialog, binding ->
                    binding?.let { deviceViewModel.updateDevice(binding.device!!) }
                    dialog.dismiss()
                }
                .build()
                .show(supportFragmentManager, "sensor_info")
        })
    }

    private fun showRecheckDialog(dialog: LTDialog<*>, data: Device?) {
        if (data == null) {
            dialog.dismiss()
            return
        }
        AlertDialog.Builder(this)
            .setTitle("등록된 센서 삭제")
            .setMessage("정말 해당 센서를 삭제하시겠습니까?\n더 이상 센서의 신호를 감지하지 않습니다.")
            .setPositiveButton("삭제") { thisDialog, _ ->
                deviceViewModel.deleteDevice(data.deviceAddress)
                ServiceUtil.restartService(application)
                thisDialog.dismiss()
                dialog.dismiss()
            }
            .setNegativeButton("취소") { thisDialog, _ ->
                thisDialog.dismiss()
            }
            .show()
    }

    private fun obtainViewModel(): DeviceViewModel = obtainViewModel(DeviceViewModel::class.java)
}
