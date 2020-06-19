package com.seoultech.livingtogether_android.ui.scan

import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.seoultech.livingtogether_android.R
import com.seoultech.livingtogether_android.base.BaseActivity
import com.seoultech.livingtogether_android.databinding.ActivityInsertLocationBinding
import com.seoultech.livingtogether_android.viewmodel.LocationViewModel

class InsertLocationActivity : BaseActivity<ActivityInsertLocationBinding>(R.layout.activity_insert_location) {
    private lateinit var vm: LocationViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        vm = viewModelProvider.get(LocationViewModel::class.java)

        vm.finishHandler.observe(this, Observer {
            if (it) {
                finish()
            }
        })

        binding.run {
            viewModel = vm
        }
    }
}
