package com.seoultech.livingtogether_android.ui.sensor

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.recyclerview.widget.GridLayoutManager
import com.seoultech.livingtogether_android.R
import com.seoultech.livingtogether_android.device.adapter.DeviceAdapter
import com.seoultech.livingtogether_android.base.BaseActivity
import com.seoultech.livingtogether_android.databinding.ActivitySensorListBinding
import com.seoultech.livingtogether_android.util.MarginDecoration
import com.seoultech.livingtogether_android.ui.scan.ScanActivity
import com.seoultech.livingtogether_android.device.viewmodel.DeviceViewModel

class SensorListActivity : BaseActivity<ActivitySensorListBinding>(R.layout.activity_sensor_list) {

    companion object {
        private const val NUM_OF_COLUMN = 2
    }

    private val deviceAdapter: DeviceAdapter by lazy { DeviceAdapter() }

    private lateinit var vm: DeviceViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setToolbar(binding.toolbar,"센서")

        vm = viewModelProvider.get(DeviceViewModel::class.java)

        binding.run {
            viewModel = vm

            recyclerDeviceList.layoutManager = GridLayoutManager(baseContext, NUM_OF_COLUMN)
            recyclerDeviceList.adapter = deviceAdapter
            recyclerDeviceList.addItemDecoration(
                MarginDecoration(
                    baseContext,
                    NUM_OF_COLUMN,
                    60,
                    60
                )
            )
        }
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
}
