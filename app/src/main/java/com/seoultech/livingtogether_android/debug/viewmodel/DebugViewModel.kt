package com.seoultech.livingtogether_android.debug.viewmodel

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import com.seoultech.livingtogether_android.ApplicationImpl
import com.seoultech.livingtogether_android.device.data.Device
import java.util.*

class DebugViewModel(application: Application) : AndroidViewModel(application) {

    companion object {
        private const val DEBUG_ADDRESS = "70:6C:90:AB:0D:42"
        private const val DEBUG_LOCATION = "디버그"
    }

    private val db = ApplicationImpl.db

    fun addDevice() {
        val calendar = GregorianCalendar()
        db.deviceDao().insert(
            Device("발판", DEBUG_ADDRESS, DEBUG_LOCATION, calendar.timeInMillis, calendar.timeInMillis, calendar.timeInMillis))
        Toast.makeText(getApplication(), "디버깅 디바이스가 추가되었습니다.", Toast.LENGTH_SHORT).show()
    }

    fun deleteAll() {
        db.deviceDao().deleteAll()
        Toast.makeText(getApplication(), "모든 기기가 제거되었습니다.", Toast.LENGTH_SHORT).show()
    }
}