package com.seoultech.livingtogether_android

import android.app.Application
import android.content.Context
import com.seoultech.livingtogether_android.manager.SimpleBluetoothManager

//TODO: 활용이 안된다.. 고쳐보자
class LivingTogetherApplication : Application() {
    lateinit var context: Context

    init {
        instance = this
    }

    companion object {
        private var instance: LivingTogetherApplication? = null
        fun applicationContext(): Context {
            return instance!!.applicationContext
        }
    }
}