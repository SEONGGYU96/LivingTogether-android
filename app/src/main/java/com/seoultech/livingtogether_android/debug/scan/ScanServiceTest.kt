package com.seoultech.livingtogether_android.debug.scan

import android.os.Bundle
import com.seoultech.livingtogether_android.R
import com.seoultech.livingtogether_android.base.BaseActivity
import com.seoultech.livingtogether_android.databinding.ActivityScanServiceTestBinding
import com.seoultech.livingtogether_android.debug.scan.adapter.ScanServiceTestAdapter

class ScanServiceTest : BaseActivity<ActivityScanServiceTestBinding>(R.layout.activity_scan_service_test) {
    private val actionSignalTestAdapter: ScanServiceTestAdapter by lazy { ScanServiceTestAdapter() }
    private val preserveSignalTestAdapter: ScanServiceTestAdapter by lazy { ScanServiceTestAdapter() }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.run {
            rvActionTest.adapter = actionSignalTestAdapter
            rvPreserveTest.adapter = preserveSignalTestAdapter
        }
    }
}
