package com.seoultech.livingtogether_android

import android.app.Application
import com.seoultech.livingtogether_android.bluetooth.service.ServiceLiveData
import com.seoultech.livingtogether_android.database.LivingTogetherDatabase
import com.seoultech.livingtogether_android.util.SharedPreferenceManager

class ApplicationImpl : Application() {

    companion object {
        private lateinit var instance: ApplicationImpl
        fun getInstance() = instance

        lateinit var db: LivingTogetherDatabase
    }

    override fun onCreate() {
        super.onCreate()
        instance = this

        db = LivingTogetherDatabase.buildDatabase(applicationContext)

        ServiceLiveData.value = false

        SharedPreferenceManager.init(this)
    }
}