package com.seoultech.livingtogether_android

import android.app.Application
import com.seoultech.livingtogether_android.model.room.DataBaseManager

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
    }
}