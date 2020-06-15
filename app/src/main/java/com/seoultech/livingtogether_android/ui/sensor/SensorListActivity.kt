package com.seoultech.livingtogether_android.ui.sensor

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.seoultech.livingtogether_android.R
import com.seoultech.livingtogether_android.adapter.DeviceAdapter
import com.seoultech.livingtogether_android.base.BaseActivity
import com.seoultech.livingtogether_android.databinding.ActivitySensorListBinding
import com.seoultech.livingtogether_android.ui.main.decoration.MarginDecoration
import com.seoultech.livingtogether_android.ui.scan.ScanActivity
import com.seoultech.livingtogether_android.viewmodel.DeviceViewModel
import kotlinx.android.synthetic.main.activity_sensor_list.*

class SensorListActivity : BaseActivity<ActivitySensorListBinding>(R.layout.activity_sensor_list) {

    companion object {
        private const val NUM_OF_COLUMN = 2
    }

    private val deviceAdapter: DeviceAdapter by lazy { DeviceAdapter() }

    private lateinit var vm: DeviceViewModel
    private lateinit var viewModelFactory: ViewModelProvider.AndroidViewModelFactory


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModelFactory = ViewModelProvider.AndroidViewModelFactory(application)
        vm = ViewModelProvider(this, viewModelFactory).get(DeviceViewModel::class.java)

        binding.run {
            lifecycleOwner = this@SensorListActivity

            viewModel = vm

            recyclerDeviceList.layoutManager = GridLayoutManager(baseContext, NUM_OF_COLUMN)
            recyclerDeviceList.adapter = deviceAdapter
            recyclerDeviceList.addItemDecoration(MarginDecoration(baseContext, NUM_OF_COLUMN, 60, 60))
        }

        setSupportActionBar(toolbar_device_list)

        supportActionBar?.let {
            it.setDisplayShowTitleEnabled(false)
            it.setDisplayHomeAsUpEnabled(true)
            it.setHomeAsUpIndicator(R.drawable.ic_keyboard_arrow_left_black_32dp)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.add_menu, menu)       // main_menu 메뉴를 toolbar 메뉴 버튼으로 설정
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish()
                true
            }

            R.id.menu_go_to_add_activity -> {
                startActivity(Intent(this, ScanActivity::class.java))
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }
}
