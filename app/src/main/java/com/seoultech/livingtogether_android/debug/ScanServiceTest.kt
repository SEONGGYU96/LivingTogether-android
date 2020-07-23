package com.seoultech.livingtogether_android.debug

import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.seoultech.livingtogether_android.R
import com.seoultech.livingtogether_android.base.BaseActivity
import com.seoultech.livingtogether_android.databinding.ActivityScanServiceTestBinding
import com.seoultech.livingtogether_android.debug.adapter.ScanServiceTestAdapter
import com.seoultech.livingtogether_android.bluetooth.viewmodel.ScanTestViewModel

class ScanServiceTest : BaseActivity<ActivityScanServiceTestBinding>(R.layout.activity_scan_service_test) {
    private val actionSignalTestAdapter: ScanServiceTestAdapter by lazy { ScanServiceTestAdapter() }
    private val preserveSignalTestAdapter: ScanServiceTestAdapter by lazy { ScanServiceTestAdapter() }

    private lateinit var vm: ScanTestViewModel
    private lateinit var viewModelFactory: ViewModelProvider.AndroidViewModelFactory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModelFactory = ViewModelProvider.AndroidViewModelFactory(application)
        vm = ViewModelProvider(this, viewModelFactory).get(ScanTestViewModel::class.java)

        binding.run {
            lifecycleOwner = this@ScanServiceTest

            viewModel = vm

            rvActionTest.layoutManager = LinearLayoutManager(baseContext, LinearLayoutManager.VERTICAL, false)
            rvActionTest.adapter = actionSignalTestAdapter

            rvPreserveTest.layoutManager = LinearLayoutManager(baseContext, LinearLayoutManager.VERTICAL, false)
            rvPreserveTest.adapter = preserveSignalTestAdapter
        }

    }
}
