package com.seoultech.livingtogether_android.ui.sensor

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.recyclerview.widget.GridLayoutManager
import com.seoultech.livingtogether_android.R
import com.seoultech.livingtogether_android.device.adapter.DeviceAdapter
import com.seoultech.livingtogether_android.base.BaseActivity
import com.seoultech.livingtogether_android.databinding.ActivitySensorListBinding
import com.seoultech.livingtogether_android.device.data.Device
import com.seoultech.livingtogether_android.util.MarginDecoration
import com.seoultech.livingtogether_android.ui.scan.ScanActivity
import com.seoultech.livingtogether_android.device.viewmodel.DeviceViewModel
import kotlinx.android.synthetic.main.dialog_device_detail.view.*

class SensorListActivity : BaseActivity<ActivitySensorListBinding>(R.layout.activity_sensor_list) {

    companion object {
        private const val NUM_OF_COLUMN = 2
    }

    private val deviceAdapter: DeviceAdapter by lazy { DeviceAdapter() }

    private lateinit var deviceViewModel: DeviceViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setToolbar(binding.toolbar,"센서")

        deviceViewModel = obtainViewModel()

        initDeviceAdapterListener()

        binding.run {
            viewModel = deviceViewModel

            recyclerDeviceList.layoutManager = GridLayoutManager(baseContext, NUM_OF_COLUMN)
            recyclerDeviceList.adapter = deviceAdapter
            recyclerDeviceList.addItemDecoration(MarginDecoration(baseContext, NUM_OF_COLUMN, 12, 12))
        }
    }

    private fun initDeviceAdapterListener() {
        deviceAdapter.setOnDeviceClickListener(object: DeviceAdapter.OnDeviceClickListener {
            val view = layoutInflater.inflate(R.layout.dialog_device_detail, null)
            val dialog = AlertDialog.Builder(this@SensorListActivity)
                .setView(view)
                .create()

            override fun onClick(data: Device) {
                view.run {
                    textview_dialogdevice_macpresent.text = data.deviceAddress
                    textview_dialogdevice_activepresent.text = data.getLastDetectedActiveTimeToString()
                    textview_dialogdevice_initdatepresent.text = data.getInitDateToString()
                    textview_dialogdevice_kindpresent.text = data.deviceType
                    textview_dialogdevice_availablepresent.text = data.getDeviceAvailable()
                    textview_dialogdevice_preservepresent.text = data.getLastDetectedPreserveTimeToString()
                    edittext_dialogdevice_locationpresent.setText(data.location)
                }
                dialog.show()

                view.button_dialogdevice_confirm.setOnClickListener {
                    if (view.edittext_dialogdevice_locationpresent.text.toString() != data.location) {
                        data.location = view.edittext_dialogdevice_locationpresent.text.toString()
                        deviceViewModel.updateDevice(data)
                    }
                    dialog.dismiss()
                }

                view.button_dialogdevice_delete.setOnClickListener {
                    showRecheckDialog(dialog, data)
                }
            }

        })
    }

    private fun showRecheckDialog(dialog: AlertDialog, data: Device) {
        AlertDialog.Builder(this)
            .setTitle("등록된 센서 삭제")
            .setMessage("정말 해당 센서를 삭제하시겠습니까?\n더 이상 센서의 신호를 감지하지 않습니다.")
            .setPositiveButton("삭제") { thisDialog, _ ->
                deviceViewModel.deleteDevice(data.deviceAddress)
                thisDialog.dismiss()
                dialog.dismiss()
            }
            .setNegativeButton("취소") { thisDialog, _ ->
                thisDialog.dismiss()
            }
            .show()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.add_menu, menu)       // main_menu 메뉴를 toolbar 메뉴 버튼으로 설정
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.item_noklist_addnok -> {
                startActivity(Intent(this, ScanActivity::class.java))
                return true
            }
        }

        return super.onOptionsItemSelected(item)
    }

    private fun obtainViewModel(): DeviceViewModel = obtainViewModel(DeviceViewModel::class.java)
}
