package com.seoultech.livingtogether_android.debug.scan

import android.os.Bundle
import com.seoultech.livingtogether_android.R
import com.seoultech.livingtogether_android.base.BaseActivity
import com.seoultech.livingtogether_android.databinding.ActivityScanServiceTestBinding
import com.seoultech.livingtogether_android.debug.scan.adapter.ScanServiceTestAdapter
import com.seoultech.livingtogether_android.model.room.DataBaseManager
import com.seoultech.livingtogether_android.model.room.entity.SignalHistoryEntity

class ScanServiceTest : BaseActivity<ActivityScanServiceTestBinding>(R.layout.activity_scan_service_test) {
    private val actionSignalTestAdapter: ScanServiceTestAdapter by lazy { ScanServiceTestAdapter() }
    private val preserveSignalTestAdapter: ScanServiceTestAdapter by lazy { ScanServiceTestAdapter() }

    private val actionSignalHistory = mutableListOf<SignalHistoryEntity>()
    private val preserveSignalHistory = mutableListOf<SignalHistoryEntity>()

    private val db = DataBaseManager.getInstance(application)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.run {
            rvActionTest.adapter = actionSignalTestAdapter
            rvPreserveTest.adapter = preserveSignalTestAdapter
        }
    }

    override fun onResume() {
        super.onResume()

        actionSignalHistory.clear()
        actionSignalHistory.addAll(db.signalHistoryDao().getAll(0))
        preserveSignalHistory.clear()
        preserveSignalHistory.addAll(db.signalHistoryDao().getAll(1))

        actionSignalTestAdapter.notifyDataSetChanged()
        preserveSignalTestAdapter.notifyDataSetChanged()
    }
}
