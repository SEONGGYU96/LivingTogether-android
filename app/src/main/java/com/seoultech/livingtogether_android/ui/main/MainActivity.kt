package com.seoultech.livingtogether_android.ui.main

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.seoultech.livingtogether_android.R
import com.seoultech.livingtogether_android.adapter.NOKAdapter
import com.seoultech.livingtogether_android.adapter.SensorAdapter
import com.seoultech.livingtogether_android.base.BaseActivity
import com.seoultech.livingtogether_android.databinding.ActivityMainBinding
import com.seoultech.livingtogether_android.model.room.DataBaseManager
import com.seoultech.livingtogether_android.model.room.entity.UserEntity
import com.seoultech.livingtogether_android.ui.scan.ScanActivity
import com.seoultech.livingtogether_android.ui.main.decoration.MarginDecoration
import com.seoultech.livingtogether_android.viewmodel.MainViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class MainActivity : BaseActivity<ActivityMainBinding>(R.layout.activity_main) {
    private val sensorAdapter: SensorAdapter by lazy { SensorAdapter() }
    private val nokAdapter: NOKAdapter by lazy { NOKAdapter() }
    private val vm: MainViewModel by lazy { MainViewModel() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.run {
            viewModel = vm
            recyclerSensorListMain.layoutManager =
                LinearLayoutManager(baseContext, LinearLayoutManager.HORIZONTAL, false)
            recyclerSensorListMain.adapter = sensorAdapter
            recyclerSensorListMain.addItemDecoration(
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

            textSensorMoreMain.setOnClickListener {
                val intent = Intent(this@MainActivity, ScanActivity::class.java)
                startActivity(intent)
            }
        }

        val dataBaseManager = DataBaseManager.getInstance(this)

        //테스트
        dataBaseManager.userDao().getAllObservable().observe(this, Observer {
            Log.d("texst", it.toString())
        })

        binding.testButton.setOnClickListener {
            lifecycleScope.launch(Dispatchers.IO) {
                dataBaseManager.userDao().insert(UserEntity("테스트", "공릉동", "010-9885-5658", "노원구", "공릉동"))
            }
        }

        binding.textMainLocation.setOnClickListener {
            lifecycleScope.launch(Dispatchers.IO) {
                dataBaseManager.userDao().deleteAll()
            }
        }
    }
}