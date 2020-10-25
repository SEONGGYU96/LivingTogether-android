package com.seoultech.livingtogether_android.ui.scan

import android.os.Bundle
import androidx.lifecycle.Observer
import com.seoultech.livingtogether_android.R
import com.seoultech.livingtogether_android.base.BaseActivity
import com.seoultech.livingtogether_android.databinding.ActivityInsertLocationBinding
import com.seoultech.livingtogether_android.device.viewmodel.LocationViewModel

class InsertLocationActivity : BaseActivity<ActivityInsertLocationBinding>(R.layout.activity_insert_location) {
    private lateinit var locationViewModel: LocationViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        locationViewModel = obtainViewModel().apply {
            updateLocationEvent.observe(this@InsertLocationActivity, Observer {
                finish()
            })
        }

        locationViewModel.start()

        binding.run {
            viewModel = locationViewModel
        }
    }

    private fun obtainViewModel() = obtainViewModel(LocationViewModel::class.java)
}
