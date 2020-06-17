package com.seoultech.livingtogether_android.debug.viewmodel

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import com.seoultech.livingtogether_android.model.room.DataBaseManager
import com.seoultech.livingtogether_android.model.room.entity.DeviceEntity
import java.util.*

class DebugViewModel(application: Application) : AndroidViewModel(application) {

    companion object {
        private const val DEBUG_MAJOR = "1234"
        private const val DEBUG_MINOR = "4321"
        private const val DEBUG_ADDRESS = "70:6C:90:AB:0D:42"
        private const val DEBUG_LOCATION = "디버그"
    }

    private val db : DataBaseManager by lazy { DataBaseManager.getInstance(application) }

    fun addDevice() {
        val calendar = GregorianCalendar()
        db.deviceDao().insert(DeviceEntity("발판", DEBUG_MAJOR, DEBUG_MINOR, DEBUG_ADDRESS, DEBUG_LOCATION,
            null, calendar.timeInMillis, null, true))
        Toast.makeText(getApplication(), "디버깅 디바이스가 추가되었습니다.", Toast.LENGTH_SHORT).show()
    }

    fun deleteAll() {
        db.deviceDao().deleteAll()
        Toast.makeText(getApplication(), "모든 기기가 제거되었습니다.", Toast.LENGTH_SHORT).show()
    }
}