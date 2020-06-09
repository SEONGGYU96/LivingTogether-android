package com.seoultech.livingtogether_android.util

import android.app.ActivityManager
import android.app.Application
import android.content.Context

object ServiceUtil {
    @JvmStatic
    fun isServiceRunning(application: Application, serviceClass: Class<*>): Boolean {
        val manager = application.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        for (service in manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.name == service.service.className) {
                return true
            }
        }
        return false
    }
}
