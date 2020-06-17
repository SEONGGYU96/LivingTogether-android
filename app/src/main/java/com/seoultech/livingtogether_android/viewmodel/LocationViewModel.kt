package com.seoultech.livingtogether_android.viewmodel

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.seoultech.livingtogether_android.model.room.DataBaseManager
import com.seoultech.livingtogether_android.model.room.entity.DeviceEntity

class LocationViewModel(application: Application) : AndroidViewModel(application) {

    companion object {
        private const val TAG = "LocationViewModel"
    }

    val db = DataBaseManager.getInstance(application)

    var finishHandler = MutableLiveData<Boolean>()

    var location: String? = null

    private val device = getMostRecentDevice()

    private fun getMostRecentDevice() : DeviceEntity? {
        val mostResentDevice = db.deviceDao().getMostRecentDevice()

        if (mostResentDevice == null) {
            Log.d(TAG, "empty device")
            Toast.makeText(getApplication(), "저장된 디바이스가 없습니다.", Toast.LENGTH_SHORT).show()

            finishHandler.value = true
            return null
        }

        Log.d(TAG, "most recent device : ${db.deviceDao().getMostRecentDevice()}")

        return mostResentDevice
    }

    fun updateLocation() {
        device!!.location = location

        Log.d(TAG, "update Location : ${device.location}")

        db.deviceDao().update(device)
        finishHandler.value = true
    }
}