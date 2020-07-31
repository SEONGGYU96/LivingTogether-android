package com.seoultech.livingtogether_android

import android.app.Application
import com.seoultech.livingtogether_android.bluetooth.service.ServiceLiveData
import com.seoultech.livingtogether_android.database.DataBaseManager
import com.seoultech.livingtogether_android.util.SharedPreferenceManager

class ApplicationImpl : Application() {

    companion object {
        private lateinit var instance: ApplicationImpl
        fun getInstance() = instance

        lateinit var db: DataBaseManager
    }

    override fun onCreate() {
        super.onCreate()
        instance = this

        db = DataBaseManager.buildDatabase(applicationContext)

        ServiceLiveData.value = false

        SharedPreferenceManager.init(this)
    }
}